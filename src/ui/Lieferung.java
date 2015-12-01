package ui;

import model.Model;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.*;

public class Lieferung extends JFrame implements Observer {
	final Map<Model.LagerHalle, Integer> buchungen = new HashMap<>();
	final Vector<Model.LagerHalle> reihenfolge = new Vector<>();

	public Lieferung(Model model) {
		model.addObserver(this);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				model.deleteObserver(Lieferung.this);
			}
		});
		update(model, null);
	}

	@Override
	public void update(Observable o, Object arg) {
		Model m = (Model)o;
		getContentPane().removeAll();
		LagerTree tree = new LagerTree(m);
		tree.geklickteLager.addObserver((view, lager) -> System.out.println(lager));
		add(tree, BorderLayout.WEST);
		pack();
		repaint();
	}
}
