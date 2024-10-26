package aufgabe5;

/**
 * instanziierbare Klasse Fachnote.
 */
public final class Fachnote {

    /**
     * oeffentliche konstante Instanzvariable.
     */
    public final String fach;

    /**
     * oeffentliche konstante Instanzvariable.
     */
    public final Note note;

    /**
     * öffentlicher Konstruktor zum Initialisieren der beiden Instanzvariablen.
     * @param fach die zur Fachnote gehört
     * @param note vom Fach
     */
    public Fachnote(String fach, Note note) {
        if (fach == null || fach.length() == 0) {
            throw new IllegalArgumentException("Fachname darf nicht 0 sein "
                                                + "und Länge 0 haben.");
        }
        if (note == null) {
            throw new IllegalArgumentException("Note darf nicht 0 sein.");
        }
        this.fach = fach;
        this.note = note;
    }
}