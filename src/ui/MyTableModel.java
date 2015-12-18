package ui;

import java.util.Vector;

import javax.swing.table.DefaultTableModel;
/**
 * Die Klasse MyTableModell definiert das in der Startansicht verwendetet TableModell
 * @author Kuhaku
 *
 */
public class MyTableModel extends DefaultTableModel {
	boolean edibility=false;

	public MyTableModel(Vector<Vector<Object>> tableData, Vector<String> columnNames) {
		// TODO Auto-generated constructor stub
		super(tableData, columnNames);
	}
	/**
	 * Die Methode gibt zurück ob eine Zelle Editierbar ist.
	 * Von dem Namen abgesehen sind keine Zellen veränderbar().
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
			return edibility;	
		}
		
	}
	/**
	 * Methode setzt der Variable edibility einnm boolschen Wert
	 * @param b Übergabeparameter, der entscheidet ob der Name veränderbar ist oder nicht
	 */
	public void setEdibility(boolean b)
	{
		edibility=b;
	}

}
