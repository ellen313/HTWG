package aufgabe1;

import aufgabe1.AbstractFrequencyTable;
import aufgabe1.Word;
import java.util.Arrays;

public class ArrayFrequencyTable extends AbstractFrequencyTable {

    private int size = 0;
    private Word fqTable[];
    private final int DEFAULT_SIZE = 100;

    public ArrayFrequencyTable() {
        clear();
    }

    @Override
    public int size() {
        return size;
    }

    //liste geleert, neues Array erstellt mit größe von default size
    @Override
    public final void clear() {
        size = 0;
        fqTable = new Word[DEFAULT_SIZE];
    }

    //Wort und zugehörige Häufigkeit zu Liste hinzuzufügen
    @Override
    public void add(String w, int f) {

        //festellen ob Häufigkeit eines Worts bereits aktualisiert
       boolean fqSummed = false;

         //wenn size >= aktuelle Anzahl Wörter: array auf doppelte
         //seiner Größe erweitert -> um ausreichend Platz zu haben
         //für neue Aufnahmen

       if (size >= fqTable.length) {
           fqTable = Arrays.copyOf(fqTable, 2 * size);
       }
        //Schelife iteriert durch Array um nachzusehen ob w bereits vorhanden
        //ja: auf true gesetzt, Häufigkeit des Worts um f erhöht
       for (int i = 0; i < size; i++) {
           if (w.equals(fqTable[i].getWord())) {
               fqSummed = true;
               fqTable[i].addFrequency(f);
           }
       }

       //nein: neues Wort objekt erstellt und an pos. size hinzugefügt
       if(!fqSummed) {
           fqTable[size] = new Word(w, f);
           size++;
       }

       //array in absteigender Reihenfolge d. Häufigk. sortiert
        //Schleife geht Array rw durch: Wenn Häufigk. > als vorheriges Wort -> getauscht
        //Wörter mit höchsten Häufigk. werden zuerst angezeigt
       for (int i = size - 1; i > 0; i--) {
           if(fqTable[i].getFrequency() > fqTable[i - 1].getFrequency()) {
               Word temp = fqTable[i];
               fqTable[i] = fqTable[i - 1];
               fqTable[i - 1] = temp;
           }
       }
    }

    @Override
    public Word get(int pos) {
        if (pos < 0 || pos >= size) {
            throw new IndexOutOfBoundsException();
        }
            return fqTable[pos];
    }

    //Schleife überprüft ob w gleich dem Wort in der aktuellen pos. fqTable[i] ist
    //Wort gefunden: frequency aufgerufen um Häufigk. im fqTable abzurufen und zurückzugeben
    //nicht gefunden: 0 zurückgeben -> Wort nicht in Liste
    @Override
    public int get(String w) {
        for (int i = 0; i < size; i++) {
            if (w.equals(fqTable[i].getWord())) {
                return fqTable[i].getFrequency();
            }
        }
        return 0;
    }
}

