//enumvar.cpp

#include "month.h" //htwg::month, htwg::oct, ...
using namespace htwg;

#include <iostream> //std::cout, std::oct, ...
using namespace std;

int main()
{
    month m = htwg::oct; //month eindeutig htwg::month, oct mehrdeutig

    //-------------------- print variable value
    cout << "m = " << m << '\n'; //cout eindeutig std::cout

    //-------------------- print variable address
    cout << "&m = " << &m << '\n';
    
    //-------------------- print variable size
    cout << "sizeof m = " << sizeof m << '\n';
}