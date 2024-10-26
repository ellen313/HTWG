#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <sys/wait.h>

int main(int argc, char *argv[])
{
    int rc = fork();

    if (rc < 0)
    {
        fprintf(stderr, "fork failed\n");
        exit(1);
    } else if (rc == 0)
    {
        printf("child process\n");
        //char *args[4];
        //args[0] = strdup("/bin/ls");
        //args[1] = strdup("ls");
        //args[2] = strdup("-l");
        //args[3] = NULL;
        //execl(args[0], args); // Programm ls mit den angegebenen Argumenten ausführen
        execl("/bin/ls", "ls", "-l", NULL);
        //execl() wird verwendet, um neues Programm in einem Prozess auszuführen, wobei aktuelles Programm ersetzt wird
    } else
    {
        int wc = waitpid(rc, NULL, 0);
        printf("parent process\n");
    }

    return 0;
}