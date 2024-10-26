import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class GUI extends JFrame {

    private static final String username = "dbsys19";
    private static final String password = "dbsys19";
    private static Connection conn;

    private JComboBox<String> countryComboBox;
    private JComboBox<Double> betragComboBox;
    private JTextField arrivalDateField;
    private JTextField departureDateField;
    private JComboBox<String> amenitiesComboBox;
    private JTable resultTable;
    private DefaultTableModel tableModel;

    public GUI() {
        super("Ferienwohnung Buchungsanwendung");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600); // Größere Größe für das Hauptfenster

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout()); // Verwenden eines GridBagLayouts für flexiblere Platzierung

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Randabstand für die Komponenten
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Labels und Komponenten hinzufügen
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Land:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1;
        countryComboBox = new JComboBox<>();
        countryComboBox.setPreferredSize(new Dimension(90, 25));
        panel.add(countryComboBox, gbc);

        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.weightx = 0;
        panel.add(new JLabel("Anreise:"), gbc);

        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.weightx = 1;
        arrivalDateField = new JTextField(40); // Breite für das Textfeld festlegen
        panel.add(arrivalDateField, gbc);

        gbc.gridx = 4;
        gbc.gridy = 0;
        gbc.weightx = 0;
        panel.add(new JLabel("Abreise:"), gbc);

        gbc.gridx = 5;
        gbc.gridy = 0;
        gbc.weightx = 1;
        departureDateField = new JTextField(40); // Breite für das Textfeld festlegen
        panel.add(departureDateField, gbc);

        gbc.gridx = 6;
        gbc.gridy = 0;
        gbc.weightx = 0;
        panel.add(new JLabel("Ausstattung:"), gbc);

        gbc.gridx = 7;
        gbc.gridy = 0;
        gbc.weightx = 1;
        amenitiesComboBox = new JComboBox<>();
        amenitiesComboBox.setPreferredSize(new Dimension(90, 25));
        panel.add(amenitiesComboBox, gbc);

        gbc.gridx = 8;
        gbc.gridy = 0;
        gbc.weightx = 0;
        panel.add(new JLabel("Preis:"), gbc);

        gbc.gridx = 9;
        gbc.gridy = 0;
        gbc.weightx = 1;
        betragComboBox = new JComboBox<>();
        betragComboBox.setPreferredSize(new Dimension(100, 25));
        panel.add(betragComboBox, gbc);

        // Platzhalter hinzufügen, um den Abstand zum unteren Bereich zu erhöhen
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 8;
        gbc.weighty = 1.0; // Vertikaler Abstand erhöhen
        panel.add(Box.createGlue(), gbc);

        JButton searchButton = new JButton("Ferienwohnungen suchen");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 4;
        gbc.weighty = 0.0; // Zurücksetzen der Gewichtung
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchApartmentsFromGUI();
            }
        });
        panel.add(searchButton, gbc);

        JButton bookButton = new JButton("Ferienwohnung buchen");
        gbc.gridx = 4;
        gbc.gridy = 2;
        gbc.gridwidth = 4;
        gbc.weighty = 0.0; // Zurücksetzen der Gewichtung
        bookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                bookApartmentFromGUI();
            }
        });
        panel.add(bookButton, gbc);

        // JTable für die Ergebnisse
        tableModel = new DefaultTableModel(new String[]{"ID", "Name", "Durchschnittliche Bewertung", "Preis"}, 0);
        resultTable = new JTable(tableModel);
        resultTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(resultTable);
        scrollPane.setPreferredSize(new Dimension(750, 300));
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 8;
        gbc.weighty = 1.0; // Vertikaler Abstand erhöhen
        gbc.fill = GridBagConstraints.BOTH;
        panel.add(scrollPane, gbc);

        add(panel);
        setVisible(true);

        try {
            conn = DatabaseManager.getConnection(username, password);
            loadLand(); // Lade Länder in die ComboBox
            loadAusstattung(); // Lade Ausstattungen in die ComboBox
            loadBetrag();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Verbindung zur Datenbank fehlgeschlagen!");
            ex.printStackTrace();
        }
    }

    private void loadBetrag() {
        try {
            String query = "SELECT betrag FROM dbsys19.Buchung";
            PreparedStatement pstmt = conn.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                double betrag = rs.getDouble("betrag");
                betragComboBox.addItem(betrag);
            }

            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Fehler beim Laden des Betrags aus der Datenbank!");
            e.printStackTrace();
        }
    }

    private void loadLand() {
        try {
            String query = "SELECT landname FROM dbsys19.Land";
            PreparedStatement pstmt = conn.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String country = rs.getString("landname");
                countryComboBox.addItem(country);
            }

            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Fehler beim Laden der Länder aus der Datenbank!");
            e.printStackTrace();
        }
    }

    private void loadAusstattung() {
        try {
            String query = "SELECT ausstattungsname FROM dbsys19.Ausstattung";
            PreparedStatement pstmt = conn.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();

            // Zuerst den leeren Eintrag hinzufügen
            amenitiesComboBox.addItem("");

            while (rs.next()) {
                String amenity = rs.getString("ausstattungsname");
                amenitiesComboBox.addItem(amenity);
            }

            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Fehler beim Laden der Ausstattungen aus der Datenbank: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void searchApartmentsFromGUI() {
        String land = (String) countryComboBox.getSelectedItem();
        String anreiseDatum = arrivalDateField.getText();
        String abreiseDatum = departureDateField.getText();
        String ausstattung = (String) amenitiesComboBox.getSelectedItem();

        // Leeren des TableModels
        tableModel.setRowCount(0);

        // Aufruf der Methode in DatabaseManager mit den richtigen Parametern
        DatabaseManager.searchApartments(land, anreiseDatum, abreiseDatum, ausstattung, tableModel);
    }

    private void bookApartmentFromGUI() {
        int selectedRow = resultTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Bitte wählen Sie eine Ferienwohnung aus.");
            return;
        }

        int ferienwohnungID = (int) tableModel.getValueAt(selectedRow, 0);
        double betrag = (double) tableModel.getValueAt(selectedRow, 3);

        String email = JOptionPane.showInputDialog(this, "Kunden E-Mail:");
        // An- und Abreisedaten aus den Textfeldern der GUI lesen
        String beginn = arrivalDateField.getText();
        String ende = departureDateField.getText();
        /*try {
            betrag = Double.parseDouble(JOptionPane.showInputDialog(this, "Betrag:"));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Ungültiger Betrag");
            return;
        }*/

        boolean erfolgreich = DatabaseManager.bookApartment(ferienwohnungID, email, beginn, ende, betrag);

        if (erfolgreich) {
            JOptionPane.showMessageDialog(this, "Die Buchung war erfolgreich.");
        } else {
            JOptionPane.showMessageDialog(this, "Die Buchung konnte nicht durchgeführt werden. Bitte versuchen Sie es erneut.");
        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GUI();
            }
        });
    }
}