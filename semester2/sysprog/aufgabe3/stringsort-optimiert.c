#include <stdio.h>
#include <stdlib.h>
#include <stddef.h>
#include <string.h>
#include <time.h>

void bubblesort(void *p, size_t n, size_t size, //zeiger auf array, anzahl elemente, groesse elements
                int (*cmp)(const void*, const void*));//funktionszeiger auf Vergleichsfunktion

int main(int argc, char *argv[])
{
    //-------------------------------------- Arraygroesse bestimmen

    if (argc != 2)
    {
        printf("Aufruf: ./Stringsort Anzahl\n");
        return 1;
    }

    srand((unsigned int) time(NULL));
    int m = strlen(argv[1])+ 1; // Stringendzeichen '\0'
    int n = atoi(argv[1]);

    if (n < 1)
    {
        printf("Anzahl muss mindestens 1 sein\n");
        return 1;
    }
    char *t = (char*) malloc(n* m* sizeof (char));

    if (t == NULL)
    {
        printf("Speicherreservierung fehlgeschlagen\n");
        return 1;
    }

    printf("Unsoriertes Array:\n");
    for (int i = 0; i < n; ++i)
    {
        int r = rand() % n;
        sprintf(t + i* m, "%d", r);
        printf("%s ", t + i* m);
    }
    printf("\n");
    //---------------------------------------- Strings sortieren
    bubblesort(t, n, m, (int (*)(const void*, const void*)) strcmp);

    //---------------------------------------- Strings ausgeben
    printf("Sortiertes Array:\n");
    char *sa = (char*) malloc(n* m* sizeof (char)); //stringbuilder
    if (sa == NULL)
    {
        printf("Speicherreservierung fehlgeschlagen\n");
        return 1;
    }

    strcpy(sa, t);
    printf("%s\n", sa);
    for (int i = 1; i < n; ++i)
    {
        if (strcmp(t + 1* m, t + (i - 1)* m) == 0)
        {
            strcat(sa, "*"); //amfuegen an sa (stringbuilder)
        }
        else
        {
            strcat(sa, " ");
            strcat(sa, t + i* m);
        }
    }
    printf("%s\n", sa);


    free(sa);
    free(t);
    return 0;
}
    
//bubblesort algorithmus
void bubblesort(void *p, size_t n, size_t size,//p auf anfang vom array
                int (*cmp)(const void*, const void*))//void: mit versch datentypen arbeiten
{
    void* tmp = malloc(size); //zeiger auf temporaeren speicherplatz
    for (int i = n; i > 1; --i)
    {
        for (int j = 0; j < i - 1; ++j)
        {
            void *ln = ((char*) p) + j * size;
            void *rn = ((char*) p) + (j + 1) * size;

            if (cmp(ln, rn) > 0)
            {
            memcpy(tmp, rn, size);
            memcpy(rn, ln, size);
            memcpy(ln, tmp, size);
            }
        }
    }
    free(tmp);
}
