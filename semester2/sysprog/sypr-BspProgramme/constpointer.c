//constpointer.c

//nach initialisierung des const pointer kann er
//nicht mehr auf eine andere Adresse zeigen
#include <stdio.h>

int main(void)
{
    const int i = 0;
    const int *p = &i; //zeiger auf variablenadresse

    int j = 0;
    //q zwigt immer auf adresse von j, wert auf den j zeigt kann sich aber aendern
    int * const q = &j; //konstanter zeiger auf variablenadresse

    //*p = 0; //Fehler, *p konstant
    p = NULL;

    *q = 0;
    //q = NULL; //Fehler, q konstant

    return 0;
}