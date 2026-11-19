#include "measurements.hpp"


std::string path = "../../analysis/";

void openmp_resolutions()
{
	std::vector<int> resolutions = { 400, 600, 800, 1000, 1200 };

	std::ofstream csv(path + "openmp_resolution.csv");
	csv << "width;height;time_ms\n";

	for (int res : resolutions) {
		WIDTH = res;
		HEIGHT = res;

		auto complex_values = mapping(WIDTH, HEIGHT, x_min, x_max, y_min, y_max);
		auto start = std::chrono::high_resolution_clock::now();
		auto colors = mandelbrot_colors(complex_values, max_iter);
		auto end = std::chrono::high_resolution_clock::now();

		double duration_ms = std::chrono::duration<double, std::milli>(end - start).count();

		csv << WIDTH << ";" << HEIGHT << ";" << duration_ms << "\n";

		std::cout << "Resolution: " << WIDTH << "x" << HEIGHT << " - Time: " << duration_ms << " ms" << std::endl;
	}

	csv.close();
}

void openmp_zoom_levels() {

	std::vector<ZoomLevel> zooms = {
		{"zoom1x", -2.0, 1.0, -1.5, 1.5},
		{"zoom2x", -1.0, 0.0, -0.5, 0.5},
		{"zoom5x", -0.75, -0.5, 0.15, 0.35},
	};


	std::ofstream csv(path + "openmp_zoom.csv");
	csv << "zoom_level;x_min;x_max;y_min;y_max;time_ms\n";

	for (auto& z : zooms) {
	
		auto complex_values = mapping(WIDTH, HEIGHT, z.x_min, z.x_max, z.y_min, z.y_max);

		auto start = std::chrono::high_resolution_clock::now();
		auto colors = mandelbrot_colors(complex_values, max_iter);
		auto end = std::chrono::high_resolution_clock::now();

		double duration_ms = std::chrono::duration<double, std::milli>(end - start).count();

		csv << z.name << ";" << z.x_min << ";" << z.x_max << ";" << z.y_min << ";" << z.y_max << ";" << duration_ms << "\n";
		std::cout << z.name << " : " << duration_ms << " ms" << std::endl;
	}
	csv.close();
}
