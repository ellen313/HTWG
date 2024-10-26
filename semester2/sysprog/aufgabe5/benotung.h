//benotung.h

#ifndef BENOTUNG_H
#define BENOTUNG_H

class benotung final
{
private:
    int note;

public:
    static const benotung beste;
    static const benotung schlechteste;

    benotung() = default; //default konstruktor
    explicit benotung(int n); //expliziter konstruktor

    int int_value() const;
    bool ist_bestanden() const;

    friend bool operator==(const benotung&, const benotung&);
};

#endif

