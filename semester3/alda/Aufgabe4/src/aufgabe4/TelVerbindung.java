package aufgabe4;
/**Klasse für die Verbindung zwischen zwei Knoten und Verbindungskosten */
public class TelVerbindung {
    TelKnoten u;
    TelKnoten v;
    int cost;
    /**Konstruktor der Klasse.
     * @param u Knoten
     * @param v  Knoten
     * @param cost Kosten, der Verbindung zwischen u und v */
    public  TelVerbindung(TelKnoten u, TelKnoten v, int cost){
        this.u = u;
        this.v = v;
        this.cost = cost;
    }


    @Override
    public String toString(){
        return "Anfangsknoten: " + this.u + "Endknoten: "
                + this.v + "Kosten: " + this.cost;
    }

}
