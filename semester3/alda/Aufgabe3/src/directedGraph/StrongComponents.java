// O. Bittel;
// 22.02.2017

package directedGraph;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Klasse für Bestimmung aller strengen Komponenten.
 * Kosaraju-Sharir Algorithmus.
 * @author Oliver Bittel
 * @since 22.02.2017
 * @param <V> Knotentyp.
 */
public class StrongComponents<V> {
	// comp speichert jede Komponente die zughörigen Knoten. 
	private final Map<Integer,Set<V>> comp = new TreeMap<>();
	
	// Anzahl der Komponenten:
	private int numberOfComp = 0;
	
	/**
	 * Ermittelt alle strengen Komponenten mit
	 * dem Kosaraju-Sharir Algorithmus.
	 * @param g gerichteter Graph.65
	 * graph muss azyklisch sein, weil sonst der ursprüngliche graph zyklisch wäre
	 * zyklischer graph hat dann auch zyklische komponenten
	 * -> widerspruch zur definition einer starken zsmkomponenten (von jedem Knoten zu jedem anderen Knoten in der Komponente
	 * muss es einen Weg geben, ohne dass ein Zyklus durchlaufen wird)
	 */
	public StrongComponents(DirectedGraph<V> g) {
		// a)
		System.out.println("Start");
		DepthFirstOrder<V> p = new DepthFirstOrder<>(g); // graph g in tiefensuche durchlaufen
		System.out.println("DFS beendet");
		List<V> pi = new LinkedList<>(p.postOrder());
		Collections.reverse(pi); // post order reihenfolge pi ermitteln indem reihenfolge von p umgekehrt wird

		// b)
		DirectedGraph<V> gi = g.invert(); // invertierten graph erzeugen

		// c)
		//Menge besucht erstellt, um besuchten Knoten zu verfolgen. Die Verwendung eines Sets stellt sicher, dass jeder Knoten nur einmal besucht wird
		System.out.println("Start c)");
		Set<V> besucht = new HashSet<>();
		for (var v : pi) { // knoten in pi werden besucht
			if (!besucht.contains(v)) {
				comp.put(numberOfComp, new HashSet<>()); //neue leere Menge für aktuelle Zsmkomponente erstellen und in Map comp einfügen
				comp.get(numberOfComp).add(v); //zu komponente den knoten hinzufügen
				visitDF(v, gi, besucht); //visitDF wird rekursiv aufgerufen, um alle Knoten zu besuchen, die von v aus erreichbar sind
				numberOfComp++; //komponentenanzahl erhöhen
			}
		}
	}

	void visitDF(V v, DirectedGraph<V> g, Set<V> besucht) {
		besucht.add(v); //endlosschleife vermeiden
		for (var w : g.getSuccessorVertexSet(v)) { //über nachfolger von v in g iterieren
			if (!besucht.contains(w)) {
				comp.get(numberOfComp).add(w); //aktuellen Nachfolger der aktuellen Zusammenhangskomponente hinzufügen
				visitDF(w, g, besucht); // visitDF wird rekursiv aufgerufen, um Nachfolger und alle seine Nachfolger zu besuchen
				// ->  um alle Knoten in derselben Zusammenhangskomponente von w zu finden und markieren
			}
		}
	}
	
	/**
	 * 
	 * @return Anzahl der strengen Komponeneten.
	 */
	public int numberOfComp() {
		return numberOfComp;
	}

	@Override
	public String toString() {
			StringBuilder sb = new StringBuilder();
			for (var key : comp.entrySet()) {
				sb.append("Component ").append(key.getKey()).append(": ");
				for (var val : key.getValue()) {
					sb.append(val).append(", ");
				}
				sb.append(("\n"));
			}
			return sb.toString();
	}

	public static void main(String[] args) {
		DirectedGraph<Integer> g = new AdjacencyListDirectedGraph<>();
		g.addEdge(1,2);
		g.addEdge(1,3);
		g.addEdge(2,1);
		g.addEdge(2,3);
		g.addEdge(3,1);
		
		g.addEdge(1,4);
		g.addEdge(5,4);
		
		g.addEdge(5,7);
		g.addEdge(6,5);
		g.addEdge(7,6);
		
		g.addEdge(7,8);
		g.addEdge(8,2);
		
		StrongComponents<Integer> sc = new StrongComponents<>(g);
		
		System.out.println(sc.numberOfComp());  // 4
		
		System.out.println(sc);
			// Component 0: 5, 6, 7, 
        	// Component 1: 8, 
            // Component 2: 1, 2, 3, 
            // Component 3: 4, 
	}
}
