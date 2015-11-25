package controler;

import model.Model;
import model.Model.LagerHalle;
import ui.AlleBuchungen;
import ui.Auslieferung;
import ui.LagerAnsicht;
import ui.StartAnsicht;
import ui.Zulieferung;

public class Controler {
static Model model;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Controler controler = new Controler();
		model = new Model();
		StartAnsicht startansicht = new StartAnsicht(model,controler );
	}

	public void öffneAlleBuchungen() {
		AlleBuchungen alleBuchungen= new AlleBuchungen(model);
	}

	public void öffneAuslieferung() {
		Auslieferung auslieferung = new Auslieferung(model);
	}

	public void öffneZulieferung() {
		Zulieferung zulieferung = new Zulieferung(model);
	}

	public void öffneLagerX(LagerHalle lager) {
		LagerAnsicht lagerAnsicht = new LagerAnsicht(model, lager);

	}

}
