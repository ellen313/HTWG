//fachnoten_liste.cpp

#include "fachnoten_liste.h"
#include "fachnote.h"

class fachnoten_liste::element final
{
private:
    element *next; //deklariert zeiger next ->um auf naechstes zeichen in liste zu verweisen
    fachnote *n; //deklariert zeiger n

public:
    element(element *e, fachnote* m) : next(e), n(m)
    { }

    friend class fachnoten_liste;
    friend class fachnoten_liste::iterator;
};

fachnoten_liste::iterator::iterator(element *e) : current(e)
{ }

bool fachnoten_liste::iterator::operator!=(const iterator& i) const //Operator wird mit Konstanten Referenzparameter i aufgerufen
{
    // Operator vergleicht Membervariable current des aktuellen Iterators (this->current) mit Membervariable current des uebergebenen Iterators (i.current)
    return current != i.current;
}

fachnote* fachnoten_liste::iterator::operator*() const
{
    //gibt Zeiger auf Objekt vom Typ fachnote zurueck, auf das der Iterator zeigt
    return current->n;
}

//gibt Referenz auf den aktualisierten Iterator zurueck
fachnoten_liste::iterator& fachnoten_liste::iterator::operator++()
{
    //aktualisiert den Iterator, indem er zum naechsten Element in der Liste vorrueckt
    this->current = this->current->next; //next-Zeiger zeigt auf naechste Element der Liste
    return *this; //gibt Referenz auf aktualisierten Iterator zurueck
}

fachnoten_liste::fachnoten_liste(void (*f)(fachnote* n))/*zeiger auf funktion, parameterliste der funktion auf die zeiger zeigt*/
//Funktionszeiger in Klasse fachnoten_liste speichern
: head(nullptr), function(f)//Zeiger zeigt auf kein gueltiges Objekt -> Liste leer
{ }

fachnoten_liste::~fachnoten_liste()
{
    element *e = head;
    while (e != nullptr)
    {
        element *temp = e; //temporaere Variable wird erstellt und auf aktuelles Element e gesetzt
        function(e->n); //Funktion wird mit Inhalt des aktuellen Elements e aufgerufen
        e = e->next; //Zeiger e wird auf naechstes Element in der Liste gesetzt.
        delete temp; //aktuelles Element wird geloescht.
    }
}

fachnoten_liste& fachnoten_liste::insert(fachnote* n)
{
    //Zeiger head wird auf neu erstellte Element gesetzt. Damit wird neu erstellte Element zum neuen Kopf der Liste
    head = new element(head, n); //erstellt neues Objekt der Klasse element und gibt Zeiger darauf zurueck
    return *this; //Methode gibt Referenz auf aktuelles Objekt zurueck
}

fachnoten_liste::iterator fachnoten_liste::begin()
{
    return fachnoten_liste::iterator(head);
}

fachnoten_liste::iterator fachnoten_liste::end()
{
    return fachnoten_liste::iterator(nullptr);
}