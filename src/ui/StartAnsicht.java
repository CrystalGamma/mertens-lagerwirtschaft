package ui;

import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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
import javax.swing.JTextField;

import model.Lager;
import model.LagerHalle;
import model.Model;
import model.OberLager;

public class StartAnsicht extends JFrame implements Observer {
	Model model;
	Vector<Vector<String>> tableData;
	Lager[] lager;
	public static void main(String[] args) {
		StartAnsicht frame= new StartAnsicht(new Model());
		frame.pack();
//		frame.setExtendedState(MAXIMIZED_BOTH);
		frame.setVisible(true);
	}
	public StartAnsicht(Model model) {
		this.model=model;
		//this.setLayout(new BoxLayout(getContentPane(),BoxLayout.Y_AXIS));
		JLabel titel= new JLabel("Lagerstruktur");
		JPanel tablePanel= new JPanel();
		tablePanel.setLayout(new BoxLayout(tablePanel, BoxLayout.Y_AXIS));
		Vector<String> columnNames= new Vector<>();
		columnNames.addElement("Lager");
		columnNames.addElement("Bestand");
		columnNames.addElement("Kapazität");
		//tableData= new String[][]{{"1","2","3"},{"1","2","3"}};
		tableData= new Vector<Vector<String>>();
		fülleTabellenDaten(model.getLager());
		JTable table = new JTable(tableData,columnNames);
		
		tablePanel.add(table.getTableHeader());
		tablePanel.add(table);
		JButton menu= new JButton("Menü");
		this.add(menu, BorderLayout.EAST);
		this.add(titel, BorderLayout.WEST);
		this.add(tablePanel, BorderLayout.SOUTH);
		JPopupMenu menuPopup= new JPopupMenu();
		JMenuItem menuItemRückgängig= new JMenuItem("Rückgängig");
		menuPopup.add(menuItemRückgängig);
		JMenuItem menuItemWiederholen= new JMenuItem("Wiederholen");
		menuPopup.add(menuItemWiederholen);
		JMenuItem menuItemZulieferung= new JMenuItem("Zulieferung");
		menuPopup.add(menuItemZulieferung);
		JMenuItem menuItemAuslieferung= new JMenuItem("Auslieferung");
		menuPopup.add(menuItemAuslieferung);
		JMenuItem menuItemAlleBuchungen= new JMenuItem("Alle Buchungen");
		menuPopup.add(menuItemAlleBuchungen);
		MouseListener popupListener = new MouseListener() {		
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				menuPopup.show(menu,0,0);
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
		};
		menu.addMouseListener(popupListener);
	}
	
	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		
	}
/*
 * 	
 */
	public void fülleTabellenDaten(Model.LagerView[] inputlager)
	{
		for(Model.LagerView lager : inputlager)
		{
			Model.LagerView[] unterLager = lager.getUnterLager();
			if(unterLager != null)
			{
				Vector<String> tmpVector= new Vector<String>();
				tmpVector.addElement(lager.getName());
				tableData.addElement(tmpVector);
				fülleTabellenDaten(unterLager);
			} else {
				Vector<String> tmpVector= new Vector<String>();
				tmpVector.addElement(lager.getName());
				tmpVector.addElement(lager.getBestand()+"");
				tmpVector.addElement(lager.getKapazität()+"");
				tableData.addElement(tmpVector); 
			}
		}
	}
}
