#include <chrono>
#include <vector>
#include <iostream>
#include <fstream>
#include <string>

#include "mapping.hpp"
#include "mandelbrot.hpp"
#include "types.hpp"

extern int WIDTH;
extern int HEIGHT;
extern double x_min;
extern double x_max;
extern double y_min;
extern double y_max;
extern int max_iter;

struct ZoomLevel {
	std::string name;
	double x_min;
	double x_max;
	double y_min;
	double y_max;
};

void openmp_resolutions();
void openmp_zoom_levels();
