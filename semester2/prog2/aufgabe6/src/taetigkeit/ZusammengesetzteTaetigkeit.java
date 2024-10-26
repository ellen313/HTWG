package taetigkeit;

import java.util.ArrayList;
import java.util.List;

public abstract class ZusammengesetzteTaetigkeit implements Taetigkeit {
    protected List<Taetigkeit> tasks;

    public ZusammengesetzteTaetigkeit() {
        tasks = new ArrayList<>();
    }

    public void add(Taetigkeit task) { //mit add zsmgebaut
        tasks.add(task);
    }

    public void remove(Taetigkeit task) { //mit remove abgebaut
        tasks.remove(task);
    }

    public int getAnzahl() { //anzahl elemantarer taetigkeiten
        int count = 0; //aktuellen stand kennen
        for (Taetigkeit task : tasks) {
            count += task.getAnzahl(); //getAnzahl auf jedes Taetigkeit Objekt rufen
                                       // und Reslutat auf den totalcount addieren
        }
        return count;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (Taetigkeit task : tasks) {
            sb.append(task.toString()).append("\n");
        }
        return sb.toString();
    }
}
