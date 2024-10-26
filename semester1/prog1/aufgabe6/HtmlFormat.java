package aufgabe6;
import java.io.PrintWriter;

/**
 * Utility-Klasse Html-Format.
 */
public final class HtmlFormat {
    private HtmlFormat() { }
    /**
     * oeffentliche Klassenmethode ausgeben.
     * Implementierung als Html-Datei.
     * @param ausgabe Printwriter erstellt diese datei object
     * @param name vorname nachname
     * @param leistungen liste der leistung der Person
     */
    public static void ausgeben(PrintWriter ausgabe, String[] name,
                                LeistungsListe leistungen) {

        ausgabe.println(
                "<html lang=\"de\"><head><meta http-equiv=\"Content-Type\""
                + "content=\"text/html; charset=UTF-8\">\n"
                + "<meta name=\"viewport\" content=\"width=device-width,"
                + "initial-scale=1.0\">\n"
                + "<style>\n"
                + "table { width:100%; }\n"
                + "th { text-align:left; }\n"
                + "</style>\n"
                + "<title>Notenspiegel</title>\n"
                + "</head>\n"
                + "<body>\n"
                + "<h1>Notenspiegel für ");

        for (String s : name) {
            ausgabe.println(s);
        }

        ausgabe.println(
            "</h1>\n"
            + "<hr>\n"
            + "<table>\n"
            + "<tr><th>Fach:</th><th>Art:</th><th>Note:</th></tr>");

        for (Leistung n : leistungen) {
            ausgabe.print("<tr><th");
            if (!n.istBestanden()) {
                ausgabe.print(" style=\"color:red\"");
            }
            ausgabe.printf(">%s</td><td>", n.getFach());
            ausgabe.println(n.istBenotet() ? "L" : "S");

            if (n.istBenotet()) {
                ausgabe.printf("</td><td>%s (%s)</td></tr>%n",
                        n.getNoteInWorten(), n.getNote());
            } else {
                ausgabe.printf("</td><td>%s</td></tr>%n",
                        n.istBestanden() ? "bestanden" : "nicht bestanden");
            }
        }
        ausgabe.println(
                "</table>\n"
                + "<hr>\n"
                + "L = Leistungsnachweis, S = Schein\n"
                + "</body>\n"
                + "</html>");

    }
}

