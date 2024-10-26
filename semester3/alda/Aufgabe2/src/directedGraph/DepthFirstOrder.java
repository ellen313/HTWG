// O. Bittel;
// 22.02.2017
package directedGraph;

import com.sun.source.doctree.VersionTree;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;
import java.util.*;

/**
 * Klasse für Tiefensuche.
 *
 * @author Oliver Bittel
 * @since 22.02.2017
 * @param <V> Knotentyp.
 */
public class DepthFirstOrder<V> {

    private final List<V> preOrder = new LinkedList<>();
    private final List<V> postOrder = new LinkedList<>();
    private final DirectedGraph<V> myGraph;
    private int numberOfDFTrees = 0;

    /**
     * Führt eine Tiefensuche für g durch.
     *
     * @param g gerichteter Graph.
     */
    public DepthFirstOrder(DirectedGraph<V> g) {
        myGraph = g;
        visitDF(myGraph);
    }

    // startet von Knoten v eine Tiefensuche im Graph g
    private void visitDF(DirectedGraph<V> g) {
        Set<V> besucht =  new TreeSet<>(); // um knoten in aufsteigender reihenfolge zu halten
        for (var vex : g.getVertexSet()) { // knoten in g durchlaufen
            if (!besucht.contains(vex)) { // prüfen ob knoten bereits besucht wurde wenn nicht -> tiefensuche starten
                visitDF(vex, g, besucht); // methode aufrufen um suche zu starten
                numberOfDFTrees++; // anzahl der bäume inkrementieren
            }

        }
    }

    // besucht Knoten v im Graphen g und ist rekursiv
    private void visitDF(V v, DirectedGraph<V> g, Set<V> besucht) {
        besucht.add(v); // Menge aller bereits besuchten Knoten -> gegen Endlosschleifen, da nicht mehrmals gleichen knoten besuchen
        preOrder.add(v); // knoten in preorder liste hinzufügen um reihenfolge im durchlauf beizubehalten bzw zu speichern
        for (V w : g.getSuccessorVertexSet(v)) {// schleife über alle nachfolgerknoten von v
            if (!besucht.contains(w)) { // w noch nicht besucht
                visitDF(w, g, besucht); //durchlauf für fortsetzen
            }
        }
        postOrder.add(v); // knoten speichern wenn durchlauf zuende
    }

    /**
     * Liefert eine nicht modifizierbare Liste (unmodifiable view) mit einer
     * Pre-Order-Reihenfolge zurück.
     *
     * @return Pre-Order-Reihenfolge der Tiefensuche.
     *
     * ergibt sich indem jeder Knoten sobald er besucht wird in eine Liste angehängt wird
     */
    public List<V> preOrder() {
        return Collections.unmodifiableList(preOrder);
    }

    /**
     * Liefert eine nicht modifizierbare Liste (unmodifiable view) mit einer
     * Post-Order-Reihenfolge zurück.
     *
     * @return Post-Order-Reihenfolge der Tiefensuche.
     *
     * knoten wird erst dann angehängt sobald rekusrive besuchsmethode für knoten verlassen wird
     */
    public List<V> postOrder() {
        return Collections.unmodifiableList(postOrder);
    }

    /**
     *
     * @return Anzahl der Bäume des Tiefensuchwalds.
     */
    public int numberOfDFTrees() {
        return numberOfDFTrees;
    }

    public static void main(String[] args) {
        DirectedGraph<Integer> g = new AdjacencyListDirectedGraph<>();
        g.addEdge(1, 2);
        g.addEdge(2, 5);
        g.addEdge(5, 1);
        g.addEdge(2, 6);
        g.addEdge(3, 7);
        g.addEdge(4, 3);
        g.addEdge(4, 6);
        //g.addEdge(7,3);
        g.addEdge(7, 4);

        DepthFirstOrder<Integer> dfs = new DepthFirstOrder<>(g);
        System.out.println(dfs.numberOfDFTrees());	// 2
        System.out.println(dfs.preOrder());		// [1, 2, 5, 6, 3, 7, 4]
        System.out.println(dfs.postOrder());		// [5, 6, 2, 1, 4, 7, 3]

    }
}
