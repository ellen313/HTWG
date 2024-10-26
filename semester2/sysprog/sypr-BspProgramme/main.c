//main.c
#include <stdio.h>

int main(int argc, char *argv[]) //argc der C-Ersatz fuer die Instanzvariable args.length
{
    for (int i = 0; i <= argc; ++i)
    {
        printf("%d: %s\n", i, argv[i]);
    }
    return 0;
}