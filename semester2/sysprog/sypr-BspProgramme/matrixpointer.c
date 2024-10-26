//matrixpointer.h

#include <stdio.h>
#include <stdlib.h>

int main(void)
{
    //---------------------- allocate and initialize memory for 2x3-matrix
    const int n = 2; // number of lines
    /*
    zeiger auf array ist zeiger auf erstes element.
    element hier von typ int[3] eshalb zeiger typ int(*)[3].
    () im Typ erforderlich, weil int*[3] Array von 3 Zeigern auf int waere

    Spaltenzahl M muss trotz Speicherung auf dem Heap zu Uebersetzungszeit
    feststehen, weil sie fuer Recheneinheit beim Zugriff matrix[i] gebraucht wird
    */
    int (*matrix)[M] = (int(*)[M] malloc(n * sizeof (int[M])));
    if (matrix == NULL)
    {
        printf("Speicherallokierungsfehler!");
        return 1;
    }

    matrix[0][0] = 10;
    matrix[0][1] = 11;
    matrix[0][2] = 12;
    matrix[1][0] = 20;
    matrix[1][1] = 21;
    matrix[1][2] = 22;

    //----------------------- print matrix addresses and values
    printf("&matrix = %p\n", (void*) &matrix);
    printf("matrix = %p\n", (void*) matrix);

    for (int i = 0; i < n; ++i)
    {
        printf("[%d] %p: %p\n", i, (void*) &matrix[i], (void*) matrix[i]);

        for (int j = 0; j < M; ++j)
        {
            printf("  [%d] %p: %d\n", j, (void*) &matrix[i][j], matrix[i][j]);
        }
    }

    //------------------------ print matrix size
    printf("sizeof matrix = %zu\n", sizeof matrix);
    printf("%d * sizeof *matrix = %zu\n", n, n * sizeof *matrix);

    free(matrix);

    return 0; 
}
