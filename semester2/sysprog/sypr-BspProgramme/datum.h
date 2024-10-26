//datum.h

#ifndef DATUM_H
#define DATUM_H

typedef struct class_datum datum;

datum* datum_heute(void);
datum* datum_vom(int t, int m, int j);
void datum_freigeben(datum *this_p);

void datum_ausgeben(datum *this_p);

#endif