// Klausurergebnis.java
package aufgabe4;
//import aufgabe4.schweiz.Noten;
import java.util.Locale;
import java.util.Scanner;

/**
 * Klausurergebnis erstellt eine Notenstatistik.
 * <p>
 * Das Programm liest Noten als Strings ein und bestimmt die beste und
 * die schlechteste Note, den Durchschnitt der Bestandenen sowie
 * die Durchfallquote in Prozent.
 * Das Programm ber&uuml;cksichtigt dabei nur die laut Noten.istZulaessig
 * erlaubten Noten. Andere Noten werden unter Ausgabe einer Warnung ignoriert.
 * Welche Noten besser und schlechter sind, welche als bestanden oder
 * durchgefallen gelten und wie die String-Darstellung der Noten aussieht,
 * wird mit Methoden der Klasse Noten bestimmt.
 * </p>
 * Das Programm gibt als Klausurergebnis folgende Werte aus:
 * <ul>
 * <li>die Anzahl der ber&uuml;cksichtigten Noten</li>
 * <li>die Anzahl der Bestandenen</li>
 * <li>die beste Note</li>
 * <li>die schlechteste Note</li>
 * <li>den Durchschnitt der Bestandenen</li>
 * <li>die Durchfallquote</li>
 * </ul>
 *
 * @author Ellen Peppmueller
 * @version 12.05.23
 */
public final class Klausurergebnis {
    private Klausurergebnis() { }

    private static final Scanner EINGABE = new Scanner(System.in);

    /**
     * main ist der Startpunkt des Programms.
     * @param args wird nicht verwendet.
     */
    public static void main(String[] args) {
        Locale.setDefault(Locale.GERMAN);

        int passed = 0;
        int failed = 0;
        double best = Noten.BESTE;
        double worst = Noten.SCHLECHTESTE;
        double average = 0.0;

        //--------------------------------------------------- Noten einlesen
        System.out.println("Noten im Format Ganze,Zehntel "
                           + "oder Ganze.Zehntel eingeben (Ende mit Strg-D):");

        while (EINGABE.hasNext()) {
            System.out.println("while");
            String note = EINGABE.next();

            double noteValue = 0.0; //temporaerer wert fuer die Note

            //---------------------------------------------- Eingabe pruefen

            String noteReplaceS = note.replace(",", ".");
            boolean istZulaessig = true;

            try {
                noteValue = Noten.doubleNote(noteReplaceS);
            } catch (Exception e) {
                istZulaessig = false;
            }


            //------------------------------------------------ Note erfassen

            /*(2) Notensumme Bestandene, Anzahl Bestandene,
                         Anzahl Durchgefallene sowie
                         beste und schlechteste Note aktualisieren ... */

            if (istZulaessig) { //falls Note gueltig, auswerten
                if (Noten.istBestanden(noteValue)) {
                    passed++; //wenn bestanden, passed erhöht
                    //Bestandene aktualisieren
                    if (average != 0.0) {
                        average = ((average * (passed - 1)) / passed)
                                    + (noteValue / passed);
                    } else {
                        average = noteValue;
                    }
                } else if (noteValue != 0.0) {
                    failed++;
                }
                //beste und schlechteste aktualisieren
                best = Noten.bessere(best, noteValue);
                worst = Noten.schlechtere(worst, noteValue);
            } else { //Fehlermeldung ausgeben
                System.out.println("Unzulaessige Note " + note
                                    + " wird ignoriert!");
            }


        } // while

        final int checked = passed + failed;
        final double failureRate = ((double) failed / (double) checked) * 100.0;

        average =
                average / (double) passed;

        //------------------------------------------ Notenstatistik ausgeben

        /*(3) Durchschnitt und Durchfallquote berechnen
                     und dann die gesamte Statistik ausgeben ... */

        System.out.println("\nAnzahl beruecksichtigter Noten: " + checked);
        System.out.println("Anzahl Bestandene: " + passed);

        if (passed > 0) {
            System.out.println("Beste Note: "
                    + String.format("%.1f", best));
            System.out.println("Schlechteste Note: "
                    + String.format("%.1f", worst));
            System.out.println("Durchschnitt Bestandene: "
                    + String.format("%.1f", average));
            System.out.println("Durchfallquote: "
                    + String.format("%.1f%%", failureRate));
        }


    } // main
}

