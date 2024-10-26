//while-schleife.c

#include <stdio.h>

int main(void)
{
    printf("Zahlen eingeben (Ende mit Strg-D): ");
    int sum = 0;
    int n;
    while (scanf("%d", &n) == 1)
    {
        sum += n;
    }

    printf("Summe: %d\n", sum);

    return 0;
}