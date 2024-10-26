//list.c

#define _POSIX_C_SOURCE 1 //Code basiert auf POSIX-Standards

#include <stdio.h> //fprintf, printf
#include <string.h> //strerror

#include <sys/stat.h> //struct stat, S_ISDIR
#include <dirent.h> //DIR, struct dirent, opendir, readdir
#include <errno.h> //errno

int main(int argc, char void *argv[])
{
    for (int i = 0; i < argc; ++i)
    {
        struct stat s; //Dateistatus
        if (stat(argv[i], &s) == -1) //Dateistatus der aktuellen Datei wird mit stat ueberprueft
        {
            fprintf(stderr, "%s existiert nicht (errno %d: %s)\n",
                    argv[i], errno, strerror(errno));
            continue;
        }

        if (!S_ISDIR(s.st_mode)) //wenn datei kein verzeichnis: fehlermeldung
        {
            fprintf(stderr, "%s ist kein Verzeichnis\n", argv[i]);
            continue;
        }

        DIR *d = opendir(argv[i]); //geoffnetes Verzeichnis
        if (d == NULL) //wenn oeffnen fehlschlaegt: fehlermeldung
        {
            fprintf(stderr, "%s kann nicht geoffnet werden (errno %d: %s)\n",
                    argv[i], errno, strerror(errno));
            continue;
        }

        errno = 0;

        struct dirent *e; //gelesener Verzeichniseintrag
        while ((e = readdir(d)) != NULL)
        {
            //d_name: zeiger auf auf Null-terminierten zeichenarray der namen des verzeichniseintrags enthaelt
            //e->d_name: um auf Mitglied d_name der Struktur dirent zuzugreifen
            printf("%s/%s\n", argv[i], e->d_name); // / -> um pfad zum verzeichniseintrag anzuzeigen
        }

        if (errno)
        {
            fprintf(stderr, "Lesefehler in %s (errno %d: %s)\n",
                    argv[i], errno, strerror(errno));
        }

        closedir(d); //geoeffnetes verzeichnis wird geschlossen
    }

    return 0;
}