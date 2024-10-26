// Histogramm.java
package aufgabe2;

import java.util.Scanner;

/**
 * Histogramm liest ganze Zahlen zwischen 1 und 6 ein und
 * gibt deren Häufigkeitsverteilung als Histogramm aus.
 * @author Ellen Peppmüller
 * @version 12.04.23
 */
public final class Histogramm {
    private Histogramm() { }

    private static final Scanner EINGABE = new Scanner(System.in);

    /**
     * main ist der Startpunkt des Programms.
     * @param args wird nicht verwendet.
     */
    public static void main(String[] args) {

        //----------------------------------------- Feld von Zählern definieren
        final int five = 5;
        final int max = 6;

        int[] anIntArray = {0, 0, 0, 0, 0, 0};

        //---------------------------------------------------- Zahlen einlesen
        System.out.println("Ganze Zahlen zwischen 1 und 6 eingeben "
                    + "(Ende mit Strg-D/Strg-Z):");


        while (EINGABE.hasNextInt()) {
            int number = EINGABE.nextInt();


        //----------------------------------------------- Prüfen und Zählen
            if (number >= 1 && number <= max) {
                ++anIntArray[number - 1];
            } else {
                System.out.println("Error");
            }

        }

        //------------------------------------------------ Histogramm ausgeben 

        System.out.println("Histogramm:");
        for (int i = 0; i < anIntArray.length; ++i) {
            for (int j = 1; j <= anIntArray[i]; ++j) {
                if (j % five == 0) {
                    System.out.print("$ ");
                } else {
                    System.out.print(i + 1);
                }
            }
            System.out.printf("(%d)%n", anIntArray[i]);
        }
    }
}
