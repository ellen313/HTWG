#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>

int main()
{
    int rc = fork();

    if (rc == 0)
    {
        close(STDOUT_FILENO); //Standardausgabestrom des aktuellen Prozesses wird geschlossen
        printf("child porcess\n");
    }

    return 0;
}

//What happens if the child calls printf() to print
// some output after closing the descriptor? -> Nichts, da geschlossen