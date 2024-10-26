package aufgabe1;

public class Word {
    final private String word;
    private int frequency;

    public Word(String word, int f) {
        this.word = word;
        this.frequency = f;
    }

    public String getWord() {
        return word;
    }

    public int getFrequency() {
        return frequency;
    }

    public void addFrequency(int f) {
        frequency += f;
    }

    @Override
    public String toString() {
        return word + ":" + frequency;
    }
}
