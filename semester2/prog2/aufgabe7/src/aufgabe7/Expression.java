package aufgabe7;

import java.util.Map;
import java.util.Set;

public interface Expression {
    double eval(Map<String, Double> mp);
    Set<String> getVars();
    String toString();
}
