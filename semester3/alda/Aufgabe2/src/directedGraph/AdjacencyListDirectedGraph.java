// O. Bittel;
// 19.03.2018

package directedGraph;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;
import java.util.Set;

/**
 * Implementierung von DirectedGraph mit einer doppelten TreeMap 
 * für die Nachfolgerknoten und einer einer doppelten TreeMap 
 * für die Vorgängerknoten. 
 * <p>
 * Beachte: V muss vom Typ Comparable&lt;V&gt; sein.
 * <p>
 * Entspricht einer Adjazenzlisten-Implementierung
 * mit schnellem Zugriff auf die Knoten.
 * @author Oliver Bittel
 * @since 19.03.2018
 * @param <V> Knotentyp.
 */
public class AdjacencyListDirectedGraph<V> implements DirectedGraph<V> {
    // doppelte Map für die Nachfolgerknoten:
    private final Map<V, Map<V, Double>> succ = new TreeMap<>(); 
    
    // doppelte Map für die Vorgängerknoten:
    private final Map<V, Map<V, Double>> pred = new TreeMap<>(); 

    private int numberEdge = 0;

	@Override
	public boolean addVertex(V v) { // Vertex = Knoten
		if (!containsVertex(v)) { //wenn knoten noc nicht enthalten
			succ.put(v, new TreeMap<>()); //knoten in map hinzufügen
			pred.put(v, new TreeMap<>());
			return true;
		}
		return false;
	}

    @Override
    public boolean addEdge(V v, V w, double weight) { // Kante (v,w)
		// Knoten hinzufügen wenn nicht schon vorhanden
		addVertex(v);
		addVertex(w);

		//prüfen ob Kante bereits existiert
		if (containsEdge(v, w)) {
			succ.get(v).put(w, weight); // Kante existiert, Gewicht überschreiben
			pred.get(w).put(v, weight);
			return false; //Kante war bereits vorhanden
		} else { // neue Kante hinzufügen
			succ.get(v).put(w, weight);
			pred.get(w).put(v, weight);
			numberEdge++; //kantenanzahl erhöhen
			return true; // Kante war nicht vorhanden und wurde hinzugefügt
		}
    }

    @Override
    public boolean addEdge(V v, V w) {
		// Knoten hinzufügen wenn nicht schon vorhanden
		addVertex(v);
		addVertex(w);

		//prüfen ob Kante bereits existiert
		if (containsEdge(v, w)) {
			succ.get(v).put(w, 1.0); // Kante existiert, Gewicht mit 1 überschreiben
			pred.get(w).put(v, 1.0);
			return false; //Kante war bereits vorhanden
		} else { // neue Kante hinzufügen
			succ.get(v).put(w, 1.0);
			pred.get(w).put(v, 1.0);
			numberEdge++;
			return true; // Kante war nicht vorhanden und wurde hinzugefügt
		}
	}

    @Override
    public boolean containsVertex(V v) {
        return succ.containsKey(v); //prüfen ob v als schlüssel in map vorliegt -> im graph vorhanden
	}

    @Override
    public boolean containsEdge(V v, V w) {
		if (!containsVertex(v) || !containsVertex(w)) { // prüfen ob beide knoten enthalten
			throw new IllegalArgumentException();
		}
		return succ.get(v).containsKey(w) && pred.get(w).containsKey(v); // prüfen ob kante von w nach v und v nach w
		// ob w Schlüssel in succ von v und gleichzeitig v ein Schlüssel in pred von w
    }

    @Override
    public double getWeight(V v, V w) { //reelle Zahl an Kante
        if (!containsEdge(v, w)) //prüfen ob knoten enthalten
			throw new IllegalArgumentException();
		return succ.get(v).get(w); // Gewicht aus succ des Knotens v unter Verwendung des Knotens w als Schlüssel abrufen
    }

	
    @Override
    public int getInDegree(V v) { //Eingangsgrad = Anzahl der Vorgänger
		if (!containsVertex(v)) //prüfen ob knoten enthalten
			throw new IllegalArgumentException();
		return pred.get(v).size(); //Anzahl der Vorgänger zurückgeben
    }

    @Override
    public int getOutDegree(V v) { //Ausgangsgrad = Anzahl der Nachfolger
		if (!containsVertex(v)) //prüfen ob knoten enthalten
			throw new IllegalArgumentException();
		return succ.get(v).size(); //Anzahl der Nachfolger zurückgeben
    }
	
	@Override
    public Set<V> getVertexSet() {
		return Collections.unmodifiableSet(succ.keySet()); // nicht modifizierbare Sicht
    }

    @Override
    public Set<V> getPredecessorVertexSet(V v) { //Vorgängerknoten
		if (!containsVertex(v))
			throw new IllegalArgumentException();
		return Collections.unmodifiableSet(pred.get(v).keySet()); //menge der vorgängerknoten abrufen
    }

    @Override
    public Set<V> getSuccessorVertexSet(V v) { //Nachfolgeknoten
		if (!containsVertex(v))
			throw new IllegalArgumentException();
		return Collections.unmodifiableSet(succ.get(v).keySet()); // menge der nachfolgerknoten abrufen
    }

    @Override
    public int getNumberOfVertexes() { //Knotenanzahl
		return getVertexSet().size();
    }

    @Override
    public int getNumberOfEdges() { //Kantenanzahl
		return numberEdge;
    }
	
	@Override
    public DirectedGraph<V> invert() {
		DirectedGraph<V> invertedGraph = new AdjacencyListDirectedGraph<>();

		//durch alle Knoten im aktuellen Graph iterieren
		for (V vertex : succ.keySet()) {
			//durch alle Nachfolger des Knotens iterieren
			Map<V, Double> successors = succ.get(vertex);
			for (V successor : successors.keySet()) {
				//eine Kante im invertierten Graphen in umgekehrter Richtung hinzufügen
				invertedGraph.addEdge(successor, vertex);
			}
		}
		return invertedGraph;
	}

	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (var v : getVertexSet()) {
			for (var t : getSuccessorVertexSet(v)) {
				sb.append(v).append("-> ").append(t).append("weight = ").append(getWeight(v, t)).append("\n");
			}
		}
		return sb.toString();
	}
	
	
	public static void main(String[] args) {
		DirectedGraph<Integer> g = new AdjacencyListDirectedGraph<>();
		g.addEdge(1,2);
		g.addEdge(2,5);
		g.addEdge(5,1);
		g.addEdge(2,6);
		g.addEdge(3,7);
		g.addEdge(4,3);
		g.addEdge(4,6);
		g.addEdge(7,4);
		
		
		System.out.println(g.getNumberOfVertexes());	// 7
		System.out.println(g.getNumberOfEdges());		// 8
		System.out.println(g.getVertexSet());	// 1, 2, ..., 7
		System.out.println(g);
			// 1 --> 2 weight = 1.0 
			// 2 --> 5 weight = 1.0
			// 2 --> 6 weight = 1.0
			// 3 --> 7 weight = 1.0
			// ...
		
		System.out.println("");
		System.out.println(g.getOutDegree(2));				// 2
		System.out.println(g.getSuccessorVertexSet(2));	// 5, 6
		System.out.println(g.getInDegree(6));				// 2
		System.out.println(g.getPredecessorVertexSet(6));	// 2, 4
		
		System.out.println("");
		System.out.println(g.containsEdge(1,2));	// true
		System.out.println(g.containsEdge(2,1));	// false
		System.out.println(g.getWeight(1,2));	// 1.0	
		g.addEdge(1, 2, 5.0);
		System.out.println(g.getWeight(1,2));	// 5.0	
		
		System.out.println("");
		System.out.println(g.invert());
			// 1 --> 5 weight = 1.0
			// 2 --> 1 weight = 5.0
			// 3 --> 4 weight = 1.0 
			// 4 --> 7 weight = 1.0
			// ...
			
		Set<Integer> s = g.getSuccessorVertexSet(2);
		System.out.println(s);
		//s.remove(5);	// Laufzeitfehler! Warum?
		// -> unmodifizierbare Menge, man kann daher kein Element aus dieser Menge entfernen, da es nicht erlaubt ist
	}
}
