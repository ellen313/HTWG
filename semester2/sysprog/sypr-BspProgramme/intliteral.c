//intliteral.c

#include <stdio.h> //printf bekannt

/*
in C sind Funktionen die oberste Ebene eines Programms.
Klassen gibt es nicht
Parametertyp void: wird explizit ausgerueckt, dass die
Kommandzeilenagrumente des Programmaufrufs ignoriert werden sollen*/
int main(void)
{
    printf("%x\n", 12); //hexadezimal
    printf("%d\n", 012); //dezimal
    printf("%o\n", 0x12); //oktal
    printf("%u\n", 34U); //unsigned (ohne vorzeichen) 
    printf("%ld\n", 56L); //long
    printf("%lld\n", 78LL); //long long
    /*
    da main den Rueckgabetyp int hat, muss hier ein Wert
    zurueckgegeben werden.
    0 bedeutet das kein Fehler aufgetreten ist
    Kann auch weggelassen werden (ab C99)
    */
    return 0;
}