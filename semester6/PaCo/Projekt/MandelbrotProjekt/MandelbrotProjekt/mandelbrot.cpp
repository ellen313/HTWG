#include "mandelbrot.hpp"

#include <complex>
#include <vector>
#include <cmath>
#include <algorithm>
#include <omp.h>

// verschiedene Formen des Mandelbrots
enum class MandelType {
    Mandelbrot, // z -> z^2 + c (normales Mandelbrot-Set)
    Multibrot3, // z -> z^3 + c (Mandelbrot-Set)
    Multibrot4, // z -> z^4 + c
    Mandelbar2, // z -> conj(z)^2 + c (Mandelbar ist mit Konjugation)
    Mandelbar3, // z -> conj(z)^3 + c
    Mandelbar4 // z -> conj(z)^4 + c
};

//constexpr MandelType MANDEL_TYPE = MandelType::Mandelbrot;
constexpr MandelType MANDEL_TYPE = MandelType::Multibrot3;
//constexpr MandelType MANDEL_TYPE = MandelType::Multibrot4;
//constexpr MandelType MANDEL_TYPE = MandelType::Mandelbar2;
//constexpr MandelType MANDEL_TYPE = MandelType::Mandelbar3;
//constexpr MandelType MANDEL_TYPE = MandelType::Mandelbar4;

inline std::complex<double>
iterate(const std::complex<double>& z,
    const std::complex<double>& c)
{
    switch (MANDEL_TYPE) {
    case MandelType::Mandelbrot:
        return z * z + c;

    case MandelType::Multibrot3:
        return z * z * z + c;

    case MandelType::Multibrot4:
        return z * z * z * z + c;

    case MandelType::Mandelbar2: {
        auto w = std::conj(z);
        return w * w + c;
    }

    case MandelType::Mandelbar3: {
        auto w = std::conj(z);
        return w * w * w + c;
    }

    case MandelType::Mandelbar4: {
        auto w = std::conj(z);
        return w * w * w * w + c;
    }
    }
    return z;
}

int mandelbrot_iterations(double x, double y, int max_iter) {
    std::complex<double> c(x, y);
    std::complex<double> z(0.0, 0.0);

    int iter = 0;
    while (std::norm(z) <= 4.0 && iter < max_iter) {
        z = iterate(z, c);
        ++iter;
    }

    return iter;
}

// Farbpalette anhand der max Iterationen erzeugen; logarithmisch skaliert
std::vector<RGB> create_palette(int max_iter) {
    std::vector<RGB> color_palette(max_iter + 1);

    // Vermeide Division durch 0 bei log(max_iter)
    const double denom = (max_iter > 1)
        ? std::log(static_cast<double>(max_iter))
        : 1.0;

    for (int i = 0; i <= max_iter; ++i) {
        double j = (i == 0)
            ? 3.0
            : 3.0 * (std::log(static_cast<double>(i)) / denom); // Zielbereich ungefähr [0,3]

        if (j < 1.0) {
            color_palette[i] = {
                0,
                static_cast<unsigned char>(255.0 * j),
                0
            };
        }
        else if (j < 2.0) {
            color_palette[i] = {
                static_cast<unsigned char>(255.0 * (j - 1.0)),
                255,
                0
            };
        }
        else {
            color_palette[i] = {
                static_cast<unsigned char>(255.0 * (j - 2.0)),
                255,
                255
            };
        }
    }

    return color_palette;
}

// Farben smoothing und Farben Ausgabe
std::vector<RGB> mandelbrot_colors(const std::vector<std::complex<double>>& complex_values,
    int max_iter) {

    const size_t n = complex_values.size();

	std::vector<RGB> output(n); // size = n

    const auto color_palette = create_palette(max_iter);

	#pragma omp parallel for schedule(static)
	for (long long i = 0; i < static_cast<long long>(n); ++i) {
		const auto &c = complex_values[static_cast<size_t>(i)];

		std::complex<double> z(0.0, 0.0);
		int iteration = 0;

		while (std::norm(z) <= 4.0 && iteration < max_iter) {
			z = iterate(z, c);
			++iteration;
		}

		double smoothing_i = static_cast<double>(iteration);

		if (iteration < max_iter) {
			const double norm_z = z.real() * z.real() + z.imag() * z.imag();
			if (norm_z > 0.0) {
				const double log_zn = std::log(norm_z) / 2.0;
				const double smoothing_factor = 
					std::log(log_zn / std::log(2.0)) / std::log(2.0);
				smoothing_i = iteration + 1.0 - smoothing_factor;
			}
		}

		int idx1 = static_cast<int>(smoothing_i);
		int idx2 = idx1 + 1;
		const double t = smoothing_i - static_cast<double>(idx1);

		if (idx1 < 0) idx1 = 0;
		if (idx1 > max_iter) idx1 = max_iter;

		if (idx2 < 0) idx2 = 0;
		if (idx2 > max_iter) idx2 = max_iter;

		const RGB c1 = color_palette[idx1];
		const RGB c2 = color_palette[idx2];

		output[static_cast<size_t>(i)] = {
			static_cast<unsigned char>(interpolate<double>(c1.r, c2.r, t)),
			static_cast<unsigned char>(interpolate<double>(c1.g, c2.g, t)),
			static_cast<unsigned char>(interpolate<double>(c1.b, c2.b, t))
		};
	}
	return output;
}
