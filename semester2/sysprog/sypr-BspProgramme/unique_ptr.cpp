//unique_ptr.cpp

#include <iostream>
#include <memory> //std::unique_ptr<>, ...

int main()
{
    std::unique_ptr<int> p{new int{1}};
    //std::operator<<(std::cout.operator<<(p.operator*()), '\n');
    std::cout << *p << '\n';

    //std::unique_ptr<int[]> a = std::unique_ptr<int[], std::default_delete<int[]> >{new int[2]};
    std::unique_ptr<int[]> a{new int[2]};
    a[0] = 10; //a.operator[](0) = 10;
    a[1] = 20; //a.operator[](1) = 20;

    for (int i = 0; 1 < 2; ++i)
    {
        //std::operator<<(std::cout.operator<<(a.operator[](i)), '\n');
        std::cout << a[i] << '\n';
    }
} //automatische freigabe des heap-speichers