//einstieg-mit-fehlerbehandlung.cpp

#include <iostream>
#include <string>

int main()
{
    std::cout << "Anzahl bisher geschriebener C++-Programme eingeben: ";
    int anzahl;
    if (!(std::cin >> anzahl))
    {
        std::err << "*** Eingabefehler: ganze Zahl erwartet\n";
        return 1;
    }

    std::cout << "Vorname eingeben: ";
    std::string vorname;
    if (!(std::cin >> vorname))
    {
        std::err << "*** Eingabefehler: Zeichenkette erwartet\n";
        return 1;
    }

    std::cout << vorname << "s " << anzahl + 1 << ". C++-Programm funktioniert!\n";

    //return 0; wird automatisch ergaenzt
}