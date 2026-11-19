#include "rendering.hpp"
#include "types.hpp"

static const std::vector<RGB>* g_colors = nullptr;
static int g_width = 0;
static int g_height = 0;

void drawdisplay() {
    if (!g_colors) return; 

    const size_t expected = static_cast<size_t>(g_width) * g_height;
    if (g_colors->size() < expected) return;

    glClear(GL_COLOR_BUFFER_BIT);
    glBegin(GL_POINTS);

    for (int y = 0; y < g_height; ++y) {
        for (int x = 0; x < g_width; ++x) {
            int idx = y * g_width + x;
            const RGB c = (*g_colors)[idx];
            glColor3ub(c.r, c.g, c.b);
            glVertex2i(x, g_height - 1 - y);
        }
    }

	glEnd();
	glutSwapBuffers();
}

void render(const std::vector<RGB>& colors, int WIDTH, int HEIGHT) {
	g_colors = &colors;
	g_width = WIDTH;
	g_height = HEIGHT;

	glutPostRedisplay();
}
 