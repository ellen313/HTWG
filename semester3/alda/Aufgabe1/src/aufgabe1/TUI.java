package aufgabe1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.Scanner;

public class TUI {
    private static Dictionary<String, String> dictionary = new SortedArrayDictionary<>();

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("Befehlseingabe (create, r, p, s, i, d, exit) : ");
            String command = sc.nextLine();
            if (command.startsWith("create")) {
                String[] splitted = command.split(" "); //anhand leerzeichen aufsplitten
                if (splitted[1].equals("SortedArrayDictionary")) {
                    dictionary = new SortedArrayDictionary<>(); //Instanz erstellen
                } else {
                    System.out.println("Implementierung fehlgeschlagen");
                }
            } else if (command.startsWith("r ")) {
                try {
                    //wenn befehl aus 3 teilen besteht wird neues dictionary erstellt
                    String[] splitted = command.split(" ");
                    if (splitted.length == 3) {
                        int n = Integer.parseInt(splitted[1]); //anzahl wörter
                        createDictionary(n, splitted[2]); //erstellt mit angegebenen anzahl von wörtern aus angegebenen datei
                    } else if (splitted.length == 2) {
                        //-1: alle Wörter der Datei
                        createDictionary(-1, splitted[1]);
                    }
                } catch (Exception e) {
                    e.printStackTrace(); //standardoutput
                }
            } else if (command.equals("p")) {
                System.out.println(dictionary);
            } else if (command.startsWith("s ")) {
                String[] splitted = command.split(" ");
                System.out.println(dictionary.search(splitted[1])); //sucht nach zweitem wort
            } else if (command.startsWith("i ")) {
                String[] splitted = command.split(" ");
                dictionary.insert(splitted[1], splitted[2]); //fügt zweites wort als schlüssel und drittes als wert ein
            } else if (command.startsWith("d ")) {
                String[] splitted = command.split(" ");
                dictionary.remove(splitted[1]); //entfernt eintrag mit schlüssel der dem zweiten wort entspricht
            } else if (command.equals("exit")) {
                System.exit(1);
            } else if (command.equals("m")) { //performance untersuchung- CPU Zeit
                System.out.println("SortedArrayDictionary");
                System.out.println("SortedArrayDictionary");
                measureCreationTime(8000, 10, "Aufbau 8000: ", Type.sorted);
                measureCreationTime(16000, 10, "Aufbau 16000: ", Type.sorted);
                measureSearchTime(8000, 100, "Search Success 8000: ", Type.sorted, true);
                measureSearchTime(16000, 100, "Search Success 16000: ", Type.sorted, true);
                measureSearchTime(8000, 100, "Search Failure 8000: ", Type.sorted, false);
                measureSearchTime(16000, 100, "Search Failure 16000: ", Type.sorted, false);
                System.out.println("");
                System.out.println("HashDictionary");
                measureCreationTime(8000, 500, "Aufbau 8000: ", Type.hash);
                measureCreationTime(16000, 500, "Aufbau 16000: ", Type.hash);
                measureSearchTime(8000, 100, "Search Success 8000: ", Type.hash, true);
                measureSearchTime(16000, 100, "Search Success 16000: ", Type.hash, true);
                measureSearchTime(8000, 100, "Search Failure 8000: ", Type.hash, false);
                measureSearchTime(16000, 100, "Search Failure 16000: ", Type.hash, false);
                System.out.println("");
                System.out.println("BinaryTreeDictionary");
                measureCreationTime(8000, 500, "Aufbau 8000: ", Type.binary);
                measureCreationTime(16000, 500, "Aufbau 16000: ", Type.binary);
                measureSearchTime(8000, 100, "Search Success 8000: ", Type.binary, true);
                measureSearchTime(16000, 100, "Search Success 16000: ", Type.binary, true);
                measureSearchTime(8000, 100, "Search Failure 8000: ", Type.binary, false);
                measureSearchTime(16000, 100, "Search Failure 16000: ", Type.binary, false);
                System.out.println("");
            }
        }
    }
    private static void createDictionary(int n, String file) {
        BufferedReader rd;
        try {
            //öffne Textdatei für readinf
            rd = new BufferedReader(new FileReader("C:\\Users\\ellen\\OneDrive\\Documents\\Studium\\Semester3\\AuD\\uebung\\Aufgabe1\\src\\aufgabe1\\" + file));
            String line = rd.readLine();
            int i = 0;
            //liest Zeilen
            while (line != null && i < n) {
                //aufteilen in deutsch und englische wörter
                String[] words = line.split(" ");
                String german = words[0];
                String english = words[1];
                //füge das Wortpaar ins Dictionary ein
                dictionary.insert(german, english);
                //lese die nächste Zeile
                line = rd.readLine();
                i++;
            }
        } catch (Exception e) {
            //gebe exception aus während dem datei lesen
            e.printStackTrace();
        }
    }

    private static void initDictionary(Type type) {
        switch (type) { //abhängig von wert von type wird bestimmte implementeierung ausgewählt
            case sorted:
                dictionary = new SortedArrayDictionary<>();
                break;
            case hash:
                dictionary = new HashDictionary<>(11);
                break;
            case binary:
                dictionary = new BinaryTreeDictionary<>();
                break;
        }
    }
    //misst durchschnittl. Suchzeit für die spezifischen Schleifenanzahl
    private static void measureSearchTime(int n, int loops, String message, Type type, boolean success) {
        initDictionary(type);
        //erstellt ein dictionary mit spezifischen Wortanzahl
        createDictionary(n, "dtengl.txt");
        //erstellt eine Wortliste die für die suche benutzt wird
        LinkedList<String> list = wordsInList(n, success);
        double sumElapsedTime = 0;

        for (int i = 0; i < loops; i++) {
            long start = System.nanoTime();
            //führt die suche für jedes wort in der liste aus
            for (String word : list) {
                dictionary.search(word);
            }
            long end = System.nanoTime();
            //berechnet die verstrichene zeit für die aktuelle schleife
            double elapsedTime = (double) (end - start) / 1.0e06; //von nano in millisek
            sumElapsedTime += elapsedTime;
        }

        double average = sumElapsedTime / loops;
        System.out.println(message + " " + average);
    }

    //erstellt eine Wortliste aus dem dictionary
    private static LinkedList<String> wordsInList(int n, boolean success) {
        BufferedReader rd;
        LinkedList<String> list = new LinkedList<>();
        try {
            //liest dictionary datei
            rd = new BufferedReader(new FileReader("C:\\Users\\ellen\\OneDrive\\Documents\\Studium\\Semester3\\AuD\\uebung\\Aufgabe1\\src\\aufgabe1\\dtengl.txt"));
            String line = rd.readLine();
            int i = 0;
            while (line != null && i < n) {
                //Zeile aufteilen in deutsch und englisch
                String[] words = line.split(" ");
                String german = words[0];
                String english = words[1];

                if (success) {
                    list.add(german);
                } else {
                    list.add(english);
                }

                line = rd.readLine();
                i++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    //Misst die zeit die benötigt wird um ein dictionary mit der spezifischen wortanzahl zu erstellen
    private static void measureCreationTime(int n, int loops, String msg, Type type) {
        double sumElapsedTime = 0;

        for (int i = 0; i < loops; i++) {
            BufferedReader rd;
            try {
                //initialisiere dictionary basierend auf dem typ
                initDictionary(type);
                rd = new BufferedReader(new FileReader("C:\\Users\\ellen\\OneDrive\\Documents\\Studium\\Semester3\\AuD\\uebung\\Aufgabe1\\src\\aufgabe1\\dtengl.txt"));
                String line = rd.readLine();
                int j = 0;

                long start = System.nanoTime();
                //wort ins dictionary einfügen
                while (line != null && j < n) {
                    String[] words = line.split(" ");
                    String german = words[0];
                    String english = words[1];
                    dictionary.insert(german, english);
                    line = rd.readLine();
                    j++;
                }

                long end = System.nanoTime();
                double elapsedTime = (double) (end-start) / 1.0e06; //von nano in millisek
                sumElapsedTime += elapsedTime;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //durchschnittl. erstellungszeit berechnen
        double average = sumElapsedTime / loops;
        System.out.println(msg + "" + average);
    }

    private enum Type {
        sorted, hash, binary;
    }
}
