/**
 * class Evaluator
 * repl-Schleife: lese von der Konsole eine Ziele und werte sie als
 * arithmetischen Ausdruck aus.
 * Da tokenizer String-Konstante zurueckliefert, reicht Gleichheitspruefung mit == aus
 * siehe z.B. shift-Methode
 *
 * Ihr Name: Ellen
 * Datum:26.20.23
 */

package expressionevaluation;
 import java.util.Arrays;
 import java.util.Scanner;
 import static expressionevaluation.Tokenizer.*;

/**
 * Klasse zum Auswerten von arithmetischen Ausdruecken.
 */
public class Evaluator {
 private static final String ANSI_BLUE = "\u001B[34m";
 private static Object[] stack = new Object[10]; //Stack
 private static int top = -1; //Index des obersten Kellerelements
 private static Object token; // Aktuelles Token
 private static Tokenizer tokenizer; // Zerlegt String-Eingabe in Token

 /**
  * Wertet expr als arithmetischen Ausdruck aus.
  *
  * @param expr Arithmetischer Ausdruck als String
  * @return Wert des Ausdrucks oder null, falls der Ausdruck fehlerhaft ist.
  */
 public static Double eval(String expr) {
  //Dollar in leeren Stack ablegen:
  top = -1;
  stack[++top] = DOLLAR;

  //expr in Tokens zerlegen und erstes Token abholen:
  tokenizer = new Tokenizer(expr);
  token = tokenizer.nextToken();

  while (token != null) {
   if (accept()) {
    //wenn aktuelles Token akzeptiert wird, wird Element an der Spitze
    // des Stapels in einen double umgewandelt und zurückgegeben
    return (double)stack[top];
   } else if (shift()) {
    //wenn nicht akzeptiert wird shift() ueberprueft
    //naechster token wird aus tokenizer geholt  und in Var token gespeichert
    token = tokenizer.nextToken();
   } else if (reduce()) {
    //keine Rückgabe oder Aktion -> Parser nicht erforderlich
   } else {
    //abbruch (token nicht erkannt oder verarbeitet oder so)
    break;
   }
  }
  //nach Verarbeitungsende kein akzeptabler wert gefunden
  return null;
 }

 private static boolean shift() { //token von eingabe in stack verschoben
  if (stack[top] == DOLLAR && (token == KL_AUF || isVal(token))) { // 1. Regel
   //token verschieben
   doShift();
   return true;
  } else if (isOp(stack[top]) && (token == KL_AUF || isVal(token))) { //2. Regel
   doShift();
   return true;
  } else if (stack[top] == KL_AUF && (token == KL_AUF || isVal(token))) { //3. Regel
   doShift();
   return true;
  } else if (stack[top - 1] == DOLLAR && isVal(stack[top]) && isOp(token)) { //6. Regel
   doShift();
   return true;
  } else if (stack[top - 1] == KL_AUF && isVal(stack[top]) && (token == KL_ZU || isOp(token))) { //7. Regel
   doShift();
   return true;
  } else if ((top - 2 > -1 && isVal(stack[top - 2]) && isVal(stack[top]) && isOp(token) &&
          //Präzedenz Angabe
          ((stack[top - 1] == PLUS && token != PLUS) || (stack[top - 1] == MULT && token == POWER))) ) {// 9. Regel
   doShift();
   return true;
  } else {
   //kein shift durchgefuehrt
   return false;
  }
 }

 private static void doShift() {
  if (top == stack.length - 1) { //stack fast voll
   //wenn true: stack erweitert -> Stack kopieren, in neuen doppelt so großen array speichern
   stack = Arrays.copyOf(stack, 2*stack.length);
  }
  //token an naechster verfuegbaren Pos. im stack platziert
  top++;
  //aktuelle token in stack an pos top verschoben
  stack[top] = token;
 }

 private static boolean isOp(Object o) {
  return (o == PLUS || o == MULT || o == POWER);
 }

 private static boolean isVal(Object o) {
  return o instanceof Double;
 }

 private static boolean reduce() {
  //muss minimum an elementen im stack vorhanden sein um reduktion durchzufuehren
  //vorletztes element muss oeffnendes klammer zeichen sein
  //element direkt vor klammer muss als wert erkannt sein
  //aktuelles element muss schließendes klammerzeichen sein
  //aktuelles token muss schließendes KZ oder Operator oder Ende der eingabe sein
  if (top > 1 && stack[top -2] == KL_AUF && isVal(stack[top -1]) && stack[top] == KL_ZU &&
          (token == KL_ZU || isOp(token) || token == DOLLAR)) { //4. Regel
   doReduceKlValKl(); //um reduktionsaktion durchzufuehren
   return true;
   //muss minimum an elementen im stack vorhanden sein um reduktion durchzufuehren
   //vorletzte element muss als wert erkannt sein
   //element direkt vor vorletztem element muss als operator erkannt sein
   //aktuelles element muss alswert erkannt sein
   //aktuelle Token muss schließendes Klammerzeichen, Ende der Eingabe oder Operator sein
   //bestimmte Kombinationen von Operatoren müssen erfüllt sein
  } else if (top > 1 && isVal(stack[top - 2]) && isOp(stack[top - 1]) && isVal(stack[top]) &&
          (token == KL_ZU || token == DOLLAR || (isOp(token) &&
                  (token == PLUS || stack[top - 1] == POWER || token == stack[top - 1]) )) ) { //8. und 9. Regel
   doReduceValOpVal();
   return true;
  }
  return false;
 }

 private static void doReduceKlValKl() {
  //Object[] old = stack; //temp. Referenz die auf aktuellen parser stack zeigt (speicherung)
  stack[top - 2] = stack[top - 1]; //vorletztes el. auf wert des vorherigen el.
  stack[top] = null; //um alten werte zu löschen
  stack[top - 1] = null; // -"-
  top -= 2;//um auf zuletzt hinzugefuegtes el. zu verweisen
 }

 private static void doReduceValOpVal() {
  if (stack[top - 1] == PLUS) {
   //vorletztes und oberstes el. wird zu double wert gecastet,dann addiert, dann gespeichert (im vorletzten stack)
   stack[top - 2] = (double)stack[top - 2] + (double)stack[top];
  } else if (stack[top - 1] == MULT) {
   stack[top - 2] = (double)stack[top - 2] * (double)stack[top];
  } else if (stack[top - 1] == POWER) {
   stack[top - 2] = Math.pow((double)stack[top - 2], (double)stack[top]);
  }
  stack[top] = null;
  stack[top - 1] = null;
  top -= 2;
 }

 private static boolean accept() {
  //mind. 1 el. erforderlich im stack
  //vorletzte el. muss endmarkierungssymbol DOLLAR sein
  if (top > 0 && stack[top - 1] == DOLLAR && isVal(stack[top]) && token == DOLLAR) { //5. Regel
   return true;
  }
  return false;
 }

 /**
  * Liest von der Konsole eine Folge von Zeilen, wertet jede Zeile als
  * Ausdruck aus und gibt seinen Wert aus. (repl = read-evaluate-print-loop).
  */
 public static void repl() {
  Scanner in = new Scanner(System.in);
  System.out.println(ANSI_BLUE + ">> ");

  while (in.hasNextLine()) {
   String line = in.nextLine();
   if (line.equals("end")) {
    System.out.println("bye!");
    break;
   }
   System.out.println(eval(line) == null ? "!!! error" : eval(line));
   System.out.println(ANSI_BLUE + ">> ");
  }
 }

 /**
  * Testprogramm.
  *
  * @param args wird nicht benutzt.
  */
 public static void main(String[] args) {
  // Zum Testen, spÃ¤ter auskommentieren:
  String s1 = "1+2";
  String s2 = "2^10+5";
  String s3 = "5+2^10";
  String s4 = "(2+3*4+4)^2";
  String s5 = "(2+3*4+4))*2";
  String s6 = "2+3**4";
  String s7 = "2^2^3";
  String s8 = "2^2*5";
  String s9 = "1+(2+(3+(4+(5+6))))";
  String s10 = "1+2+3+4+5+6";

  System.out.println(eval(s1));    // 3.0
  System.out.println(eval(s2));    // 1029.0
  System.out.println(eval(s3));    // 1029.0
  System.out.println(eval(s4));    // 324.0
  System.out.println(eval(s5));    // null; Syntaxfehler
  System.out.println(eval(s6));    // null; Syntaxfehler
  System.out.println(eval(s7));    // 64.0
  System.out.println(eval(s8));    // 20.0
  System.out.println(eval(s9));    // 21.0
  System.out.println(eval(s10));   // 21.0

  repl();
  }
 }

