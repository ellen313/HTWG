//voidpointer.c

//void zeiger ist generischer Zeiger der auf 
//Objekte jedes Typs zeigen kann
#include <stdio.h>

int main(void)
{
    float n = 1;
    void *vp = &n; //zeiger auf variablenadresse 
    int *ip = (int*) vp; //vp in int-Zeiger gecastet,
                        //ip auf diese adresse gesetzt

    /*
    wird versucht, den Gleitkommazahlenwert
    der eigentlich in n gespeichert ist
    als Ganzzahl (int) zu interpretieren
    */
    printf("%x\n", *ip);

    return 0;
}