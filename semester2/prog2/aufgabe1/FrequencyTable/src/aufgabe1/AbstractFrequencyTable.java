package aufgabe1;

import aufgabe1.FrequencyTable;
import aufgabe1.Word;
import java.lang.StringBuilder;


public abstract class AbstractFrequencyTable implements FrequencyTable {
    @Override
    public boolean isEmpty() {
        return this.size() == 0;
    }

    @Override
    public void add(String w) {
        add(w,1);
    }

    @Override
    public void addAll(FrequencyTable fq) {
        /**
         * iteriert über Elemente der FrequencyTable fq
         * für jedes Element fq wird ein Objekt vom Typ Word aus fq abgerufen
         * und in Variable w gespeichert
         * add Aufruf: Wort und Häufigkeit in Instanz d. Kl. speichern
         */
        for (int i = 0; i < fq.size(); i++) {
            Word w = fq.get(i);
            add(w.getWord(), w.getFrequency());
        }
    }

    @Override
    public void collectNMostFrequent(int n, FrequencyTable fq) {

        //sicherstellen das alle vorherigen Daten in frequencyTable gelöscht
        fq.clear();

        /**
         * < size/n stellt sicher das schleife nicht über verfügbare
         * Daten hinausgeht und nicht mehr als n Elemente sammelt
         * Word aus der Instanz abgrufen und in variable gespeichert
         * add Aufruf: Wort und Häufigkeit in fq hinzuzufügen
         */
        for (int i = 0; i < size() && i < n; i++) {
            Word w = get(i);
            fq.add(w.getWord(), w.getFrequency());
        }
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("{");

        for (int i = 0; i < size(); i++) {
            s.append(get(i).toString()).append(", ");
        }
        s.append("} size = ").append(size());

        return s.toString();
    }

}
