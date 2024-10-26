//intsum.c

#include <stdio.h>

int main(void)
{
    int sum = 0;

    printf("Ganze Zahlen eingeben (Ende mit Strg-D):\n");

    int number;
    /*
    scanf ist C-Entsprechung zu hasNext und next-Methoden 
    rueckgabewert ist 1 wenn einlesen erfolgreich
    sonst 0. -1 bei eingabeende
    */
    while (scanf("%d\n", &number) > 0)
    {
        sum += number;
    }

    printf("Summe: %d\n", sum);

    return 0;
}