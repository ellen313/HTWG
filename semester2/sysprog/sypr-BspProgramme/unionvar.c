//unionvar.c

#include <stdio.h>

//wird spaeter verwendet um den typ des union-members in der struktur anzugeben
enum int_or_string {type_int, type_string};

/*
Struktur kann entweder eine Ganzzahl (int) oder
einen Zeiger auf einen Zeichenketten-Array (char *) enthalten
abhaengig von einem zusaetzlichen Enum (int_or_string)
*/
struct struct_with_union
{
    enum int_or_string u_type; //typ des union-members

    union //ermoeglicht es nur eine der members zur gleichen zeit zu verwenden
    {
        int i; //ganzzahl
        char *s; //zeiger auf zeichenkette
    };
};
//je nach wert von u_type wird auf i oder s zugegriffen

int main(void)
{
    struct struct_with_union x;

    //------------------------- print variable values
    x.u_type = type_int;
    x.i = 1;
    printf("%d: %d\n", x.u_type, x.i);

    x.u_type = type_string;
    x.s = "Hallo";
    printf("%d: %s\n", x.u_type, x.s);

    //------------------------- print variable address
    printf("&x = %p\n", (void*) &p);
    printf("&x.u_type = %p\n", (void*) &x.u_type);
    printf("&x.i = %p\n", (void*) &x.i);
    printf("&x.s = %p\n", (void*) &x.s);

    return 0;
}