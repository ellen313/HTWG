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
        printf("Hello\n");
    } else
    {
        int rc_wait = waitpid(rc, NULL, 0);
        printf("Bye\n");
    }

    return 0;
}

//When would waitpid() be useful? -> when u want to wait on a child process with a specific PID