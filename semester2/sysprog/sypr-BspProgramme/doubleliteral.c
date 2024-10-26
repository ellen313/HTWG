//doubleliteral.c

#include <stio.h>

int main(void)
{
    printf("%g\n", (1e-30 + 1e30) - 1e30); //fest oder gleitkomma
    printf("%g\n", 1e-30 + (1e30 - 1e30)); // -"-
    printf("%f\n", 12.3456789); //festkomma
    printf("%Lf\n", 1234567.89L); //Long double fk
    printf("%e\n", 12.3456789); //gleitkomma
    printf("%Le\n", 1234567.89L);//long double gk
    return 0;
}