// O. Bittel;
// 26.09.2022

package shortestPath;

import directedGraph.*;
import sim.SYSimulation;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

// ...

/**
 * Kürzeste Wege in Graphen mit A*- und Dijkstra-Verfahren.
 * @author Oliver Bittel
 * @since 27.01.2015
 * @param <V> Knotentyp.
 */
public class ShortestPath<V> {
	
	SYSimulation sim = null;
	
	Map<V,Double> dist; 		// Distanz für jeden Knoten
	Map<V,V> pred; 				// Vorgänger für jeden Knoten
	IndexMinPQ<V,Double> cand; 	// Kandidaten als PriorityQueue PQ
	DirectedGraph<V> graph;
	Heuristic<V> h;
	V start;
	V goal;
	double infinite = Double.MAX_VALUE;

	/**
	 * Konstruiert ein Objekt, das im Graph g k&uuml;rzeste Wege 
	 * nach dem A*-Verfahren berechnen kann.
	 * Die Heuristik h schätzt die Kosten zwischen zwei Knoten ab.
	 * Wird h = null gewählt, dann ist das Verfahren identisch 
	 * mit dem Dijkstra-Verfahren.
	 * @param g Gerichteter Graph
	 * @param h Heuristik. Falls h == null, werden kürzeste Wege nach
	 * dem Dijkstra-Verfahren gesucht.
	 */
	public ShortestPath(DirectedGraph<V> g, Heuristic<V> h) {
		dist = new HashMap<>();
		pred = new HashMap<>();
		cand = new IndexMinPQ<>();
		// ...
		this.h = h;
		graph = g;
	}

	/**
	 * Diese Methode sollte nur verwendet werden, 
	 * wenn kürzeste Wege in Scotland-Yard-Plan gesucht werden.
	 * Es ist dann ein Objekt für die Scotland-Yard-Simulation zu übergeben.
	 * <p>
	 * Ein typische Aufruf für ein SYSimulation-Objekt sim sieht wie folgt aus:
	 * <p><blockquote><pre>
	 *    if (sim != null)
	 *       sim.visitStation((Integer) v, Color.blue);
	 * </pre></blockquote>
	 * @param sim SYSimulation-Objekt.
	 */
	public void setSimulator(SYSimulation sim) {
		this.sim = sim;
	}

	/**
	 * Sucht den kürzesten Weg von Starknoten s zum Zielknoten g.
	 * <p>
	 * Falls die Simulation mit setSimulator(sim) aktiviert wurde, wird der Knoten,
	 * der als nächstes aus der Kandidatenliste besucht wird, animiert.
	 * @param s Startknoten
	 * @param g Zielknoten
	 */
	public void searchShortestPath(V s, V g) {
		// ...
		shortestPath(s, g, graph, dist, pred, cand);
	}

	private boolean shortestPath(V s, V g, DirectedGraph<V> graph, Map<V, Double> dist,
								 Map<V, V> pred, IndexMinPQ<V, Double> cand) {

		dist.clear();
		cand.clear();
		pred.clear();
		start = s;
		goal = g;

		for (V v : graph.getVertexSet()) {
			dist.put(v, infinite); //distanz unendlich
			pred.put(v, null); //undefinierter Vorgänger
		}

		dist.put(s, 0.0); // Startknoten auf Distanz 0 setzen

		//startknoten zur Kandidatenliste hinzufügen
		if (h == null) { //Dijkstra-Verfahren
			cand.add(s, 0.0);
		} else { //A*-Verfahren
			cand.add(s, h.estimatedCost(s, g));
		}
		while (!cand.isEmpty()) { //solange liste nicht leer
			V v = cand.removeMin(); //Knoten mit kleinstem Wert entfernen
			printCurrentVertex(v);

			if (v.equals(g)) {
				return true; //Zielknoten erreicht
			}

			for (V w : graph.getSuccessorVertexSet(v)) { //über alle Vorgängerknoten iterieren
				if (h == null) { //Dijkstra-Verfahren
					if (dist.get(w) == infinite) { //w wurde noch nicht besucht
						pred.put(w, v); //vorgängerknoten auf v setzen
						dist.put(w, dist.get(v) + graph.getWeight(v, w)); //distanz zu w aktualisieren
						cand.add(w, dist.get(w)); //w mit neuer distanz zu liste hinzufügen
					} else if (dist.get(v) + graph.getWeight(v, w) < dist.get(w)) { //kürzerer Weg wurde gefunden
						pred.put(w, v); //vorgängerknoten setzen
						dist.put(w, dist.get(v) + graph.getWeight(v, w)); //distanz zu w auf kürzere aktualisiert
						cand.change(w, dist.get(w)); //wert von w in liste mit neuer distanz aktualisieren
					}
				} else { //A*-Verfahren
					if (dist.get(w) == infinite) { //wenn knoten noch nicht besucht
						pred.put(w, v);
						dist.put(w, dist.get(v) + graph.getWeight(v, w)); //knoten um die geschätzten Gesamtkosten berechnen
						cand.add(w, dist.get(w) + h.estimatedCost(w, g)); //und zur liste hinzufügen
					} else if (dist.get(v) + graph.getWeight(v, w) < dist.get(w)) { //kürzerer Weg
						pred.put(w, v);
						dist.put(w, dist.get(v) + graph.getWeight(v, w));
						cand.change(w, dist.get(w) + h.estimatedCost(w, g));
					}
				}
			}
		}
		return false;
	}

	private void printCurrentVertex(V v) {
		System.out.println("Besuchter Knoten " + v + " mit d: " + dist.get(v));
		if (sim != null) {
			sim.visitStation((Integer) v, Color.blue);
		}
	}



	/**
	 * Liefert einen kürzesten Weg von Startknoten s nach Zielknoten g.
	 * Setzt eine erfolgreiche Suche von searchShortestPath(s,g) voraus.
	 * @throws IllegalArgumentException falls kein kürzester Weg berechnet wurde.
	 * @return kürzester Weg als Liste von Knoten.
	 */
	public List<V> getShortestPath() {
		LinkedList<V> shortestPath = new LinkedList<>(); //Pfad speichern
			V z = pred.get(goal); //variable z auf vorgängerknoten des zielknotens setzen
			shortestPath.add(goal); //liste hinzufügen
			while (z != start) { //bis startknoten erreicht ist
				shortestPath.add(z); //jedes mal in liste hinzufügen
				z = pred.get(z); //z auf vorgängerkoten setzen
			}
			shortestPath.add(start);
			Collections.reverse(shortestPath); //umkehren um Pfad in richtiger Reihenfolge zu erhalten
			return shortestPath;
	}

	/**
	 * Liefert die Länge eines kürzesten Weges von Startknoten s nach Zielknoten g zurück.
	 * Setzt eine erfolgreiche Suche von searchShortestPath(s,g) voraus.
	 * @throws IllegalArgumentException falls kein kürzester Weg berechnet wurde.
	 * @return Länge eines kürzesten Weges.
	 */
	public double getDistance() {
			return dist.get(goal);
	}

}
