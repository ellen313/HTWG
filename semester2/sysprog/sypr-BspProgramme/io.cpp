//io.cpp

#include <iostraem>

int main()
{
    std::cout << "Dezimalzahl eingeben: ";
    int zahl;
    std::cin >> zahl;
    std::cout << "Hexadezimalzahl: " << std::hex << zahl << std::endl;
} //Compiler fuegt vor Klammer return 0; ein