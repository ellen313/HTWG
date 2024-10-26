// O. Bittel;
// 22.02.2017

package directedGraph;

import java.util.*;

/**
 * Klasse zur Erstellung einer topologischen Sortierung.
 * @author Oliver Bittel
 * @since 22.02.2017
 * @param <V> Knotentyp.
 */
public class TopologicalSort<V> {
    private final List<V> ts = new LinkedList<>(); // topologisch sortierte Folge

	/**
	 * Führt eine topologische Sortierung für g durch.
	 * @param g gerichteter Graph.
	 */
	public TopologicalSort(DirectedGraph<V> g) {
		List<V> sl = new LinkedList<>(); //sortierte liste
		Map<V, Integer> inDegree = new TreeMap<>(); //eingangsgrade für knoten speichern
		Queue<V> q = new LinkedList<>(); //besuchte knoten speichern

		for (V v : g.getVertexSet()) {
			inDegree.put(v, g.getInDegree(v)); //eingangsgrad abrufen und in map speichern
			if (inDegree.get(v) == 0) //wenn grad 0...
				q.add(v); //... in q hinzufügen, da keine vorgänger und somit startknoten
		}
		// topologische Sortierung
		while (!q.isEmpty()) {
			V v = q.remove(); //knoten ohne vorgänger aus der queue entfernen
			sl.add(v); // knoten zur sortierten liste hinzufügen
			for (V w : g.getSuccessorVertexSet(v)) {
				inDegree.put(w, inDegree.get(w) - 1); // grad von w dekrementieren
				if (inDegree.get(w) == 0)
					q.add(w); // füge w zur queue hinzu wenn alle vorgänger besucht wurden
			}
		}
		// überprüfen ob der graph zyklisch ist
		if (sl.size() == g.getNumberOfVertexes()) {
			ts.addAll(sl); //rückgabe der topologisch sortierten liste
		}
	}

	/**
	 * Liefert eine nicht modifizierbare Liste (unmodifiable view) zurück,
	 * die topologisch sortiert ist.
	 * @return topologisch sortierte Liste
	 */
	public List<V> topologicalSortedList() {
        return Collections.unmodifiableList(ts);
    }
    

	public static void main(String[] args) {
		DirectedGraph<Integer> g = new AdjacencyListDirectedGraph<>();
		g.addEdge(1, 2);
		g.addEdge(2, 3);
		g.addEdge(3, 4);
		g.addEdge(3, 5);
		g.addEdge(4, 6);
		g.addEdge(5, 6);
		g.addEdge(6, 7);
		System.out.println(g);

		TopologicalSort<Integer> ts = new TopologicalSort<>(g);
		
		if (ts.topologicalSortedList() != null) {
			System.out.println(ts.topologicalSortedList()); // [1, 2, 3, 4, 5, 6, 7]
		}
	}
}
