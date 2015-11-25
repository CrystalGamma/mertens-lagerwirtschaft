package ui;

import controller.Controller;
import model.Model;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class CustomTable extends JTable {
    public CustomTable(Controller controller, Object[][] objects, String[] columnNames) {
        super(objects, columnNames);
        this.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int rowIndex = CustomTable.this.rowAtPoint(evt.getPoint());
                int columnIndex = CustomTable.this.columnAtPoint(evt.getPoint());
                if (rowIndex >= 0 && columnIndex >= 0) {
                    Object value = getValueAt(rowIndex, columnIndex);
                    if (value instanceof String) {
                        new LieferungDatum(controller, value.toString());
                    } else if (value instanceof Model.LagerHalle) {
                        new LagerAnsicht(controller, (Model.LagerHalle) value);
                    }
                }
            }
        });
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
