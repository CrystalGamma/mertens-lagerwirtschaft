package model;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LagerAnsicht extends JFrame {
    public LagerAnsicht(Model model, LagerHalle lager) {
        this.setResizable(false);
        this.setTitle("Lageransicht: " + lager.getName());
        this.setLayout(new BorderLayout());

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        JLabel titleLabel = new JLabel(lager.getName());
        titleLabel.setFont(new Font(titleLabel.getFont().getName(), Font.BOLD, 20));
        titlePanel.add(titleLabel);

        JLabel bestandLabel = new JLabel("Bestand: " + lager.getBestand());
        JLabel kapazitätLabel = new JLabel("Kapazität: " + lager.getKapazität());

        JPanel buchungsPanel = new JPanel();
        buchungsPanel.setLayout(new BoxLayout(buchungsPanel, BoxLayout.Y_AXIS));
        JLabel buchungsLabel = new JLabel("Buchungsübersicht");
        buchungsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        buchungsLabel.setFont(new Font(titleLabel.getFont().getName(), Font.BOLD, 16));
        buchungsPanel.add(buchungsLabel);

        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new BoxLayout(tablePanel, BoxLayout.Y_AXIS));
        String[] columnNames = {"Datum", "Bestandsänderung"};
        JTable table = new JTable(parseBuchungen(model.getBuchungenFürHalle(lager), lager), columnNames);
        table.setRowSelectionAllowed(false);
        table.setAutoCreateRowSorter(true);
        table.setEnabled(false);
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(table.getModel());
        table.setRowSorter(sorter);
        List<RowSorter.SortKey> sortKeys = new ArrayList<>();
        sortKeys.add(new RowSorter.SortKey(0, SortOrder.DESCENDING));
        sorter.setSortKeys(sortKeys);
        sorter.sort();
        tablePanel.add(table.getTableHeader());
        tablePanel.add(table);
        buchungsPanel.add(tablePanel);

        this.add(titlePanel, BorderLayout.NORTH);
        this.add(bestandLabel, BorderLayout.WEST);
        this.add(kapazitätLabel, BorderLayout.EAST);
        this.add(buchungsPanel, BorderLayout.SOUTH);

        this.pack();
        this.setLocationRelativeTo( null );
        this.setVisible( true );
    }

    public Object[][] parseBuchungen(Map<String, Map<LagerHalle, Integer>> lieferungen, LagerHalle lager) {
        Object[][] data = new Object[lieferungen.size()][2];
        int pos = 0;
        for (Map.Entry<String, Map<LagerHalle, Integer>> entry : lieferungen.entrySet()) {
            Map<LagerHalle, Integer> buchungen = entry.getValue();
            data[pos][0] = entry.getKey();
            data[pos][1] = buchungen.get(lager);
            pos++;
        }
        return data;
    }
}
