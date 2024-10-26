//fallunterscheidung.c

#include <stdio.h>

int main(void)
{
    printf("Monat eingeben [1-12]: ");

    int month;
    if (scanf("%d", &month) < 1)
    {
        month = 0;
    }

    switch (month)
    {
    case 2:
        printf("28 oder 29 Tage\n");
        break;
    case 4:
    case 6:
    case 9:
    case 11:
        printf("30 Tage\n");
        break;
    case 1:
    case 3:
    case 5:
    case 7:
    case 8:
    case 10:
    case 12:
        printf("31 Tage\n");
        break;
    default:
        fprintf(stderr, "Eingabefehler!\n");
    }

    return 0;
}