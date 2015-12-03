package ui;

import model.Model;
import model.Utils;
import utils.Stream;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
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
    private JLabel titleLabel = new JLabel();
    private CustomTable table = new CustomTable(new String[]{"Lager", "Menge"});

    public LieferungDatum(String datum) {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.datum = datum;
        this.init();
    }

    /**
     * Initialisiert den Standardinhalt der Lieferungsansicht.
     */
    public void init() {
        this.setResizable(false);

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        this.titleLabel.setFont(new Font(this.titleLabel.getFont().getName(), Font.BOLD, 20));
        titlePanel.add(this.titleLabel);

        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new BoxLayout(tablePanel, BoxLayout.Y_AXIS));
        DefaultTableCellRenderer valueRenderer = new DefaultTableCellRenderer();
        valueRenderer.setHorizontalAlignment(DefaultTableCellRenderer.RIGHT);
        this.table.getColumn("Menge").setCellRenderer(valueRenderer);
        tablePanel.add(this.table.getTableHeader());
        tablePanel.add(this.table);

        this.add(titlePanel, BorderLayout.NORTH);
        this.add(tablePanel, BorderLayout.SOUTH);

        this.setLocationRelativeTo(null);
    }

    public Object[][] parseBuchungen(Map<String, Map<Model.LagerHalle, Integer>> lieferungen, String datum) {
        Object[][] data = null;
        int pos = 0;
        for (Map.Entry<String, Map<Model.LagerHalle, Integer>> entry : lieferungen.entrySet()) {
            if (entry.getKey() != datum)
                continue;

            Map<Model.LagerHalle, Integer> buchungen = entry.getValue();
            data = new Object[buchungen.size()][2];

            for (Map.Entry<Model.LagerHalle, Integer> buchung : buchungen.entrySet()) {
                data[pos][0] = buchung.getKey();
                data[pos][1] = buchung.getValue();
                pos++;
            }
        }
        return data;
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof Model) {
            this.setTitle("Lieferung vom " + Utils.parseDate(this.datum));
            this.titleLabel.setText("Lieferung vom " + Utils.parseDate(this.datum));

            this.table.setStream((Stream) this.geklicktesLager);
            this.table.setRows(parseBuchungen(((Model) o).getLieferungen(), this.datum));

            this.pack();
            this.setVisible(true);
        }
    }
}
