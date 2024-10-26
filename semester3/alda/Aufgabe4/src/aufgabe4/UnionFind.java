package aufgabe4;

public class UnionFind {
    int[] p;
    int size;
    int[] rank; //repräsentiert tiefe der bäume


    /***Legt eine neue Union-Find-Struktur mit der Partitionierung {{0}, {1}, ..., {n-1}} an.
     @param n - Größe der Grundmenge.*/
    public UnionFind(int n) {
        p = new int[n];
        rank = new int[n];
        for(int i = 0; i < n; i++) {
            //p[i] = -1;
            p[i] = i;
            rank[i] = 0;
        }
        size = n;

    }
    /***Liefert den Repräsentanten der Menge zurück, zu der e gehört.
     @param e - Element
     @return Repräsentant der Menge, zu der e gehört.
     @throws java.lang.IllegalArgumentException - falls e nicht zur Grundmenge gehört.*/
    public int find(int e) {
        /*while(p[e] >= 0) {
            e = p[e];
        }
        return e;*/
        if (p[e] != e) {
            p[e] = find(p[e]);
        }
        return p[e];

    }

    /***Vereinigt die beiden Menge s1 und s2. s1 und s2 müssen Repräsentanten der jeweiligen Menge sein. Die Vereinigung
     * wird nur durchgeführt, falls s1 und s2 unterschiedlich sind.
     * Es wird union-by-height durchgeführt
     @param s1 - Element, das eine Menge repräsentiert.
     @param s2 - Element, das eine Menge repräsentiert.
     @throws java.lang.IllegalArgumentException - falls s1 oder s2 nicht zur Grundmenge gehören.*/
    public void union(int s1, int s2) {
        /*if(p[s1] >= 0 || p[s2] >= 0) return;
        if(s1 == s2) return;
        if(-p[s1] < -p[s2]){ //Höhe von s1 < Höhe von s2
            p[s1]  = s2;
        } else {
            if(-p[s1] == -p[s2]) {
                p[s1]--; //Höhe von s1 erhöht sich um 1
            } else {
                p[s2] = s1;
            }
        }
        size--;*/
        int root1 = find(s1); //Wurzeln suchen
        int root2 = find(s2);
        if (root1 == root2) { //sind bereits in derslben Menge
            return;
        }
        if (rank[root1] < rank[root2]) {
            p[root1] = root2; //root1 an root2 anhängen
        } else if (rank[root1] > rank[root2]) {
            p[root2] = root1; //root2 an root1 anhängen
        } else {
            p[root2] = root1; //root2 an root1
            rank[root1]++;
        }
        size--;
    }
    /***Liefert die Anzahl der Mengen in der Partitionierung zurück.*/
    public int size(){
        return size;
    }


    public static void main(String[] args) {
        UnionFind u = new UnionFind(10);
        System.out.println("Anzahl Partitionierungen nach Initialisierung: " + u.size()); // Erwartet: 10

        // find-Tests
        for (int i = 0; i < 10; i++) {
            System.out.println("Repräsentant von " + i + ": " + u.find(i)); // Erwartet: i
        }
        // union-Tests
        u.union(2, 5);
        System.out.println("Anzahl Partitionierungen nach Union(2, 5): " + u.size()); // Erwartet: 9
        System.out.println("Repräsentant von 2 nach Union(2, 5): " + u.find(2)); // Erwartet: 2 oder 5
        System.out.println("Repräsentant von 5 nach Union(2, 5): " + u.find(5)); // Erwartet: 2 oder 5

        u.union(1, 2);
        System.out.println("Anzahl Partitionierungen nach Union(1, 2): " + u.size()); // Erwartet: 8
        System.out.println("Repräsentant von 1 nach Union(1, 2): " + u.find(1)); // Erwartet: 1 oder 2 oder 5
        System.out.println("Repräsentant von 2 nach Union(1, 2): " + u.find(2)); // Erwartet: 1 oder 2 oder 5

        u.union(8, 9);
        System.out.println("Anzahl Partitionierungen nach Union(8, 9): " + u.size()); // Erwartet: 7

        u.union(6, 7);
        System.out.println("Anzahl Partitionierungen nach Union(6, 7): " + u.size()); // Erwartet: 6

        u.union(4, 5);
        System.out.println("Anzahl Partitionierungen nach Union(4, 5): " + u.size()); // Erwartet: 5
        System.out.println("Repräsentant von 4 nach Union(4, 5): " + u.find(4)); // Erwartet: 1, 2, 4 oder 5

    }
}
