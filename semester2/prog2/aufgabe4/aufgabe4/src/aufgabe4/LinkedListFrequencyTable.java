package aufgabe4;

import javax.swing.tree.AbstractLayoutCache;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class LinkedListFrequencyTable<T> extends AbstractFrequencyTable<T> {
    private Node<T> begin;
    private Node<T> end;
    private int size;

    @Override
    public Iterator<Element<T>> iterator() {
        return new Iterator<Element<T>>() {
            Node<T> p = begin.next;

            @Override
            public boolean hasNext() {
                return p != end;
            }

            @Override
            public Element<T> next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                Element<T> element = p.element;
                p = p.next;
                return element;
            }
        };
    }


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
        begin = new Node<>(null, null, null);
        end = new Node<>(null, null, begin);
        begin.next = end;
    }

    @Override
    public void add(T e, int f) {
        if (e == null || f < 1) {
            return;
        }

        Node<T> p = begin.next;
        while (p != end && !p.element.getElement().equals(e)) {
            p = p.next;
        }

        if (p == end) {
            Element<T> newEl = new Element<T>(e, f);
            Node<T> newNode = new Node<T>(newEl, end, end.prev);
            end.prev.next = newNode;
            end.prev = newNode;
            size++;
            moveLeft(newNode);
        } else {
            p.element.addFrequency(f);
            moveLeft(p);
        }
    }
    @Override
    public Element<T> get(int pos) {
        if (pos < 0 || pos >= size) {
            throw new IndexOutOfBoundsException();
        }
        Node<T> p = begin.next;
        for (int i = 0; i < pos; i++) {
            p = p.next;
        }
        return p.element;
    }

    @Override
    public int get(T e) {
        Node<T> p = begin.next;
        while (p != end) {
            if (p.element.getElement().equals(e)) {
                return p.element.getFrequency();
            }
            //zeiger wird verschoben
            p = p.next;
        }
        //false: 0 zurückgeben, da nicht existent
        return 0;
    }

    private void sort(Node<T> a, Node<T> b) {
        Element<T> tmp = a.element;
        a.element = b.element;
        b.element = tmp;
    }

    private void moveLeft(Node<T> node) {
        Node<T> p = node;
        while (p.prev != begin && p.element.getFrequency() > p.prev.element.getFrequency()) {
            sort(p, p.prev);
            p = p.prev;
        }
    }
    private static class Node<T> {
        private LinkedListFrequencyTable.Node<T> next;
        private LinkedListFrequencyTable.Node<T> prev;
        private Element<T> element;

        public Node(Element<T> e, LinkedListFrequencyTable.Node<T> n, LinkedListFrequencyTable.Node<T> p) {
            this.element = e;
            this.next = n;
            this.prev = p;
        }
    }
}