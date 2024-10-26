package taetigkeit;

public class SerielleTaetigkeit extends ZusammengesetzteTaetigkeit {
    @Override
    public double getTime() { //liefert benoetigte Zeit zurueck
        double totalTime = 0;

        for (Taetigkeit task : tasks) {
            totalTime += task.getTime(); //getTime auf jedes Taetigkeit Objekt rufen
                                        // und Reslutat auf totalTime addieren
        }
        return totalTime;
    }
}
