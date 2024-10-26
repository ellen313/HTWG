package aufgabe7;

import java.util.Set;
import java.util.Map;
import java.util.TreeSet;

public class Var implements Expression {
    private final String var;
    public Var(String var) {
        this.var = var;
    }

    public double eval(Map<String, Double> mp) {
        return mp.get(var); //liefert Wert, der Variable 'var' in uebergebenen Map entspricht
    }

    public Set<String> getVars() {
        Set<String> var = new TreeSet<>(); //um eindeutige Zeichenketten in sortierter Reihenfolge zu speichern
        var.add(this.var); //Zeichenkette, die durch this.var repräsentiert wird, zum Set hinzufuegen
        return var; //Set zuruckgeben, das die hinzugefügte Zeichenkette enthält
    }

    @Override
    public String toString() {
        return var;
    }
}
