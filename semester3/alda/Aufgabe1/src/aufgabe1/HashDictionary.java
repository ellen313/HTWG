package aufgabe1;

import java.util.Iterator;

public class HashDictionary<K,V> implements Dictionary<K,V> {

    public static class Entry<K, V> { //jedes entry objekt enthält schlüssel wert paar, referenz auf nächste entry objekt
        K key;
        V value;
        Entry<K, V> next;

        Entry(K key, V value) {
            this.key = key;
            this.value = value;
            this.next = null; //entry ist letzte element in der liste
        }

        Entry(K key, V value, Entry<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    private static final int DEFAULT_CAPACITY = 11; //standardgröße spezifizieren
    private Entry[] data; //elemente der datenstruktur speichern
    private int size; //aktuellen füllstand der datenstruktur verfolgen

    public HashDictionary(int capacity) {
        size = 0; //größe auf 0 setzen
        if (isPrime(capacity)) //prüfen ob angegebene kapazität primzahl ist
            data = new Entry[capacity]; //ja: array mit angegebenen kapazität initialisieren
        else
            data = new Entry[DEFAULT_CAPACITY]; //nein: array mit standardkpazität initialisieren
    }

    private boolean isPrime(int n) {
        for (int i = 2; i * i < n; i++) { //über alle zahlen von 2 bis wurzel von n iterieren
            if (n % i == 0) { //wenn n durch i ohne rest teilbar, keine primzahl
                return false;
            }
        }
        return true; //wenn keine zahl n teilt, ist n primzahl
    }

    private void doubleCapacity() { //kapazität des arrays verdoppeln
        //hole nächste primzahl
        int newCapacity = data.length * 2; //neue kapazität berechnen
        while (!isPrime(newCapacity)) { //sucht nach der nächsten primzahl
            newCapacity++;
        }
        Entry[] tmp = new Entry[newCapacity]; //temporäres array wird mit neuer kapazität erstellt
        for (Entry entry : data) { //eintröge aus dem aktuellen array werden ins neue kopiert
            while (entry != null) {
                //floorMod: sicherstellen dass berechnete hashwert innerhalb der arraygrenzen liegt
                int hash = Math.floorMod(entry.key.hashCode(), tmp.length);
                //an berechneten pos. hash wird neuer eintrag mit schlüssel und wert des aktuellen eintrags entry erstellt
                tmp[hash] = new Entry<>(entry.key, entry.value, tmp[hash]);
                entry = entry.next; //next verweis des neuen eintrags auf den vorherigen gesetzt -> verkettete liste von einträgen die selben hashwert haben
            }
        }
        data = tmp; //data aktualisiert dass es auf neues array zeigt
    }

    @Override
    public V insert(K key, V value) {
        int hash = Math.floorMod(key.hashCode(), data.length); //hashwert berechnen
        if (data[hash] == null) {
            data[hash] = new Entry<>(key, value); //wenn kein einbtrag vorhanden, neuen mit schlüssel und wert erstellen
        } else {
            Entry entry = data[hash];
            while (true) { //über alle einträge an berechneten hashpos iterieren
                if (entry.key.equals(key)) { //wenn schlüssel übereinstimmen
                    V old = (V) entry.value; //alten wert des eintrags speichern
                    entry.value = value; //neuen wert für eintrag setzen
                    return old; //alten wert zurückgeben
                }
                if (entry.next == null) { //wenn nächster eintrag null, ende der liste
                    break; //schleife abbrechen
                }
                entry = entry.next; //zum nächsten eintrag gehen
            }
            entry.next = new Entry<>(key, value);
        }
        size++; //inkrementieren, da neuer eintrag hinzugekommen
        if (size >= data.length / 2) { //prüfen ob größe der hashtabelle die hälfte der kapazität erreicht hat
            doubleCapacity(); //wenn ja, kapazität verdoppeln
        }
        return null; //falls kein wert für schlüssel vorhanden
    }

    @Override
    public V search(K key) {
        int hash = Math.floorMod(key.hashCode(), data.length); //hashwert berechnen um index im array zu erhalten
        if (data[hash] == null) { //wenn an pos nichts vorhanden
            return null; //null, da schlüssel nicht gefunden im worterbuch
        } else {
            while (data[hash] != null) { //durch liste iterieren
                if (data[hash].key.equals(key)) { //jeden eintrag überprüfen ob schlüssel gleich gesuchtem
                    return (V) data[hash].value; //wenn gefunden wert des eintrags zurückgeben
                }
                data[hash] = data[hash].next; //zeiger auf nächsten eintrag schieben
            }
        }
        return null;
    }

    @Override
    public V remove(K key) {
        int hash = Math.floorMod(key.hashCode(), data.length); //hashwert berechnen
        if (data[hash] == null) { //prüfen ob eintrag vorhanden
            return null;
        } else {
            Entry prev = null;
            Entry entry = data[hash];
            while (entry != null) {
                if (entry.key.equals(key)) { //prüfen ob vorhandener eintrag mit angegebenen schlüssel übereinstimmt
                    if (prev == null) { //wenn erster eintrag in liste
                        data[hash] = entry.next; //next direkt auf nächsten eintrag setzen
                    } else {  //wenn eintrag gefunden mit schlüssel
                        prev.next = entry.next; //next wird auf nächsten eintrag gesetzt um zu entfernen
                    }
                    size--; //größe verringern nach entfernen
                    return (V) entry.value; //wert des entfernten elements zurückliefern
                }
                prev = entry; //vorherigen eintrag speichern
                entry = entry.next; //verkettete liste von einträgen durchlaufen
            }
        }
        return null;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Iterator<Dictionary.Entry<K, V>> iterator() {
        return new Iterator<>() {
            private int i = 0; //inde der den aktuellen speicherplatz in data darstellt und durch itrator iteiert
            Entry prev = null; //referenz auf vorherigen eintrag in verketteten liste (um vorherigen eintrag zu bekommen beim durchlaufen)
            Entry entry = null; //referenz auf aktuellen eintrag (um aktuellen eintrag bei durchlaufen der liste zu bekommen)

            @Override
            public boolean hasNext () { //prüfen ob i einen nächsten eintrag hat
                if (data[i] != null && data[i].next != null) {
                    return true;
                } //wenn data[i] null über restliche iterieren
                for (int j = i + 1; j < data.length; j++) {
                    if (data[j] != null) { //eintrag gefunden
                        return true;
                    }
                }
                return false;
            }

            @Override
            public Dictionary.Entry<K, V> next() {
                while (data[i] == null) {
                    i++;
                }
                if (prev == null) {
                    entry = data[i]; //wenn eintrag nicht null wird er als entry festgelegt
                    prev = entry; //wenn prev noch nicg gesetzt, auf aktuellen entry setzen
                } else if (hasNext()) {
                    entry = entry.next; //nächster eintrag in liste als neues entry festlegen
                    if (entry == null) { //falls entry null
                        do {
                            i++; // i erhöhen
                        } while (i < data.length && data[i] == null); //bis nicht null eintrag gefunden
                        entry = data[i]; // und entsprechender eintrag als neues entry festgelegt
                    }
                } else {
                    throw new IndexOutOfBoundsException(); //keine weiteren elemente gefunden
                }
                //aktuellen eintrag als Dictionary.Entry objekt zurückgeben
                return (Dictionary.Entry<K, V>) new Dictionary.Entry<>(entry.key, entry.value);
            }
        };
    }
}
