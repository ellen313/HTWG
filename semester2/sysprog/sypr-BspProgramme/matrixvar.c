//matrixvar.c

#include <stdio.h>
#include <stdlib.h>

int main(void)
{
    /*
    Anzahl M der Spalten -> nicht weglassen!
    */
    int matrix[][M] = {{10, 11, 12}, {20, 21, 22}};
    const int n = sizeof matrix / sizeof *matrix;

    //-------------------------------- print matrix addresses and values
    printf("&matrix = %p\n", (void*) &matrix);
    printf("matrix = %p\n", (void*) matrix);

    for (int i = 0; i < n; ++i)
    {
        printf("[%d] %p\n", i, (void*) matrix[i]);

        for (int j = 0; j < M; ++j)
        {
            printf("  [%d] %p: %d\n", j, (void*) &matrix[i][j], matrix[i][j]);
        }
    }
    return 0;
}