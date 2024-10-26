//count.c

#include <stdio.h> //fopen, fgetc, fclose, ferror, perror
#include <stdint.h> //uintmax_t

int main(int argc, char *argv[])
{
    for (int i = 1; i < argc; ++i) //verarbeitet alle uebergebenen dateinamen
    {
        FILE *fp = fopen(argv[i], "r"); //jede datei wird versucht im lesemodus zu oeffnen
        if (fp == NULL)
        {
            perror(argv[i]); //oeffnen fehlgeschlagen: fehlermeldung
            continue; //naechster schleifenzyklus wird gestartet
        }

        uintmax_t n = 0;
        while (fgetc(fp) != EOF) // jedes Zeichen der Datei lesen, und Zaehler n inkrementieren, bis Dateiende erreicht
        {
            ++n
        }
        if (ferror(fp)) //ueberpruefen ob fehler beim lesen aufgetreteten
        {
            perror(argv[i]); //falls ja, fehlermeldung ausgeben
            fclose(fp); //datei schliessen
            continue; //naechsten schleifenzyklus starten
        }

        if (fclose(fp) != 0) 
        {
            perror(argv[i]);
            continue;
        }

        printf("%s: %ju Byte\n", argv[i], n); //wenn schlissen erfolgreich: Anzahl gelesener bytes ausgeben
    }

    return 0;
}