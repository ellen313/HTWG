#include <stdio.h>
#include <stdlib.h>

int main()
{
    int *data = malloc(10 * sizeof(int));
    
    data[30] = 10;
    free(data[30]);
    int *funny = &data[30];
    print("%d\n", data[30]);

    return 0;
}