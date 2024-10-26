// ByteVar.java

package aufgabe1;

import java.util.Scanner;

/**
 * ByteVar zeigt den Umgang mit Variablen vom Typ byte.
 * &Uuml;bungsaufgabe 1 zur Programmiertechnik 1.
 * @author Ellen Peppmüller
 * @version 10.04.2023
 */
public final class ByteVar {
    private ByteVar() { }

    private static final Scanner EINGABE = new Scanner(System.in);

    /**
     * main ist der Startpunkt des Programms.
     * @param args wird nicht verwendet.
     */
    public static void main(String[] args) {
        
        final byte min = Byte.MIN_VALUE;
        final byte max = Byte.MAX_VALUE;

        System.out.printf("Zwei ganze Zahlen zwischen %d und %d eingeben: %n",
            min, max);

        byte x = EINGABE.nextByte();
        byte y = EINGABE.nextByte();

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

