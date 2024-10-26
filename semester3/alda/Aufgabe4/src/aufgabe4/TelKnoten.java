package aufgabe4;

public class TelKnoten {
    int x, y;
    /**Konstruktor der Klasse TelKnoten.
     * @param x Koordinaten
     * @param y Koordinaten*/
    public TelKnoten(int x, int y) {
        this.x = x;
        this.y = y;
    }
    @Override
    public String toString(){
        return ("x: " + this.x + ",y: " + this.y);
    }
    @Override
    public boolean equals(Object obj) {
        if(this == obj) {
            return true;
        }
        if(obj == null || getClass() != obj.getClass()) {
            return false;
        }
        TelKnoten other = (TelKnoten) obj;
        return this.x == other.x && this.y == other.y;
    }
}
