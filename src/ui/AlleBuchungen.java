package ui;

import model.Model;
import utils.Stream;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

/**
 * Die Klassse AlleBuchungen bietet eine Übersicht über alle bisherigen Buchungen.
 *
 * @author Florian Bussmann
 */
public class AlleBuchungen extends JFrame implements Observer {
    private static AlleBuchungen sharedInstance;
    final public Observable geklicktesDatum = new Stream();
    private CustomTable table = new CustomTable(new String[]{"Datum", "Menge"});

    private AlleBuchungen() {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.init();
    }

    /**
     * Globaler Zugriffspunkt auf die Instanz.
     *
     * @return Instanz der Ansicht für alle Buchungen.
     */
    public static AlleBuchungen getInstance() {
        if (sharedInstance == null) {
            sharedInstance = new AlleBuchungen();
        }

        // Ansicht in den Vordergrund holen.
        sharedInstance.requestFocus();

        return sharedInstance;
    }

    /**
     * Initialisiert den Standardinhalt der Ansicht für alle Buchungen.
     */
    public void init() {
        this.setResizable(false);
        this.setTitle("Alle Buchungen");

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        JLabel titleLabel = new JLabel("Buchungsübersicht");
        titleLabel.setFont(new Font(titleLabel.getFont().getName(), Font.BOLD, 20));
        titlePanel.add(titleLabel);

        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new BoxLayout(tablePanel, BoxLayout.Y_AXIS));
        DefaultTableCellRenderer keyRenderer = new DefaultTableCellRenderer();
        keyRenderer.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
        this.table.getColumn("Datum").setCellRenderer(keyRenderer);
        DefaultTableCellRenderer valueRenderer = new DefaultTableCellRenderer();
        valueRenderer.setHorizontalAlignment(DefaultTableCellRenderer.RIGHT);
        this.table.getColumn("Menge").setCellRenderer(valueRenderer);
        tablePanel.add(this.table.getTableHeader());
        tablePanel.add(this.table);

        this.add(titlePanel, BorderLayout.NORTH);
        this.add(tablePanel, BorderLayout.SOUTH);

        this.setLocationRelativeTo(null);
    }

    /**
     * Führt die Daten in das benötigte Format für die Tabelle zusammen.
     *
     * @param lieferungen Alle Buchungen
     * @return Mehrdimensionales Array mit Daten (jeweils Datum und Menge)
     */
    public Object[][] parseBuchungen(Map<String, Map<Model.LagerHalle, Integer>> lieferungen) {
        Object[][] data = new Object[lieferungen.size()][2];
        int pos = 0;

        // Iteriere über alle Tage an denen es Buchungen gab
        for (Map.Entry<String, Map<Model.LagerHalle, Integer>> entry : lieferungen.entrySet()) {
            data[pos][0] = entry.getKey();

            // Iteriere über alle Buchungen des Tages um die Summe der Mengeänderung zu erfassen
            Map<Model.LagerHalle, Integer> buchungen = entry.getValue();
            int menge = 0;
            for (Map.Entry<Model.LagerHalle, Integer> buchung : buchungen.entrySet()) {
                menge += buchung.getValue();
            }
            data[pos][1] = menge;

            pos++;
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
            this.table.setStream((Stream) this.geklicktesDatum);
            this.table.setRows(parseBuchungen(((Model) o).getLieferungen()));

            this.pack();
            this.setVisible(true);
        }
    }
}
