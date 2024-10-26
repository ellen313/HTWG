package aufgabe1;

public class LinkedListFrequencyTable extends AbstractFrequencyTable {
    private Node begin;
    private Node end;
    private int size;

    public LinkedListFrequencyTable() {
        clear();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        size = 0;
        begin = new Node(null, null, null);
        end = new Node(null, null, begin);
        begin.next = end;
    }

    @Override
    public void add(String w, int f) {
        //ob bereits aktualisiert
        boolean fqSummed = false;

        //zeiger auf nächsten Knoten setzen
        Node p = begin.next;
        //ob w gleich im aktuellen knoten p ist (prüfen ob wort bereits vorhanden)
        //ja: auf true setzen
        //addFrequency um Häuf. des Worts im knoten p um Wert f zu erhöhen (um gesamthäufigkeit des worts erhöhen)
        for (int i = 0; i < size; i++) { // for (Node p = begin.next; p < next) oder so
            if (w.equals(p.word.getWord())) {
                fqSummed = true;
                p.word.addFrequency(f);
                break;
            }
            //zeiger wird auf nächsten knoten in der Liste verschoben
            p = p.next;
        }

        //nein: neuen Knoten erstellen der vor dem vorherigen wort eingefügt wird
        if (!fqSummed) {
            Node r = new Node(new Word(w, f), end, end.prev);
            r.prev.next = r;
            end.prev = r;
            size++;
        }

        //Zeiger wird auf aktuellen knoten in der liste gesetzt
        p = end.prev;

        //Schleife läuft rw durch und gleicht Häufigkeiten ab
        // wenn Häufigk. des Worts größer als des vorherigen werden sie getauscht
        for (int i = size - 1; i > 0; i--) {
            if (p.word.getFrequency() > p.prev.word.getFrequency()) {
                Word temp = p.word; //wird in temporärer variable gespeichert
                p.word = p.prev.word; //wird durch häufig. des vorherigen worts ersetzt
                p.prev.word = temp; //wird durch temp ersetzt
            }
            p = p.prev;
        }
    }
    @Override
    public Word get(int pos) {
        if (pos < 0 || pos >= size) {
            throw new IndexOutOfBoundsException();
        }
        if (size == 0) {
            throw new IllegalStateException();
        }
        Node p = begin.next;
        for (int i = 0; i < pos; i++) {
            p = p.next;
        }
        return p.word;
    }

    @Override
    public int get(String w) {
        Node p = begin.next;
        for (int i = 0; i < size; i++) {
            //prüfen ob w gleich dem wort im aktuellen knoten p
            //true: Häufigk. des wortes abrufen
            if (w.equals(p.word.getWord())) {
                return p.word.getFrequency();
            }
            //zeiger wird verschoben
            p = p.next;
        }
        //false: 0 zurückgeben, da nicht existent
        return 0;
    }
    private static class Node {
        private Node next;
        private Node prev;
        private Word word;

        public Node(Word w, Node n, Node p) {
            word = w;
            next = n;
            prev = p;
        }
    }
}
