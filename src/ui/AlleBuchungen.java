package ui;

import controller.Controller;
import model.Model;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.ArrayList;
import java.util.Map;

public class AlleBuchungen extends JFrame {
    public AlleBuchungen(Controller controller) {
        this.setResizable(false);
        this.setTitle("Alle Buchungen");
        this.setLayout(new BorderLayout());

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        JLabel titleLabel = new JLabel("Buchungsübersicht");
        titleLabel.setFont(new Font(titleLabel.getFont().getName(), Font.BOLD, 20));
        titlePanel.add(titleLabel);

        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new BoxLayout(tablePanel, BoxLayout.Y_AXIS));
        String[] columnNames = {"Datum", "Menge"};
        CustomTable table = new CustomTable(controller, parseBuchungen(controller.getModel().getLieferungen()), columnNames);
        table.setRowSelectionAllowed(false);
        table.setAutoCreateRowSorter(true);
        table.setEnabled(false);
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(table.getModel());
        table.setRowSorter(sorter);
        java.util.List<RowSorter.SortKey> sortKeys = new ArrayList<>();
        sortKeys.add(new RowSorter.SortKey(0, SortOrder.DESCENDING));
        sorter.setSortKeys(sortKeys);
        sorter.sort();
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

        this.pack();
        this.setLocationRelativeTo(null);
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
