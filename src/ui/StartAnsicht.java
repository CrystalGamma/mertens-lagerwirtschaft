package ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import model.Model;
import utils.Stream;
/**
 * Diese Klasse erzeugt die Startansicht der Applikation
 * @author Leon Westhof
 *	
 */
public class StartAnsicht extends JFrame implements Observer {
	Model model;
	Vector<Vector<Object>> tableData;
	HashMap<String, Model.Lager> LagerNameZuLager;
	HashMap<Model.Lager,Boolean> LagerZuklappen;
	JTable table;
	int[] gesamtBestandUndKapazität;
	JLabel bestand;
	JLabel kapazität;
	JLabel titel;
	JButton menu;
	final int LINIEGRÖßE=3;

	final public Observable ÖffneLagerX = new Stream();
	final public Observable öffneAlleBuchungen = new Stream();
	final public Observable öffneAuslieferung = new Stream();
	final public Observable öffneZulieferung = new Stream();
	final public Observable ändereLagerName = new Stream();

	String alterName;
	/**
	 * Der Konstruktor dieser Klasse erzeugt, befüllt und versieht die Elemente der Start-GUI mit Actionlistenern
	 * @param model Das Modell was der Controller erzeugt hat
	 */

	public StartAnsicht(Model model) {
		this.model = model;
		LagerNameZuLager = new HashMap<>();
		LagerZuklappen= new HashMap<>();
		
		//TITEL-PANEL
		titel = new JLabel("Lagerstruktur");
		//BODY-PANEL
		JPanel bodyPanel= new JPanel();
		bodyPanel.setLayout(new BoxLayout(bodyPanel, BoxLayout.Y_AXIS));
		
		//TABLE-Subpanel
		JPanel tablePanel = new JPanel();
		tablePanel.setLayout(new BoxLayout(tablePanel, BoxLayout.Y_AXIS));
		Vector<String> columnNames = new Vector<>();
		columnNames.addElement("");
		columnNames.addElement("Lager");
		columnNames.addElement("Bestand");
		columnNames.addElement("Kapazität");
		tableData = new Vector<>();
		gesamtBestandUndKapazität=fülleTabellenDaten(model.getLager(), 0);
		StartansichtTableModel defaultModel= new StartansichtTableModel(tableData, columnNames); 
		table= new JTable(defaultModel);
		table.getTableHeader().setReorderingAllowed(false);
		setTableSize(table);
		
		//Hinzufügen der Tabelle und Header an das Tabellenpanel
		tablePanel.add(table.getTableHeader());
		tablePanel.add(table);
		
		//STATUS-Subpanel
		JPanel statusPanel= new JPanel();
		statusPanel.setLayout(new FlowLayout());
		
		//Belegung der Werte im Statuspanel
		bestand= new JLabel("Bestand: "+String.valueOf(gesamtBestandUndKapazität[0]));
		kapazität= new JLabel("Kapazität:"+String.valueOf(gesamtBestandUndKapazität[1]));
		statusPanel.add(bestand);
		statusPanel.add(kapazität);
		
		//Hinzufügen der ELemente des Bodypanels
		bodyPanel.add(statusPanel);
		bodyPanel.add(tablePanel);
				
		//MENU-PANEL
		menu = new JButton("Menü");
		//Popup Menu
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
		// Menubar action listener
		menu.addMouseListener(popupListener);
		menuItemAlleBuchungen.addActionListener(x -> ((Stream)öffneAlleBuchungen).push(null));
		menuItemAuslieferung.addActionListener(x -> ((Stream)öffneAuslieferung).push(null));
		menuItemZulieferung.addActionListener(x -> ((Stream)öffneZulieferung).push(null));
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				Vector<Object> vectorAusgewählteZeile;
				//Wenn ein linksklick erfolgte
				System.out.println("buttonarg"+arg0.getButton());
				if (arg0.getButton() == 1) {
					int selectedColumn = table.getSelectedColumn();
					vectorAusgewählteZeile = (tableData.get(table.getSelectedRow()));
					//Wenn eine Spalte äusgewählt wurde, die nicht die 0. ist
					if (selectedColumn != 0 & selectedColumn != -1) {
							Model.Lager tmp = LagerNameZuLager
									.get(vectorAusgewählteZeile.get(1).toString().trim());
							((Stream)ÖffneLagerX).push(tmp);
					}//	Wenn die ausgewählte Spalte die 0. ist und sie eine instance eines Lagers ist wird sie Zu bzw ausgeklappt
					else if (selectedColumn == 0) {
						Model.Lager lager=LagerNameZuLager.get(vectorAusgewählteZeile.get(1).toString().trim());
						if(lager instanceof Model.OberLager )
						{
							LagerZuklappen.put(lager, !(LagerZuklappen.get(lager)));
							update(null, null);
						}
					}
				}
				//Wenn ein Rechtsklick erfolgte, wird bei der Auf- und Zuklappspalte nichts unternommen, bei den anderen Spalten wird der Name des Lagers editierbar
				else if (arg0.getButton() == 3) {
					System.out.println(table.getSelectedColumn());
					Point klickedPoint = arg0.getPoint();
					table.changeSelection(table.rowAtPoint(klickedPoint), table.columnAtPoint(klickedPoint), false, false);
					if (table.getSelectedColumn() != 0) {
						//Markierung auf aktuelle Spalte setzen und Zelle markierbar machen
						int selectedTableRow = table.getSelectedRow();
						table.requestFocus();
						table.editCellAt(selectedTableRow, 1);
						vectorAusgewählteZeile = (tableData.get(selectedTableRow));
						//Namensspeicherung zwecks Hashmap
						alterName = vectorAusgewählteZeile.get(1).toString().trim();
						//In dem geänderten TableModell sind die Zellen standardmäßig nicht editierbar, damit Änderungen der Zelleninhalte nur über diese Funktion und nicht bspw den Doppelklick realisiert werden können
						defaultModel.setEdibility(true);

						table.getModel().addTableModelListener(arg01 -> {
							//Dem Observer wird der neue Name und das geänderte Lager in einer Klasse übergeben
							((Stream)ändereLagerName).push( new LagerNamensänderung(table.getValueAt(arg01.getLastRow(), 1).toString().trim(), LagerNameZuLager.get(alterName)));
							//Wiederherrstellung der Nicht-editierbarkeit
							defaultModel.setEdibility(false);
						});
					}
				}
		}});		
				
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				model.deleteObserver(StartAnsicht.this);
			}
		});
		
		//Hinzufügen der Panels ins Frame
		this.add(menu, BorderLayout.EAST);
		this.add(titel, BorderLayout.WEST);
		this.add(bodyPanel, BorderLayout.SOUTH);
		
		setResizable(false);
		this.pack();
		this.setVisible(true);

	}
	/**
	 * Die Methode wird bei Änderungen am Lager oder dem aufzuklappen aufgerufen.
	 * Sie leert die Tabelle, aktualisiert die Werte und zeichnet mit angepasster Größe neu. Abschließend wird der Bestandswert aktualisiert
	 */
	@Override
	public void update(Observable arg0, Object arg1) {
		tableData.clear();
		gesamtBestandUndKapazität=fülleTabellenDaten(model.getLager(), 0);
		this.setResizable(true);
		Dimension optimalFrameDimension= setTableSize(table);
		optimalFrameDimension.setSize(optimalFrameDimension.getWidth(), optimalFrameDimension.getHeight()+getPanelHeight());
		this.setSize(optimalFrameDimension);
		System.out.println("realPanel"+titel.getSize().getHeight()+kapazität.getSize().getHeight()+18);
		System.out.println(table.getSize().getHeight());
		this.setResizable(false);
		table.repaint();
		bestand.setText("Bestand: "+String.valueOf(gesamtBestandUndKapazität[0]));
	}

	
	/**
	 * Die Methode befüllt die für die Erzeugung der Tabelle notwenigen Vectoren.
	 * Dafür werden alle Übergebenene Lager rekursiv bis zum letzten durchsucht und es werden die unterschiedlichen Parameter dafür gesetzt.
	 * @param inputlager Lager die in der Tabelle stehen sollen
	 * @param tiefe Hirarchiebene
	 * @return ein int[] mit dem Gesamtbestand und der Gesamtkapazität
	 */
	public int[] fülleTabellenDaten(Model.Lager[] inputlager, int tiefe) {
		int[] gesamtBestandUndKapazität=new int[2];
		for (Model.Lager lager : inputlager) {
			Model.Lager[] unterLager = lager.getUnterLager();
			//Wenn das Lager Unterlager hat ist es ein Oberlager
			if (unterLager != null) {
				
				//Befüllung
				Vector<Object> tmpVector = new Vector<Object>();
				tmpVector.addElement("-");
				tmpVector.addElement(getEinrückung(tiefe)+lager.getName());
				tmpVector.addElement("");
				tmpVector.addElement("");
				tableData.addElement(tmpVector);
				int indexErstesUnterelement=tableData.indexOf(tmpVector)+1;
				//Befüllung der HashMap mit der Zuordnung von Lagernamen zu Lagern
				fülleHashmap(LagerNameZuLager, lager.getName(), lager);
				//Damit bei jedem Update der Tabelle nicht der Status aufgeklppt oder zugeklappt verlohren geht, wird die HashMap nur befüllt wenn zu dem Schlüssel noch nichts drinnen steht
				if(!LagerZuklappen.containsKey(lager))
				{
					LagerZuklappen.put(lager, false);
				}
				//rekursiver Neuaufruf der Methode
				int[] UnterlagerBestandUndKapazität=fülleTabellenDaten(unterLager, tiefe + 1);
				//Aufsummierung der Kapazitäts- und Bestandswerte
				gesamtBestandUndKapazität[0]+=UnterlagerBestandUndKapazität[0];
				gesamtBestandUndKapazität[1]+=UnterlagerBestandUndKapazität[1];
				//Eintrag der Werte in die Tabelle
				tmpVector.set(2,UnterlagerBestandUndKapazität[0]);
				tmpVector.set(3,UnterlagerBestandUndKapazität[1]);
				//Wenn das Lager zugeklappt ist und nicht in der Tabelle auftauchen soll, werden alle darunterliegenden Lager (Vektoren bis zum Vektor des Oberlagers) gelöscht
				if(LagerZuklappen.get(lager))
				{
					int indexLetztesUnterelement=tableData.indexOf((tableData.lastElement()));
					for(int j=indexErstesUnterelement;j<=indexLetztesUnterelement;j++)
					tableData.remove(indexErstesUnterelement);
					tableData.lastElement().set(0,"+");
				}
			}// Wenn das Lager eine Lagerhalle ist, werden die Werte aus der Lagerhalle geholt und in den Vector geschrieben
			else {
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

	/**
	 * Diese Methode befüllt die HashMap mit der Zuordnung von Lagernamen zu Lagern
	 */
	public void fülleHashmap(HashMap<String, Model.Lager> map, String key, Model.Lager value) {
		map.put(key, value);
	}
	/**
	 * Die Methode bestimmt die Anzahl an Einrückungsschritten entsprechend der Tiefe des Lagers in der Hierarchie
	 * @param anzahl Hierarchieebene
	 * @return Der String der Einrückung
	 */
	public String getEinrückung(int anzahl)
	{
		String rückgabe="";
		for(int i=0;i<anzahl;i++)
			rückgabe+="   ";
		return rückgabe;
	}
	/**
	 * Die Methode setzt die Größe der Tabelle entsprechend zum Inhalt
	 * @param tmpTable Tabelle die angepasst werden soll
	 * @return Die Gesamtgröße der Tabelle als Dimension
	 */
	public Dimension setTableSize(JTable tmpTable)
	{
		
		Dimension tableDimension= new Dimension(0, 0);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		//Spalten der Tabelle
		for(int spalte=0;spalte< tmpTable.getColumnCount(); spalte++)
		{
			TableColumn tableColumn= tmpTable.getColumnModel().getColumn(spalte);
			Dimension zellenDimension= new Dimension(0,0);
			tmpTable.setIntercellSpacing(new Dimension(5, 0) );
			//Headergröße wird bestimmt
			Object headerValue = tableColumn.getHeaderValue();
			TableCellRenderer renderer = tableColumn.getHeaderRenderer();

			if (renderer == null)
			{
				renderer = tmpTable.getTableHeader().getDefaultRenderer();
			}

			Component c = renderer.getTableCellRendererComponent(tmpTable, headerValue, false, false, -1, spalte);
			double rowHeight = c.getPreferredSize().height+tmpTable.getIntercellSpacing().height;
			tmpTable.setRowHeight((int) rowHeight);
			zellenDimension.setSize(c.getPreferredSize().width+tmpTable.getIntercellSpacing().width,LINIEGRÖßE+rowHeight+LINIEGRÖßE);
			//Zeilen der Tabelle- Zellengröße wird bestimmt
			for(int zeile=0; zeile <tmpTable.getRowCount();zeile++)
			{
				TableCellRenderer cellRenderer = tmpTable.getCellRenderer(zeile, spalte);
		        Component comp = tmpTable.prepareRenderer(cellRenderer, zeile, spalte);
		        Dimension tmpZellenDimension= new Dimension(0, 0);
		        tmpZellenDimension.setSize(comp.getPreferredSize().width + tmpTable.getIntercellSpacing().width,comp.getPreferredSize().height + tmpTable.getIntercellSpacing().height);
		        if(rowHeight<tmpZellenDimension.getHeight())
		        {
		        	tmpTable.setRowHeight((int) tmpZellenDimension.getHeight());
		        }
		        zellenDimension.setSize((Math.max(zellenDimension.getWidth(), tmpZellenDimension.getWidth())),zellenDimension.getHeight()+ tmpZellenDimension.getHeight()+LINIEGRÖßE);
			}
			tableColumn.setPreferredWidth((int) (zellenDimension.getWidth()));
			tableDimension.setSize((tableDimension.getWidth()+zellenDimension.getWidth()+LINIEGRÖßE),Math.max(tableDimension.getHeight(),zellenDimension.getHeight())+LINIEGRÖßE);
		}
		tmpTable.setSize(tableDimension);
		System.out.println("berechnete Table Height"+tableDimension.getHeight());
		return tableDimension;	
	}
	/**
	 * Diese Methode liefert die Höhe der Panels(Ausgenommen die des Tablepanels, da dieses schon mit setTableSize() ermittelt wurde)
	 *@return die gesamthöhe der Panels
	 */
	private double getPanelHeight()
	{
		double gesamtHöhe=0;
		gesamtHöhe+=LINIEGRÖßE;
		gesamtHöhe=Math.max(titel.getPreferredSize().getHeight(), menu.getPreferredSize().getHeight());
		titel.setSize(titel.getPreferredSize());
		gesamtHöhe+=LINIEGRÖßE;
		gesamtHöhe+=LINIEGRÖßE;
		gesamtHöhe+=LINIEGRÖßE;
		gesamtHöhe+=Math.max(kapazität.getPreferredSize().getHeight(), bestand.getPreferredSize().getHeight());
		gesamtHöhe+=LINIEGRÖßE;
		gesamtHöhe+=LINIEGRÖßE*3;
		
		System.out.println("gesamtpanelhöhe"+gesamtHöhe);
		return gesamtHöhe;
	}
}


