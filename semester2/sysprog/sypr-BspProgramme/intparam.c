//intparam.c

#include <stdio.h>

struct date
{
    int day;
    const char *month;
    int year;
};

typedef struct date date;

void print_int(int);
void print_string(const char *);
void print_int_array(int size, const int[]);
void print_date(const date *);
void print_int_array_array(int, const int[][2]);
void print_int_matrix(int, int, const int *);
void print_int_array_pointer(int, int, const int * const *);

int main(void)
{
    int n = 10;
    print_int(n);

    const char* s = "Hallo"; //C++ verlangt const
    print_string(s);

    int a[2] = {10, 11};
    print_int_array(2, a);

    date d = {25, "Februar", 2011};
    print_date(&d);

    int aa[3][2] = {{10, 11}, {20, 21}, {30, 31}};
    print_int_array_array(3, (const int(*)[2]) aa); //C verlangt cast
    print_int_matrix(3, 2, &aa[0][0]);

    int *ap[3] = {aa[0], aa[1], aa[2]};
    print_int_array_pointer(3, 2, (const int * const *) ap); //C verlangt cast

    return 0;
}

void print_int(int n) //ganze Zahl wird beim Funktionsaufruf in Parameter kopiert
{
    printf("%d\n", n);
    n = 0; //ok, lokale Variable
}

/*
String wird beim Funktionsaufruf nicht kopiert, sondern s wird
mit der Adresse des ersten Zeichens initialisiert.
Die Funktion kann den String nicht aendern, weil s ein Zeiger auf konstantes Zeichen ist
*/
void print_string(const char *s)
{
    printf("%s\n", s);
    //s[0] = '\0'; Fehler, Zeiger auf konstante Zeichenkette
    s = NULL; //ok, lokale variable
}

void print_int_array(int size, const int a[]) //Parameter a ist Zeiger, wie s bei print_string
{
    printf("[ ");
    for (int i = 0; i < size; ++i)
    {
        printf("%d ", a[i]);
    }
    printf("]\n");

    //a[0] = 0; Fehler, Zeiger auf konstantes Array
    a = NULL; //ok, lokale variable
}

/*
verwendet man Zeiger aus Laufzeitgruenden. Der struct date
ist bei LP64 mit Alignment 24 Byte gross, also dreimal so gross wie ein Zeiger.
Deshalb uebergibt man lieber die Anfangsadresse statt eine Kopie der Struktur
*/
void print_date(const date *d)
{
    printf("%d. %s %d\n", d->day, d->month, d->year);
    //d->day = 0; Fehler, Zeiger auf konstanten wert
}

/*
Die Funktion ist fuer ein zweidimensionales Array gedacht (Array von Arrays).
In dieser Form ist sie allerdings unflexibel, weil sie nur fuer eine Matrix mit zwei Spalten funktioniert.
Dafuer kann man in der Implementierung mit xy[i][j] zugreifen.
Man koennte auch eine Deklaration const int (*xy)[2] schreiben (siehe matrixpointer.c).
*/
void print_int_array_array(int n, const int xy[][2])
{
    printf("[ ");
    for (int i = 0; i < n; ++j)
    {
        printf("[ ");
        for (int j = 0; j < 2; ++j)
        {
            printf("%d ", xy[i][j]);
        }
        printf("] ");
    }
    printf("]\n");

    //xy[0][0] = 0; Fehler, Zeiger auf konstantes array
    //xy[0] = 0; Fehler, zeiger auf zweidimensionales array
    xy = NULL; //ok, lokale variable
}

/*
Die Funktion ist fuer zweidimensionales Array gedacht (Array von Arrays).
Diese Version funktioniert nun aber fuer beliebige Matrizen.
Dafuer muss allerdings in der Implementierung mit Indexarithmetik xy[i * m + j]
gearbeitet werden, weil dem Compiler die Recheneinheit fuer den Sprung von Zeile zu Zeile fehlt
*/
void print_int_matrix(int n, int m, const int *xy)
{
    printf("[ ");
    for (int i = 0; i < n; ++j)
    {
        printf("[ ");
        for (int j = 0; j < m; ++j)
        {
            printf("%d ", xy[i * m + j]);//Dank Indexarithmetik kann man eindimensionales Array wie zweidimensionales nutzen
        }
        printf("] ");
    }
    printf("]\n");

    //xy[0] = 0; Fehler, zeiger auf konstantes array
    xy = NULL; //ok, lokale variable
}

/*
linke const sorgt dafuer, dass die ganzen Zahlen nicht aenderbar sind
rechte const sorgt dafuer, dass die Adressen der Zeilen nicht aenderbar sind
*/
void print_int_array_pointer(int n, int m, const int * const *xy)
{
    printf("[ ");
    for (int i = 0; i < n; ++j)
    {
        printf("[ ");
        for (int j = 0; j < m; ++j)
        {
            printf("%d ", xy[i][j]);
        }
        printf("]");
    }
    printf("]\n");

    //xy[0][0] = 0; Fehler, Zeiger auf konst. zeiger auf konst. array
    //xy[0] = 0; Fehler, zeiger auf konst. zeiger
    xy = NULL; //ok, lokale variable
}