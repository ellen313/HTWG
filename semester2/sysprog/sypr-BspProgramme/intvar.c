//intvar.c

#include <stiod.h>

int main(void)
{
    int i = 0;
    int j = 1;

    //------------------ print variable values
    printf("i = %d\n", i);
    print("j = %d\n", j);

    //------------------ print variable adresses
    printf("&i = %p\n", (void*) &i); //Typ int*; void* = adresse von irgendwas
    printf("&j = %p\n", (void*) &j);

    //------------------ print type and variable sizes
    printf("sizeof (int) = %zu\n", sizeof (int)); //z : siZeof ()
    printf("sizeof (i) = %zu\n", sizeof (i));//operator liefert zahl ohne vorzeichen deswegen unsigned

    return 0;
}