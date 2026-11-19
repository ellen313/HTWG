#include <iostream>

#include "rendering.hpp"
#include "mapping.hpp"
#include "mandelbrot.hpp"
#include "glut.h"
#include "types.hpp"
#include "navigation.hpp"
#include "measurements.hpp"   // added

std::vector<RGB> g_colors_data;

int WIDTH = 400;
int HEIGHT = 400;

// Bereich des komplexen Koordinatensystems
double x_min = -2.0;
double x_max = 1.0;
double y_min = -1.5;
double y_max = 1.5;

int max_iter = 100;

void display() {
    drawdisplay();
}

int main(int argc, char **argv) {

    bool run_openmp_measurements = false; // set true to run measurements (no GUI)

    if (run_openmp_measurements) {
        openmp_resolutions();
        openmp_zoom_levels();   // keep this enabled for the zoom commit
        return 0;
    }

    // initialisierung von GLUT
    glutInit(&argc, argv);
    glutInitDisplayMode(GLUT_DOUBLE | GLUT_RGB); // keep your GUI mode
    glutInitWindowSize(WIDTH, HEIGHT);
    glutCreateWindow("Mandelbrot Set");

    glMatrixMode(GL_PROJECTION);
    glLoadIdentity();
    gluOrtho2D(0, WIDTH, 0, HEIGHT);

    // erstes mal rendern
    auto complex_values = mapping(WIDTH, HEIGHT, x_min, x_max, y_min, y_max);
    g_colors_data = mandelbrot_colors(complex_values, max_iter);
    render(g_colors_data, WIDTH, HEIGHT);

    glutDisplayFunc(display);
    glutKeyboardFunc(keyboard);

    glutMainLoop();
    return 0;
}
