package aufgabe7;

import java.util.Set;
import java.util.Map;
public class CompoundExpression implements Expression {
    Expression e1;
    Expression e2;

    public CompoundExpression(Expression a, Expression b) {
        this.e1 = a;
        this.e2 = b;
    }

    @Override
    public double eval(Map<String, Double> mp) {
        return 0;
    }

    @Override
    public Set<String> getVars() {
        //ruft die getVars-Methode für e1 auf und speichert Ergebnismenge in vars
        Set<String> vars = this.e1.getVars();
        //fügt die Ergebnismenge der getVars-Methode für e2 zu vars hinzu
        vars.addAll(this.e2.getVars());
        //gibt die kombinierte Menge der Variablen von e1 und e2 zurück
        return vars;
    }
}
