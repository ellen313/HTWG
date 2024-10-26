// O. Bittel;
// 2.8.2023

package directedGraph;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.*;


/**
 * Klasse zur Analyse von Web-Sites.
 *
 * @author Oliver Bittel
 * @since 30.10.2023
 */
public class AnalyzeWebSite {
    public static void main(String[] args) throws IOException {
        // Graph aus Website erstellen und ausgeben:
        DirectedGraph<String> webSiteGraph = buildGraphFromWebSite("C:/Users/ellen/OneDrive/Documents/Studium/Semester3/AuD/uebung/Aufgabe2/src/data/WebSiteKlein");
        //DirectedGraph<String> webSiteGraph = buildGraphFromWebSite("C:/Users/ellen/OneDrive/Documents/Studium/Semester3/AuD/uebung/Aufgabe2/src/data/WebSiteGross");
        System.out.println("Anzahl Seiten: \t" + webSiteGraph.getNumberOfVertexes());
        System.out.println("Anzahl Links: \t" + webSiteGraph.getNumberOfEdges());
        //System.out.println(webSiteGraph);

        // Starke Zusammenhangskomponenten berechnen und ausgeben
        StrongComponents<String> sc = new StrongComponents<>(webSiteGraph);
        System.out.println(sc.numberOfComp());
        //System.out.println(sc);

        // Page Rank ermitteln und Top-100 ausgeben
        pageRank(webSiteGraph);
    }

    /**
     * Liest aus dem Verzeichnis dirName alle Web-Seiten und
     * baut aus den Links einen gerichteten Graphen.
     *
     * @param dirName Name eines Verzeichnis
     * @return gerichteter Graph mit Namen der Web-Seiten als Knoten und Links als gerichtete Kanten.
     */
    private static DirectedGraph buildGraphFromWebSite(String dirName) throws IOException {
        File webSite = new File(dirName);
        DirectedGraph<String> webSiteGraph = new AdjacencyListDirectedGraph();

        for (File f : webSite.listFiles()) {
            String from = f.getName();
            LineNumberReader in = new LineNumberReader(new FileReader(f));
            String line;
            while ((line = in.readLine()) != null) {
                if (line.contains("href")) {
                    String[] s_arr = line.split("\"");
                    String to = s_arr[1];
                    webSiteGraph.addEdge(from, to);
                }
            }
        }
        return webSiteGraph;
    }

    /**
     * pageRank ermittelt Gewichte (Ranks) von Web-Seiten
     * aufgrund ihrer Link-Struktur und gibt sie aus.
     *
     * @param g gerichteter Graph mit Web-Seiten als Knoten und Links als Kanten.
     */
    private static <V> void pageRank(DirectedGraph<V> g) {
        int nI = 10; // iterations anzahl
        double alpha = 0.5;

        // Definiere und initialisiere rankTable:
         Map<V, Double> rankTable = new HashMap<>(); // hash besser als tree hier: keine sortierung notwendig, schneller
         for (V v : g.getVertexSet()) { //über rang iterieren
             // rank für jede seite ist 1/n, Anfangswkeit einen bestimmten Knoten zu besuchen gleichmäßig auf alle Knoten verteilt
             rankTable.put(v, 1.0 / g.getNumberOfVertexes());
         }

        // Iteration:
        for (int i = 0; i < nI; i++) {
            Map<V, Double> newRankTable = new HashMap<>(); // um ränge zu speichern
            for (V v : g.getVertexSet()) { // über knoten in graph iterieren
                double rank = 0; // aktuellen rang des knotens speichern
                for (V w : g.getPredecessorVertexSet(v)) { //über nachfolger des knotens iterieren
                    //rang des knotens w durch größe seiner vorgänger teilen und zu rang des knotens v addieren
                    rank += rankTable.get(w) / g.getPredecessorVertexSet(w).size(); //rang eines knotens basierend auf rängen der nachfolger berechnen
                }
                // neuen rang in neuer tabelle speichern
                newRankTable.put(v, alpha * rank + (1 - alpha) / g.getNumberOfVertexes());
            }
            rankTable = newRankTable; //alte durch neue tabelle ersetzen um ränge für nächste iteration zu nuten
        }

        // Rank Table ausgeben (nur für data/WebSiteKlein):
        if (g.getNumberOfVertexes() <= 100) {
            for (V v : g.getVertexSet()) { //über rang iterieren
                System.out.println(v + ": " + rankTable.get(v));
            }
        }

        // Nach Ranks sortieren Top 100 ausgeben (nur für data/WebSiteGross):
        if (g.getNumberOfVertexes() > 100) {
            List<Map.Entry<V, Double>> list = new ArrayList<>(rankTable.entrySet()); // liste besteht aus einträgen der rankTable Map
            list.sort((o1, o2) -> o2.getValue().compareTo(o1.getValue())); // liste wird basierend auf den werten der einträge absteigend sortiert
            for (int i = 0; i < 100; i++) {
                System.out.println(list.get(i).getKey() + ": " + list.get(i).getValue()); // schlüssel und wert in liste ausgeben
            }
        }

        // Top-Seite mit ihren Vorgängern und Ranks ausgeben (nur für data/WebSiteGross):
        if (g.getNumberOfVertexes() > 100) {
            V top = Collections.max(rankTable.entrySet(), Map.Entry.comparingByValue()).getKey();
            System.out.println("Top-Seite: " + top);
            System.out.println("Vorgänger: " + g.getPredecessorVertexSet(top));
            System.out.println("Rank: " + rankTable.get(top));
        }
        
    }
}
