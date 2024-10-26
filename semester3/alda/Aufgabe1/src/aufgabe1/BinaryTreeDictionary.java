package aufgabe1;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Implementation of the Dictionary interface as AVL tree.
 * The entries are ordered using their natural ordering on the keys,
 * or by a Comparator provided at set creation time, depending on which constructor is used.
 * p
 * An iterator for this dictionary is implemented by using the parent node reference.
 *
 * @param <K> Key.
 * @param <V> Value.
 */
public class BinaryTreeDictionary<K extends Comparable<? super K >, V> implements Dictionary<K, V> {

    static private class Node<K, V> {
        K key;
        V value;
        int height;
        Node<K, V> left;
        Node<K, V> right;
        Node<K, V> parent;

        Node(K k, V v) {
            key = k;
            value = v;
            height = 0;
            left = null;
            right = null;
            parent = null;
        }
    }

    private Node<K, V> root = null;
    private int size = 0;

    // ...

    private V oldValue; //Rückgabeparameter

    public V insert(K key, V value) {
        root = insertR(key, value, root);
        if (root != null)
            root.parent = null;
        return oldValue; //alten wert zurückliefern
    }

    private Node<K,V> insertR(K key, V value, Node<K,V> p) {
        if (p == null) { //Knoten kommt nicht vor, füge neuen ein
            size++;
            p = new Node<>(key, value);
            oldValue = null;
        }
        else if (key.compareTo(p.key) < 0) { //key ist kleiner als Knoten, suche links weiter
            p.left = insertR(key, value, p.left);
            if (p.left != null)
                p.left.parent = p;
        }
        else if (key.compareTo(p.key) > 0) { //key ist größer als Knoten, suche rechts weiter
            p.right = insertR(key, value, p.right);
            if (p.right != null)
                p.right.parent = p;
        }
        else { //Schlüssel bereits vorhanden -> speicher alten wert und aktualisiere Knoten mit neuem
            oldValue = p.value;
            p.value = value;
        }
        p = balance(p); // balanciere Baum, damit der AVL Baum bleibt
        return p; //baum zurückliefern
    }

    public V search(K key) {
        return searchR(key, root);
    }
    private V searchR(K key, Node<K,V> p) {
        if (p == null) //knoten kommt nicht vor
            return null;
        else if (key.compareTo(p.key) < 0) // key kleiner knoten, links weitersuchen
            return searchR(key, p.left);
        else if (key.compareTo(p.key) > 0) // key größer knoten, rechts weitersuchen
            return searchR(key, p.right);
        else //key gefunden, wert zurücliefern
            return p.value;
    }

    public V remove(K key) {
        root = removeR(key, root);
        if (root != null) {
            root.parent = null;
        }
        return oldValue;
    }

    private Node<K,V> removeR(K key, Node<K,V> p) {
        if (p == null) {//knoten kommt nicht vor, nichts tun
            oldValue = null;
            return null;
        } else if (key.compareTo(p.key) < 0) {
            p.left = removeR(key, p.left);
            if (p.left != null) {
                p.left.parent = p;
            }
        } else if (key.compareTo(p.key) > 0) {
            p.right = removeR(key, p.right);
            if (p.right != null) {
                p.right.parent = p;
            }
        } else {
            if (p.left == null || p.right == null) { //knoten hat genau 1 kind und wird ausgehängt
                //p muss gelöscht werden
                //und hat ein oder kein Kind
                oldValue = p.value;
                --size;
                return (p.left != null) ? p.left : p.right;
            } else { //knoten hat 2 kinder, ersetze p durch kleinsten knoten im rechten teilbaum
                //p muss gelöscht werden und hat zwei Kinder
                MinEntry<K,V> min = new MinEntry<K,V>();
                p.right = getRemMinR(p.right, min);
                oldValue = p.value;
                --size;
                p.key = min.key;
                p.value = min.value;
            }
        }
        p = balance(p);
        return p;
    }

    //löscht im Baum p den Knoten mit kleinstem Schlüssel und
    // liefert Schlüssel und Daten des gelöschten Knotens über min zurück
    private Node<K,V> getRemMinR(Node<K,V> p, MinEntry<K,V> min) {
        assert p != null; //sicherstelen das baum nicht leer
        if (p.left == null) { //kleinsten wert gefunden, initialisiere min mit p
            min.key = p.key;
            min.value = p.value;
            p = p.right;
        } else //linker teilbaum leer, rechts suchen
            p.left = getRemMinR(p.left, min);
        p = balance(p);
        return p;
    }

    //Hilfsdatentyp für den Rückgabeparameter min von getRemMinR
    private static class MinEntry<K,V> {
        private K key;
        private V value;
    }

    @Override
    public int size() {
        return size;
    }

    private int getBalance(Node<K,V> p) {
        if (p == null)
            return 0;
        else
            return getHeight(p.right) - getHeight(p.left);
    }
    private Node<K,V> balance(Node<K,V> p) {
        if (p == null)
            return null;
        p.height = Math.max(getHeight(p.left), getHeight(p.right)) + 1; //höhe aktualisieren
        if (getBalance(p) == -2) {
            if (getBalance(p.left) <= 0)
                p = rotateRight(p); //Fall A1
            else
                p = rotateLeftRight(p); //Fall A2
        } else if (getBalance(p) == +2) {
            if (getBalance(p.right) >= 0)
                p = rotateLeft(p); //B1
            else
                p = rotateRightLeft(p); //Fall B2
        }
        return p;
    }

    private Node<K,V> rotateRight(Node<K,V> p) {
        assert p.left != null;
        Node<K,V> q = p.left;
        p.left = q.right;
        if (p.left != null) {
            p.left.parent = p;
        }
        q.right = p;
        q.right.parent = q;
        p.height = Math.max(getHeight(p.left), getHeight(p.right)) + 1;
        q.height = Math.max(getHeight(q.left), getHeight(q.right)) + 1;
        return q;
    }

    private Node<K,V> rotateLeft(Node<K,V> p) {
        assert p.right != null;
        Node<K,V> q = p.right;
        p.right = q.left;
        if (p.right != null) {
            p.right.parent = p;
        }
        q.left = p;
        q.left.parent = q;
        p.height = Math.max(getHeight(p.left), getHeight(p.right)) + 1;
        q.height = Math.max(getHeight(q.left), getHeight(q.right)) + 1;
        return q;
    }

    private Node<K,V> rotateLeftRight(Node<K,V> p) {
        assert p.left != null;
        p.left = rotateLeft(p.left);
        //p.left.parent = p;
        return rotateRight(p);
    }

    private Node<K,V> rotateRightLeft(Node<K,V> p) {
        assert p.right != null;
        p.right = rotateRight(p.right);
        //p.right.parent = p;
        return rotateLeft(p);
    }

    private int getHeight(Node<K,V> p) {
        if (p == null)
            return -1;
        else
            return p.height;
    }

    @Override
    public Iterator<Entry<K, V>> iterator() {
        return new Iterator<>() {

            Node<K, V> p = leftMostDescendant(root); //startpunkt für iteration festlegen


            @Override
            public boolean hasNext() {
                return p != null;
            }

            @Override
            public Entry<K, V> next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }

                var e = new Entry<>(p.key, p.value);

                p = (p.right != null) ? leftMostDescendant(p.right) : parentOfRightMostAncestor(p);
                return e;
            }
        };
    }

    /**
     * gibt den am weitesten links stehenden Nochkommen des angegebenen Knotens p zurück
     * er durchläuft den linken teilbaum bis er den äußersten linken blattknoten erreicht
     */
    private Node<K,V> leftMostDescendant(Node<K,V> p) {
        if (p == null) {
            return null;
        }
        while (p.left != null) {
            p = p.left;
        }
        return p;
    }

    /**
     * gibt den übergeordneten Knoten des am weitesten links stehenden
     * Vorfahren des angegebenen Knotens p zurück.
     * er durchläuft die Vorgängerkette, bis er den ersten übergeordneten Knoten findet
     * dessen rechtes kind nicht der aktuelle knoten p ist.
     */
    private Node<K,V> parentOfRightMostAncestor(Node<K,V> p) {
        assert p != null;
        while (p.parent != null && p.parent.right == p) {
            p = p.parent;
        }
        return p.parent;
    }


    /**
     * Pretty prints the tree
     */
    public void prettyPrint() {
        printR(0, root);
    }

    private void printR(int level, Node<K, V> p) {
        printLevel(level);
        if (p == null) {
            System.out.println("#");
        } else {
            System.out.println(p.key + " " + p.value + "^" + ((p.parent == null) ? "null" : p.parent.key));
            if (p.left != null || p.right != null) {
                printR(level + 1, p.left);
                printR(level + 1, p.right);
            }
        }
    }

    //Einrückung steuern
    private static void printLevel(int level) { //level gibt an auf welcher Ebene im Baum sich der aktuelle Knoten befindet
        if (level == 0) { //prüfen ob Knoten die Wurzel des Baums -> keine Einrückung erforderlich
            return;
        }
        for (int i = 0; i < level - 1; i++) {
            System.out.print("   "); //richtige Anzahl der Leerzeichen vorm Strich drucken
        }
        System.out.print("|__"); //anfang des aktuellen Knotens markieren
    }
}

