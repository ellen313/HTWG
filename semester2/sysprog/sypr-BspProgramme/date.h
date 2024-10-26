//date.h

#ifndef DATE_H
#define DATE_H

struct date
{
    int day,
    int month,
    int year
};

typedef struct date date;

#define MAXDAY 31

extern date epoch;
void print_date(const date *d);

#endif