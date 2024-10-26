//do-schleife

#include <stdio.h>

int main(void)
{
    int n = 0;

    //Dezimalzahl einlesen
    do
    {
        printf("Zahl zwischen 0 und 255 eingeben: ");
    }
    //solange wiederholen bis eingabe gueltig
    while (scanf("%d", &n) == 1 && (n < 0 || n >255));

    //Binaerzahl ausgeben
    printf("       "); //7 leerzeichen

    do
    {
        printf("%d\b\b", n % 2);
        n /= 2;
    }
    //solange wiederholt bis n nicht mehr groesser als 0
    while (n > 0);

    printf("\n");

    return 0;
}