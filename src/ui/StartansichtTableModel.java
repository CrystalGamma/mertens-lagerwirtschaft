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
		// TODO Auto-generated constructor stub
		super(tableData, columnNames);
	}
	/**
	 * Die Methode gibt zurück ob eine Zelle Editierbar ist.
	 * Von dem Namen(Namensspalte) abgesehen sind keine Zellen veränderbar.
	 * @param row Die Zeile der Tabelle
	 * @param column Spalte der Tabelle
	 * @return true wenn die Zelle editierbar ist
	 */
	public boolean isCellEditable(int row,int column)
	{
		if(column!=1)
		{
			return false;
		}else
		{
			return isEditable;	
		}
		
	}
	/**
	 * Methode setzt der Variable isEditable einen boolschen Wert.
	 * @param isEditable Übergabeparameter, der entscheidet ob die Tabelle ( bzw. der Name) veränderbar ist oder nicht
	 */
	public void setEdibility(boolean isEditable)
	{
		this.isEditable=isEditable;
	}

}
