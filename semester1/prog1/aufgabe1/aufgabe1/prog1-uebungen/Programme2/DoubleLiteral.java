// DoubleLiteral.java
002
003/**
004 * DoubleLiteral zeigt die Ausgabe von Gleitkommaliteralen auf der Konsole.
005 * Beispielprogramm zur Programmiertechnik 1, Teil 2.
006 * @author H.Drachenfels
007 * @version 7.1.2019
008 */
009public final class DoubleLiteral {
010    private DoubleLiteral() { }
011
012    /**
013     * main ist der Startpunkt des Programms.
014     * @param args wird nicht verwendet.
015     */
016    public static void main(String[] args) {
017        System.out.println((1e-30 + 1e30) - 1e30);
018        System.out.println(1e-30 + (1e30 - 1e30));
019
020        System.out.println(12.3456789);
021        System.out.println(1234567.89);
022
023        System.out.printf("%f%n", 12.3456789);
024        System.out.printf("%f%n", 1234567.89);
025
026        System.out.printf("%e%n", 12.3456789);
027        System.out.printf("%e%n", 1234567.89);
028    }
029}
030