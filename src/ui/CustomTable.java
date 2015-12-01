package ui;

import controller.Controller;
import model.Model;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import java.awt.*;

public class CustomTable extends JTable {
    private Controller controller;

    public CustomTable(String[] columnNames) {
        super(new DefaultTableModel(new Object[][]{}, columnNames));
        this.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int rowIndex = CustomTable.this.rowAtPoint(evt.getPoint());
                int columnIndex = CustomTable.this.columnAtPoint(evt.getPoint());
                if (rowIndex >= 0 && columnIndex >= 0 && controller != null) {
                    Object value = getValueAt(rowIndex, columnIndex);
                    if (value instanceof String) {
                        controller.öffneLieferung(value.toString());
                    } else if (value instanceof Model.LagerHalle) {
                        controller.öffneLagerX((Model.LagerHalle) value);
                    }
                }
            }
        });
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void setRows(Object[][] rows) {
        DefaultTableModel tableModel = ((DefaultTableModel) this.getModel());
        tableModel.setRowCount(0);

        for (Object[] row : rows) {
            tableModel.addRow(row);
        }
    }

    @Override
    public Component prepareRenderer(TableCellRenderer renderer, int rowIndex,
                                     int columnIndex) {
        JComponent component = (JComponent) super.prepareRenderer(renderer, rowIndex, columnIndex);

        if (columnIndex > 0 && Integer.parseInt(getValueAt(rowIndex, columnIndex).toString()) < 0) {
            component.setBackground(Color.PINK);
        } else {
            component.setBackground(null);
        }

        return component;
    }
}
