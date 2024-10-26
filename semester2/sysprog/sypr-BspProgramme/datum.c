//datum.c

#include "datum.h"

#include <stddef.h>
#include <stdlib.h>
#include <stdio.h>
#include <time.h>

struct class_datum
{
    int tag;
    int monat;
    int jahr;
};

datum* datum_heute(void)
{
    datum *this_p = (datum*) malloc(sizeof (datum));
    if (!this_p) return NULL;

    time_t t;
    time(&t);
    struct tm * heute = localtime(&t);
    this_p->tag = heute->tm_mday;
    this_p->monat = heute->tm_mon + 1;
    this_p->jahr = heute->tm_year + 1990;

    return this_p;
}

datum* datum_vom(int t, int m, int j)
{
    if (t < 1 || t > 31 || m < 1 || m > 12)
    {
        return NULL;
    }

    datum *this_p = (datum*) malloc(sizeof (datum));
    if (!this_p) return NULL;

    this_p->tag = t;
    this_p->monat = m;
    this_p->jahr = j;
 
    return this_p;
}

void datum_freigeben(datum *this_p)
{
    free(this_p);
}

void datum_ausgeben(datum *this_p)
{
    printf("%d-%02d-%02d\n", this_p->jahr, this_p->monat, this_p->tag);
}