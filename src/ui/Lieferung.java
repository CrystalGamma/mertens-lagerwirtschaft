package ui;

import model.Model;
import model.Model.LagerHalle;
import utils.Stream;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.*;

public class Lieferung extends JFrame implements Observer {
	private final Map<LagerHalle, Integer> buchungen = new HashMap<>();
	private final Vector<LagerHalle> reihenfolge = new Vector<>(), redo = new Vector<>();
	private final Strategy strategy;
	public final Observable commitment = new Stream();

	/** Event für das Observable: Nutzer möchte eine Lieferung übernehmen */
	public static class Commitment {
		final public Map<LagerHalle, Integer> buchungen;
		final public String datum;

		private Commitment(Map<LagerHalle, Integer> b, String d) {buchungen = b; datum = d;}
	}

	public interface Strategy {
		/** wandelt die Liefermenge in den entsprechenden Wert für die Buchung um */
		int buchungsWert(int menge);
		/** gibt die maximale Liefermenge für ein Lager nach dieser Strategy an */
		int maxWert(LagerHalle halle);
		/** gibt den Namen der Strategy */
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

	/** fügt der Lieferung ein neues betroffenes Lager hinzu */
	private void addHalle(Model m, Model.LagerHalle halle) {
		buchungen.put(halle, 1);
		reihenfolge.add(halle);
		redo.clear();
		rerender(m);
	}

	/** fügt einem Container ein Widget zur Auswahl einer Menge hinzu */
	private void mengenAuswahl(Model m, LagerTree tree, Container p) {
		Panel form = new Panel();
		form.add(new JLabel("Menge"), BorderLayout.WEST);
		JSpinner menge = new JSpinner(new SpinnerNumberModel(1, 1, null, 1));
		menge.addChangeListener(ev -> {lieferungsMenge = (Integer)menge.getValue();});
		form.add(menge);
		p.add(form);
		tree.geklickteLager.addObserver((stream, halle_) -> addHalle(m, (LagerHalle)halle_));
	}

	/** rendert den Inhalt (z. B. nach Änderung im Model) erneut */
	private void rerender(Model m) {
		getContentPane().removeAll();
		LagerTree tree = new LagerTree(m);
		add(tree, BorderLayout.WEST);
		Panel p = new Panel();
		LayoutManager layout = new BoxLayout(p, BoxLayout.PAGE_AXIS);
		p.setLayout(layout);
		add(p, BorderLayout.CENTER);
		p.add(new JLabel(strategy.toString()));
		undoRedoButtons(m, p);
		JTextField datumField = new JTextField();
		datumField.addActionListener(ev -> {datum = datumField.getText();});
		JPanel datumPanel = new JPanel();
		datumPanel.add(new JLabel("Datum"), BorderLayout.WEST);
		datumPanel.add(datumField, BorderLayout.EAST);
		if (reihenfolge.size() == 0) {
			mengenAuswahl(m, tree, p);
		} else {
			verteilung(m, tree, p);
		}
		pack();
		repaint();
	}

	/** Fügt die Undo/Redo Buttons zu einem Container hinzu */
	private void undoRedoButtons(Model m, Container panel) {
		Panel undoRedo = new Panel();
		if (reihenfolge.size() > 0) {
			addButton(undoRedo, "Rückgängig", ev -> {
				redo.add(reihenfolge.lastElement());
				reihenfolge.setSize(reihenfolge.size() - 1);
				rerender(m);
			});
		}
		if (redo.size() > 0) {
			addButton(undoRedo, "Wiederherstellen", ev -> {
				reihenfolge.add(redo.lastElement());
				redo.setSize(redo.size() - 1);
				rerender(m);
			});
		}
		panel.add(undoRedo);
	}

	/** fügt einen Button mit gegebener Beschriftung und ActionListener zu einem Panel hinzu */
	private void addButton(Container panel, String label, ActionListener actionListener) {
		JButton redoButton = new JButton(label);
		redoButton.addActionListener(actionListener);
		panel.add(redoButton);
	}

	/** fügt die Verteilungsansicht der Lieferung einem Container hinzu */
	private void verteilung(Model m, LagerTree tree, Container panel) {
		panel.add(new JLabel("Menge: " + lieferungsMenge));
		int verteilteMenge = 0;
		LagerHalle aktuelleHalle = reihenfolge.lastElement();
		for (LagerHalle halle : reihenfolge) {
			if (halle == aktuelleHalle)
				break;
			int teilmenge = buchungen.get(halle);
			panel.add(new JLabel(halle.getName() + ": " + teilmenge + "(" + ((float) teilmenge / (float) lieferungsMenge * 100) + "%)"));
			verteilteMenge += teilmenge;
		}
		final int vertMenge = verteilteMenge;
		addSliderPanel(panel, verteilteMenge, aktuelleHalle);
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

	/** fügt ein Widget zur Auswahl des zu vergebenden Anteils einem Container hinzu */
	private void addSliderPanel(Container panel, final int verteilteMenge, LagerHalle halle) {
		Panel sliderPanel = new Panel();
		JSlider slider = new JSlider(1, Math.min(lieferungsMenge - verteilteMenge, strategy.maxWert(halle)), buchungen.get(halle));
		JLabel sliderLabel = new JLabel(halle.getName() + ": 1 (" + (100.0f / lieferungsMenge) + "%)");
		sliderPanel.add(sliderLabel);
		sliderPanel.add(slider);
		panel.add(sliderPanel);
		JButton commit = new JButton("Übernehmen");
		panel.add(commit);
		commit.addActionListener(ev -> commit());
		commit.setEnabled(buchungen.get(halle) + verteilteMenge >= lieferungsMenge);
		ChangeListener slListener = ev -> {
			int value = slider.getValue();
			buchungen.put(halle, value);
			sliderLabel.setText(halle.getName() + ": " + value + " (" + ((float)value / lieferungsMenge * 100) + "%)");
			commit.setEnabled(buchungen.get(halle) + verteilteMenge >= lieferungsMenge);
		};
		slListener.stateChanged(null);
		slider.addChangeListener(slListener);
	}

	/** aktuelle Verteilung wird übernommen */
	private void commit() {
		Map<LagerHalle, Integer> lieferung = new HashMap<>();
		for (LagerHalle hall: reihenfolge) {lieferung.put(hall, strategy.buchungsWert(buchungen.get(hall)));}
		((Stream)commitment).push(new Commitment(lieferung, datum));
	}

	@Override
	public void update(Observable o, Object arg) {
		Model m = (Model)o;
		buchungen.forEach((halle, wert) -> buchungen.put(halle, Math.min(wert, strategy.maxWert(halle))));
		rerender(m);
	}
}
