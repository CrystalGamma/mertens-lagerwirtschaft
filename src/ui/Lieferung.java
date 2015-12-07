package ui;

import model.Model;
import model.TransaktionsFehler;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.*;
import java.util.function.Function;

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

	private int lieferungsMenge = 1;

	private void addHalle(Model m, Model.LagerHalle halle) {
		buchungen.put(halle, 1);
		reihenfolge.add(halle);
		rerender(m);
	}

	private void rerender(Model m) {
		getContentPane().removeAll();
		LagerTree tree = new LagerTree(m);
		add(tree, BorderLayout.WEST);
		Panel p = new Panel();
		LayoutManager layout = new BoxLayout(p, BoxLayout.PAGE_AXIS);
		p.setLayout(layout);
		add(p, BorderLayout.EAST);
		p.add(new JLabel(strategy.toString()));
		Panel undoRedo = new Panel();
		p.add(undoRedo);
		int verteilteMenge = 0;
		JTextField datum = new JTextField();
		if (reihenfolge.size() == 0) {
			Panel form = new Panel();
			form.add(new JLabel("Menge"), BorderLayout.WEST);
			JSpinner menge = new JSpinner(new SpinnerNumberModel(1, 1, null, 1));
			menge.addChangeListener(ev -> {lieferungsMenge = (Integer)menge.getValue();});
			form.add(menge);
			p.add(form);
			tree.geklickteLager.addObserver((stream, halle_) -> addHalle(m, (Model.LagerHalle)halle_));
		} else {
			p.add(new JLabel("Menge: " + lieferungsMenge));
			Model.LagerHalle aktuelleHalle = reihenfolge.lastElement();
			for (Model.LagerHalle halle : reihenfolge) {
				if (halle == aktuelleHalle)
					break;
				int teilmenge = buchungen.get(halle);
				p.add(new JLabel(halle.getName() + ": " + teilmenge + "(" + ((float) teilmenge / (float) lieferungsMenge * 100) + "%)"));
				verteilteMenge += teilmenge;
			}
			Panel sliderPanel = new Panel();
			JSlider slider = new JSlider(1, lieferungsMenge - verteilteMenge, buchungen.get(aktuelleHalle));
			JLabel sliderLabel = new JLabel(aktuelleHalle.getName() + ": 1 (" + (100.0f / lieferungsMenge) + "%)");
			sliderPanel.add(sliderLabel);
			sliderPanel.add(slider);
			p.add(sliderPanel);
			JButton commit = new JButton("Übernehmen");
			p.add(commit);
			commit.addActionListener(ev -> {
				try {
					m.übernehmeLieferung(buchungen, datum.getText());
					dispose();
				} catch (TransaktionsFehler e) {
					JOptionPane.showMessageDialog(this, e.getMessage());
				}
			});
			commit.setEnabled(buchungen.get(aktuelleHalle) + verteilteMenge >= lieferungsMenge);
			final int vertMenge = verteilteMenge;
			ChangeListener slListener = ev -> {
				int value = slider.getValue();
				buchungen.put(aktuelleHalle, value);
				sliderLabel.setText(aktuelleHalle.getName() + ": " + value + " (" + ((float)value / lieferungsMenge * 100) + "%)");
				commit.setEnabled(buchungen.get(aktuelleHalle) + vertMenge >= lieferungsMenge);
			};
			slListener.stateChanged(null);
			slider.addChangeListener(slListener);
			tree.geklickteLager.addObserver((_dummy, halle_) -> {
				if (buchungen.get(aktuelleHalle) + vertMenge < lieferungsMenge)
					addHalle(m, (Model.LagerHalle)halle_);
			});
		}
		pack();
		repaint();
	}

	@Override
	public void update(Observable o, Object arg) {
		Model m = (Model)o;
		rerender(m);
	}
}
