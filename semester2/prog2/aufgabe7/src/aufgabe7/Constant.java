package aufgabe7;

import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class Constant implements Expression {
    private final double constant;
    public Constant(double constant) {
        this.constant = constant;
    }

    @Override
    public double eval(Map<String, Double> mp) {
        return constant; //liefert konstante
    }

    @Override
    public Set<String> getVars() {
        return new TreeSet<>(); //gibt neues leeres treeset zurueck
    }

    @Override
    public String toString() {
        return String.valueOf(constant); //in string
    }
}
