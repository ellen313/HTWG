#include <unistd.h>
#include <stdio.h>
#include <sys/wait.h>
//demonstriert die Verwendung von Pipes zur Kommunikation zwischen Prozessen

int main()
{
    int fd[2]; //fd[0]: zum lesen von pipe, fd[1]: zum schreiben in pipe
    pipe(fd); //pipe erstellen

    int child1 = fork();

    if (child1 < 0)
    {
        printf("fork failed\n");
    } else if (child1 == 0) 
    {
        write(fd[1], "child1\n", 7); //in pipe schreiben
        close(fd[1]); //Schreibdescriptor schließen, um anzuzeigen, dass Schreibvorgang abgeschlossen
    } else
    {
        int child2 = fork();

        if (child2 < 0)
        {
            printf("fork 2 failed");
        } else if (child2 == 0) //von pipe lesen
        {
            char buf[7];
            read(fd[0], buf, 7);
            close(fd[0]);
            printf("%s", buf);
        } else
        {
            wait(NULL); //parent process
        }
        wait(NULL); //auf child2 warten
    } 
        
    return 0;
}