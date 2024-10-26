package aufgabe4;

import java.awt.*;
import java.util.*;
import java.util.List;

/**Klasse zur Verwaltung von Telefonknoten mit (x,y)-Koordinaten und zur Berechnung eines
 * minimal aufspannenden Baums mit dem Algorithmus von Kruskal. Kantengewichte sind durch
 * den Manhattan-Abstand definiert.*/
public class TelNet {
    int lbg;
    List<TelKnoten> telMap;
    List<TelVerbindung> verbindungen;
    int size;

    /**Legt ein neues Telefonnetz mit dem Leitungsbegrenzungswert lbg an.
     * @param lbg, Leistungsbegrenzungswert*/
    public TelNet(int lbg) {
        this.lbg = lbg;
        this.telMap = new ArrayList<>();
        this.verbindungen = new ArrayList<>();

    }
    /**Fügt einen neuen Telefonknoten mit Koordinate (x,y) dazu.
     @param x - x-Koordinate.
     @param y - y-Koordinate.
     @return true, falls die Koordinate neu ist, sonst false.*/
    public boolean addTelKnoten(int x, int y) {
        TelKnoten knoten = new TelKnoten(x,y);
        if(!telMap.contains(knoten)){
            telMap.add(new TelKnoten(x,y));
            size++;
            return true;
        } else return false;
    }
    /**Berechnet ein optimales Telefonnetz als minimal aufspannenden Baum mit dem Algorithmus von Kruskal.
     @return true, falls es einen minimal aufspannden Baum gibt, sonst false.
     */
    public boolean computeOptTelNet() {
        if (size() == 0) {
            return false;
        }
        verbindungen.clear();
        UnionFind forest = new UnionFind(size);
        PriorityQueue<TelVerbindung> edges = new PriorityQueue<>(Comparator.comparingInt(x -> x.cost));

        //PriorityQueue befüllen
        for (int i = 0; i < size(); i++) {
            for (int j = i + 1; j < size(); j++) {
                TelKnoten v = telMap.get(i);
                TelKnoten w = telMap.get(j);
                // dist((x1,y1),(x2,y2)) = |x1-x2| + |y1-y2|
                int costs = Math.abs(v.x - w.x) + Math.abs(v.y - w.y);
                //edges.add(new TelVerbindung(v, w, costs));
                if (costs <= lbg) {
                    edges.add(new TelVerbindung(v, w, costs));
                }
            }
        }

        int edgesAdded = 0;
        while(!edges.isEmpty() && edgesAdded < size - 1) {
            TelVerbindung verbindung = edges.poll();
            int t1 = forest.find(telMap.indexOf(verbindung.u));
            int t2 = forest.find(telMap.indexOf(verbindung.v));
            if (t1 != t2) {
                forest.union(t1, t2);
                verbindungen.add(verbindung);
                edgesAdded++;
            }
        }
        return verbindungen.size() == size - 1;
    }
    /**Liefert ein optimales Telefonnetz als Liste von Telefonverbindungen zurück.
     * @return Liste von Telefonverbindungen.
     * @throws IllegalStateException,  falls nicht zuvor computeOptTelNet() erfolgreich durchgeführt wurde.
     */
    public List<TelVerbindung> getOptTelNet(){
        return verbindungen;
    }
    /**Liefert die Gesamtkosten eines optimalen Telefonnetzes zurück.
     @return Gesamtkosten eines optimalen Telefonnetzes.
     @throws java.lang.IllegalStateException - falls nicht zuvor computeOptTelNet() erfolgreich durchgeführt wurde.*/
    public int getOptTelNetKosten() {
        int costs = 0;
        for (var v : verbindungen) {
            costs += v.cost;
        }
        return costs;
    }
    /**Zeichnet das gefundene optimale Telefonnetz mit der Größe xMax*yMax in ein Fenster.
     @param xMax - Maximale x-Größe.
     @param yMax - Maximale y-Größe.
     @throws java.lang.IllegalStateException - falls nicht zuvor computeOptTelNet() erfolgreich durchgeführt wurde.*/
    public void drawOptTelNet(int xMax, int yMax) {
        StdDraw.setCanvasSize(300, 300);
        if (xMax < 8) xMax = 8;
        if (yMax < 8) yMax = 8;

        StdDraw.setXscale(0, xMax);
        StdDraw.setYscale(0, yMax);


        // Erste Spalte grau füllen
        StdDraw.setPenColor(Color.LIGHT_GRAY);
        for (int i = 0; i < yMax; i++) {
            StdDraw.filledSquare(0.5, i + 0.5, 0.5);
            StdDraw.setPenColor(Color.BLACK);
            StdDraw.text(0.5, i + 0.5, String.valueOf(i));
            StdDraw.setPenColor(Color.LIGHT_GRAY);
        }
        // Unterste Zeile grau füllen
        for (int i = 0; i < xMax; i++) {
            StdDraw.filledSquare(i + 0.5, 0.5, 0.5);
            if (i >= 1 && i <= 7) { // Zahlen in der untersten Zeile einfügen
                StdDraw.setPenColor(Color.BLACK);
                StdDraw.text(i + 0.5, 0.5, String.valueOf(i));
                StdDraw.setPenColor(Color.LIGHT_GRAY);
            }
        }
        //Raster
        StdDraw.setPenColor(StdDraw.BLACK);
        for(int i = 0; i <= xMax; i++) {
            StdDraw.line(i, 0, i ,yMax);
        }
        for(int i = 0; i <= yMax; i++) {
            StdDraw.line(0, i, xMax, i );
        }
        //Knoten einzeichnen
        StdDraw.setPenColor(StdDraw.BLUE);
        for (TelKnoten knoten : telMap) {
            double x = knoten.x;
            double y = knoten.y;
            StdDraw.filledSquare(x + 0.5, y + 0.5, 0.48);
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.filledCircle(x + 0.5, y + 0.5, 0.2);
            StdDraw.setPenColor(StdDraw.BLUE);
        }

        //Verbindungen einzeichnen
        StdDraw.setPenColor(StdDraw.RED);
        for (TelVerbindung verbindung: verbindungen) {
            double x1 = verbindung.u.x + 0.5;
            double y1 = verbindung.u.y + 0.5;
            double x2 = verbindung.v.x + 0.5;
            double y2 = verbindung.v.y + 0.5;
            StdDraw.line(x1, y1, x2, y2);

            // Draw the cost value on the line
            double midX = (x1 + x2) / 2;
            double midY = (y1 + y2) / 2;
            StdDraw.text(midX, midY, String.valueOf(verbindung.cost));

        }
        StdDraw.show();

        // Vor dem Zeichnen der Verbindungen in drawOptTelNet(xMax, yMax)
        System.out.println("Anzahl der berechneten Verbindungen: " + verbindungen.size());
        for (TelVerbindung verbindung : verbindungen) {
            System.out.println("Verbindung von (" + verbindung.u.x + "," + verbindung.u.y + ") nach (" + verbindung.v.x + "," + verbindung.v.y + ") Kosten: " + verbindung.cost);
        }

    }

    public void drawOptTelNet2(int xMax, int yMax) {
        StdDraw.setCanvasSize(500, 500);
        StdDraw.setXscale(0, xMax);
        StdDraw.setYscale(0, yMax);
        StdDraw.clear();

        //double x = (double) xMax / telMap.stream().mapToInt(o -> o.x).max().orElse(0);
        //double y = (double) yMax / telMap.stream().mapToInt(o -> o.y).max().orElse(0);

        // Knoten einzeichnen
        StdDraw.setPenColor(StdDraw.BLUE);
        for (TelKnoten knoten : telMap) {
            double x = knoten.x;
            double y = knoten.y;
            StdDraw.filledRectangle(x, y, 2,2);
        }
        StdDraw.show();
        // Verbindungen animiert einzeichnen
        StdDraw.setPenColor(StdDraw.BLACK);
        for (TelVerbindung verbindung : verbindungen) {
            double x1 = verbindung.u.x + 0.5;
            double y1 = verbindung.u.y + 0.5;
            double x2 = verbindung.v.x + 0.5;
            double y2 = verbindung.v.y + 0.5;
            StdDraw.line(x1, y1, x2, y1);
            StdDraw.line(x2, y1, x2, y2);
            StdDraw.show();
        }
    }

    /**Fügt n zufällige Telefonknoten zum Netz dazu mit x-Koordinate aus [0,xMax] und y-Koordinate aus [0,yMax].
     @param n - Anzahl Telefonknoten
     @param xMax - Intervallgrenz für x-Koordinate.
     @param yMax - Intervallgrenz für y-Koordinate.*/
    public void generateRandomTelNet(int n, int xMax, int yMax){
        int i = 0;
        while (i < n) {
            int px = (int) (Math.random() * xMax);
            int py = (int) (Math.random() * yMax);
            if (this.addTelKnoten(px, py)) {
                i++;
            }
        }
    }

    /**Liefert die Anzahl der Knoten des Telefonnetzes zurück.
     Returns:
     Anzahl der Knoten des Telefonnetzes.*/
    public int size(){
        return size;
    }

    @Override
    public String toString(){
        return null;
    }

    private static void test1(){
        TelNet telNet = new TelNet(7);
        telNet.addTelKnoten(1,1);
        telNet.addTelKnoten(3,1);
        telNet.addTelKnoten(4,2);
        telNet.addTelKnoten(3,4);
        telNet.addTelKnoten(2,6);
        telNet.addTelKnoten(4,7);
        telNet.addTelKnoten(7,6);

        boolean success = telNet.computeOptTelNet();
        if (!success) {
            System.out.println("Berechnung des optimalen Telefonnetzes fehlgeschlagen.");
        } else {
            System.out.println("Gesamtkosten: " + telNet.getOptTelNetKosten());

            // Debugging-Ausgabe für alle Verbindungen
            List<TelVerbindung> verbindungen = telNet.getOptTelNet();
            for (TelVerbindung v : verbindungen) {
                System.out.println("Verbindung von (" + v.u.x + "," + v.u.y + ") nach (" + v.v.x + "," + v.v.y + ")");
            }

            // Zeichne das optimale Telefonnetz
            telNet.drawOptTelNet(8, 8);
        }
    }
    private static void test2() {
        int lbg = 1000;
        TelNet telnet2 = new TelNet(lbg);

        telnet2.generateRandomTelNet(lbg, 700, 700);
        telnet2.computeOptTelNet();
        telnet2.drawOptTelNet2(700, 700);
    }

    public static void main(String[] args) {
        //test1();
        test2();
    }
}