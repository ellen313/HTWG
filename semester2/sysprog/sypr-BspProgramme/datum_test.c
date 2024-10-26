//datum_test.c

#include "datum.h"

int main()
{
    d = datum_heute();
    if (d)
    {
        datum_ausgeben(d);
        datum_freigeben(d);
    }

    d = datum_vom(30, 1, 2018);
    if (d)
    {
        datum_ausgeben(d);
        datum_freigeben(d);
    }

    return 0;
}