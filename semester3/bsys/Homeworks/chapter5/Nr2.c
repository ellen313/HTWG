#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <fcntl.h>
#include <string.h>

int main()
{
    int file = open("Nr2.txt", O_CREAT | O_RDWR, S_IRWXU); //öffnet Datei Nr2.txt, erstellt sie wenn nicht vorhanden
    if (file < 0)//prüfen ob öffnen erfolgreich           //und öffnet sie sowohl für das Lesen als auch für das Schreiben
    {
        fprintf(stderr, "Failed to open file\n");
        exit(1);
    }

    int rc = fork(); //neuen Prozess erzeugen
    if (rc < 0)
    {
        fprintf(stderr, "fork failed\n");
        exit(1);
    } else if (rc == 0) //prüfen ob Kindprozess ist
    {
        printf("child process\n");
        char *child = "child process\n"; //zeichenfolge definieren um später zu schreiben
        write(file, child, strlen(child)); //in textdatei schreiben
    } else
    {
        printf("parent process\n");
        char *parent = "parent process\n";
        write(file, parent, strlen(parent));
    }

    close(file);
    return 0;
}

//Can both the child and parent access the file descriptor returned by open()?
//-> yes because when wriding at the same time the output can't be determined