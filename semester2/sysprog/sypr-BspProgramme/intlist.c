//intlist.c

#include <stdio.h>

struct int_list
{
    struct int_list *next; //Verkettung; Zeiger auf naechste Element in der Liste
    int n; //Ganzzahlwert des Elements
};

int main(void)
{
    struct int_list last = {NULL, 10}; //Zeiger auf NULL
    struct int_list first = {&last, 20}; //Zeiger auf last element

    /*
    zeiger p wird auf das erste element der liste (&first) gesetzt
    solange p nicht auf NULL zeigt, wird wert des aktuellen elements (p->n) ausgegeben
    und der zeiger p wird auf das naechste element in der liste (p->next) gesetzt
    */
    for (struct int_list *p = &first; p != NULL; p = p->next)
    {
        printf("%d\n", p->n); //erste element hat wert 20 und eine zeiger auf zweite mit wert 10
    }

    return 0;
}