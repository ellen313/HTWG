//pointervar.c

#include <stdio.h>

int main(void)
{
    int i = 3082;
    /*
    Zeiger auf int (Typ int*)
    stern an den Variablennamen, weil somit variable typ int* hat
    Bsp: int *p, q -> q hat den Typint nd nicht int, also keine Zeigervariable
    egal wo stern steht: int* n, int * n, int *n, int*n */
    int *p = &i; 

    //--------------------- print pointer value
    printf("p = %p\n", (void*) p);

    //--------------------- print pointer address
    printf("&p = %p\n", (void*) p);

    //--------------------- print pointer size
    printf("sizeof p = %zu\n", sizeof p);

    //--------------------- print dereferenced pointer value
    printf("*p = %d\n", *p);

    return 0;
}