//operator-overloading.cpp

#include <iostream>

enum jahreszeit {fruehling, sommer, herbst, winter};

std::ostream& operator<<(std::ostream& os, jahreszeit j);

int main()
{
    //in c++ kann jahreszeit ohne enum geschrieben werden. In c waere typedef noetig
    jahreszeit j = sommer;
    std::cout << j << '\n';
    operator<<(std::cout, j) << '\n';
}

std::ostream& operator<<(std::ostream& os, jahreszeit j)
{
    static const char *jahreszeiten[] = {"Fruehling", "Sommer", "Herbst", "Winter"};
    os << jahreszeiten[j];
    return os;
}