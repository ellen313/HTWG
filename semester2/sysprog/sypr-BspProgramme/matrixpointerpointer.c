//matrixpointerpointer.c

#include <stdio.h>
#include <stdlib.h>

int main(void)
{
    //--------------------allocate and initialize memory for 2x3-matrix
    const int n = 2; //number of lines
    const int m = 3; //number of columns
    int **matrix = (int**) malloc(n * sizeof (int*));
    if (matrix == NULL)
    {
        printf("Speicherallokierungsfehler!");
        return 1;
    }

    for (int i = 0; i < n; ++i)
    {
        //reserviert speicher fuer m ganzzahlen und gibt zeiger von typ int* zurueck
        //dieser zeiger wird der iten zeile der matrix zugewiesen
        matrix[i] = (int*) malloc(m * sizeof (int));
        if (matrix[i] == NULL)
        {
            printf("Speicherallokierungsfehler!");
            for (int j = 0; j < i; ++j) //laeuft alle vorherigen zeilen durch
            {
                //Bei fehler: speicher freigeben der bereits allokiert
                free(matrix[j]);
            }
            free(matrix); 
            return 1;
        }
    }

    matrix[0][0] = 10;
    matrix[0][1] = 11;
    matrix[0][2] = 12;
    matrix[1][0] = 20;
    matrix[1][1] = 21;
    matrix[1][2] = 22;

    //--------------------------- print matrix addresses and values
    printf("&matrix = %p\n", (void*) &matrix);
    printf("matrix = %p\n", (void*) matrix);

    for (int i = 0; i < n; ++i)
    {
        printf("[%d] %p: %p\n", i, (void*) &matrix[i][j], (void*) matrix[i][j]);

        for (int j = 0; j < m; ++j)
        {
            printf("  [%d] %p: %d\n", j, (void*) &matrix[i][j], matrix[i][j]);
        }
    }

    //---------------------------- free matrix memory
    for (int i = 0; i < n; ++i)
    {
        free(matrix[i]);
    }

    free(matrix);

    return 0;
}