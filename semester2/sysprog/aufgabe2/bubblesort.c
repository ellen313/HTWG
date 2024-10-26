#include <stdio.h>
#include <stdlib.h>
#include <stddef.h>
#include <time.h> //wie java Bibliothek; damit Compiler time erkennt

int main(int argc, char *argv[]) //argc fuer args.length()
{
    if (argc != 2) //weil Programmname und weiteres Argument uebergeben werden muessen
    {
        printf("Aufruf: ./Bubblesort Anzahl\n");
        return 1; //return 1, wenn true ist
    }

    //Integer.parseInt(args[0]) ist C Ausdruck atoi(argv[1])
    int n = atoi(argv[1]);

    int *a = (int*) malloc(n* sizeof (int));

    //Fehlerbehandlung nach Speicherreservierung
    if (a == NULL)
    {
        printf("Speicherreservierung fehlgeschlagen\n");
        return 1;
    }

    /*
    wenn die Adresse des i ten Elements als Zahl eingescannt wurde -> 1
    n ist die Anzahl an eingegeben Zahlen
    */
    printf("Bitte %d ganze Zahlen eingeben: ", n);
    int k = 0;

    for (int i = 0; i < n; i++)
    {
        if (scanf("%d", &a[i]) == 1)
        {
            k++;
        }   
    }

    //Zufallsgenerator der C Standardbibliothek
    srand((unsigned int) time(NULL));
    for (int i = k; i < n; i++)
    {
        a[i] = rand(); //rand() ruft naechstes Element auf
        printf("%d\n", a[i]);
    }

    for (int i = n; i > 1; --i)
    {
        //groessten Wert nach hinten schieben
        for (int j = 0; j < i - 1; ++j)
        {
            if (a[j] > a[j + 1])
            {
                //Werte tauschen
                int tmp = a[j + 1];
                a[j + 1] = a[j];
                a[j] = tmp;
            }
        }
    }

    printf("Sortierte Zahlenfolge:\n");
    for (int i = 0; i < n; i++)
    {
        printf("%d\n", a[i]);
    }

    //Freigabe des Speichers
    free(a);
    return 0;
}