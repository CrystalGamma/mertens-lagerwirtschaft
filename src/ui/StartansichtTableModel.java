package ui;

import java.util.Vector;

import javax.swing.table.DefaultTableModel;
/**
 * Die Klasse MyTableModell definiert das in der Startansicht verwendetet TableModell
 * @author Leon Westhof
 *
 */
public class StartansichtTableModel extends DefaultTableModel {
	//Boolscher Wert, der angiebt, ob die Spalte editierbar ist
	boolean isEditable=false;

	public StartansichtTableModel(Vector<Vector<Object>> tableData, Vector<String> columnNames) {
		super(tableData, columnNames);
	}
	/**
	 * Die Methode gibt zurück ob eine Zelle Editierbar ist.
	 * Von dem Namen(Namensspalte) abgesehen sind keine Zellen veränderbar.
	 * @return gibt zurück ob die Zelle editierbar ist oder nicht
	 */
	public boolean isCellEditable(int row,int column) {
		return column == 1 && isEditable;
	}
	/**
	 * Methode setzt der Variable isEditable einen boolschen Wert.
	 * @param isEditable Übergabeparameter, der entscheidet ob die Tabelle ( bzw. der Name) veränderbar ist oder nicht
	 */
	public void setEditability(boolean isEditable)
	{
		this.isEditable=isEditable;
	}
}
