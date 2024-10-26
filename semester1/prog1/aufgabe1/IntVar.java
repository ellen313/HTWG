// IntVar.java

package aufgabe1;

import java.util.Scanner;

/**
 * IntVar zeigt den Umgang mit Variablen vom Typ int.
 * Uebungsaufgabe 1 zur Programmiertechnik 1.
 * @author Ellen Peppmüller
 * @version 30.03.2023
 */
public final class IntVar {
    private IntVar() { }

    private static final Scanner EINGABE = new Scanner(System.in);

    /**
     * main ist der Startpunkt des Programms.
     * @param args wird nicht verwendet.
     */
    public static void main(String[] args) {

        final int min = Integer.MIN_VALUE;
        final int max = Integer.MAX_VALUE;

        System.out.printf("Zwei ganze Zahlen zwischen %d und %d eingeben: %n",
                            min, max);
        
        /**
         * prueft, ob naechste Eingabe eine Ganzzahl enthaelt.
         * prueft, ob Eingabe einen nicht leeren Text enthaelt.
         * springt in die zweite schleife, wenn nicht mehr true
         */
        while (!EINGABE.hasNextInt() && !EINGABE.hasNext()) {
            System.out.println("Gültigen Wert angeben");
        }
        /**
         * prueft nur noch die Eingabe auf Integer ab.
         * EINGABE.next() liest Eingabe ohne sie zu verwenden.
         * ueberspringt die ungueltige Eingabe um zur naechsten zu gelangen.
         */
        while (!EINGABE.hasNextInt() && EINGABE.hasNext()) {
            System.out.println("Gültigen Wert angeben");
            EINGABE.next();
        }

        int x = EINGABE.nextInt();
        int y = EINGABE.nextInt();

        System.out.printf("%d ist oktal %o und hexadezimal %x%n", x, x, x);
        System.out.printf("%d ist oktal %o und hexadezimal %x%n", y, y, y);


        System.out.printf("%d + %d ist %d%n", x, y, x + y);
        System.out.printf("%d - %d ist %d%n", x, y, x - y);
        System.out.printf("%d * %d ist %d%n", x, y, x * y);
        System.out.printf("%d / %d ist %d%n", x, y, x / y);
        System.out.printf("%d %% %d ist %d%n", x, y, x % y);


        System.out.printf("%d == %d ist %b%n", x, y, x == y);
        System.out.printf("%d != %d ist %b%n", x, y, x != y);
        System.out.printf("%d < %d ist %b%n", x, y, x < y);
        System.out.printf("%d <= %d ist %b%n", x, y, x <= y);
        System.out.printf("%d > %d ist %b%n", x, y, x > y);
        System.out.printf("%d >= %d ist %b%n", x, y, x >= y);





    }
}

