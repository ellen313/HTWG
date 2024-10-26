#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>

int main(int argc, char *argv[])
{
    int x = 100; //x Wert definieren
    int rc = fork(); //neuen prozess erzeugen

    if (rc < 0) //fork failed
    {
        fprintf(stderr, "fork failed\n");
        exit(1);
    } else if (rc == 0) //child (new process)
    {
        printf("child process x: %d\n", x);
        x = 50; //Wert x vom Kindprozess ändern
        printf("child process after modifying x: %d\n", x);
    } else //parent goes down this path (main)
    {
        printf("parent process x: %d\n", x);
        x = 200; //Wert vom elternprozess ändern
        printf("parent process after modifying x: %d\n", x);
    }

    return 0;
}

//What value is the variable in the child process?
//-> will be the same as in the parent process cause the child process 
// inherits a copy of the parent's memory space. x will be 100

//What happens to the variable when both change the value of x?
//-> each process will be modifying its own copy of x, which exists in its mem space
// changes made to x in one process will not affect the value of x in the other process