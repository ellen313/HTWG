//fachnote.h

#ifndef FACHNOTE_H
#define FACHNOTE_H

#include "benotung.h"
#include <string>

class fachnote final
{
public:
    const std::string fach;
    const benotung note;

    //konstruktor zum initialisieren der membervariablen
    fachnote(const std::string&, const benotung&);

    fachnote(const fachnote&) = delete; //kopieren nicht erlaubt
    fachnote& operator=(const fachnote&) = delete; //zuweisen von objekten nicht erlaubt
    fachnote(fachnote&&) = default; //verschieben von objekten der klasse erlaubt
    fachnote& operator=(fachnote&&) = default; //default: man wll public anstatt private
};

#endif