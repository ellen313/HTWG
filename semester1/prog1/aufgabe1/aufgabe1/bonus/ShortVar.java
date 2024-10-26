// ShortVar.java

package aufgabe1;

import java.util.Scanner;

/**
 * ShortVar zeigt den Umgang mit Variablen vom Typ short.
 * &Uuml;bungsaufgabe 1 zur Programmiertechnik 1.
 * @author Ellen Peppmüller
 * @version 30.03.2023
 */
public final class ShortVar {
    private ShortVar() { }

    private static final Scanner EINGABE = new Scanner(System.in);

    /**
     * main ist der Startpunkt des Programms.
     * @param args wird nicht verwendet.
     */
    public static void main(String[] args) {

        final short min = Short.MIN_VALUE;
        final short max = Short.MAX_VALUE;

        System.out.printf("Zwei ganze Zahlen zwischen %d und %d eingeben: %n",
            min, max);

        short x = EINGABE.nextInt();
        short y = EINGABE.nextInt();

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

