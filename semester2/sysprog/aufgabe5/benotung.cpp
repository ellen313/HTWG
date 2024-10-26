//benotung.cpp

#include "benotung.h"
#include <string>
#include <stdexcept>

//Membervariablen mit bester, schlechtester note
const benotung benotung::beste = benotung(10);
const benotung benotung::schlechteste = benotung(50);

benotung::benotung(int n) : note(n)
{
    if (n != 10 && n != 13 && n != 17 && n != 20 && n != 23
        && n != 27 && n != 30 && n != 33 && n != 37 && n != 40 && n != 50)
    {
        throw std::invalid_argument("unzulaessige Note " + std::to_string(n));
    }
}

int benotung::int_value() const //const: Funktion aendert den Zustand des Objekts, auf dem sie aufgerufen wird, nicht 
{
    //gibt Wert der Member-Variable note zurueck
    return note;
}

bool benotung::ist_bestanden() const
{
    //liefert true, wenn der Wert der Note kleiner gleich 40 ist
    return note <= 40;
}

bool operator==(const benotung& l, const benotung& r)
{
    //Adressen verglichen, wenn nicht gleich: wert note verglichen
    return &l == &r || l.note == r.note;
}
