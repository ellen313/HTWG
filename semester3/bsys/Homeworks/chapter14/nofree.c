#include <stdio.h>
#include <stdlib.h>

int main()
{
    int *x = malloc(sizeof(int));
    *x = 8;
    printf("%d\n", *x);
    return 0;
}