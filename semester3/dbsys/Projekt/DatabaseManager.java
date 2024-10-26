import javax.swing.table.DefaultTableModel;
import java.sql.*;


public class DatabaseManager {
    private static Connection conn = null;
    private static final String username = "dbsys19";
    private static final String password = "dbsys19";
    public static Connection getConnection(String username, String password) throws SQLException {
        if (conn == null || conn.isClosed()) {
            /*BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            String name = null;
            String passwd = null;
            try {
                System.out.println("Benutzername:");
                name = in.readLine();
                System.out.println("Passwort:");
                passwd = in.readLine();
            } catch (IOException e) {
                System.out.println("Fehler beim Lesen der Eingabe: " + e);
                System.exit(-1);
            }*/
            try {
                DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
                String url = "jdbc:oracle:thin:@oracle19c.in.htwg-konstanz.de:1521:ora19c";
                conn = DriverManager.getConnection(url, username, password);
                conn.setAutoCommit(false); // Kein automatisches Commit setzen
            } catch(SQLException se) {
                System.out.println("SQLException: " + se.getMessage());
                System.out.println("- SQL state  : " + se.getSQLState());
                System.out.println("- Vendor code: " + se.getErrorCode());
                se.printStackTrace(); // Ausgabe der Fehlerdetails
            }

        }
        return conn;
    }

    public static void closeConnection() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    public static void rollbackConnection() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.rollback();
                System.out.println("Rollback durchgeführt.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*erste Teilaufgabe*/
    /**Diese Methode sucht nach Ferienwohnungen. Es werden alle Ferienwohnungen mit ihren durchschnittlichen Bewertungen
     * ausgegeben, die die angegebenen
     * Eigenschaften erfüllen.*/
    public static void searchApartments(String land, String anreiseDatum, String abreiseDatum, String ausstattung, DefaultTableModel tableModel) {
        /*Scanner scanner = new Scanner(System.in);
        System.out.println("Suche nach Ferienwohnungen");
        System.out.println("Land: ");
        String land = scanner.nextLine();
        System.out.println("Anreisedatum (DD.MM.YYYY): ");
        String anreiseDatum = scanner.nextLine();
        System.out.print("Abreisedatum (DD.MM.YYYY): ");
        String abreiseDatum = scanner.nextLine();
        System.out.print("Optional: Ausstattung (z.B. Küche): ");
        String ausstattung = scanner.nextLine();*/

        try{
            Connection conn = DatabaseManager.getConnection(username, password);
            PreparedStatement pstmt;
            String selectQuery;
            if (ausstattung == null || ausstattung.isEmpty()){ //keine Ausstattung wurde angegeben
                selectQuery = "SELECT f.ferienwohnungID, f.ferienwohnung_name, AVG(b.sterne) AS durchschnittliche_bewertung, f.preis " +
                        "FROM dbsys19.Ferienwohnung f " +
                        "LEFT JOIN dbsys19.Buchung b ON f.ferienwohnungID = b.ferienwohnungID " +
                        "JOIN dbsys19.Adresse a ON f.adressID = a.adressID " +
                        "JOIN dbsys19.Land l ON a.landname = l.landname " +
                        "WHERE l.landname = ? " +
                        "AND f.ferienwohnungID NOT IN (SELECT ferienwohnungID FROM Buchung WHERE (beginn <= TO_DATE(?, 'DD.MM.YYYY') AND ende >= TO_DATE(?, 'DD.MM.YYYY'))) " +
                        "GROUP BY f.ferienwohnungID, f.ferienwohnung_name, f.preis";
                pstmt = conn.prepareStatement(selectQuery);
                pstmt.setString(1, land);
                pstmt.setString(2, abreiseDatum);
                pstmt.setString(3, anreiseDatum);
            } else { // Ausstattung wurde angegeben
                selectQuery = "SELECT f.ferienwohnungID, f.ferienwohnung_name, AVG(b.sterne) AS durchschnittliche_bewertung, f.preis " +
                        "FROM dbsys19.Ferienwohnung f " +
                        "LEFT JOIN dbsys19.Buchung b ON f.ferienwohnungID = b.ferienwohnungID " +
                        "JOIN dbsys19.Adresse a ON f.adressID = a.adressID " +
                        "JOIN dbsys19.Land l ON a.landname = l.landname " +
                        "JOIN dbsys19.beinhaltet bi ON f.ferienwohnungID = bi.ferienwohnungID " +
                        "WHERE l.landname = ? " +
                        "AND f.ferienwohnungID NOT IN (SELECT ferienwohnungID FROM Buchung WHERE (beginn <= TO_DATE(?, 'DD.MM.YYYY') AND ende >= TO_DATE(?, 'DD.MM.YYYY'))) " +
                        "AND bi.ausstattungsname = ? " +
                        "GROUP BY f.ferienwohnungID, f.ferienwohnung_name, f.preis";
                pstmt = conn.prepareStatement(selectQuery);
                pstmt.setString(1, land);
                pstmt.setString(2, abreiseDatum);
                pstmt.setString(3, anreiseDatum);
                pstmt.setString(4, ausstattung);
            }
            // Debug-Ausgabe der SQL-Abfrage
            //System.out.println("SQL Query: " + selectQuery);

            ResultSet rs = pstmt.executeQuery();
            /*boolean hasResults = false;
            resultArea.setText(""); // Zurücksetzen des Textbereichs
            while (rs.next()) {
                hasResults = true;
                String name = rs.getString("ferienwohnung_name");
                int ferienwohnungId = rs.getInt("ferienwohnungID");
                double durchschnittlicheBewertung = rs.getDouble("durchschnittliche_bewertung");
                resultArea.append("Ferienwohnung ID: " + ferienwohnungId);
                resultArea.append(" Name: " + name);
                resultArea.append(" Durchschnittliche Bewertung: " + durchschnittlicheBewertung + "\n");

            }
            if (!hasResults) {
                System.out.println("Keine Ferienwohnungen gefunden, die den Suchkriterien entsprechen.");
            }*/
            while (rs.next()) {
                int id = rs.getInt("ferienwohnungID");
                String name = rs.getString("ferienwohnung_name");
                double bewertung = rs.getDouble("durchschnittliche_bewertung");
                double preis = rs.getDouble("preis");
                tableModel.addRow(new Object[]{id, name, bewertung, preis});
            }
            rs.close();
            pstmt.close();
            DatabaseManager.closeConnection();


            rs.close();
            pstmt.close();
            DatabaseManager.closeConnection();
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            rollbackConnection();
        }
    }
    public static boolean bookApartment(int ferienwohnungID, String email, String beginn, String ende, double betrag) {
        /*Scanner scanner = new Scanner(System.in);
        System.out.println("Buchen einer Ferienwohnung");
        System.out.print("Ferienwohnung ID: ");
        int ferienwohnungID = scanner.nextInt();
        scanner.nextLine(); // Zeilenumbruch einlesen
        System.out.print("Kunden E-Mail: ");
        String email = scanner.nextLine();
        System.out.print("Anreisedatum (DD-MM-YYYY): ");
        String beginn = scanner.nextLine();
        System.out.print("Abreisedatum (DD-MM-YYYY): ");
        String ende = scanner.nextLine();
        System.out.print("Betrag: ");
        double betrag = scanner.nextDouble();*/

        boolean erfolgreich = false;
        try {
            Connection conn = DatabaseManager.getConnection(username,password);
            String insertQuery = "INSERT INTO dbsys19.Buchung (buchungsnummer, mail, ferienwohnungID, storniert, beginn, ende, buchungsdatum, sterne, betrag) " +
                    "VALUES (Buchung_seq.NEXTVAL, ?, ?, 'N',TO_DATE(?, 'DD.MM.YYYY'), TO_DATE(?, 'DD.MM.YYYY'), SYSDATE, NULL, ?)";
            PreparedStatement pstmt = conn.prepareStatement(insertQuery);
            pstmt.setString(1, email);
            pstmt.setInt(2, ferienwohnungID);
            pstmt.setString(3, beginn);
            pstmt.setString(4, ende);
            pstmt.setDouble(5, betrag);

            // Debug-Ausgabe der SQL-Abfrage
            System.out.println("SQL Query: " + pstmt.toString());

            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted > 0) {
                conn.commit();
                erfolgreich = true;
                System.out.println("Ferienwohnung erfolgreich gebucht!");
            } else {
                System.out.println("Fehler bei der Buchung der Ferienwohnung.");
            }

            pstmt.close();
            DatabaseManager.closeConnection();
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            rollbackConnection();
        }
        return erfolgreich;
    }
    public static void showBookingDetails(String email) {
        try {
            Connection conn = DatabaseManager.getConnection(username, password);
            String query = "SELECT * FROM dbsys19.Buchung WHERE mail = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();

            boolean foundBookings = false;

            while (rs.next()) {
                int bookingId = rs.getInt("buchungsnummer");
                int ferienwohnungID = rs.getInt("ferienwohnungID");
                String beginn = rs.getString("beginn");
                String ende = rs.getString("ende");
                double betrag = rs.getDouble("betrag");

                if (!foundBookings) {
                    System.out.println("Details der Buchungen für E-Mail: " + email);
                    foundBookings = true;
                }

                System.out.println("Buchungsnummer: " + bookingId);
                System.out.println("Ferienwohnung ID: " + ferienwohnungID);
                System.out.println("Anreisedatum: " + beginn);
                System.out.println("Abreisedatum: " + ende);
                System.out.println("Betrag: " + betrag);
                System.out.println("---------------------------------------");
            }

            if (!foundBookings) {
                System.out.println("Keine Buchungen gefunden für E-Mail: " + email);
            }
            rs.close();
            pstmt.close();
            DatabaseManager.closeConnection();
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        }
    }


    public static void main(String[] args) {
        try {
            Connection conn = DatabaseManager.getConnection(username, password);
            if (conn != null && !conn.isClosed()) {
                System.out.println("Verbindung zur Datenbank erfolgreich hergestellt!");
                conn.close(); // Verbindung schließen
            } else {
                System.out.println("Verbindung zur Datenbank fehlgeschlagen!");
            }
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        }
        try {
            //searchApartments();
            //bookApartment();
            showBookingDetails("sophiemann@example.com");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
