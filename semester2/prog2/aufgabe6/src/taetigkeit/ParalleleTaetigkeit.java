package taetigkeit;

public class ParalleleTaetigkeit extends ZusammengesetzteTaetigkeit {
    @Override
    public double getTime() {
        double totalTime = 0;

        for (Taetigkeit task : tasks) {
            double time = task.getTime(); //Zeit holen auf objekt und in double time speichern
            if (task.getTime() > totalTime) {
                totalTime = time; //auf totale zeit gesetzt
            }
        }
        return totalTime;
    }
}
