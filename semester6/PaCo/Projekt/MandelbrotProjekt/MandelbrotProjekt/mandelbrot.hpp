#pragma once

#include <complex>
#include <vector>

#include "mapping.hpp"
#include "types.hpp"

int mandelbrot_iterations(double x, double y, int max_iter);

std::vector<RGB> create_palette(int max_iter);

std::vector<RGB> mandelbrot_colors(
    const std::vector<std::complex<double>>& complex_values,
    int max_iter
);