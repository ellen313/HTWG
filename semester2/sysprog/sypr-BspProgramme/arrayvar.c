//arrayvar.c

#include <stdio.h>

int main(void)
{
    int a[] = {3421, 3442, 3635, 3814};
    /*
    in C array nicht gespeichert wieviele ELemente es hat.
    hier:Konstante wird definiert und mit Elementanzahl initialisiert
    anzahl mit hilfe von sizeof berechnet
    (liefert eine ganze zahl ohne vorzeichen (Typname size_t) deshalb typanpassung/cast int)*/
    const int n = (int)(sizeof a / sizeof (int));

    //----------------------------------- print array values
    printf("&a = %p, &a + 1 = %p\n", (void*) &a, (void*) (&a+1));

    printf("a = %p, a + 1 = %p\n", (void*) a, (void*) (a + 1));

    for (int i = 0; i < n; ++i) //kein length sondern sizeof siehe oben
    {
        printf("%d: %p %d\n", i, (void*) &a[i], a[i]);
    }

    //----------------------------------- print array size
    printf("sizeof a = %zu\n", sizeof a);

    return 0;
}