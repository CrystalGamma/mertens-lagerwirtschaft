package ui;

import model.Model;
import utils.Stream;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

/**
 * Die Klassse LagerAnsicht bietet eine Einsicht in die Buchungen für ein Datum.
 *
 * @author Florian Bussmann
 */
public class LieferungDatum extends JFrame implements Observer {
    final public Observable geklicktesLager = new Stream();
    private final String datum;
    private CustomTable table = new CustomTable(new String[]{"Lager", "Menge"});

    public LieferungDatum(Model model, String datum) {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                model.deleteObserver(LieferungDatum.this);
            }
        });
        this.datum = datum;
        this.init();
        this.update(model, this.datum);
    }

    /**
     * Initialisiert den Standardinhalt der Lieferungsansicht.
     */
    public void init() {
        this.setResizable(false);

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        JLabel titleLabel = new JLabel();
        titleLabel.setFont(new Font(titleLabel.getFont().getName(), Font.BOLD, 20));
        titleLabel.setText("Lieferung vom " + this.datum);
        titlePanel.add(titleLabel);

        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new BoxLayout(tablePanel, BoxLayout.Y_AXIS));
        DefaultTableCellRenderer valueRenderer = new DefaultTableCellRenderer();
        valueRenderer.setHorizontalAlignment(DefaultTableCellRenderer.RIGHT);
        this.table.getColumn("Menge").setCellRenderer(valueRenderer);
        tablePanel.add(this.table.getTableHeader());
        tablePanel.add(this.table);

        this.add(titlePanel, BorderLayout.NORTH);
        this.add(tablePanel, BorderLayout.SOUTH);

        this.table.setStream((Stream) this.geklicktesLager);

        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    /**
     * Führt die Daten in das benötigte Format für die Tabelle zusammen.
     *
     * @param lieferungen Alle Buchungen
     * @return Mehrdimensionales Array mit Daten (jeweils Datum und Menge)
     */
    public Object[][] parseBuchungen(Map<String, Map<Model.LagerHalle, Integer>> lieferungen, String datum) {
        Object[][] data = null;
        int pos = 0;

        // Iteriere über alle Buchungen und suche Buchungen zu dem geüwnschten Tag.
        for (Map.Entry<String, Map<Model.LagerHalle, Integer>> entry : lieferungen.entrySet()) {
            if (entry.getKey() != datum)
                continue;

            Map<Model.LagerHalle, Integer> buchungen = entry.getValue();
            data = new Object[buchungen.size()][2];

            // Iteriere über alle Buchungen des Tages.
            for (Map.Entry<Model.LagerHalle, Integer> buchung : buchungen.entrySet()) {
                data[pos][0] = buchung.getKey();
                data[pos][1] = buchung.getValue();
                pos++;
            }
        }
        return data;
    }

    /**
     * Aktualisieren der Ansicht bei Änderungen am Model.
     *
     * @param o   Beobachtetes Objekt
     * @param arg nicht verwendet
     */
    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof Model) {
            this.table.setRows(parseBuchungen(((Model) o).getLieferungen(), this.datum));
            this.pack();
        }
    }
}
