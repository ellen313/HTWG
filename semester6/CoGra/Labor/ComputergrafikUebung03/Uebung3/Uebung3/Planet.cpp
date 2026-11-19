/////////////////////////////////////////////////////////////////////////////
//																									//
// Code framework for lecture computer graphics WS 2025/26 assignment 2  	//
//																									//
// (c) Georg Umlauf																			//
//																									//
/////////////////////////////////////////////////////////////////////////////


#include "Planet.h"
#include "glut.h"

//
// Implement HERE your Bresenham algorithm for circles 
//

// draw point for every octant
static inline void drawCircle(int cx, int cy, int x, int y)
{
	glVertex2i(cx + x, cy + y);
	glVertex2i(cx + y, cy + x);
	glVertex2i(cx - x, cy + y);
	glVertex2i(cx - y, cy + x);
	glVertex2i(cx - x, cy - y);
	glVertex2i(cx - y, cy - x);
	glVertex2i(cx + x, cy - y);
	glVertex2i(cx + y, cy - x);
}

void Planet::draw() const
{
	const int cx = int(std::round(position[0]));
	const int cy = int(std::round(position[1]));
	const int r = std::max(1, radius);

	glBegin(GL_POINTS);
	glColor3d(color.r, color.g, color.b);

	int x = 0;
	int y = r;
	int d = 5 - 4 * r;

	drawCircle(cx, cy, x, y);

	while (y > x)
	{
		if (d >= 0)
		{
			int delta_SE = 4 * (2 * (x - y) + 5);
			d += delta_SE;
			x++;
			y--;
		}
		else
		{
			int delta_E = 4 * (2 * x + 3);
			d += delta_E;
			x++;
		}
		// reflecions
		drawCircle(cx, cy, x, y);
	}

	glEnd();
}