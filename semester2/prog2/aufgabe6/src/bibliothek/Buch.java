package bibliothek;

public class Buch {
    private final String name;
    private Person entleiher;

    public Buch(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Person getEntleiher() {
        return entleiher;
    }

    public boolean wirdAusgeliehen(Person p) {
        if (entleiher != null) { //wenn schon ausgeliehen
            return false; //nicht erneut ausleihen
        } else {
            entleiher = p; //wird an Person verliehen
            p.leihtAus(this); //person wird als entleiher fuer buch festgelegt
        }
        return true; //ausleihe erfolgreich
    }

    public boolean wirdZurueckGegeben() {
        if (entleiher == null) { //wenn nicht ausgeliehen
            return false; //kann nicht zurueckgegeben werden
        } else {
            entleiher.gibtZurueck(this); //information aktualisiert, dass zurueckgegeben
            entleiher = null; //entleiher zurueckgegeben
            return true; //erfolgreich
        }
    }

    public void print() {
        System.out.println();
        System.out.println(name + " : " + (entleiher != null ? entleiher.getName() : " "));
    }
}
