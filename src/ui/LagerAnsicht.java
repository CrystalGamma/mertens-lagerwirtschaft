package ui;

import controller.Controller;
import model.Model;

import javax.swing.*;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Die Klassse LagerAnsicht bietet eine Einsicht in die bisherigen Buchungen für
 * ein Lager.
 *
 * @author Florian Bussmann
 */
public class LagerAnsicht extends JFrame {
    private static LagerAnsicht sharedInstance;
    private JLabel titleLabel = new JLabel();
    private JLabel bestandLabel = new JLabel();
    private JLabel kapazitätLabel = new JLabel();
    private CustomTable table = new CustomTable(new String[]{"Datum", "Bestandsänderung"});

    private LagerAnsicht() {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.init();
    }

    public static LagerAnsicht getInstance() {
        if (sharedInstance == null) {
            sharedInstance = new LagerAnsicht();
        }

        sharedInstance.requestFocus();
        return sharedInstance;
    }

    private void init() {
        this.setResizable(false);
        this.setLayout(new BorderLayout());

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        titleLabel.setFont(new Font(titleLabel.getFont().getName(), Font.BOLD, 20));
        titlePanel.add(titleLabel);

        JPanel buchungsPanel = new JPanel();
        buchungsPanel.setLayout(new BoxLayout(buchungsPanel, BoxLayout.Y_AXIS));
        JLabel buchungsLabel = new JLabel("Buchungsübersicht");
        buchungsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        buchungsLabel.setFont(new Font(titleLabel.getFont().getName(), Font.BOLD, 16));
        buchungsPanel.add(buchungsLabel);

        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new BoxLayout(tablePanel, BoxLayout.Y_AXIS));
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

        this.setLocationRelativeTo(null);
    }

    public void build(Controller controller, Model.LagerHalle lager) {
        this.setTitle("Lageransicht: " + lager.getName());
        titleLabel.setText(lager.getName());

        bestandLabel.setText("Bestand: " + lager.getBestand());
        kapazitätLabel.setText("Kapazität: " + lager.getKapazität());

        table.setController(controller);
        table.setRows(parseBuchungen(controller.getModel().getBuchungenFürHalle(lager)));

        this.pack();
        this.setVisible(true);
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
        for (Map.Entry<String, Map<Model.LagerHalle, Integer>> entry : lieferungen.entrySet()) {
            Map<Model.LagerHalle, Integer> buchungen = entry.getValue();
            data[pos][0] = entry.getKey();
            data[pos][1] = buchungen.get(buchungen.keySet().toArray()[0]);
            pos++;
        }
        return data;
    }
}
