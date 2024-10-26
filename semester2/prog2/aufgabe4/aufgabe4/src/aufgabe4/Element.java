package aufgabe4;

public class Element<T> {
    final private T element;
    private int frequency;

    public Element(T element, int f) {
        this.element = element;
        this.frequency = f;
    }

    public T getElement() {
        return element;
    }

    public int getFrequency() {
        return frequency;
    }

    public void addFrequency(int f) {
        frequency += f;
    }

    @Override
    public String toString() {
        return element + ":" + frequency;
    }
}
