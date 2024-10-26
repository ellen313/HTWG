package aufgabe6;

/**
 * Utility Klasse um Faecher festzulegen aus AIN1.
 */
public final class Faecher {
    private Faecher() { }

    //pvt konstante Klassenvariable vom Typ Array von Strings
    private static final String[] FAECHER = {"Mathematik1",
        "Digitaltechnik", "Programmiertechnik1", "Softwaremodellierung"};

    /**
     * oeffentliche Klassenmethode, die prueft ob Fach vorgesehen ist.
     * for schleife geht jedes element fach im Array FAECHER durch und
     * ueberprueft ob f mit einem fach uebereinstimmt.
     * @param f gegebenes fach
     * @return true wenn vorhanden
     */
    public static boolean istZulaessig(String f) {
        //fuer jedes Element vom Typ Strings mit dem Namen fach in FAECHER...
        for (String fach: FAECHER) {
            if (fach.equals(f)) {
                return true; //im Stundenplan
            }
        } return false; //nicht im Stundenplan
    }
}

