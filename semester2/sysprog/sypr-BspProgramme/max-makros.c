//max-makro.c

#include <stdio.h>

//#define max(a, b) a > b ? a : b //falsch, Ausdruck klammern!
//#define max(a, b) (a > b ? a : b) //falsch, Parameterverwendung klammern!
#define max(a, b) ((a) > (b) ? (a) : (b)) //richtig, aber Mehrfachverwendung von a und b

int main(void)
{
    int value = 2 * max(7, 8 | 9); //bei Makros Ausdruecke als Argument vermeiden!
    printf("2 * max(%d, %d) = %d\n", 7, 8 | 9, value);
}