package aufgabe1;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class SortedArrayDictionary<K extends Comparable<? super K>, V>  implements Dictionary<K,V> {

    private static final int DEF_CAPACITY = 16;
    private int size;
    private Dictionary.Entry<K, V>[] data;

    @SuppressWarnings("unchecked")
    public SortedArrayDictionary() {
        size = 0;
        data = new Dictionary.Entry[DEF_CAPACITY];
    }

    private int searchKey(K key) { //binäre Suche
        int li = 0;
        int re = size - 1;

        while (re >= li) {
            int m = (li + re) / 2;
            if (key.compareTo(data[m].getKey()) < 0)
                re = m - 1;
            else if (key.compareTo(data[m].getKey()) > 0)
                li = m + 1;
            else
                return m; //key gefunden
        }
        return -1; //key nicht gefunden
    }
    @Override
    public V search(K key) {
        int i = searchKey(key);
        if (i >= 0)
            return data[i].getValue();
        else
            return null;
    }

    @Override
    public V insert(K key, V value) {
        int i = searchKey(key);

        //Vorhandener eintrag wird überschrieben:
        if (i >= 0) {
            V r = data[i].getValue();
            data[i].setValue(value);
            return r;
        }

        //Neueintrag:
        if (data.length == size) {
            data = Arrays.copyOf(data, 2 * size);
        }
        int j = size - 1;
        while (j >= 0 && key.compareTo(data[j].getKey()) < 0) {
            data[j + 1] = data[j];
            j--;
        }
        data[j + 1] = new Dictionary.Entry<K, V>(key, value);
        size++;
        return null;
    }

    @Override
    public V remove(K key) {
        int i = searchKey(key);
        if (i == -1)
            return null;
        //Datensatz löschen und Lücke schließen
        V r = data[i].getValue();
        for (int j = i; j < size - 1; j++)
            data[j] = data[j + 1];
        data[--size] = null;
        return r;
    }

    @Override
    public int size() {
        return size;
    }

    static private class Entry<K, V> {
        K key;
        V value;

        Entry(K k, V v) {
            key = k;
            value = v;
        };
    }

    @Override
    public Iterator<Dictionary.Entry<K, V>> iterator() {
        return new SortedArrayDictionaryIterator();
    }

    private class SortedArrayDictionaryIterator implements Iterator<Dictionary.Entry<K, V>> {
        private int currentIndex = 0;

        @Override
        public boolean hasNext() {
            return currentIndex < size;
        }

        @Override //gibt nächstes Element im Dictiobary zurück und verschiebt Iterator zum nächsten Element
        public Dictionary.Entry<K, V> next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return data[currentIndex++];
        }
        @Override
        public void remove() {throw new UnsupportedOperationException();}
    }

}
