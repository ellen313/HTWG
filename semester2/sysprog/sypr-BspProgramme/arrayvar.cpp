//arrayvar.cpp

#include <iostream>
#include <array> //std::array, sze, operator[ ], ...
#include <vector> //std::vector, size, operator[ ], ...

int main()
{
    std::array<double, 4> a = {3.625, 3.648, 3.853, 4.042};
    for (std::size_t i = 0; i < a.size(); ++i)
    {
        //std::cout.operator<<(a.operator[](i)).operator<<(std::endl);
        std::cout << a[i] << std::endl;
    }

    std::cout << "sizeof a = " << sizeof a << '\n';

    std::vector<double> v = {3.625, 3.648, 3.853, 4.042};
    for (std::size_t i = 0; i < v.size(); ++i)
    {
        //std::cout.operator<<(v.operator[](i)).operator<<(std::endl);
        std::cout << v[i] << std::endl;
    }

    std::cout << "sizeof v = " << sizeof v << '\n';
}