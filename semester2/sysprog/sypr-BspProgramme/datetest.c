//datetest.c

#include "date.h"

int main(void)
{
    print_data(&epoch);

    date d = {1, 9, 2000};
    print_date(&d);

    return 0;
}