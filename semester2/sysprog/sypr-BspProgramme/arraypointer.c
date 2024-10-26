//arraypointer.c

#include <stdio.h>
#include <stdlib.h> //malloc, calloc, free
#include <stddef.h> //NULL, size_t

int main(void)
{
    const int n = 4;
    /*
    gibt in C keinen Unterschied zwischen Zeiger auf einzelnen int wert
    und zeiger auf array von int werten.
    Calloc ist quasi entsprechung zum Operator new in Java
    calloc reserviert n * sizeof (int) byte speicher auf heap
    und initialisiert ihn mit 0en
    Typ void* auf den benoetigten Zeigertyp anzupassen, hier int
    **/
    int *a = (int*) calloc((size_t) n, sizeof (int));
    //oder ohne Initialisierung mit 0: int *a = malloc(n * sizeof (int));
    if (a == NULL)
    {
        printf("Speicherallokierungsfehler!");
        return 1;
    }
    //Zeigervariable kann genauso wie eine Arrayvariable verwendet werden
    a[0] = 3421;
    a[1] = 3442;
    a[2] = 3635;
    a[3] = 3814;

    //---------------------------------- print array values and addresses
    printf("&a = %p\n", (void*) &a);
    printf("a = %p\n", (void*) a);

    for (int i = 0; i < n; ++i)
    {
        printf("%d: %p %d\n", i, (void*) &a[i], a[i]);
    }

    //---------------------------------- print array size
    printf("sizeof a = %zu\n", sizeof a); //pointer size
    printf("%d * sizeof *a = %zu\n", n, n * sizeof *a);

    free(a);
    return 0;
}