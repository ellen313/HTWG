#include "rendering.hpp"
#include "mapping.hpp"
#include "mandelbrot.hpp"
#include "types.hpp"
#include "navigation.hpp"

#include <iostream>
#include <vector>
#include <ctime>
#include <sstream>
#include <iomanip>
#include <opencv2/opencv.hpp>

extern std::vector<RGB> g_colors_data;

extern int WIDTH;
extern int HEIGHT;

extern double x_min;
extern double x_max;
extern double y_min;
extern double y_max;
extern int max_iter;

void zoom(double z_factor)
{
    double x_center = (x_min + x_max) / 2.0;
    double y_center = (y_min + y_max) / 2.0;

    double width = (x_max - x_min) * z_factor;
    double hight = (y_max - y_min) * z_factor;

    x_min = x_center - width / 2;
    x_max = x_center + width / 2;
    y_min = y_center - hight / 2;
    y_max = y_center + hight / 2;
}

void panning(double px, double py) {
    double width = (x_max - x_min);
    double height = (y_max - y_min);

    x_max += px * width;
    x_min += px * width;

    y_max += py * height;
    y_min += py * height;
}

void save_image()
{
    if (g_colors_data.size() != WIDTH * HEIGHT)
    {
        std::cerr << "ERROR: g_colors_data size mismatch!"
            << g_colors_data.size() << " != "
            << WIDTH * HEIGHT << std::endl;
        return;
    }

    std::time_t now = std::time(nullptr);
    std::tm ltm;
    localtime_s(&ltm, &now);

    std::ostringstream filename;
    filename << "out/mandelbrot_"
        << (1900 + ltm.tm_year) << "-"
        << std::setw(2) << std::setfill('0') << (1 + ltm.tm_mon) << "-"
        << std::setw(2) << std::setfill('0') << ltm.tm_mday << "_"
        << std::setw(2) << std::setfill('0') << ltm.tm_hour << "-"
        << std::setw(2) << std::setfill('0') << ltm.tm_min << "-"
        << std::setw(2) << std::setfill('0') << ltm.tm_sec
        << ".png";

    cv::Mat img(HEIGHT, WIDTH, CV_8UC3);

    for (int y = 0; y < HEIGHT; ++y)
    {
        for (int x = 0; x < WIDTH; ++x)
        {
            int i = y * WIDTH + x;
            const RGB& c = g_colors_data[i];

            cv::Vec3b& pixel = img.at<cv::Vec3b>(y, x);
            pixel[0] = c.b;
            pixel[1] = c.g;
            pixel[2] = c.r;
        }
    }

    bool result = cv::imwrite(filename.str(), img);

    if (result)
        std::cout << "Image saved as: " << filename.str() << std::endl;
    else
        std::cerr << "Error saving the PNG with OpenCV!" << std::endl;
}

void keyboard(unsigned char input, int x, int y) {

    if (input == '+') zoom(0.7);
    else if (input == '-')zoom(1.25);
    else if (input == 'q') exit(0);

    else if (input == 'w') panning(0, 0.08); 
    else if (input == 'a') panning(-0.08, 0); 
    else if (input == 's') panning(0, -0.08);
    else if (input == 'd') panning(0.08, 0);

    auto complex_values = mapping(WIDTH, HEIGHT, x_min, x_max, y_min, y_max);

    std::cout << "mapping size = " << complex_values.size() << std::endl;

    g_colors_data = mandelbrot_colors(complex_values, max_iter);
    std::cout << "colors size = " << g_colors_data.size() << std::endl;
    render(g_colors_data, WIDTH, HEIGHT);

    if (input == 'p') save_image();
}

