package aufgabe7;

import java.util.Map;

public class Sum extends CompoundExpression {
    public Sum(Expression a, Expression b) {
        super(a, b);
    }

    @Override
    public double eval(Map<String, Double> mp) {
        // Evaluiert e1 und speichert Ergebnis in Variable var1
        double var1 = e1.eval(mp);
        // Evaluiert e2 und speichert Ergebnis in Variable var2
        double var2 = e2.eval(mp);
        // Gibt Summe der evaluierten Werte von e1 und e2 zurück
        return var1 + var2;
    }

    @Override
    public String toString() {
        return "(" + this.e1.toString() + " + " + this.e2.toString() + ")";
    }
}
