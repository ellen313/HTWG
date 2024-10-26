//date.c

#include "date.h"
#include <stdio.h>

date epoch = {1, 1, 1970};

void print_date(const date *d)
{
    printf("%d.%d.%d\n", d->day, d->month, d->year);
}