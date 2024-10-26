// Notenspiegel.java
package aufgabe5;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Notenspiegel liest die Namen von Faechern mit den zugehoerigen Noten
 * in eine verkettete Liste ein und gibt dann einen Notenspiegel aus.
 * @author Ellen Peppmueller
 * @version 23.05.23
 */
public final class Notenspiegel {
    private Notenspiegel() { }

    private static final Scanner EINGABE = new Scanner(System.in);

    /**
     * main ist der Startpunkt des Programms.
     * @param args wird nicht verwendet.
     */
    public static void main(String[] args) {
        FachnotenListe liste = new FachnotenListe(); // leere Liste

        //--------------------------------------------- Notenspiegel einlesen
        System.err.printf(
            "Faecher mit Noten zwischen %d und %d eingeben "
            + "(Ende mit Strg-D):%n",
            Note.BESTE.intValue(), Note.SCHLECHTESTE.intValue());

        while (EINGABE.hasNext()) {
            try {
                //------------------------------------ Fach und Note einlesen

                /* Fach mit next() einlesen, mit hasNextInt()
                 * pruefen, ob die Note als ganze Zahl vorliegt,
                 * Note je nachdem mit nextInt() oder next()
                 * einlesen und schliesslich in value object verpacken
                 * ein neues Fachnote-Objekt erzeugen
                 * und am Listenanfang einfuegen
                 */

                String fach = EINGABE.next();

                if (EINGABE.hasNextInt()) {
                    var note = Note.valueOf(EINGABE.nextInt());
                    liste.insert(new Fachnote(fach, note));
                } else {
                    var note = Note.valueOf(EINGABE.next());
                    liste.insert(new Fachnote(fach, note));
                }

                //--------------------- neue Fachnote in Notenliste eintragen

            } catch (IllegalArgumentException x) {
                System.err.printf("Eingabefehler: %s%n", x.getMessage());
                continue;
            } catch (NoSuchElementException x) {
                System.err.println("Fach ohne Note ignoriert!");
                break;
            }
        }

        //--------------------------------------------- Notenspiegel ausgeben

        // tabellarischen Notenspiegel mit Ueberschrift NOTENSPIEGEL ausgeben
        System.out.println("\nNOTENSPIEGEL");
        final int ten = 10;

        //nächste Eintrag in Liste wird über Iterator abgerufen und in
        //Variable next gespeichert.Jedes Element in Liste wird als FachnoteObj
        //erwartet.Variable noteOutput wird deklariert die Ausgabetext enthält
        FachnotenListe.Iterator iterator = liste.new Iterator();
        while (iterator.hasNext()) {
            Fachnote next = iterator.next();

            String noteOutput;
            if (next.note.intValue() == ten) {
                noteOutput = "mit Bestnote bestanden";
            } else if (next.note.istBestanden()) {
                noteOutput = "bestanden";
            } else {
                noteOutput = "nicht bestanden";
            }

            //linksbündig (-) mit einer Breite von 25, 5 Zeichen formatiert
            System.out.printf("%-25s%-5s%s%n", next.fach, next.note,
                            noteOutput);
        }

    } // main
}

