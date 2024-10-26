#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <unistd.h>

int main(int argc, char *argv[])
{
    if (argc != 3)
    {
        printf("please enter 2 arguments: %s (allocated memory in MB) (time in seconds)\n", argv[0]);
        return 1;
    }

    printf("Current PID : %d\n", getpid());

    //multiply with 1048576 to get MB from User input byte
    int mem = atoi(argv[1]) * 1048576;
    int time = atoi(argv[2]);

    if (mem <= 0 || time <= 0)
    {
        printf("please enter values > 0");
        return 1;
    }

    int size = (int)(mem / sizeof(int));
    int *arr = (int*) malloc(mem);
    if (arr == NULL)
    {
        printf("allocation error\n");
        return 1;
    }

    int i = 0;
    while (1)
    {
        if (i == size)
        {
            i = 0; 
        }

        arr[i] = 0;
        i++;
    }

    //free(arr);
    return 0;
   
}