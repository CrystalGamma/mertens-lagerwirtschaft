package ui;

import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTable;

import controller.Controller;
import model.Model;

public class StartAnsicht extends JFrame implements Observer {
	Model model;
	Vector<Vector<Object>> tableData;
	HashMap<String, Model.Lager> LagerNameZuLager;
	JTable table;

	public static void main(String[] args) {
		new StartAnsicht(new Model(), new Controller());
	}

	public StartAnsicht(Model model, Controller controler) {
		LagerNameZuLager = new HashMap<>();
		this.model = model;
		JLabel titel = new JLabel("Lagerstruktur");
		JPanel tablePanel = new JPanel();
		tablePanel.setLayout(new BoxLayout(tablePanel, BoxLayout.Y_AXIS));
		Vector<String> columnNames = new Vector<>();
		columnNames.addElement("");
		columnNames.addElement("Lager");
		columnNames.addElement("Bestand");
		columnNames.addElement("Kapazität");
		tableData = new Vector<Vector<Object>>();
		fülleTabellenDaten(model.getLager());
		table = new JTable(tableData, columnNames);

		tablePanel.add(table.getTableHeader());
		tablePanel.add(table);
		JButton menu = new JButton("Menü");
		this.add(menu, BorderLayout.EAST);
		this.add(titel, BorderLayout.WEST);
		this.add(tablePanel, BorderLayout.SOUTH);
		JPopupMenu menuPopup = new JPopupMenu();
		JMenuItem menuItemZulieferung = new JMenuItem("Zulieferung");
		menuPopup.add(menuItemZulieferung);
		JMenuItem menuItemAuslieferung = new JMenuItem("Auslieferung");
		menuPopup.add(menuItemAuslieferung);
		JMenuItem menuItemAlleBuchungen = new JMenuItem("Alle Buchungen");
		menuPopup.add(menuItemAlleBuchungen);
		MouseListener popupListener = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				menuPopup.show(menu, 0, 20);
			}
		};
		// Menü bar action listener
		menu.addMouseListener(popupListener);
		menuItemAlleBuchungen.addActionListener(arg0 -> controler.öffneAlleBuchungen());
		menuItemAuslieferung.addActionListener(x -> controler.öffneAuslieferung());
		menuItemZulieferung.addActionListener(x -> controler.öffneZulieferung());
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				// TODO Auto-generated method stub
				Vector<Object> vectorAusgewählteZeile;
				if (arg0.getButton() == 1) {
					vectorAusgewählteZeile = (tableData.get(table.getSelectedRow()));
					if (vectorAusgewählteZeile.get(0).equals("")) {

						Model.LagerHalle tmp = (Model.LagerHalle) LagerNameZuLager.get(vectorAusgewählteZeile.get(1));
						controler.öffneLagerX(tmp);
					}
				}
				else if(arg0.getButton()==3)
				{
					Point klickedPoint= arg0.getPoint();
					table.changeSelection(table.rowAtPoint(klickedPoint),table.columnAtPoint(klickedPoint), false, false);
					int selectedTableRow =table.getSelectedRow();
					table.requestFocus();
					table.editCellAt(selectedTableRow, 1);
					vectorAusgewählteZeile = (tableData.get(selectedTableRow));
					String alterName= vectorAusgewählteZeile.get(1).toString();
					table.getModel().addTableModelListener(e -> controler.ändereLagerName(table.getValueAt(selectedTableRow, 1).toString(),LagerNameZuLager.get(alterName)));
				}
			}

		});
		this.pack();
		this.setVisible(true);

	}

	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		// Änderungen am Lager werden mit der Neuinitialisierung der Tabelle
		// behandelt

		// muss npch getestet werden
		tableData.clear();
		fülleTabellenDaten(model.getLager());
		table.repaint();
	}

	/*
	 * 	
	 */
	public void fülleTabellenDaten(Model.Lager[] inputlager) {
		for (Model.Lager lager : inputlager) {
			Model.Lager[] unterLager = lager.getUnterLager();
			if (unterLager != null) {
				Vector<Object> tmpVector = new Vector<Object>();
				JButton aufZuKlappen = new JButton("^");
				tmpVector.addElement(aufZuKlappen);
				tmpVector.addElement(lager.getName());
				tmpVector.addElement("");
				tmpVector.addElement("");
				tableData.addElement(tmpVector);
				fülleHashmap(LagerNameZuLager, lager.getName(), lager);
				fülleTabellenDaten(unterLager);
			} else {
				Vector<Object> tmpVector = new Vector<Object>();
				tmpVector.addElement(new String(""));
				tmpVector.addElement(lager.getName());
				tmpVector.addElement(lager.getBestand() + "");
				tmpVector.addElement(lager.getKapazität() + "");
				tableData.addElement(tmpVector);
				fülleHashmap(LagerNameZuLager, lager.getName(), lager);

			}
		}
	}

	/*
	 * muss noch verallgemeinert werden
	 */
	public void fülleHashmap(HashMap<String, Model.Lager> map, String key, Model.Lager value) {
		map.put(key, value);
	}
}
