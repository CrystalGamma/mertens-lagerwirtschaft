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

    public static AlleBuchungen getInstance() {
        if (sharedInstance == null) {
            sharedInstance = new AlleBuchungen();
        }

        sharedInstance.requestFocus();
        return sharedInstance;
    }

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

    public void refresh(Model model) {
        this.table.setStream((Stream) this.geklicktesDatum);
        this.table.setRows(parseBuchungen(model.getLieferungen()));

        this.pack();
        this.setVisible(true);
    }

    public Object[][] parseBuchungen(Map<String, Map<Model.LagerHalle, Integer>> lieferungen) {
        Object[][] data = new Object[lieferungen.size()][2];
        int pos = 0;
        for (Map.Entry<String, Map<Model.LagerHalle, Integer>> entry : lieferungen.entrySet()) {
            Map<Model.LagerHalle, Integer> buchungen = entry.getValue();
            data[pos][0] = entry.getKey();
            int menge = 0;
            for (Map.Entry<Model.LagerHalle, Integer> buchung : buchungen.entrySet()) {
                menge += buchung.getValue();
            }
            data[pos][1] = menge;
            pos++;
        }
        return data;
    }

    @Override
    public void update(Observable o, Object arg) {
        if(o instanceof Model) {
            this.refresh((Model) o);
        }
    }
}
