package ui;

import controller.Controller;
import model.Model;
import model.Utils;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.ArrayList;
import java.util.Map;

public class LieferungDatum extends JFrame {
    private static LieferungDatum sharedInstance;
    JLabel titleLabel = new JLabel();
    CustomTable table = new CustomTable(new String[]{"Lager", "Menge"});

    public static LieferungDatum getInstance() {
        if (sharedInstance == null) {
            sharedInstance = new LieferungDatum();
        }

        sharedInstance.requestFocus();
        return sharedInstance;
    }

    private LieferungDatum() {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.init();
    }

    public void init() {
        this.setResizable(false);
        this.setLayout(new BorderLayout());

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        titleLabel.setFont(new Font(titleLabel.getFont().getName(), Font.BOLD, 20));
        titlePanel.add(titleLabel);

        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new BoxLayout(tablePanel, BoxLayout.Y_AXIS));
        table.setRowSelectionAllowed(false);
        table.setAutoCreateRowSorter(true);
        table.setEnabled(false);
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(table.getModel());
        table.setRowSorter(sorter);
        java.util.List<RowSorter.SortKey> sortKeys = new ArrayList<>();
        sortKeys.add(new RowSorter.SortKey(0, SortOrder.DESCENDING));
        sorter.setSortKeys(sortKeys);
        sorter.sort();
        DefaultTableCellRenderer valueRenderer = new DefaultTableCellRenderer();
        valueRenderer.setHorizontalAlignment(DefaultTableCellRenderer.RIGHT);
        table.getColumn("Menge").setCellRenderer(valueRenderer);
        tablePanel.add(table.getTableHeader());
        tablePanel.add(table);

        this.add(titlePanel, BorderLayout.NORTH);
        this.add(tablePanel, BorderLayout.SOUTH);

        this.setLocationRelativeTo(null);
    }

    public void build(Controller controller, String datum) {
        this.setTitle("Lieferung vom " + Utils.parseDate(datum));
        titleLabel.setText("Lieferung vom " + Utils.parseDate(datum));

        table.setController(controller);
        table.setRows(parseBuchungen(controller.getModel().getLieferungen(), datum));

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
}
