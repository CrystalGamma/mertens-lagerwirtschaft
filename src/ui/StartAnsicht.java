package ui;

import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import controller.Controller;
import model.Model;

public class StartAnsicht extends JFrame implements Observer {
	Model model;
	Vector<Vector<Object>> tableData;
	Model.Lager[] lager;
	HashMap<String, Model.Lager> LagerNameZuLager;
	JTable table;

	public static void main(String[] args) {
		StartAnsicht frame = new StartAnsicht(new Model(), new Controller());

	}

	public StartAnsicht(Model model, Controller controler) {
		LagerNameZuLager = new HashMap<>();
		this.model = model;
		// this.setLayout(new BoxLayout(getContentPane(),BoxLayout.Y_AXIS));
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
		MouseListener popupListener = new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				menuPopup.show(menu, 0, 20);
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseReleased(MouseEvent e) {
			}
		};
		// Menü bar action listener
		menu.addMouseListener(popupListener);
		menuItemAlleBuchungen.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				controler.öffneAlleBuchungen();

			}
		});
		menuItemAuslieferung.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				controler.öffneAuslieferung();

			}
		});
		menuItemZulieferung.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				controler.öffneZulieferung();

			}
		});
		table.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

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
					// controler.öffneLagerX(lager);
				}
				else if(arg0.getButton()==3)
				{
					Point klickedPoint= arg0.getPoint();
					table.changeSelection(table.rowAtPoint(klickedPoint),table.columnAtPoint(klickedPoint), false, false);
					int selectedTableRow =table.getSelectedRow();
					vectorAusgewählteZeile = (tableData.get(selectedTableRow));
					Model.Lager tmp = (Model.Lager) LagerNameZuLager.get(vectorAusgewählteZeile.get(1));
					table.requestFocus();
					table.editCellAt(selectedTableRow, 1);
					table.getModel().addTableModelListener(new TableModelListener() {
						
						@Override
						public void tableChanged(TableModelEvent e) {
							controler.ändereLagerName(table.getValueAt(selectedTableRow, 1).toString());
						}
					});
				}
			}

		});
		this.pack();
		// frame.setExtendedState(MAXIMIZED_BOTH);
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
		// if(!map.containsKey(key) &&!map.containsValue(value))
		// {
		map.put(key, value);
		// }
	}
}
