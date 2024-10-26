// IntVarLong.java

package aufgabe1.bonus;

import java.util.Scanner;

/**
 * IntVar zeigt den Umgang mit Variablen vom Typ int.
 * Uebungsaufgabe 1 zur Programmiertechnik 1.
 * @author Ellen Peppmüller
 * @version 30.03.2023
 */
public final class IntVarLong {
    private IntVarLong() { }

    private static final Scanner EINGABE = new Scanner(System.in);

    /**
     * main ist der Startpunkt des Programms.
     * @param args wird nicht verwendet.
     */
    public static void main(String[] args) {

        final int min = Integer.MIN_VALUE;
        final int max = Integer.MAX_VALUE;
        String[] operatoren = {"+", "-", "/", "*", "%", "==",
                                "!=", "<", "<=", ">", ">="};

        System.out.printf("Zwei ganze Zahlen zwischen %d und %d eingeben: %n",
                            min, max);

        /**
         * prueft, ob naechste Eingabe eine Ganzzahl enthaelt.
         */
        while (!EINGABE.hasNextInt()) {
            System.out.println("Gültigen Wert angeben");
            EINGABE.next();
        }

        long x = EINGABE.nextInt();
        long y = EINGABE.nextInt();

        System.out.println("Operator eingeben: ");

        /** Operator einlesen */
        boolean istOperator = false;
        String operatorString = "";
        while (EINGABE.hasNext()) {
            String tempOperatorString = EINGABE.next();
            for (i = 0; i < operatoren.length(); i++) {
                if (tempOperatorString.equals(operatoren[i])) {
                    istOperator = true;
                }
            }
            if (istOperator) {
                operatorString = tempOperatorString;
                break;
            } else {
                System.out.println("Gültigen Operator eingeben");
            }
        }

        System.out.printf("%d ist oktal %o und hexadezimal %x%n", x, x, x);
        System.out.printf("%d ist oktal %o und hexadezimal %x%n", y, y, y);


        switch (operatorString) {
        case "+":
            System.out.println(x + "+" + y + "ist"
                + istInteger(x + y));
            break;
        case "-":
            System.out.println(x + "-" + y + "ist"
                + istInteger(x - y));
            break;
        case "/":
            System.out.println(x + "/" + y + "ist"
                + istInteger(x / y));
            break;
        case "*":
            System.out.println(x + "*" + y + "ist"
                + istInteger(x * y));
            break;
        case "%":
            System.out.println(x + "%" + y + "ist"
                + istInteger(x % y));
            break;
        case "==":
            System.out.println(x + "==" + y + "ist" + (x == y));
            break;
        case "!=":
            System.out.println(x + "!=" + y + "ist" + (x != y));
            break;
        case "<":
            System.out.println(x + "<" + y + "ist" + (x < y));
            break;
        case "<=":
            System.out.println(x + "<=" + y + "ist" + (x <= y));
            break;
        case ">":
            System.out.println(x + ">" + y + "ist" + (x > y));
            break;
        case ">=":
            System.out.println(x + ">=" + y + "ist" + (x >= y));
            break;
        default:
            break;
        }

    }
    /**
     * istInteger testet, ob der uebergebene Wert long im Wertbereich fuer
     * Integer liegt und gibt entsprechenden String zurueck.
     * @param testValue : Wert, dessen Groeße ueberprueft wird.
     * @return String der zurueckgegeben wird.
     */
    public static String istInteger(final long testValue) {
        if (testValue <= max && testValue >= min) {
            return Long.toString(testValue);
        } else {
            return "nicht im Wertbereich für Integer.";
        }
    }
}