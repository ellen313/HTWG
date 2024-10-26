//stringvar.c

#include <stdio.h>
#include <stdlib.h>
#include <stddef.h>
#include <string.h> //strxxx functions

int main(void)
{
    char a[] = "halli";
    const char *s = "hallo";
    char *t = NULL;

    //------------------------ compare, copy and concenate strings
    if (strcmp(a, s) < 0)
    {
        t = (char*) malloc(sizeof a + strlen(s));
        if (t == NULL)
        {
            fprintf(stderr, "Kein Heap-Speicher mehr\n");
            return 1;
        }

        strcat(strcpy(t, a), s); //or: strcpy(t, a); strcat(t, s);      
    }
    else
    {
        t = (char*) calloc(1, sizeof (char));
        if (t == NULL)
        {
            fprintf(stderr, "Kein Heap-Speicher mehr\n");
            return 1;
        }
    }

    //------------------------- print string values and addresses
    printf("a = %p %s\ns = %p %s\nt = %p %s\n",
            (void*) a, a, (void*) s, s, (void*) t, t);
    printf("sizeof a = %zu\n", sizeof a);
    printf("sizeof s = %zu\n", sizeof s);
    printf("sizeof t = %zu\n", sizeof t);

    printf("strlen(a) = %zu\n", strlen(a));
    printf("strlen(s) = %zu\n", strlen(s));
    printf("strlen(t) = %zu\n", strlen(t));

    s = a; // copies the address, not the string 
    //a = s; // syntax error

    free(t);

    return 0;
}