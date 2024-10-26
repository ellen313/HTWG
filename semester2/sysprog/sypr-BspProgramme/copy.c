//copy.c

#define _POSIX_C_SOURCE 1 //code basiert auf POSIX Standards. 1: Mit der ersten Version kompatibel

#include <stdio.h> //fprintf
#include <string.h> //strerror

#include <fcntl.h> //open, O_RDONLY, O_WRONLY, O_CREAT, O_EXCL
#include <sys/stat.h> //mode_t, S_IRUSR | S_IWUSR | S_IRGRP | S_IROTH
#include <unistd.h> //read, write, fsync
#include <errno.h> //errno

int main(void)
{
    if (argc != 3) //Programm erwartet genau 2 argumente: Quelle und Datei
    {
        fprintf(stderr, "Aufruf: %s Quelle Ziel\n", argv[0]); //wenn nicht genau 2 uebergeben: Fehlermeldung
        return 1; //mit exit-code 1 beendet
    }

    int in = open(argv[1], O_RDONLY); //Dateiendeskriptor Quelle; Quelldatei wird im Lesemodus geoeffnet
    if (in == -1) //wenn fehler beim oeffnen
    {
        fprintf(stderr, //fehlermeldung ausgeben
                "Quelle %s kann  icht geoeffnet werden (errno %d: %s)\n",
                argv[1], errno, strerror(errno));
                return 1;
    }

    const mode_t mode = S_IRUSR | S_IWUSR | S_IRGRP | S_IROTH; //rw-r--r-- : Dateiberechtigungsmodus mode auf rw-r--r-- festgelegt
    //zieldatei im schreibmodus geoeffnet. Wenn nicht existiert ->erstellt; wenn existiert schlaegt oeffnen fehl
    int out = open(argv[2], O_WRONLY | O_CREAT | O_EXCL, mode); //Dateideskriptor Ziel
    if (out == -1)
    {
        fprintf(stderr,
                "Ziel %s kann nicht erzeugt werden (errno %d: %s)\n",
                argv[2], errno, strerror(errno));
                return 1;
    }

     int n;
     unsigned char b;
     //eigentliche Kopierschleife liest ein Byte von Quelldatei und schreibt in Zieldatei, bis Ende der Quelldatei erreicht
     while ((n = read(in, &b, 1)) > 0)
     {
        int m = write(out, &b, 1);
        if (m != 1)
        {
            fprintf(stderr,
                    "Schreibfehler (errno %d: %s)\n", errno, strerror(errno));
                    return 1;
        }
     }

     if (n < 0)
     {
        fprintf(stderr,
                "Lesefehler (errno %d: %s)\n", errno, strerror(errno));
                return 1;
     }

     if (fsync(out) < 0) //Puffer leeren(flush) um sicherzustellen dass alle dateien auf festplatte geschrieben werden
     {
        fprintf(stderr,
                "Schreibfehler (errno %d: %s)\n", errno, strerror(errno));
                return 1;
     }

     if (close(out) < 0)//Dateideskriptor der zieldatei geschlossen
     {
        fprintf(stderr,
                "Ziel %s kann nicht geschlossen werden (errno %d: %s)\n",
                argv[2], errno, strerror(errno));
                return 1;
     }

     if (close(in) < 0)//Dateideskriptor der quelldatei geschlossen
     {
        fprintf(stderr,
                "Quelle %s kann nicht geschlossen werden (errno %d: %s)\n",
                argv[1], errno, strerror(errno));
                return 1;
     }

     return 0; //ohn efehler durchgelaufen: 0 zurucekgeben (alles erfolgreich)
}