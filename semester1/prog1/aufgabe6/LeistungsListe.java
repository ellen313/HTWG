//LeistungsListe.java
package aufgabe6;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * LeistungsListe kapselt eine Liste von Leistungen.
 * @author ellen
 * @version 19.06.23
 */
public final class LeistungsListe implements Iterable<Leistung> {

    private Element head = null; //leere Liste

    /**
     * Fuegt eine Zahl am Listenanfang ein.
     * @param n die einzufuegende Zahl
     * @return die Liste
     */
    public LeistungsListe insert(Leistung n) {
        this.head = new Element(this.head, n);
        return this;
    }

    /**
     * Element speichert eine einzelne Zahl als Teil einer Liste.
     * Beispiel fuer eine statisch eingebettete Klasse.
     */
    private static final class Element {
        private final Element next;
        private final Leistung n;

        private Element(Element e, Leistung n) {
            this.next = e;
            this.n = n;
        }
    }
    @Override
    public Iterator<Leistung> iterator() {
        // Beispiel fuer eine anonyme lokale Klasse
        //wird verwendet, um ein Objekt zu erstellen,
        //das das Iterator-Interface implementiert und
        //die Elemente der LeistungsListe iteriert
        return new Iterator<>() {
            private Element current = LeistungsListe.this.head;

            @Override
            public boolean hasNext() {
                return this.current != null;
            }
            //wenn aktuelles element nicht null gibt es naechstes.
            @Override
            public Leistung next() {
                if (this.current == null) {
                    throw new NoSuchElementException();
                }
                Element e = this.current;
                this.current = this.current.next;
                return e.n;
            }
        };
    }
}