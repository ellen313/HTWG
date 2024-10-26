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
        exit(1);
    } else if (rc == 0)
    {
        //int rc_wait = wait(NULL);
        printf("child process\n");
        //printf("wait returns: %d\n", rc_wait);
    } else
    {
        int rc_wait = wait(NULL); //Status des beendeten Kindprozesses wird nicht abgerufen, da NULL als Argument übergeben wird
        printf("parent process\n");
        printf("wait returns: %d\n", rc_wait);
    }

    return 0;
}
//What does wait returns? -> the childs PID
//What happens if you use wait in the child? -> returns -1 cause of no child process to wait for