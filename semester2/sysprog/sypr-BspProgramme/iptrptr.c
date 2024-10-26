//iptrptr.c

#include <stdio.h>

int main(void)
{
    int i = 0;
    int *p = &i; //Zeiger auf ganzzahl int
    /*
    **pp: Zeiger auf einen Zeiger. Erstes *: pp zeigt auf etwas
    zweites *: dieses "etwas" ist selbst ein zeiger
    */
    //pp zeigt auf Adresse des Zeigers p,
    //der wiederum auf Ganzzahl zeigt
    int **pp = &p; //&p : gibt adresse des zeigers p

    printf("\n");
    printf("%d\n", *p);
    printf("%d\n", *&i);
    printf("%d\n", *&*p);

    return 0;
}