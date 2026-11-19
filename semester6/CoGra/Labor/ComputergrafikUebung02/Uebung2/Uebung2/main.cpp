/////////////////////////////////////////////////////////////////////////////
//																									//
// Code framework for lecture computer graphics WS 2025/26 assignment 2+3	//
//																									//
// A generic class for n-dimensional coordinate vectors							//
//																									//
// (c) Georg Umlauf																			//
//																									//
/////////////////////////////////////////////////////////////////////////////


#include "Vector.h"
#include "Matrix.h"
#include "Planet.h"

#include <iostream>

// maybe you have to switch to your local include path, depending on your GLUT installation
#include "glut.h"

////////////////////////////////////////////////////////////
//
// system relevant global variables
//

// resolution of the of the main window (can be changed by the user)
int g_WinWidth = 800;
int g_WinHeight = 800;

// global variable to tune the timer interval
int g_iTimerMSecs;

////////////////////////////////////////////////////////////
//
// private, global variables ...

Planet g_sun;
Planet g_earth;
Planet g_moon;
//
/////////////////////////////////////////////////////////////

float g_earthAngle = 0.0f;
float g_earthAngleIncr = 0.02f; // earths orbit speed 

float g_moonAngle = 0.0f;
float g_moonAngleIncr = 0.08f; // moons orbit speed

float g_earthOrbitRadius = 200.0f;
float g_moonOrbitRadius = 40.0f;

// create a 2x2 rotationMatrix
static CMat2f rotationMatrix(float angle)
{
	float c = std::cos(angle);
	float s = std::sin(angle);

	CMat2f R;
	R(0, 0) = c; R(0, 1) = -s;
	R(1, 0) = s; R(1, 1) = c;

	return R;
}

// rotates point p around origin with angle
static CVec2f rotateAroundOrigin(const CVec2f& p, float angle)
{
	CMat2f R = rotationMatrix(angle);
	CVec2f res;
	res[0] = R(0, 0) * p[0] + R(0, 1) * p[1]; // matrix vector multiplication
	res[1] = R(1, 0) * p[0] + R(1, 1) * p[1];

	return res;
}

// rotates point p around any point center (not just origin)
static CVec2f rotateAroundPoint2D(const CVec2f& p, const CVec2f& center, float angle)
{
	CVec2f translated = p - center; // move p so that center is at origin
	CVec2f rotated = rotateAroundOrigin(translated, angle);

	return rotated + center; // move back
}


// Function to initialize our own variables
void init()
{
	// init timer interval
	g_iTimerMSecs = 10;

	// init variables for display2
	int aiPos[2] = { 0, 0 };
	int aiPosIncr[2] = { 2, 2 };

	// init sun, moon and earth
	g_sun.setColor(Color{ 1.0, 0.8, 0.0 });
	g_sun.setRadius(50);
	g_sun.setPosition(CVec2f(0.0f, 0.0f));

	g_earth.setColor(Color{ 0.0, 0.5, 1.0 });
	g_earth.setRadius(20);

	g_moon.setColor(Color{ 1, 0, 0 });
	g_moon.setRadius(9);

	// earth and moon on positive x axis
	g_earthAngle = 0.0f;
	g_moonAngle = 0.0f;
}

// Function to initialize the view to ortho-projection.
// Please do not change.
void initGL()
{
	glViewport(0, 0, g_WinWidth, g_WinHeight);		// Establish viewport to cover entire window.

	glMatrixMode(GL_PROJECTION);					// Start modifying the projection matrix.
	glLoadIdentity();								// Init projection matrix.
	glOrtho(-g_WinWidth / 2, g_WinWidth / 2,		// Map abstract coords directly to window coords.
		-g_WinHeight / 2, g_WinHeight / 2, 0, 1);
	glDrawBuffer(GL_BACK);							// Tell GL that we draw to the back buffer and swap ...
	// ... buffers when image is ready to avoid flickering	
	float b = 70.0f / 100.0f;
	glClearColor(b, b, b, 1.0f);					// Tell which color to use to clear image
}

// Callback Function to change the window size.
// Please do not change.
void reshape(int w, int h)
{
	g_WinWidth = w;
	g_WinHeight = h;
	glViewport(0, 0, w, h);							// Establish viewport to cover entire window.

	glMatrixMode(GL_PROJECTION);					// Start modifying the projection matrix.
	glLoadIdentity();								// Reset projection matrix.
	glOrtho(-w / 2, w / 2, -h / 2, h / 2, 0, 1);	// Map abstract coords directly to window coords.

	glutPostRedisplay();
}

// timer callback function
void timer(int value)
{
	// increase angle each time the timer is called -> rotates earth and moon
	g_earthAngle += g_earthAngleIncr;
	g_moonAngle += g_moonAngleIncr; //muss zwischen 0 und 2pi liegen 

	// the last two lines should always be
	glutPostRedisplay();
	glutTimerFunc(g_iTimerMSecs, timer, 0);	// call timer for next iteration
}

// display callback function for EXERCISE 3
void displayExercise3(void)
{

	glClear(GL_COLOR_BUFFER_BIT);

	CVec2f sunPos = g_sun.getPosition();

	// rotate earth Base around origin by the angle
	CVec2f earthBase(g_earthOrbitRadius, 0.0f);
	CVec2f earthPos = rotateAroundOrigin(earthBase, g_earthAngle);

	// rotate moon Base around earths local origin
	CVec2f moonBase(g_moonOrbitRadius, 0.0f);
	CVec2f moonPos = earthPos + rotateAroundOrigin(moonBase, g_moonAngle);

	g_earth.setPosition(earthPos);
	g_moon.setPosition(moonPos);

	// draw bodies
	g_sun.draw();
	g_earth.draw();
	g_moon.draw();

	// In double buffer mode the last two lines should always be
	glFlush();
	glutSwapBuffers(); // swap front and back buffer
}

// creates 3x3 rotataion matrix
static CMat3f rotate(float angle)
{
	float c = cos(angle);
	float s = sin(angle);
	CMat3f R(0.0f); // einheitsmatrix
	R(2,2) = 1;

	R(0, 0) = c; R(0, 1) = -s;
	R(1, 0) = s; R(1, 1) = c;

	return R;
}

// creates 3x3 translation matrix
static CMat3f translate(float tx, float ty)
{
	CMat3f M(0.0f);
	M(0, 2) = tx;
	M(1, 2) = ty;
	M(2, 2) = 1;
	M(0, 0) = 1;
	M(1, 1) = 1;

	return M;
}

static CMat3f rotateAroundPoint3D(const CVec3f& P, float angle)
{
	//CMat3f T1 = translate(-P[0], -P[1]); // move p to origin
	CMat3f R = rotate(angle); // rotate around origin
	CMat3f T2 = translate(P[0], P[1]); // move back

	return T2 * R; //*T1;
}

// display callback function for EXERCISE 4
void displayExercise4(void)
{
	glClear(GL_COLOR_BUFFER_BIT);

	// define intial local homogenous positions 
	CVec3f sunPos(0.0f, 0.0f, 1.0f);
	CVec3f earthLocal(g_earthOrbitRadius, 0.0f, 1.0f);
	CVec3f moonLocal(g_moonOrbitRadius, 0.0f, 1.0f);

	// earth rotation around sun in homogeneous coordinates
	CMat3f Rearth = rotate(g_earthAngle);
	CVec3f earthPos = Rearth * earthLocal;

	// moon rotation around earth using homogeneous matrix
	CMat3f Rmoon = rotateAroundPoint3D(earthPos, g_moonAngle);
	CVec3f moonPos = Rmoon * moonLocal;

	g_sun.setPosition(CVec2f(sunPos[0], sunPos[1]));
	g_earth.setPosition(CVec2f(earthPos[0], earthPos[1]));
	g_moon.setPosition(CVec2f(moonPos[0], moonPos[1]));

	// draw bodies
	g_sun.draw();
	g_earth.draw();
	g_moon.draw();

	// In double buffer mode the last two lines should always be
	glFlush();
	glutSwapBuffers(); // swap front and back buffer
}

void keyboard(unsigned char key, int x, int y)
{
	switch (key) {
	case 'q':
	case 'Q':
		exit(0); // quit program
		break;
	case '1':
		glutDisplayFunc(displayExercise3);
		break;
	case '2':
		glutDisplayFunc(displayExercise4);
		break;
	default:
		// do nothing ...
		break;
	};
}

// The main
// Please do not change.
int main(int argc, char** argv)
{
	glutInit(&argc, argv);
	glutInitDisplayMode(GLUT_DOUBLE | GLUT_RGB);
	glutCreateWindow("Computer graphics assignment 2: 2d-Rotations (WS25/26)");
	glutReshapeWindow(g_WinWidth, g_WinHeight);

	init();	// init my variables first
	initGL();	// init the GL (i.e. view settings, ...)

	glutTimerFunc(10, timer, 0);
	glutReshapeFunc(reshape);			// is triggered on window size changes
	glutDisplayFunc(displayExercise3);	// is triggered to redraw the viewport/display
	glutKeyboardFunc(keyboard);			// is triggered on keyboard events

	// start main loop
	glutMainLoop();

	return 0;
}