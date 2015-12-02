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
    private static LieferungDatum sharedInstance;
    final public Stream stream = new Stream();
    private JLabel titleLabel = new JLabel();
    private CustomTable table = new CustomTable(new String[]{"Lager", "Menge"});

    private LieferungDatum() {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.init();
    }

    public static LieferungDatum getInstance() {
        if (sharedInstance == null) {
            sharedInstance = new LieferungDatum();
        }

        sharedInstance.requestFocus();
        return sharedInstance;
    }

    public void init() {
        this.setResizable(false);

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        titleLabel.setFont(new Font(titleLabel.getFont().getName(), Font.BOLD, 20));
        titlePanel.add(titleLabel);

        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new BoxLayout(tablePanel, BoxLayout.Y_AXIS));
        DefaultTableCellRenderer valueRenderer = new DefaultTableCellRenderer();
        valueRenderer.setHorizontalAlignment(DefaultTableCellRenderer.RIGHT);
        table.getColumn("Menge").setCellRenderer(valueRenderer);
        tablePanel.add(table.getTableHeader());
        tablePanel.add(table);

        this.add(titlePanel, BorderLayout.NORTH);
        this.add(tablePanel, BorderLayout.SOUTH);

        this.setLocationRelativeTo(null);
    }

    public void build(Model model, String datum) {
        this.setTitle("Lieferung vom " + Utils.parseDate(datum));
        titleLabel.setText("Lieferung vom " + Utils.parseDate(datum));

        table.setStream(stream);
        table.setRows(parseBuchungen(model.getLieferungen(), datum));

        this.pack();
        this.setVisible(true);
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

    }
}
