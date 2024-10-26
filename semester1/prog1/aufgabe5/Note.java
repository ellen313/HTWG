package aufgabe5;

/**
 * instanziierbare Klasse fuer Wertobjekte.
 */
public final class Note {
    private int note;

    /**
     * privater Konstruktor zum Initialisieren der Parameter.
     * @param note wird initialisiert
     */
    private Note(int note) {
        this.note = note;
    }

    /**
     * beste moegliche Note definieren.
     */
    public static final Note BESTE = new Note(1);

    /**
     * schlechteste mögliche Note definieren.
     */
    public static final Note SCHLECHTESTE = new Note(5);

    /**
     * öffentliche Fabrikmethode die ein Objekt zurückgibt.
     * @param note ist com Typ int
     * @return Wert ist ein Note-Objekt
     */
    public static Note valueOf(int note) {
        switch (note) {
        case 10, 13, 17, 20, 23, 27, 30, 33, 37, 40, 50:
            return new Note(note);
        default:
            throw new IllegalArgumentException("unzulaessige Note " + note);
        }
    }

    /**
     * weitere öffentliche Fabrikmethode mit Fehlertext.
     * @param note vom Typ String
     * @return im Format toString
     */
    public static Note valueOf(String note) {
        switch (note) {
        case "1,0", "1,3", "1,7", "2,0", "2,3", "2,7", "3,0",
                "3,3", "3,7", "4,0", "5,0":
            return new Note(Integer.parseInt(note.replace(",", "")));
        default:
            throw new IllegalArgumentException("unzulaessige Note " + note);
        }
    }

    /**
     * öffentliche Instanzmethode, die im Objekt gespeicherte Note liefert.
     * @return note als int
     */
    public int intValue() {
        return this.note;
    }

    /**
     * öffentliche Instanzmethode, true wenn Wert größer als 40.
     * @return true oder false
     */
    public boolean istBestanden() {
        return this.note <= 40;
    }

    /**
     * öffentliche Instanzmethode toString.
     * @return String Format der Note
     */
    @Override
    public String toString() {
        return String.format("%.1f", note / 10.0).replace('.', ',');
    }

    /**
     * öffentliche Instanzmethode prüft, ob zwei Objekte equal sind.
     * @param o das zu vergleichende Objekt
     * @return true wenn sie gleich sind
     */
    @Override
    public boolean equals(Object o) {
        if (o instanceof Note) {
            Note that = (Note) o;
            return this.note == that.note;
        }
        return false;
    }

    /**
     * Erstelle einen hashcode mit dem Wert der Note.
     * @return die Note
     */
    @Override
    public int hashCode() {
        return this.note;
    }
}