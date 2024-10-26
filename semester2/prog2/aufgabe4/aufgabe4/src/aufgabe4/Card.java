package aufgabe4;

public abstract class Card {

    public enum Farbe {
        KREUZ, PIQUE, HERZ, KARO
    }

    public enum Wert {
        SIEBEN, ACHT, NEUN, ZEHN, BUBE, DAME, KÖNIG, ASS
    }

    protected String farbe;
    protected String wert;

    public final String getFarbe() {
        return this.farbe;
    }

    public final String getWert() {
        return this.wert;
    }

    @Override
    public String toString() {
        return "(" + farbe + "," + wert + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Card other = (Card) obj;
        return this.wert == other.wert && this.farbe == other.farbe;
    }
}
