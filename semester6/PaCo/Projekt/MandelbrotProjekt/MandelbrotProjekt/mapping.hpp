#pragma once

#include <complex>
#include <vector>

// interpolation
template <typename T>
T interpolate(T v0, T v1, double t) {
    return v0 * (1.0 - t) + v1 * t;
}

std::vector<std::complex<double>> mapping(
    int size_px,   // pixel width
    int size_py,   // pixel height
    double real_min,
    double real_max,
    double imag_min,
    double imag_max
);
