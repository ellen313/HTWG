//fachnoten_liste.h

#ifndef FACHNOTEN_LISTE_H
#define FACHNOTEN_LISTE_H

#include "fachnote.h"

class fachnoten_liste final
{
    //Entity-Klasse ohne Kopier- und Move Semantik
    fachnoten_liste(const fachnoten_liste&) = delete;
    fachnoten_liste& operator=(const fachnoten_liste&) = delete;
    fachnoten_liste(fachnoten_liste&&) = delete;
    fachnoten_liste& operator=(fachnoten_liste&&) = delete;

private:
    class element;
    element *head;
    void (*function)(fachnote*); //fachnote* statt int speichern

public:
    explicit fachnoten_liste(void (*f)(fachnote*)); //standard konstruktor zu expliziten wandeln mit parameter
    ~fachnoten_liste(); //destruktor

    fachnoten_liste& insert(fachnote*);

    class iterator final
    {
    private:
        element *current;
        explicit iterator(element*);
    
    public:
        //operatoren deklarieren
        bool operator!=(const iterator&) const;
        fachnote* operator*() const; //gibt zeiger auf objekt von typ fachnote zuruck auf das iterator zeigt
        iterator& operator++(); //um iterator vw zu bewegen; rueckgabe als referenz ermoeglicht unterstuetzung von kettenoperationen

        friend class fachnoten_liste;
    };

    iterator begin(); //iterator zeigt auf anfang der struktur
    iterator end(); // iterator zeigt auf ende der struktur
};

#endif