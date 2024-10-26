package aufgabe7;

import java.util.Map;

public class Product extends CompoundExpression {
    public Product(Expression a, Expression b) {
        super(a, b);
    }

    @Override
    public double eval(Map<String, Double> mp) {
        double var1 = e1.eval(mp);
        double var2 = e2.eval(mp);
        return var1 * var2; //gibt produkt der evaluierten werte zurück
    }

    @Override
    public String toString() {
        return "(" + this.e1.toString() + " * " + this.e2.toString() + ")"; //String format
    }
}
