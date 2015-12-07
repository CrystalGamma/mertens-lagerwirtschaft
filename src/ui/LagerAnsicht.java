package ui;

import model.Model;
import utils.Stream;

import javax.swing.*;
import java.awt.*;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

/**
 * Die Klassse LagerAnsicht bietet eine Einsicht in die bisherigen Buchungen für
 * ein Lager.
 *
 * @author Florian Bussmann
 */
public class LagerAnsicht extends JFrame implements Observer {
    final public Observable geklicktesDatum = new Stream();
    final private Model.LagerHalle lager;
    private JLabel titleLabel = new JLabel();
    private JLabel bestandLabel = new JLabel();
    private JLabel kapazitätLabel = new JLabel();
    private CustomTable table = new CustomTable(new String[]{"Datum", "Bestandsänderung"});

    public LagerAnsicht(Model.LagerHalle lager) {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.lager = lager;
        this.init();
    }

    /**
     * Initialisiert den Standardinhalt der Lageransicht.
     */
    private void init() {
        this.setResizable(false);

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        this.titleLabel.setFont(new Font(this.titleLabel.getFont().getName(), Font.BOLD, 20));
        titlePanel.add(this.titleLabel);

        JPanel buchungsPanel = new JPanel();
        buchungsPanel.setLayout(new BoxLayout(buchungsPanel, BoxLayout.Y_AXIS));
        JLabel buchungsLabel = new JLabel("Buchungsübersicht");
        buchungsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        buchungsLabel.setFont(new Font(this.titleLabel.getFont().getName(), Font.BOLD, 16));
        buchungsPanel.add(buchungsLabel);

        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new BoxLayout(tablePanel, BoxLayout.Y_AXIS));
        tablePanel.add(this.table.getTableHeader());
        tablePanel.add(this.table);
        buchungsPanel.add(tablePanel);

        this.add(titlePanel, BorderLayout.NORTH);
        this.add(bestandLabel, BorderLayout.WEST);
        this.add(kapazitätLabel, BorderLayout.EAST);
        this.add(buchungsPanel, BorderLayout.SOUTH);

        this.setLocationRelativeTo(null);
    }

    /**
     * Führt die Daten in das benötigte Format für die Tabelle zusammen.
     *
     * @param lieferungen Buchungen für eine Lagerhalle
     * @return Mehrdimensionales Array mit Daten (jeweils Datum und Bestandsänderung)
     */
    public Object[][] parseBuchungen(Map<String, Map<Model.LagerHalle, Integer>> lieferungen) {
        Object[][] data = new Object[lieferungen.size()][2];
        int pos = 0;

        // Iteriere über alle Tage an denen es Buchungen gab
        for (Map.Entry<String, Map<Model.LagerHalle, Integer>> entry : lieferungen.entrySet()) {
            data[pos][0] = entry.getKey();

            // Hole die Buchung zu dem ausgewählten Lager zu dem aktuellen Tag
            Map<Model.LagerHalle, Integer> buchungen = entry.getValue();
            data[pos][1] = buchungen.get(this.lager);

            pos++;
        }
        return data;
    }

    /**
     * Aktualisieren der Ansicht bei Änderungen am Model.
     *
     * @param o Beobachtetes Objekt
     * @param arg Lagerhalle an der sich etwas geändert hat
     */
    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof Model && arg == this.lager) {
            this.setTitle("Lageransicht: " + this.lager.getName());
            this.titleLabel.setText(this.lager.getName());

            this.bestandLabel.setText("Bestand: " + this.lager.getBestand());
            this.kapazitätLabel.setText("Kapazität: " + this.lager.getKapazität());

            this.table.setStream((Stream) this.geklicktesDatum);
            this.table.setRows(parseBuchungen(((Model) o).getBuchungenFürHalle(this.lager)));

            this.pack();
            this.setVisible(true);
        }
    }
}
