#!/bin/sh

for s in benotung.cpp fachnote.cpp fachnoten_liste.cpp ; do
    compile_command="g++ -g -c -W -Wall -Weffc++ -Wold-style-cast -std=c++11 $s" #Variable(compile_command) die GCC-Kompilierungsbefehl f. aktuelle datei erhaelt
    echo $compile_command #wert der Variablen ausgeben
    eval $compile_command #wert der Variablen als Kommando ausfuehren
    if [ $? -ne 0 ] ; then #Rueckgabewert des Kommandos pruefen
        echo build failed #falls ungleich 0, fehlermeldung ausgeben
        exit 1 #und skript abbrechen
    fi
done #nachdem fertig, zu linkschritt uebergehen

link_command="ar rvs libaufgabe6.a benotung.o fachnote.o fachnoten_liste.o" #erhaelt linkbefehl, der objektdatei zu ausfuehrbaren datei macht
echo $link_command #gibt linkbefehl aus
eval $link_command #fuehrt linkbefehl aus
if [ $? -ne 0 ] ; then
    echo build failed
    exit 1
fi
echo build successful #wenn alle schritte erfolgreich