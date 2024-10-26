//heap.cpp

#include <iostream>
int main()
{
    //dynamische allokation fuer eine einzelne variable
    int *p = new int(1);
    std::cout << *p << '\n';
    delete p;

    //dynamische allokation fuer array mit 2 elementen
    int *a = new int[2];
    a[0] = 10;
    a[1] = 20;

    //ausgabe der array-elemente
    for (int i = 0; i < 2; ++i)
    {
        std::cout << a[i] << '\n';
    }

    delete[] a; //dynamische Speicherfreigabe fuer das array
}