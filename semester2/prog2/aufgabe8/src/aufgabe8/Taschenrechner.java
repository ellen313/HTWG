package aufgabe8;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Locale;

public class Taschenrechner extends JFrame implements ActionListener, ItemListener {

    JButton plusButton;
    JButton minusButton;
    JButton malButton;
    JButton geteiltButton;
    JButton sinusButton;
    JButton cosinusButton;
    JButton log2Button;
    JButton potenzButton;
    JButton clearButton;

    JTextField opXTextField;
    JTextField opYTextField;
    JTextField resTextField;

    JCheckBox hDButton;

    JRadioButton degButton;
    JRadioButton radButton;

    boolean isRad;
    boolean isDark;

    public Taschenrechner() {
        setTitle("Taschenrechner");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(500,300));
        setBackground(Color.lightGray);
        setForeground(Color.black);

        //Labels
        JLabel opXLabel = new JLabel("Operand x");
        JLabel opYLabel = new JLabel("Operand y");
        JLabel resLabel = new JLabel("Resultat");

        opXTextField = new JTextField("0", 10);
        opYTextField = new JTextField("0", 10);
        resTextField = new JTextField("0", 10);
        resTextField.setEditable(false);

        //Radio-, CheckButtons
        hDButton = new JCheckBox("Helles Display");
        hDButton.addItemListener(this);
        hDButton.setSelected(true);

        degButton = new JRadioButton("Deg");
        degButton.addItemListener(this);
        degButton.setSelected(true);
        radButton = new JRadioButton("Rad");
        radButton.addItemListener(this);

        ButtonGroup group = new ButtonGroup();
        group.add(degButton);
        group.add(radButton);

        //Buttons
        plusButton = new JButton(" + ");
        plusButton.addActionListener(this);
        malButton = new JButton(" * ");
        malButton.addActionListener(this);
        minusButton = new JButton(" - ");
        minusButton.addActionListener(this);
        geteiltButton = new JButton(" / ");
        geteiltButton.addActionListener(this);
        sinusButton = new JButton(" sin ");
        sinusButton.addActionListener(this);
        cosinusButton = new JButton(" cos ");
        cosinusButton.addActionListener(this);
        log2Button = new JButton(" log2 ");
        log2Button.addActionListener(this);
        potenzButton = new JButton(" x^y ");
        potenzButton.addActionListener(this);
        clearButton = new JButton(" Clear ");
        clearButton.addActionListener(this);

        //Panels
        JPanel panelO = new JPanel();
        panelO.setPreferredSize(new Dimension(450,100));
        panelO.setLayout(new GridLayout(3,2));
        panelO.add(opXLabel);
        panelO.add(opXTextField);
        panelO.add(opYLabel);
        panelO.add(opYTextField);
        panelO.add(resLabel);
        panelO.add(resTextField);
        panelO.setBorder(BorderFactory.createLineBorder(Color.gray));

        JPanel panelDrh = new JPanel();
        panelDrh.setLayout(new GridLayout(1,3));
        panelDrh.add(degButton);
        panelDrh.add(radButton);
        panelDrh.add(hDButton);

        JPanel panelK = new JPanel();
        panelK.setPreferredSize(new Dimension(450,75));
        panelK.setLayout(new GridLayout(2,2));
        panelK.add(plusButton);
        panelK.add(malButton);
        panelK.add(minusButton);
        panelK.add(geteiltButton);
        panelK.add(sinusButton);
        panelK.add(cosinusButton);
        panelK.add(log2Button);
        panelK.add(potenzButton);
        panelK.setBorder(BorderFactory.createLineBorder(Color.gray));

        JPanel panelC = new JPanel();
        panelC.add(clearButton);
        panelC.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        add(panelC, BorderLayout.SOUTH);

        //ins Hauptfenster einsetzen
        this.setLayout(new FlowLayout());
        this.add(panelO);
        this.add(panelDrh);
        this.add(panelK);
        this.add(panelC);

        pack();
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }

    public static void main(String[] args) {
        JFrame myApp = new Taschenrechner();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        String sX = opXTextField.getText();
        String sY = opYTextField.getText();
        double oX = 0;
        double oY = 0;

        try  { //moegliche NumberFormatException catchen wenn String nicht in gueltigen double wert umgewandelt
            oX = Double.parseDouble(sX);
            oY = Double.parseDouble(sY);
        } catch (Exception x) {
            if (source != clearButton) { //aufgrund benutzereingabe aufgerufen, es sei denn, Eingabe erfolgt ueber clearButton
                System.out.println("Fehlereingabe!"); //Eingabe erfolgt nicht ueber clearButton
                source = null;
            }
        }

        if (source == plusButton) {
            resTextField.setText("" + String.format(Locale.US, "%.2f",oX + oY));
        } else if (source == malButton) {
            resTextField.setText("" + String.format(Locale.US,"%.2f",oX * oY));
        } else if (source == minusButton) {
            resTextField.setText("" + String.format(Locale.US,"%.2f",oX - oY));
        } else if (source == geteiltButton) {
            resTextField.setText("" + String.format(Locale.US, "%.2f",oX / oY));
        } else if (source == sinusButton) {
            if (isRad) {
                resTextField.setText("" + String.format(Locale.US, "%.2f", Math.sin(oX)));
            } else {
                resTextField.setText("" + String.format(Locale.US, "%.2f", Math.sin(Math.toRadians(oX))));
            } opYTextField.setText("0");
        } else if (source == cosinusButton) {
            if (isRad) {
                resTextField.setText("" + String.format(Locale.US, "%.2f", Math.cos(oX)));
            } else {
                resTextField.setText("" + String.format(Locale.US, "%.2f", Math.cos(Math.toRadians(oX))));
            } opYTextField.setText("0");
        } else if (source == log2Button) {
            double log2Value = Math.log(oX) / Math.log(2);
            opYTextField.setText("0");
            resTextField.setText("" + String.format(Locale.US, "%.2f", log2Value));
        } else if (source == potenzButton) {
            double potenzValue = Math.pow(oX, oY);
            resTextField.setText("" + String.format(Locale.US, "%.2f", potenzValue));
        } else if (source == clearButton) {
            opXTextField.setText("0");
            opYTextField.setText("0");
            resTextField.setText("0");
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        Object source = e.getSource();

        if (source == hDButton) {
            if (isDark) {
                opXTextField.setBackground(Color.black);
                opYTextField.setBackground(Color.black);
                resTextField.setBackground(Color.black);

                opXTextField.setForeground(Color.yellow);
                opYTextField.setForeground(Color.yellow);
                resTextField.setForeground(Color.yellow);

                isDark = false;
            } else {
                opXTextField.setBackground(Color.white);
                opYTextField.setBackground(Color.white);
                resTextField.setBackground(Color.white);

                opXTextField.setForeground(Color.black);
                opYTextField.setForeground(Color.black);
                resTextField.setForeground(Color.black);

                isDark = true;
            }
        } else if (source == degButton) {
            isRad = false;
        } else if (source == radButton) {
            isRad = true;
        }
    }
}
