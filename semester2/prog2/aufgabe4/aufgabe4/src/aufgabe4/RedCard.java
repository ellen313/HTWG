package aufgabe4;

import java.util.Random;

public class RedCard extends Card {

    public RedCard() {
        Random rand = new Random();
        farbe = Farbe.values()[rand.nextInt(2) + 2].toString(); // +2, weil schwarzen zuerst im enum im array und insg. 4 farben
        wert = Wert.values()[rand.nextInt(8)].toString();
    }

    public RedCard(Farbe f, Wert w) {
        if (!(f == Farbe.HERZ || f == Farbe.KARO))
            throw new IllegalArgumentException("RedCard darf nur rote Karten haben.");
        farbe = f.toString();
        wert = w.toString();
    }
}
