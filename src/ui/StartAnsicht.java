package ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import javax.sound.midi.ControllerEventListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTable;

import controler.Controler;
import model.Model;

public class StartAnsicht extends JFrame implements Observer {
	Model model;
	Vector<Vector<Object>> tableData;
	Model.Lager[] lager;

	public static void main(String[] args) {
		StartAnsicht frame = new StartAnsicht(new Model(), new Controler());
		frame.pack();
		// frame.setExtendedState(MAXIMIZED_BOTH);
		frame.setVisible(true);
	}

	public StartAnsicht(Model model, Controler controler) {
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
		JTable table = new JTable(tableData, columnNames);

		tablePanel.add(table.getTableHeader());
		tablePanel.add(table);
		JButton menu = new JButton("Menü");
		this.add(menu, BorderLayout.EAST);
		this.add(titel, BorderLayout.WEST);
		this.add(tablePanel, BorderLayout.SOUTH);
		JPopupMenu menuPopup = new JPopupMenu();
		JMenuItem menuItemRückgängig = new JMenuItem("Rückgängig");
		menuPopup.add(menuItemRückgängig);
		JMenuItem menuItemWiederholen = new JMenuItem("Wiederholen");
		menuPopup.add(menuItemWiederholen);
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
			public void mouseEntered(MouseEvent e) {}
			@Override
			public void mouseExited(MouseEvent e) {}
			@Override
			public void mousePressed(MouseEvent e) {}
			@Override
			public void mouseReleased(MouseEvent e) {}
		};
		//Menü bar action listener
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
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub

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
				fülleTabellenDaten(unterLager);
			} else {
				Vector<Object> tmpVector = new Vector<Object>();
				tmpVector.addElement(new String(""));
				tmpVector.addElement(lager.getName());
				tmpVector.addElement(lager.getBestand() + "");
				tmpVector.addElement(lager.getKapazität() + "");
				tableData.addElement(tmpVector);
			}
		}
	}
}
