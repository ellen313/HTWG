//for-schleife.c

#include <stdio.h>

int main(void)
{
    int an_array[] = {3082, 3101, 3257, 3436};
    /*
    sizeof an_array gibt gesamtgroesse des arrays in bytes zurueck

    sizeof *an_array gibt groesse des einzelnen elements im array in bytes zurueck
    
    *an_array ist Zugriff auf erste element des arrays
    */
    const int array_size = (int) (sizeof an_array / sizeof * an_array);

    for (int i = 0; i < array_size; ++i)
    {
        printf("%d\n", an_array[i]);
    }

    return 0;
}