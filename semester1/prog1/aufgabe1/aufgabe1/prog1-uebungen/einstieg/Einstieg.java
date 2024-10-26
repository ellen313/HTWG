//Einstieg.java
package einstieg;

import java.util.Scanner;

/** 
* Einstieg ist ein kleines Java-Programm.
* Es verwendet die Klassen Scanner und System aus der Java-Bibliothek.
* @author TODO: Name eintragen und TODO inklusive Doppelpunkt löschen
* @version TODO: Datum eintragen und TODO inklusive Doppelpunkt löschen
*/
public final class Einstieg {
    private Einstieg() { }

    private static final Scanner EINGABE = new Scanner(System.in);

    /** 
    * Die Klassenmethode main ist der Startpunkt des Programms.
    * main verwendet die Methoden print und printf zum Ausgeben von Text
    * sowie die Methoden next und nextInt zum EInlesen von Text
    * und einer ganzen Zahl.
    * @param args wird nicht verwendet
    */
    public static void main(String[] args) {
        System.out.print("Vorname: ");
        String vorname = EINGABE.next();

        System.out.print("Anzahl bisher geschriebener Java-Programme: ");
        int anzahl = EINGABE.nextInt();

        System.out.printf("%ss %d. Java-Programm funktioniert!%n", vorname, anzahl + 1);
    }
}