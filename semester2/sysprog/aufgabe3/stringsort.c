#include <stdio.h>
#include <stdlib.h>
#include <stddef.h>
#include <string.h> //damit strxxx Funktionen bekannt
#include <time.h>

void bubblesort(int size, char *list[]); //bubblesort implementieren

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
    int n = atoi(argv[1]); //Arraygroesse

    if (n < 1)
    {
        printf("Anzahl muss mindestens 1 sein\n");
        return 1;
    }
    //------------------------------------------ Strings wuerfeln
    char **t = (char**) malloc(n* sizeof (char*)); //array mit groesse n allokieren

    if (t == NULL)
    {
        printf("Speicherreservierung fehlgeschlagen\n");
        return 1;
    }

    printf("Unsoriertes Array:\n");
    for (int i = 0; i < n; ++i)
    {
        int r = rand() % n; // % n begrenzt Zahl auf den Bereich von 0 bis n-1

        t[i] = (char*) malloc(m * sizeof (char));//jeden durchlauf speicher allokieren
        if (t[i] == NULL)
        {
            printf("Speicherreservierung fehlgeschlagen\n");
            //wenn fehlgeschlagen bereits allokierten speicher freigeben:
            for (int j = 0; j < i; ++j) //iteriert durch indizes bis i-1...
            {
                free(t[j]); //...und gibt speicher frei
            }
            free(t);//speicher fuer gesamtes array freigeben
            return 1;
        }
        sprintf(t[i], "%d", r); //zufallszahl in allokierten speicher schreiben
        printf("%s ", t[i]);
    }

    //---------------------------------------- Strings sortieren
    bubblesort(n, t);>

    //---------------------------------------- Strings ausgeben
    printf("\nSortiertes Array:\n");
    char *sa = (char*) malloc(n * m * sizeof (char)); //stringbuilder
    if (sa == NULL)
    {
        printf("Speicherreservierung fehlgeschlagen\n");
        return 1;
    }

    strcpy(sa, t[0]); //s.append(t[0])
    for (int i = 1; i < n; ++i)
    {
        if (strcmp(t[i], t[i - 1]) == 0)
        {
            strcat(sa, "*"); //anfuegen an sa (stringbuilder)
        }
        else
        {
            strcat(sa, " ");
            strcat(sa, t[i]);
        }
    }
    printf("%s\n", sa);

    for (int i = 0; i < n; ++i)
    {
        free(t[i]);
    }

    free(sa);
    free(t);
    return 0;
}
    //bubblesort algorithmus
    void bubblesort(int size, char *list[])//size: anzahl zeichenketten in list
    {                                      //list: soll sortiert werden
        for (int i = size; i > 1; --i)
        {
            for (int j = 0; j < i - 1; ++j)
            {
                if (strcmp(list[j], list[j + 1]) > 0)//> 0: j kommt nach j+1
                {
                    char *tmp = list[j + 1];
                    list[j + 1] = list[j];
                    list[j] = tmp;
                }
            }
        }
    }
    


   

