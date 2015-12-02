package ui;

import controller.Controller;
import model.Model;
import utils.Stream;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.Observable;

/**
 * Die Klassse AlleBuchungen bietet eine Übersicht über alle bisherigen Buchungen.
 *
 * @author Florian Bussmann
 */
public class AlleBuchungen extends JFrame {
    private static AlleBuchungen sharedInstance;
    final public Stream stream = new Stream();
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
        table.getColumn("Datum").setCellRenderer(keyRenderer);
        DefaultTableCellRenderer valueRenderer = new DefaultTableCellRenderer();
        valueRenderer.setHorizontalAlignment(DefaultTableCellRenderer.RIGHT);
        table.getColumn("Menge").setCellRenderer(valueRenderer);
        tablePanel.add(table.getTableHeader());
        tablePanel.add(table);

        this.add(titlePanel, BorderLayout.NORTH);
        this.add(tablePanel, BorderLayout.SOUTH);

        this.setLocationRelativeTo(null);
    }

    public void refresh(Controller controller) {
        table.setRows(parseBuchungen(controller.getModel().getLieferungen()));

        table.setStream(stream);

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
}
