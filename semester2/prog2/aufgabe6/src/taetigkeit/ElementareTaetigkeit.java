package taetigkeit;

public class ElementareTaetigkeit implements Taetigkeit{
    private final double time;
    private final String description;

    public ElementareTaetigkeit(String d, double t) {
        time = t;
        description = d;
    }

    @Override
    public double getTime() {
        return time;
    }

    @Override
    public void add(Taetigkeit a) {
        return; //exception werfen
    }

    @Override
    public void remove(Taetigkeit a) {
        return; //exception werfen
    }

    @Override
    public int getAnzahl() { //Anzahl elementarer Taetigketen
        return 1;
    }

    @Override
    public String toString() {
        return description + " (" + time + "min)";
    }
}
