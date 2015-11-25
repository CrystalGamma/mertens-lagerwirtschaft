package ui;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class CustomTable extends JTable {
    public CustomTable(Object[][] objects, String[] columnNames) {
        super(objects, columnNames);
    }

    @Override
    public Component prepareRenderer(TableCellRenderer renderer, int rowIndex,
                                     int columnIndex) {
        JComponent component = (JComponent) super.prepareRenderer(renderer, rowIndex, columnIndex);

        if (columnIndex > 0 && Integer.parseInt(getValueAt(rowIndex, columnIndex) + "") < 0) {
            component.setBackground(Color.PINK);
        } else {
            component.setBackground(null);
        }

        return component;
    }
}
