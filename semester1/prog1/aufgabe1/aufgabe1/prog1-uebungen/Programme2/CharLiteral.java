// CharLiteral.java
002
003/**
004 * CharLiteral zeigt die Ausgabe von Zeichenliteralen auf der Konsole.
005 * Beispielprogramm zur Programmiertechnik 1, Teil 2.
006 * @author H.Drachenfels
007 * @version 7.1.2019
008 */
009public final class CharLiteral {
010    private CharLiteral() { }
011
012    /**
013     * main ist der Startpunkt des Programms.
014     * @param args wird nicht verwendet.
015     */
016    public static void main(String[] args) {
017        System.out.print('H');
018        System.out.print('a');
019        System.out.print('l');
020        System.out.print('l');
021        System.out.print('o');
022        System.out.print('\n');
023        System.out.print("Hallo\12"); // Zeilenwechsel hat Zeichencode oktal 12
024        System.out.println("Hal" + "lo");
025        System.out.printf("%s%n", "Hallo");
026        System.out.printf("%c%c%c%c%c%n", 'H', 'a', 'l', 'l', 'o');
027    }
028}