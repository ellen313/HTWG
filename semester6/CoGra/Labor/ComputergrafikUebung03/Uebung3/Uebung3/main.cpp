#include "Vector.h"
#include "Matrix.h"
#include "Planet.h"
#include <cmath>

#include <iostream>
#include "Color.h"
#include "glut.h"

// pre-init 
void reset();

// resolution of the main window
const int g_iWidth = 800;
const int g_iHeight = 800;

//global variable to tune the timer interval
int g_iTimerMSecs;

// coordinate system
float fFocus;
CVec4f eyePoint;
CVec4f viewDir;
CVec4f viewUp;

// init function
void init()
{
    g_iTimerMSecs = 10;
    reset();
}

// timer callback function
void timer(int value)
{
    glutPostRedisplay();
    glutTimerFunc(g_iTimerMSecs, timer, 0);
}

// funtion to initialize the view to otho-projection
void initGL()
{
    glViewport(0, 0, g_iWidth, g_iHeight);                                      // Establish viewport to cover entire window.

    glMatrixMode(GL_PROJECTION);                                                // Start modifying the projection matrix.
    glLoadIdentity();                                                           // Init projection matrix.
    glOrtho(-g_iWidth / 2, g_iWidth / 2, -g_iHeight / 2, g_iHeight / 2, 0, 1);  // Map abstract coords directly to window coords.

    glDrawBuffer(GL_BACK);
    glClearColor(0, 0, 0, 1);
}

void reset()
{
    fFocus = 300;
    eyePoint = new float[4] {0, 0, 0, 1};
    viewDir = new float[4] {0, 0, -1, 0};
    viewUp = new float[4] {0, 1, 0, 0};
}

/*This function projects a 3D point in view coordinates onto the image plane*/
CVec4f projectZ(float fFocus, CVec4f pView)
{
    //projection matrix
    CMat4f proj(
        new float[4][4]{
            {1, 0, 0, 0},
            {0, 1, 0, 0},
            {0, 0, 0, 0},
            {0, 0, 1 / -fFocus, 1} });

    CVec4f tmp(proj * pView);

    //perspective division
    tmp[0] /= tmp[3];
    tmp[1] /= tmp[3];
    tmp[2] = 0;
    tmp[3] = 1;

    return tmp;
}

// draw lines with BresenhamLine (function from assignment 1)
void BresenhamLine(CVec2i p1, CVec2i p2, Color c)
{
    glBegin(GL_POINTS);
    glColor3f(c.r, c.g, c.b);

    int x = p1[0];
    int y = p1[1];

    int delta_x = abs(p2[0] - p1[0]);
    int delta_y = abs(p2[1] - p1[1]);

    int sx = (p2[0] >= p1[0]) ? 1 : -1;
    int sy = (p2[1] >= p1[1]) ? 1 : -1;

    if (delta_y <= delta_x) {
        int d = 2 * delta_y - delta_x;
        int delta_e = 2 * delta_y;
        int delta_ne = 2 * (delta_y - delta_x);

        while (x != p2[0]) {
            glVertex2i(x, y);
            if (d >= 0) {
                y += sy;
                x += sx;
                d += delta_ne;
            }
            else {
                d += delta_e;
                x += sx;
            }
        }
    }
    else {
        int d = 2 * delta_x - delta_y;
        int delta_n = 2 * delta_x;
        int delta_ne = 2 * (delta_x - delta_y);

        while (y != p2[1]) {
            glVertex2i(x, y);
            if (d >= 0) {
                x += sx;
                y += sy;
                d += delta_ne;
            }
            else {
                d += delta_n;
                y += sy;
            }
        }
    }
    glVertex2i(p2[0], p2[1]);
    glEnd();
}

/*draws a cuboid with the already projected 2D points*/
void drawProjektedZ(CVec3f Points[8], Color c)
{
    CVec2i p[8];
    for (int i = 0; i < 8; i++)
    {
        p[i] = (new int[2] {(int)Points[i][0], (int)Points[i][1]});
    }

    BresenhamLine(p[0], p[1], c);
    BresenhamLine(p[1], p[2], c);
    BresenhamLine(p[2], p[3], c);
    BresenhamLine(p[3], p[0], c);

    BresenhamLine(p[4], p[5], c);
    BresenhamLine(p[5], p[6], c);
    BresenhamLine(p[6], p[7], c);
    BresenhamLine(p[7], p[4], c);

    BresenhamLine(p[0], p[4], c);
    BresenhamLine(p[1], p[5], c);
    BresenhamLine(p[2], p[6], c);
    BresenhamLine(p[3], p[7], c);
}

float normalize(CVec4f v)
{
    return sqrt(v[0] * v[0] + v[1] * v[1] + v[2] * v[2]);
}

CVec4f cross4(CVec4f v1, CVec4f v2)
{
    return CVec4f(
        new float[4] {
            v1[1] * v2[2] - v1[2] * v2[1],
            v1[2] * v2[0] - v1[0] * v2[2],
            v1[0] * v2[1] - v1[1] * v2[0],
             0});
}

/*transforms the point in world coordinates to view coordniates and projects it onto the image plane*/
CVec4f projectZallg(CMat4f matTransf, float fFocus, CVec4f pWorld)
{
    CVec4f pView = matTransf * pWorld;
    return projectZ(fFocus, pView);
}

CVec4f cart2hom(CVec3f v)
{
    return new float[4] {v[0], v[1], v[2], 1};
}

CVec3f hom2cart(CVec4f v)
{
    return new float[3] {v[0], v[1], v[2]};
}

/*transforms view coordinates to world coordinates*/
CMat4f getTransform(CVec4f ViewOrigin, CVec4f ViewDir, CVec4f ViewUp)
{
    CVec4f r3 = ViewDir; //z_v
    float lenZ = normalize(ViewDir);

    r3[0] /= lenZ;
    r3[1] /= lenZ;
    r3[2] /= lenZ;
    r3[3] = 0;

    CVec4f r1 = cross4(ViewUp, r3); // x ortho. z
    float lenX = normalize(r1);

    r1[0] /= lenX;
    r1[1] /= lenX;
    r1[2] /= lenX;
    r1[3] = 0;

    CVec4f r2 = cross4(r3, r1); //y_v -> y otho. z & y otho. x
    float lenY = normalize(r2);

    r2[0] /= lenY;
    r2[1] /= lenY;
    r2[2] /= lenY;
    r2[3] = 0;


    CMat4f rotationT = new float[4][4]{
        {r1[0], r1[1], r1[2], 0},
        {r2[0], r2[1], r2[2], 0},
        {r3[0], r3[1], r3[2], 0},
        {0, 0, 0, 1} };

    CVec4f t = rotationT * ViewOrigin;

    return new float[4][4]{
        {r1[0], r1[1], r1[2], t[0]},
        {r2[0], r2[1], r2[2], t[1]},
        {r3[0], r3[1], r3[2], t[2]},
        {0, 0, 0, 1} };
}

/*projects the 3D cuboid onto the image plane and draws it*/
void drawQuader(CVec3f Cuboid[8], float fFocus, Color c)
{

    CVec3f points[8];
    CMat4f M = getTransform(eyePoint, viewDir, viewUp);

    for (int i = 0; i < 8; i++)
    {
        CVec4f hom = cart2hom(Cuboid[i]);
        CVec4f proj = projectZallg(M, fFocus, hom);
        points[i] = hom2cart(proj);
    }

    drawProjektedZ(points, c);
}


// display callback function
void display(void)
{
    glClear(GL_COLOR_BUFFER_BIT);

    CVec3f cube[8];

    cube[0] = (new float[3] {20, 10, 20});           // left bottom
    cube[1] = (new float[3] {20, 170, 20});          // left top
    cube[2] = (new float[3] {190, 170, 20});         // right top
    cube[3] = (new float[3] {190, 10, 20});          // right bottom
    cube[4] = (new float[3] {20, 10, -30});          // left bottom
    cube[5] = (new float[3] {20, 170, -30});         // left top
    cube[6] = (new float[3] {190, 170, -30});        // right top
    cube[7] = (new float[3] {190, 10, -30});         // right bottom
    drawQuader(cube, fFocus, Color(1.0, 0.0, 0.0));   // red

    cube[0] = (new float[3] {35, -110, -60});        // left bottom
    cube[1] = (new float[3] {35, 0, -60});         // left top
    cube[2] = (new float[3] {80, 0, -60});         // right top
    cube[3] = (new float[3] {80, -110, -60});        // right bottom
    cube[4] = (new float[3] {35, -110, -85});        // left bottom
    cube[5] = (new float[3] {35, 0, -85});         // left top
    cube[6] = (new float[3] {80, 0, -85});         // right top
    cube[7] = (new float[3] {80, -110, -85});        // right bottom
    drawQuader(cube, fFocus, Color(1.0, 0.35, 1.0));  // purple

    cube[0] = (new float[3] {-45, -60, -30});        // left bottom
    cube[1] = (new float[3] {-45, -30, -30});        // left top
    cube[2] = (new float[3] {80, -30, -30});         // right top
    cube[3] = (new float[3] {80, -60, -30});         // right bottom
    cube[4] = (new float[3] {-45, -60, -55});        // left bottom
    cube[5] = (new float[3] {-45, -30, -55});        // left top
    cube[6] = (new float[3] {80, -30, -55});         // right top
    cube[7] = (new float[3] {80, -60, -55});         // right bottom
    drawQuader(cube, fFocus, Color(0.35, 0.35, 1.0)); //blue

    cube[0] = (new float[3] {120, 20, -40});         // left  bottom 
    cube[1] = (new float[3] {120, 100, -40});        // left  top  
    cube[2] = (new float[3] {200, 100, -40});        // right top   
    cube[3] = (new float[3] {200, 20, -40});         // right bottom
    cube[4] = (new float[3] {120, 20, -90});         // left  bottom
    cube[5] = (new float[3] {120, 100, -90});        // left  top   
    cube[6] = (new float[3] {200, 100, -90});        // right top   
    cube[7] = (new float[3] {200, 20, -90});         // right bottom
    drawQuader(cube, fFocus, Color(1.0, 0.5, 0.0));   //orange

    cube[0] = (new float[3] {-50, 0, 0});            // left bottom
    cube[1] = (new float[3] {-50, 80, 0});           // left top
    cube[2] = (new float[3] {0, 80, 0});             // right top
    cube[3] = (new float[3] {0, 0, 0});              // right bottom
    cube[4] = (new float[3] {-50, 0, -50});          // left bottom
    cube[5] = (new float[3] {-50, 80, -50});         // left top
    cube[6] = (new float[3] {0, 80, -50});           // right top
    cube[7] = (new float[3] {0, 0, -50});            // right bottom
    drawQuader(cube, fFocus, Color(0.35, 1.0, 0.35)); //green

    glFlush();
    glutSwapBuffers();
}

//----------------------------------------------------------------------------

CMat4f rotation(char axis, float angle)
{
    angle = angle * 3.142 / 180; // Convert to radians

    float c = cosf(angle);
    float s = sinf(angle);


    switch (axis)
    {
    case 'x':
    case 'X':
        return CMat4f(
            new float[4][4]{
                {1, 0, 0, 0},
                {0, c, -s, 0},
                {0, s, c, 0},
                {0, 0, 0, 1} });
    case 'y':
    case 'Y':
        return CMat4f(
            new float[4][4]{
                {c, 0, s, 0},
                {0, 1, 0, 0},
                {-s, 0, c, 0},
                {0, 0, 0, 1} });
    case 'z':
    case 'Z':
        return CMat4f(
            new float[4][4]{
                {c, -s, 0, 0},
                {s, c, 0, 0},
                {0, 0, 1, 0},
                {0, 0, 0, 1} });
    default:
        // Return an identity matrix if no valid axis is specified
        return CMat4f(
            new float[4][4]{
                {1, 0, 0, 0},
                {0, 1, 0, 0},
                {0, 0, 1, 0},
                {0, 0, 0, 1} });
    }
}

/*updates position and orientation of the view*/
void transformation(CMat4f transMat)
{
    eyePoint = transMat * eyePoint;
    viewDir = transMat * viewDir;
    viewUp = transMat * viewUp;
}

/*rotates view about its arbitrary axis*/
CMat4f rotationCam(char axis, float angle)
{   //transformation matrix for rotating about an arbitrary axis
    //R = | (1 - c) * x^2 + c    (1 - c) * xy - sz     (1 - c) * xz + sy    0 |
    //    | (1 - c) * xy + sz    (1 - c) * y^2 + c     (1 - c) * yz - sx    0 |
    //    | (1 - c) * xz - ys    (1 - c) * yz + sx     c + z ^ 2 * (1 - c)  0 |
    //    | 0                          0                      0             1 |

    CVec4f rot_axis; //vector for rotation axis 

    switch (axis)
    {
    case 'a':
        rot_axis = viewDir;
        break;
    case 'b':
        rot_axis = viewUp;
        break;
    case 'c':
        rot_axis = cross4(viewUp, viewDir);
        break;
    default:
        return CMat4f(
            new float[4][4]{
                {1, 0, 0, 0},
                {0, 1, 0, 0},
                {0, 0, 1, 0},
                {0, 0, 0, 1} });
        break;
    }

    float len = normalize(rot_axis);
    
    rot_axis[0] /= len;
    rot_axis[1] /= len;
    rot_axis[2] /= len;

    angle = angle * 3.142 / 180; // to radians

    float c = cosf(angle);
    float s = sinf(angle);

    float t = 1 - c;

    float x = rot_axis[0];
    float y = rot_axis[1];
    float z = rot_axis[2];


    return CMat4f(new float[4][4]{
        {t * x * x + c, t * x * y - s * z, t * x * z + s * y, 0},
        {t * x * y + s * z, t * y * y + c, t * y * z - s * x, 0},
        {t * x * z - s * y, t * y * z + s * x, t * z * z + c, 0}, 
        {0, 0, 0, 1}
    });
}

void keyboard(unsigned char key, int x, int y)
{
    float angle = 30;
    float translationStep = 30;
    float focusStep = 20;

    switch (key)
    {
    case 'q': // quit program
    case 'Q':
        exit(0); 
        break;
    case 'f': // - focus distance
        if (fFocus > 40)
        {
            fFocus -= focusStep;
        }
        break;
    case 'F': // + focus distance
        fFocus += focusStep;
        break;
    case 'x':
    case 'y':
    case 'z':
        transformation(rotation(key, -angle));
        break;
    case 'X':
    case 'Y':
    case 'Z':
        transformation(rotation(key, angle));
        break;
    case 'a':
        viewUp = rotationCam('a', -angle) * viewUp;
        break;
    case 'A':
        viewUp = rotationCam('a', angle) * viewUp;
        break;
    case 'b':
        viewDir = rotationCam('b', -angle) * viewDir;
        break;
    case 'B':
        viewDir = rotationCam('b', angle) * viewDir;
        break;
    case 'c':
        viewUp = rotationCam('c', -angle) * viewUp;
        viewDir = rotationCam('c', -angle) * viewDir;
        break;
    case 'C':
        viewUp = rotationCam('c', angle) * viewUp;
        viewDir = rotationCam('c', angle) * viewDir;
        break;
    
    case 'u':
        eyePoint[0]--;
        break;
    case 'U':
        eyePoint[0]++;
        break;
    case 'v':
        eyePoint[1]--;
        break;
    case 'V':
        eyePoint[1]++;
        break;
    case 'w':
        eyePoint[2]--;
        break;
    case 'W':
        eyePoint[2]++;
        break;
    case 'r':
    case 'R':
        reset();
        break;
    default:
        break;
    }
}


int main(int argc, char** argv)
{
    glutInit(&argc, argv);

    glutInitDisplayMode(GLUT_DOUBLE | GLUT_RGB);
    glutCreateWindow("Assignment 3");

    init();
    initGL();

    glutTimerFunc(10, timer, 0);
    glutKeyboardFunc(keyboard);
    glutDisplayFunc(display);

    glutReshapeWindow(800, 800);

    glutMainLoop();

    return 0;
}
