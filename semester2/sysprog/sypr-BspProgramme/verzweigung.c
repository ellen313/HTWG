//verzweigung.c

#include <stdio.h>

int main(void)
{
    printf("zwei zahlen eingeben: ");

    int m, n;
    if (scanf("%d%d", &m, &n) < 2)
    {
        fprintf(stderr, "Eingabefehler!\n");
    }
    else if (m > n)
    {
        printf("Maximum: %d\n", m);
    }
    else
    {
        printf("Maximum: %d\n", n);
    }

    return 0;
}