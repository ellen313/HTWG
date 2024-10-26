  1  // IntLiteral.java   
  2     
  3  /**
  4   * IntLiteral zeigt die Ausgabe von Zahlenliteralen auf der Konsole.
  5   * Beispielprogramm zur Programmiertechnik 1, Teil 2.
  6   * @author H.Drachenfels
  7   * @version 7.1.2019
  8   */   
  9  public final class IntLiteral {
 10      private IntLiteral() { }  11    12      /**
 13       * main ist der Startpunkt des Programms.
 14       * @param args wird nicht verwendet.
 15       */  
 16      public static void main(String[] args) {  
 17          System.out.println(12);
 18          System.out.println(012);
 19          System.out.println(0x12);  
 20          System.out.printf("%x%n", 12);
 21          System.out.printf("%d%n", 012);
 22          System.out.printf("%o%n", 0x12);  
 23      }  
 24  }