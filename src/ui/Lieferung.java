package ui;

import model.Model;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.*;

public class Lieferung extends JFrame implements Observer {
	private final Map<Model.LagerHalle, Integer> buchungen = new HashMap<>();
	private final Vector<Model.LagerHalle> reihenfolge = new Vector<>();
	private final Strategy strategy;

	private interface Strategy {
		int buchungsWert(int menge);
		String toString();
	}
	public static final Strategy AUSLIEFERUNG = new Strategy() {
		public int buchungsWert(int menge) {return -menge;}
		public String toString() {return "Auslieferung";}
	};
	public static final Strategy ZULIEFERUNG = new Strategy() {
		public int buchungsWert(int menge) {return menge;}
		public String toString() {return "Zulieferung";}
	};

	public Lieferung(Model model, Strategy strategy) {
		this.strategy = strategy;
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
		Panel p = new Panel();
		LayoutManager layout = new BoxLayout(p, BoxLayout.PAGE_AXIS);
		p.setLayout(layout);
		add(p, BorderLayout.EAST);
		p.add(new JLabel(strategy.toString()));
		Panel form = new Panel();
		form.add(new JLabel("Menge"), BorderLayout.WEST);
		JSpinner menge = new JSpinner(new SpinnerNumberModel(1, 1, null, 1));
		form.add(menge);
		p.add(form);
		pack();
		repaint();
	}
}
