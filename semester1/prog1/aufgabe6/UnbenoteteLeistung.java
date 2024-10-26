package aufgabe6;

/**
 * instanziierbare Klasse fuer Entitaten als Unterklasse von Leistung.
 */
public final class UnbenoteteLeistung extends Leistung {

    //konstante pvt Instanzvariable, die speichert, ob bestanden.
    private boolean bestanden;

    /**
     * oeffentlicher Konstruktor fuer Objekte vom Typ UnbenoteteLeistung.
     * @param fach das Fach
     * @param bestanden ob bestanden oder nicht
     */
    public UnbenoteteLeistung(String fach, boolean bestanden) {
        super(fach);
        this.bestanden = bestanden;
    }

    /**
     * gibt an, ob Fach bestanden ist.
     * @return ob fach bestanden.
     */
    public boolean istBestanden() {
        return bestanden;
    }

    /**
     * gibt an, ob Fach benotet ist.
     * @return ob fach benotet.
     */
    public boolean istBenotet() {
        return false;
    }

    /**
     * Note des Fachs in Worten ausgeben mit get().
     * @return ob bestanden ist.
     */
    @Override
    public String getNoteInWorten() {
        if (istBestanden()) {
            return "bestanden";
        } else {
            return "nicht bestanden";
        }
    }

}