package ui;

import java.awt.BorderLayout;
import java.awt.Checkbox;
import java.awt.FlowLayout;
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
import javax.swing.JCheckBox;
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
	int[] gesamtBestandUndKapazität;
	JLabel bestand;

	public StartAnsicht(Model model, Controller controler) {
		LagerNameZuLager = new HashMap<>();
		this.model = model;
		JLabel titel = new JLabel("Lagerstruktur");
		JPanel statusPanel= new JPanel();
		JPanel tablePanel = new JPanel();
		JPanel bodyPanel= new JPanel();
		tablePanel.setLayout(new BoxLayout(tablePanel, BoxLayout.Y_AXIS));
		Vector<String> columnNames = new Vector<>();
		columnNames.addElement("");
		columnNames.addElement("Lager");
		columnNames.addElement("Bestand");
		columnNames.addElement("Kapazität");
		tableData = new Vector<Vector<Object>>();
		gesamtBestandUndKapazität=fülleTabellenDaten(model.getLager(), 0);
		table = new JTable(tableData, columnNames);
		table.getTableHeader().setReorderingAllowed(false);
		//table.getColumn("").setCellRenderer(new ButtonRenderer());
		//table.getColumn("").setCellEditor(new ButtonEditor(new JCheckBox()));

		tablePanel.add(table.getTableHeader());
		tablePanel.add(table);
		JButton menu = new JButton("Menü");
		bestand= new JLabel("Bestand: "+String.valueOf(gesamtBestandUndKapazität[0]));
		JLabel kapazität= new JLabel("Kapazität:"+String.valueOf(gesamtBestandUndKapazität[1]));
		statusPanel.setLayout(new FlowLayout());
		statusPanel.add(bestand);
		statusPanel.add(kapazität);
		bodyPanel.setLayout(new BoxLayout(bodyPanel, BoxLayout.Y_AXIS));
		bodyPanel.add(statusPanel);
		bodyPanel.add(tablePanel);
		this.add(menu, BorderLayout.EAST);
		this.add(titel, BorderLayout.WEST);
		this.add(bodyPanel, BorderLayout.SOUTH);
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
					int selectedColumn = table.getSelectedColumn();
					vectorAusgewählteZeile = (tableData.get(table.getSelectedRow()));
					if (selectedColumn != 0 & selectedColumn != -1) {
						
						if (vectorAusgewählteZeile.get(0).equals("")) {

							Model.LagerHalle tmp = (Model.LagerHalle) LagerNameZuLager
									.get(vectorAusgewählteZeile.get(1).toString().trim());
							controler.öffneLagerX(tmp);
						}
					} else if (selectedColumn == 0) {
						// button pressed
						if(LagerNameZuLager.get(vectorAusgewählteZeile.get(1).toString().trim()) instanceof Model.OberLager )
						{
							controler.setZeigeUnterlager((Model.OberLager) LagerNameZuLager.get(vectorAusgewählteZeile.get(1).toString().trim()));
							update(null, null);
						}
					}
				} else if (arg0.getButton() == 3) {
					Point klickedPoint = arg0.getPoint();
					table.changeSelection(table.rowAtPoint(klickedPoint), table.columnAtPoint(klickedPoint), false,
							false);
					if (table.getSelectedColumn() == 0) {
						//button pressed
					} else {
						int selectedTableRow = table.getSelectedRow();
						table.requestFocus();
						table.editCellAt(selectedTableRow, 1);
						vectorAusgewählteZeile = (tableData.get(selectedTableRow));
						String alterName = vectorAusgewählteZeile.get(1).toString().trim();

						table.getModel().addTableModelListener(e -> controler.ändereLagerName(
								table.getValueAt(selectedTableRow, 1).toString().trim(), LagerNameZuLager.get(alterName)));
					}
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
		
		//gegen Construktoraufruf entschieden
		tableData.clear();
		gesamtBestandUndKapazität=fülleTabellenDaten(model.getLager(), 0);
		table.repaint();
		bestand.setText("Bestand: "+String.valueOf(gesamtBestandUndKapazität[0]));
		
	}

	/*
	 * 	
	 */
	public int[] fülleTabellenDaten(Model.Lager[] inputlager, int tiefe) {
		int[] gesamtBestandUndKapazität=new int[2];
		for (Model.Lager lager : inputlager) {
			Model.Lager[] unterLager = lager.getUnterLager();
			if (unterLager != null) {
				Vector<Object> tmpVector = new Vector<Object>();
				tmpVector.addElement("+");
				tmpVector.addElement(getEinrückung(tiefe)+lager.getName());
				tmpVector.addElement("");
				tmpVector.addElement("");
			
				tableData.addElement(tmpVector);
				int indexErstesUnterelement=tableData.indexOf(tmpVector)+1;
				
				fülleHashmap(LagerNameZuLager, lager.getName(), lager);
				int[] UnterlagerBestandUndKapazität=fülleTabellenDaten(unterLager, tiefe + 1);
				gesamtBestandUndKapazität[0]+=UnterlagerBestandUndKapazität[0];
				gesamtBestandUndKapazität[1]+=UnterlagerBestandUndKapazität[1];
				tmpVector.set(2,UnterlagerBestandUndKapazität[0]);
				tmpVector.set(3,UnterlagerBestandUndKapazität[1]);
				if(!lager.getZeigeUnterlager())
				{
					int indexLetztesUnterelement=tableData.indexOf((tableData.lastElement()));
					for(int j=indexErstesUnterelement;j<=indexLetztesUnterelement;j++)
					//lösche alle nach dem das Oberlagerhinzugefügt wurde bis zum letzten hinzugefügten
					tableData.remove(indexErstesUnterelement);
					tableData.lastElement().set(0,"-");
				}
			} else {
				Vector<Object> tmpVector = new Vector<Object>();
				tmpVector.addElement(new String(""));
				tmpVector.addElement(getEinrückung(tiefe)+lager.getName());
				tmpVector.addElement(lager.getBestand() + "");
				tmpVector.addElement(lager.getKapazität() + "");
				tableData.addElement(tmpVector);
				fülleHashmap(LagerNameZuLager, lager.getName(), lager);
				gesamtBestandUndKapazität[0]+=lager.getBestand();
				gesamtBestandUndKapazität[1]+=lager.getKapazität();
			}
		}
		return gesamtBestandUndKapazität; 
	}

	/*
	 * muss noch verallgemeinert werden
	 */
	public void fülleHashmap(HashMap<String, Model.Lager> map, String key, Model.Lager value) {
		map.put(key, value);
	}
	public String getEinrückung(int anzahl)
	{
		String rückgabe="";
		for(int i=0;i<anzahl;i++)
			rückgabe+="   ";
		return rückgabe;
	}
}

