package ui;

import model.Model;
import model.Model.LagerHalle;
import utils.Stream;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.*;

public class Lieferung extends JFrame implements Observer {
	private final Map<LagerHalle, Integer> buchungen = new HashMap<>();
	private final Vector<LagerHalle> reihenfolge = new Vector<>(), redo = new Vector<>();
	private final Strategy strategy;
	public final Observable commitment = new Stream();

	public static class Commitment {
		final public Map<LagerHalle, Integer> buchungen;
		final public String datum;

		private Commitment(Map<LagerHalle, Integer> b, String d) {buchungen = b; datum = d;}
	}

	public interface Strategy {
		int buchungsWert(int menge);
		int maxWert(LagerHalle halle);
		String toString();
	}
	public static final Strategy AUSLIEFERUNG = new Strategy() {
		public int buchungsWert(int menge) {return -menge;}
		public int maxWert(LagerHalle halle) {return halle.getBestand();}
		public String toString() {return "Auslieferung";}
	};
	public static final Strategy ZULIEFERUNG = new Strategy() {
		public int buchungsWert(int menge) {return menge;}
		public int maxWert(LagerHalle halle) {return halle.getKapazität() - halle.getBestand();}
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
	private String datum = "";

	private void addHalle(Model m, Model.LagerHalle halle) {
		buchungen.put(halle, 1);
		reihenfolge.add(halle);
		redo.clear();
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
		if (reihenfolge.size() > 0) {
			JButton undo = new JButton("Rückgängig");
			undo.addActionListener(ev -> {
				redo.add(reihenfolge.lastElement());
				reihenfolge.setSize(reihenfolge.size() - 1);
				rerender(m);
			});
			undoRedo.add(undo);
		}
		if (redo.size() > 0) {
			JButton redoButton = new JButton("Wiederherstellen");
			redoButton.addActionListener(ev -> {
				reihenfolge.add(redo.lastElement());
				redo.setSize(redo.size() - 1);
				rerender(m);
			});
			undoRedo.add(redoButton);
		}
		p.add(undoRedo);
		int verteilteMenge = 0;
		JTextField datumField = new JTextField();
		datumField.addActionListener(ev -> {datum = datumField.getText();});
		JPanel datumPanel = new JPanel();
		datumPanel.add(new JLabel("Datum"), BorderLayout.WEST);
		datumPanel.add(datumField, BorderLayout.EAST);
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
			JSlider slider = new JSlider(1, Math.min(lieferungsMenge - verteilteMenge, strategy.maxWert(aktuelleHalle)), buchungen.get(aktuelleHalle));
			JLabel sliderLabel = new JLabel(aktuelleHalle.getName() + ": 1 (" + (100.0f / lieferungsMenge) + "%)");
			sliderPanel.add(sliderLabel);
			sliderPanel.add(slider);
			p.add(sliderPanel);
			JButton commit = new JButton("Übernehmen");
			p.add(commit);
			commit.addActionListener(ev -> {
				Map<Model.LagerHalle, Integer> lieferung = new HashMap<>();
				for (Model.LagerHalle halle: reihenfolge) {lieferung.put(halle, strategy.buchungsWert(buchungen.get(halle)));}
				((Stream)commitment).push(new Commitment(lieferung, datum));
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
				LagerHalle halle = (LagerHalle) halle_;
				if (strategy.maxWert(halle) < 1) {
					JOptionPane.showMessageDialog(this, "Diese Lieferung kann auf diesem Lager aufgrund von Bestand/Kapazität nicht ausgeführt werden");
					return;
				}
				if (buchungen.get(aktuelleHalle) + vertMenge < lieferungsMenge) {
					addHalle(m, halle);
				} else {
					JOptionPane.showMessageDialog(this, "Es wurde bereits die gesamte Menge der Lieferung verteilt");
				}
			});
		}
		pack();
		repaint();
	}

	@Override
	public void update(Observable o, Object arg) {
		Model m = (Model)o;
		buchungen.forEach((halle, wert) -> buchungen.put(halle, Math.min(wert, strategy.maxWert(halle))));
		rerender(m);
	}
}
