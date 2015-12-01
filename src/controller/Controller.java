package controller;

import model.Model;
import model.Model.LagerHalle;
import ui.*;
import ui.Lieferung;

public class Controller {
static Model model;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Controller controler = new Controller();
		model = new Model();
		StartAnsicht startansicht = new StartAnsicht(model,controler );
	}

	public void öffneAlleBuchungen() {
		AlleBuchungen alleBuchungen= new AlleBuchungen(model);
	}

	public void öffneZulieferung() {
		Lieferung zulieferung = new Lieferung(model);
	}

	public void öffneLagerX(LagerHalle lager) {
		LagerAnsicht lagerAnsicht = new LagerAnsicht(model, lager);

	}

}
