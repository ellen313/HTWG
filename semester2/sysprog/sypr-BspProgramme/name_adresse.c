//name_adresse.c

#include <stdio.h>
#include <stdlib.h> //malloc calloc free

int main(void)
{
    {
        //Speichernutzung per Wertvariable:
        int a = 0;
        int b = 1;

        b = a;

        printf("a = %d\n", a); // a = 0;
        printf("b = %d\n", b); // b = 0;
    }

    {
        //Speichernutzung per Zeigervariable:
        /*
        zeiger werden dynamisch allokiert
        */
        int *a = (int*) calloc(1, sizeof (int));//reserviert sepciher und initialisiert alle bits auf 0
        int *b = (int*) malloc(sizeof (int));//bits koennen hier einen undefinierten Wert haben

        if (a == NULL || b == NULL) //wenn zeiger auf NULL zeigt schlaegt allokierung fehl
        {
            printf("Speicherallokierungsfehler!");
            return 1;
        }
        //wert von b auf wert von a gesetzt
        *b = 1;
        *b = *a; //a wert wurde nicht explizit initialisiert
        //b wird auf 0 gesetzt:Speicher fuer a mit calloc allokiert -> Wert 0

        printf("*%p = %d\n", (void*) a, *a);
        printf("*%p = %d\n", (void*) b, *b);

        //Speicherlecks vermeiden mti Freigabe
        //gibt in C keinen Garbage Collector wi in java
        free(a);
        free(b);
    }
    return 0;
}