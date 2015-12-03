package ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.JTextField;

public class ButtonEditor extends DefaultCellEditor {

	  protected JButton button;

	  private String label;

	  private boolean isPushed;
	public ButtonEditor(JCheckBox arg0) {
		super(arg0);
	   JButton button = new JButton("button");
		button.setOpaque(true);
	   button.addActionListener(new  ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			fireEditingStopped();
			//todo was?
			System.out.println("halli");
		}
	});
	}
	//Wann wird das aufgerufen?
	public Component getTableCellEditorComponent(JTable table, Object value,
		      boolean isSelected, int row, int column) {
		    if (isSelected) {
		      button.setForeground(table.getSelectionForeground());
		      button.setBackground(table.getSelectionBackground());
		    } else {
		    	System.out.println(table.getForeground());
		    	//Color test= new Color(table.getForeground().getRGB());
		    	//button.setForeground(test);
		      //button.setBackground(table.getBackground());
		    }
		   // label = (value == null) ? "" : value.toString();
		   // button.setText(label);
		    isPushed = true;
		    return button;

}
public Object getCellEditorValue() {
    if (isPushed) {
 System.out.println("drückt");
    }
    isPushed = false;
    return new String(label);
  }

  public boolean stopCellEditing() {
    isPushed = false;
    return super.stopCellEditing();
  }

  protected void fireEditingStopped() {
    super.fireEditingStopped();
  }}