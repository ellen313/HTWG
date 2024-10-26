#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/wait.h>

int main()
{
    int rc = fork();

    if (rc < 0)
    {
        fprintf(stderr, "fork failed\n");
    } else if (rc == 0)
    {
        printf("hello\n");
    } else
    {
        sleep(1); //1sek warten vor Ausführung
        printf("goodbye\n");
    }

    return 0;
}

//Can you do this without calling wait() in the parent?
//-> call sleep before the printf