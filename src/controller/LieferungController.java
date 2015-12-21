/** @author Jona Stubbe */
package controller;

import model.Model;
import model.TransaktionsFehler;
import ui.Lieferung;

import javax.swing.*;
import java.util.Observable;
import java.util.Observer;

public class LieferungController implements Observer {
	final private Model model;
	final private Lieferung view;

	public LieferungController(Model model, Lieferung.Strategy strategy) {
		this.model = model;
		view = new Lieferung(model, strategy);
		view.setVisible(true);
		view.commitment.addObserver(this);
	}

	/** Lieferung wird übernommen */
	public void update(Observable o, Object arg) {
		Lieferung.Commitment lieferung = (Lieferung.Commitment) arg;
		try {
			model.deleteObserver(view);	// nicht rerendern, sonst Fehler
			model.übernehmeLieferung(lieferung.buchungen, lieferung.datum);
			view.dispose();
		} catch (TransaktionsFehler e) {
			JOptionPane.showMessageDialog(view, e.getMessage());
			model.addObserver(view);
		}
	}

	public static void main(String[] args) {
		new LieferungController(new Model(), Lieferung.ZULIEFERUNG);
	}
}
