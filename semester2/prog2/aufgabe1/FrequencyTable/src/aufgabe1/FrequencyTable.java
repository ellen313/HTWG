package aufgabe1;

public interface FrequencyTable {
    /**
     * liefert die Anzhal der Woerter in dieser Tabelle zurueck
     * @return Anzahl der Haufigkeitseintraege
     */
    int size();

    /**
     * Prueft, ob die Tabelle leer ist.
     * @return true, falls leer, sonst false
     */
    boolean isEmpty();

    /**
     * L&ouml;scht die Tabelle.
     */
    void clear();

    /**
     * Fuegt das Wort w mit der Haeufigkeit f zu dieser Tabelle dazu
     * Falls w bereits in der Tabelle enthalten ist,
     * wird die Haeufigkeit um f erhoeht
     * @param w Wort
     * @param f Haeufigkeit
     */
    void add(String w, int f);

    /**
     * Fuegt das Wort w mit der Haeufigkeit 1 zu dieser Tabelle dazu
     * Falls w bereits enthalten, Haeufigkeit um 1 erhoeht
     * @param w Wort
     */
    void add(String w);

    /**
     * Fuegt alle Woerter mit ihrer Haeufigkeiten aus fq zu dieser Tabelle dazu
     * Haeufigkeiten fuer gleiche Woerter werden addiert
     * fq bleibt unveraendert
     * @param fq Haeufigkeitstabelle
     */
    void addAll(FrequencyTable fq);

    /**
     * Liefert das Wort mit seiner Haeufigkeit zurueck, das mit seiner Haeufigkeit an Position pos steht
     * get(0) liefert das haeufigste Wort zurueck
     * get(1) liefert das zweithaeufigste Wort zurueck, etc.
     * @param pos Position
     * @return Wort mit Haeufigkeit oder null,
     * falls die Tabelle weniger als pos-1 Element enthaelt
     */
    Word get(int pos);

    /**
     * Liefert die Haeufigkeit des Worts w zurueck
     * Falls das Wort nicht vorkommt, wird 0 zurueckgeliefert
     * @param w Wort
     * @return Haeufigkeit
     */
    int get(String w);

    /**
     * speichert die n haeufgsten Woertser in fq
     * Falls die Tabelle weniger als n Eintraege hat werden alle Woerter in fq gespeichert
     * Beispiel
     * Falls tab1 = {"ein":3, "das":3, "ist":2, "der:1", "die":1},
     * dann gilt nach Aufruf von tab1.collectMostFrequent(3,tab2):
     * tab2 = {"ein":3, "das":3, "ist":2}
     * @param n Anzahl Woerter, die gespeichert werden sollen
     * @param fq Haeufigkeitstabelle
     */
    void collectNMostFrequent(int n, FrequencyTable fq);
}
