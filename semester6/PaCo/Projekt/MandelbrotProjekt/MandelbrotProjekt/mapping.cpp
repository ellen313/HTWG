#include "mapping.hpp"

//Mapping der Pixelkooridnaten in komplexe Zahlen
std::vector<std::complex<double>> mapping(
    int size_px, //Anzahl Pixel
    int size_py,
    double real_min, //min Wert auf realen x-Achse
    double real_max,
    double imag_min, //min Wert auf imaginären y-Achse 
    double imag_max
) {

    std::vector<std::complex<double>> complex_values;
    complex_values.reserve(size_px * size_py);

    for (int i = 0; i < size_py; i++) {
        double imag_t = 1.0 - static_cast<double>(i) / (size_py - 1.0);

        double imag_value = interpolate(imag_min, imag_max, imag_t); //Interpolation für Imaginärteil

        for (int j = 0; j < size_px; ++j) {
            double real_t = static_cast<double>(j) / (size_px - 1.0);
            double real_value = interpolate(real_min, real_max, real_t); //Interpolation für Realteil

            complex_values.emplace_back(real_value, imag_value);
        }
    }
    return complex_values;
}
