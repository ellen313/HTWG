import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Aufgabe2_Datenanalyse {

    public static void loesung() throws IOException {

        // a) (2P)
        // Werfen Sie einen Blick in die Statistik-Datei:
        //      data/12411-0017-KREISE_$F.csv bzw. data/12411-0017-KREISE_$F.xlsx
        // In der Datei sind alle Landkreise von Deutschland mit einer Altersverteilung aufgelistet.
        // Die gegebene statische Methode einlesen liest alle Landkreise in eine Liste ein.
        // Geben Sie die Liste (jeder Landkreis in eine Zeile) aus und bestimmen Sie die Anzahl der Landkreise.
        System.out.println("\nAufgabe 2a (2P):");
        // Ihr Code
        List<Landkreis> listKreise = einlesen("data/12411-0017-KREISE_$F.csv");

        for (Landkreis lk :  listKreise) {
            System.out.println(lk);
        }

        // b) (2P)
        // Sortieren Sie die Liste nach dem Namen und geben Sie die Liste (jeder Landkreis in eine Zeile) aus:
        System.out.println("\nAufgabe 2b (2P):");
        // Ihr Code
        listKreise.sort(Comparator.comparing(Landkreis::name));

        for (Landkreis lk :  listKreise) {
            System.out.println(lk);
        }

        // c) (2P)
        // Sortieren Sie die Liste absteigend nach der Einwohnerzahl und geben Sie die Liste (jeder Landkreis in eine Zeile) aus:
        System.out.println("\nAufgabe 2c (2P):");
        // Ihr Code
        listKreise.sort((k1,k2)->k2.anzahlEinwohner() - k1.anzahlEinwohner());

        for (Landkreis lk :  listKreise) {
            System.out.println(lk);
        }

        // d) (3P)
        // Erstellen Sie eine Map, die für jeden Landkreisnamen die Einwohnerzahl speichert.
        // Geben Sie für alle mit 'K' beginnenden Landkreise den Namen und die Einwohnerzahl aus.
        // Benutzen Sie dazu subMap!
        System.out.println("\nAufgabe 2d (3P):");
        // Ihr Code
        NavigableMap<String, Integer> mp = new TreeMap<>();

        for (Landkreis lk :  listKreise) {
            mp.put(lk.name(), lk.anzahlEinwohner());
            for (Map.Entry<String, Integer> eintrag : mp.subMap("K", "L").entrySet()) {
                System.out.println(eintrag.getKey() + ": " + eintrag.getValue());
            }
        }

        // e) (1P)
        // Geben Sie die Menge (Set) aller Landkreisnamen aus:
        System.out.println("\nAufgabe 2e (1P):");
        // Ihr Code
        Set<String> s = mp.keySet();
        System.out.println(s);
    }

    private static List<Landkreis> einlesen(String fn) throws IOException {
        List<Landkreis> kreiseBRD = new LinkedList<>();

        // lese ";"-separated file
        LineNumberReader in = new LineNumberReader(new FileReader(fn, StandardCharsets.UTF_8));
        String line;
        while ((line = in.readLine()) != null) {
            Scanner lineScanner = new Scanner(line).useDelimiter(";");
            // Zeilen, die keinen Landkreis beschreiben, werden ausgelassen:
            if (!lineScanner.hasNextInt())
                continue;
            int plz = lineScanner.nextInt();
            String name = lineScanner.next();
            // Landkreise ohne Einwohnerzahlen werden ausgelassen:
            if (!lineScanner.hasNextInt())
                continue;
            // Lese die Einwohnerzahl ewz in der letzten Spalte:
            int ewz = 0;
            while (lineScanner.hasNextInt())
                ewz = lineScanner.nextInt();
            kreiseBRD.add(new Landkreis(name,plz,ewz));
        }

        return kreiseBRD;
    }
}
