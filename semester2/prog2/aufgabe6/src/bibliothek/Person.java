package bibliothek;

import java.util.LinkedList;
import java.util.List;

public class Person {
    private final String name;
    private final List<Buch> ausgelieheneBuecher = new LinkedList<>();

    public Person(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public boolean leihtAus(Buch b) {
        if (ausgelieheneBuecher.contains(b)) { //wenn buch in liste ausgeliehener buecher enthalten
            return false;//Buch kann nicht erneut ausgegeben werden
        }
        if (b.getEntleiher() != null && b.getEntleiher() != this) { //wenn buch bereits jmd anderem ausgeliehen
            return false; //Buch kann nicht ausgeliehen werden
        }
        ausgelieheneBuecher.add(b);//buch in liste der ausgeliehenen buecher zufuegen
        b.wirdAusgeliehen(this);//aktualisieren, dass buch von aktuellen instanz ausgeliehen wird
        return true; //ausleihe erfolgreich
    }

    public boolean gibtZurueck(Buch b){
        if (!ausgelieheneBuecher.contains(b))
            return false; //kann nicht zurueckgegeben werden wenn nicht in auseih liste

        ausgelieheneBuecher.remove(b); //aus Liste ausgeliehenen Buecher entfernen
        b.wirdZurueckGegeben(); //information aktualisieren, dass zurueckgegeben
        return true; //rueckgabe erfolgreich
    }

    public void print() {
        System.out.println();
        System.out.print(name + " : ");
        for (Buch b : ausgelieheneBuecher) {
            System.out.print(b.getName()+ ", ");
        }
        System.out.println();
    }
}
