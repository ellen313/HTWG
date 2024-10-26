//qsort.c

#include <stdio.h>
#include <stdlib.h> //qsort

int intcmp(const void *p, const void *q);

int main(void)
{
    //-------------------------------- Arraygroesse einlesen
    printf("Anzahl zu sortierender Werte eingeben:\n");
    int int_array_size;
    if (scanf("%d", &int_array_size) != 1)
    {
        fprintf(stderr, "Eingabefehler!\n");
        return 1;
    }

    //-------------------------------- Zahlen einlesen
    int *int_array = malloc(sizeof (int) * int_array_size);
    printf("%d ganze Zahlen eingeben:\n", int_array_size);

    for (int i = 0; i < int_array_size; ++i)
    {
        if (scanf("%d", &int_array[i]) != 1)
        {
            fprintf(stderr, "Eingabefehler!\n");
            return 1;
        }
    }

    //------------------------------- Zahlen ausgeben
    printf("Sortierte Zahlenfolge:\n");

    for (int i = 0; i < int_array_size; ++i)
    {
        printf("%d", int_array[i]);
    }

    printf("\n");

    //-------------------------------- Array freigeben
    free(int_array);

    return 0;
}

/*
* intcmp
*
* Vergleichsfunktion fuer qsort muss liefern:
* > 0, wenn der erste Wert groesser ist
* < 0, wenn der erste Wert kleiner ist
* 0, wenn beide Werte gleich sind
*/

int intcmp(const void *p, const void *q)
{
    const int *left = (const int *) p;
    const int *right = (const int *) q;

    if (*left > *right) return 1;
    if (*left < *right) return -1;
    return 0;
}

