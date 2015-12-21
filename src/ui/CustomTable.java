package ui;

import utils.Stream;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.ArrayList;

/**
 * Die Klasse CustomTable erweitert die JTable um einen standardmäßig gesetzten
 * Highlighter für Zellen mit einer negativen Zahl und ermöglicht darüber hinaus
 * die Weiterleitung in andere Ansichten über einen Klick in eine dafür
 * vorgesehene Zelle.
 *
 * @author Florian Bussmann
 */
public class CustomTable extends JTable {
    private Stream stream;

    public CustomTable(String[] columnNames) {
        super(new DefaultTableModel(new Object[][]{}, columnNames));
        this.getTableHeader().setReorderingAllowed(false);
        this.setRowSelectionAllowed(false);
        this.setEnabled(false);

        // Tabellen absteigend sortieren.
        this.setAutoCreateRowSorter(true);
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(this.getModel());
        this.setRowSorter(sorter);
        java.util.List<RowSorter.SortKey> sortKeys = new ArrayList<>();
        sortKeys.add(new RowSorter.SortKey(0, SortOrder.DESCENDING));
        sorter.setSortKeys(sortKeys);
        sorter.sort();

        this.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int rowIndex = CustomTable.this.rowAtPoint(evt.getPoint());
                int columnIndex = CustomTable.this.columnAtPoint(evt.getPoint());
                if (stream != null) {
                    // Angegklickte Zelle an den Observer übergeben.
                    Object value = getValueAt(rowIndex, columnIndex);
                    stream.push(value);
                }
            }
        });
    }

    public void setStream(Stream stream) {
        this.stream = stream;
    }

    public void setRows(Object[][] rows) {
        DefaultTableModel tableModel = ((DefaultTableModel) this.getModel());
        // Lösche alle alten Einträge um Tabelle neu zu zeichnen.
        tableModel.setRowCount(0);

        // Füge die Einträge aus dem Parameter der Tabelle hinzu.
        for (Object[] row : rows) {
            tableModel.addRow(row);
        }
    }

    @Override
    public Component prepareRenderer(TableCellRenderer renderer, int rowIndex,
                                     int columnIndex) {
        JComponent component = (JComponent) super.prepareRenderer(renderer, rowIndex, columnIndex);

        if (columnIndex > 0 && Integer.parseInt(getValueAt(rowIndex, columnIndex).toString()) < 0) {
            // Zellen mit negativem Wert mit einer entzückenden Farbe hervorheben.
            component.setBackground(Color.PINK);
        } else {
            // Ansonsten wird die Standardhintergrundfarbe verwendet.
            component.setBackground(null);
        }

        return component;
    }
}
