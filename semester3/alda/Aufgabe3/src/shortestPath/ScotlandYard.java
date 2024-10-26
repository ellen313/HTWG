package shortestPath;

import directedGraph.*;
import java.io.FileNotFoundException;
import sim.SYSimulation;
import java.awt.Color;
import java.io.IOException;

import java.io.File;
import java.util.HashMap;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


/**
 * Kürzeste Wege im Scotland-Yard Spielplan mit A* und Dijkstra.
 * @author Oliver Bittel
 * @since 26.09.2022
 */
public class ScotlandYard {

	static int[][] blocked = {{89, 128}, {105, 108}, {88, 117}, {86, 116}, {104, 116}};

	/**
	 * Fabrikmethode zur Erzeugung eines gerichteten Graphens für den Scotland-Yard-Spielplan.
	 * <p>
	 * Liest die Verbindungsdaten von der Datei ScotlandYard_Kanten.txt.
	 * Für die Verbindungen werden folgende Gewichte angenommen:
	 * U-Bahn = 5, Taxi = 2 und Bus = 3.
	 * Falls Knotenverbindungen unterschiedliche Beförderungsmittel gestatten,
	 * wird das billigste Beförderungsmittel gewählt.
	 * Bei einer Vebindung von u nach v wird in den gerichteten Graph sowohl
	 * eine Kante von u nach v als auch von v nach u eingetragen.
	 * @return Gerichteter und Gewichteter Graph für Scotland-Yard.
	 * @throws FileNotFoundException
	 */
	public static DirectedGraph<Integer> getGraph() throws FileNotFoundException {

		DirectedGraph<Integer> sy_graph = new AdjacencyListDirectedGraph<>();
		Scanner in = new Scanner(new File("C:/Users/ellen/OneDrive/Documents/Studium/Semester3/AuD/uebung/Aufgabe3/data/ScotlandYard_Kanten.txt"));

		//Datei lesen und Kanten in den Graph einfügen
		while (in.hasNextLine()) {
			String[] line = in.nextLine().split("\\s"); //jede zeile als string einlesen
			if (line.length != 3) {
				break; // Ignoriere Zeilen mit falschem Format
			}

			int u = Integer.parseInt(line[0]);
			int vx = Integer.parseInt(line[1]);

			int dist = 0; //distanz der kante speichern
			int oldDist = Integer.MAX_VALUE;

			sy_graph.addVertex(u);
			sy_graph.addVertex(vx);

			switch(line[2]) {
				case "Taxi":
					dist = 2;
					break;
				case "Bus":
					dist = 3;
					break;
				case "UBahn":
					dist = 5;
					break;
			}

			//Teilaufgabe 4, östliche Themse Brücke blockieren
			/*if (Arrays.stream(blocked).anyMatch(x -> (x[0] == u && x[1] == vx) || (x[0] == vx && x[1] == u))) {
                  dist = 100;
			} else*/

			if (sy_graph.containsEdge(u, vx)) { //wenn kante bereits existiert
				oldDist = (int) sy_graph.getWeight(u, vx); //gewicht abrufen und speichern
			}
			if (oldDist < dist) { //falls vorhandene Gewicht kleiner als neue, dieses verwenden
				dist = oldDist;
			}
			sy_graph.addEdge(u, vx, dist);
			sy_graph.addEdge(vx, u, dist); //in beide richtungen einfügen ->ungerichteter Graph
		}

		// Test, ob alle Kanten eingelesen wurden:
		System.out.println("Number of Vertices:       " + sy_graph.getNumberOfVertexes());	// 199
		System.out.println("Number of directed Edges: " + sy_graph.getNumberOfEdges());	  	// 862
		double wSum = 0.0;
		for (Integer v : sy_graph.getVertexSet())
			for (Integer w : sy_graph.getSuccessorVertexSet(v))
				wSum += sy_graph.getWeight(v,w);
		System.out.println("Sum of all Weights:       " + wSum);	// 1972.0

		return sy_graph;
	}


	/**
	 * Fabrikmethode zur Erzeugung einer Heuristik für die Schätzung
	 * der Distanz zweier Knoten im Scotland-Yard-Spielplan.
	 * Die Heuristik wird für A* benötigt.
	 * <p>
	 * Liest die (x,y)-Koordinaten (Pixelkoordinaten) aller Knoten von der Datei
	 * ScotlandYard_Knoten.txt in eine Map ein.
	 * Die zurückgelieferte Heuristik-Funktion estimatedCost
	 * berechnet einen skalierten Euklidischen Abstand.
	 * @return Heuristik für Scotland-Yard.
	 * @throws FileNotFoundException
	 */
	public static Heuristic<Integer> getHeuristic() throws FileNotFoundException {
		return new ScotlandYardHeuristic();
	}

	/**
	 * Scotland-Yard Anwendung.
	 * @param args wird nicht verewendet.
	 * @throws FileNotFoundException
	 */
	public static void main(String[] args) throws FileNotFoundException {

		DirectedGraph<Integer> syGraph = getGraph();

		Heuristic<Integer> syHeuristic = null; // Dijkstra
		//Heuristic<Integer> syHeuristic = getHeuristic(); // A*

		ShortestPath<Integer> sySp = new ShortestPath<Integer>(syGraph,syHeuristic);

		sySp.searchShortestPath(65,157);
		System.out.println("Distance = " + sySp.getDistance()); // 9.0

		sySp.searchShortestPath(1,175);
		System.out.println("Distance = " + sySp.getDistance()); // 25.0

		sySp.searchShortestPath(1,173);
		System.out.println("Distance = " + sySp.getDistance()); // 22.0


		SYSimulation sim;
		try {
			sim = new SYSimulation();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		sySp.setSimulator(sim);
		sim.startSequence("Shortest path from 1 to 173");

		//sySp.searchShortestPath(65,157); // 9.0
		sySp.searchShortestPath(1,175); //25.0

		//sySp.searchShortestPath(1,173); //22.0
		// bei Heuristik-Faktor von 1/10 wird nicht der optimale Pfad produziert.
		// bei 1/30 funktioniert es.

		System.out.println("Distance = " + sySp.getDistance());
		List<Integer> sp = sySp.getShortestPath();

		int a = -1;
		for (int b : sp) {
			if (a != -1)
				sim.drive(a, b, Color.RED.darker());
			sim.visitStation(b);
			a = b;
		}
        sim.stopSequence();
    }

}

class ScotlandYardHeuristic implements Heuristic<Integer> {
	private Map<Integer,Point> coord = new HashMap<>(); // Ordnet jedem Knoten seine Koordinaten zu

	private static class Point {
		int x;
		int y;
		Point(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}

	public ScotlandYardHeuristic() throws FileNotFoundException {
		Scanner sc = new Scanner(new File("C:/Users/ellen/OneDrive/Documents/Studium/Semester3/AuD/uebung/Aufgabe3/data/ScotlandYard_Knoten.txt"));
		while (sc.hasNextLine()) {
			String line = sc.nextLine();
			String[] s = line.split("\\s");
			Point p = new Point(Integer.parseInt(s[1]), Integer.parseInt(s[2]));
			coord.put(Integer.parseInt(s[0]), p);
		}
	}

	public double estimatedCost(Integer u, Integer v) {
		Point uP = coord.get(u);
		Point vP = coord.get(v);
		int x = (uP.x - vP.x);
		int y = (uP.y - vP.y);
        return Math.sqrt((x*x) + (y*y)) / 30;
	}
}

