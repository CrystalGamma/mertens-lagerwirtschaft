package controller;

import model.Model;
import model.Model.LagerHalle;
import ui.*;
import ui.Lieferung;

public class Controller {
    Model model;

	public static void main(String[] args) {
		Controller controller = new Controller();
	}

    public Controller() {
        this.model = new Model();
        new StartAnsicht(this.model, this);
    }

    public Model getModel() {
        return this.model;
    }

	public void öffneAlleBuchungen() {
		AlleBuchungen alleBuchungen= new AlleBuchungen(this);
	}
	public void öffneAuslieferung() {
		//Auslieferung auslieferung = new Auslieferung(model);
		System.out.println("öffneAuslieferung");
	}

	public void öffneZulieferung() {
		Lieferung zulieferung = new Lieferung(model);
	}

	public void öffneLagerX(LagerHalle lager) {
		LagerAnsicht lagerAnsicht = new LagerAnsicht(this, lager);

	}
	public void ändereLagerName(String neuerName)
	{
		System.out.println(neuerName);
	}

}
