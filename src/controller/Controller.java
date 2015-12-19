package controller;

import model.Model;
import model.Model.Lager;
import model.Model.LagerHalle;
import model.Model.OberLager;
import ui.AlleBuchungen;
import ui.LagerAnsicht;
import ui.LieferungDatum;
import ui.StartAnsicht;
import ui.LagerNamensänderung;
import utils.Utils;

import java.util.*;

/**
 * Diese Klasse ist der zentrale Controller, der alle Klassen erzeugt. Darüber hinaus nimmt er alle Ändeurngen an den Views über den Stream Observable an und führt die FOlge Aktionen aus.
 * @author Leon Westhof
 *
 */
public class Controller {
    Model model;
/**
 * KOnstruktor erzeugt Model und Startasicht. Er fügt die Startansicht als Observer dem Modell hinzu, sowie weitere Aktionen der Startansicht als Observer
 */
    public Controller() {
        this.model = new Model();
        startDaten();
        StartAnsicht startAnsicht=new StartAnsicht(this.model);
        model.addObserver(startAnsicht);
        startAnsicht.ÖffneLagerX.addObserver((dummy, lager)->öffneLagerX((Model.Lager) lager));
        startAnsicht.öffneAlleBuchungen.addObserver((dummy,dummy2)->öffneAlleBuchungen());
        startAnsicht.öffneAuslieferung.addObserver((dummy,dummy2)->öffneAuslieferung());
        startAnsicht.öffneZulieferung.addObserver((dummy,dummy2)->öffneZulieferung());
        startAnsicht.ändereLagerName.addObserver((dummy,lagerNamensänderung)->ändereLagerName(((LagerNamensänderung)lagerNamensänderung).getNeuerLagerName(), ((LagerNamensänderung)lagerNamensänderung).getLager()));
    }

    public static void main(String[] args) {
        Controller controller = new Controller();
    }

	public void startDaten() {
		final Lager[] lager = model.getLager();
		final Lager[] ger = lager[0].getUnterLager();
		LagerHalle bremen = (LagerHalle) ger[2];
		assert bremen.getName().equals("Bremen");
		LagerHalle mv = (LagerHalle) ger[6];
		assert mv.getName().equals("MV");
		Lager[] eur = lager[1].getUnterLager();
		final Lager[] it = eur[1].getUnterLager();
		LagerHalle mailand = (LagerHalle) it[0];
		assert mailand.getName().equals("Mailand");
		LagerHalle es = (LagerHalle)eur[2];
		assert es.getName().equals("Spanien");
		LagerHalle gb = (LagerHalle) lager[2];
		assert gb.getName().equals("Großbritannien");
		Map<LagerHalle, Integer> lief1 = new HashMap<>(8), lief2 = new HashMap<>(8),
			lief3 = new HashMap<>(8), lief4 = new HashMap<>(8), lief5 = new HashMap<>();
		lief1.put(bremen, 500);
		lief1.put(mv, 200);
		lief1.put(mailand, 100);
		lief1.put(es, 100);
		lief1.put(gb, 100);
		model.übernehmeLieferung(lief1, "2015-12-14");
		Lager[] nds = ger[0].getUnterLager();
		LagerHalle nienburg = (LagerHalle) nds[1];
		assert nienburg.getName().equals("Nienburg");
		LagerHalle nrw = (LagerHalle) ger[1];
		assert nrw.getName().equals("NRW");
		LagerHalle hessen = (LagerHalle) ger[3];
		assert hessen.getName().equals("Hessen");
		LagerHalle sachsen = (LagerHalle) ger[4];
		assert sachsen.getName().equals("Sachsen");
		lief2.put(nienburg, 1000);
		lief2.put(nrw, 400);
		lief2.put(hessen, 200);
		lief2.put(sachsen, 200);
		model.übernehmeLieferung(lief2, "2015-12-15");
		LagerHalle brandenburg = (LagerHalle) ger[5];
		assert brandenburg.getName().equals("Brandenburg");
		Lager[] fr = eur[0].getUnterLager();
		LagerHalle orleans = (LagerHalle) fr[1];
		assert orleans.getName().equals("Orléans");
		LagerHalle laquila = (LagerHalle) it[1];
		assert laquila.getName().equals("L'Aquila");
		lief3.put(brandenburg, 2000);
		lief3.put(orleans, 1000);
		lief3.put(laquila, 2500);
		lief3.put(es, 2500);
		lief3.put(gb, 2000);
		model.übernehmeLieferung(lief3, "2015-12-16");
		LagerHalle nimes = (LagerHalle) fr[3];
		assert nimes.getName().equals("Nîmes");
		lief4.put(nimes, 2500);
		lief4.put(mv, 2000);
		lief4.put(nienburg, 500);
		model.übernehmeLieferung(lief4, "2015-12-17");
		LagerHalle paris = (LagerHalle) fr[0];
		LagerHalle hannover = (LagerHalle) nds[0];
		lief5.put(paris, 3750);
		lief5.put(brandenburg, 2500);
		lief5.put(hannover, 1875);
		lief5.put(bremen, 1875);
		lief5.put(mailand, 2500);
		model.übernehmeLieferung(lief5, "2015-12-18");
	}

    public void generateDummyData() {
        Map<Model.LagerHalle, Integer> buchung;
        Set<LagerHalle> lagerHallen = new HashSet<>();

        int day = 1;
        int amount = 15;
        for (Model.Lager lager : model.getLager()) {
            if (lager instanceof OberLager) {
                lagerHallen.addAll(Utils.getLagerHallen((OberLager) lager));
            } else if(lager instanceof  LagerHalle) {
                lagerHallen.add((Model.LagerHalle) lager);
            }
        }


        for (LagerHalle lagerHalle : lagerHallen) {
            buchung = new HashMap<>();
            buchung.put(lagerHalle, +amount);
            model.übernehmeLieferung(buchung, "2015-10-" + (day < 10 ? "0" + day : day));
            day++;
            amount++;
        }

        amount = -5;
        day = 1;
        for (LagerHalle lagerHalle : lagerHallen) {
            buchung = new HashMap<>();
            buchung.put(lagerHalle, amount);
            model.übernehmeLieferung(buchung, "2015-11-" + (day < 10 ? "0" + day : day));
            day++;
            amount--;
        }
    }
/**
 * Gibt das Model zurück
 * @return das Model
 */
    public Model getModel() {
        return this.model;
    }
/**
 * Die Methode öffnet alle Buchungen des Systems
 */
    public void öffneAlleBuchungen() {
        AlleBuchungen alleBuchungen = AlleBuchungen.getInstance();
        alleBuchungen.update(this.getModel(), null);
        alleBuchungen.geklicktesDatum.addObserver((view, value) -> {
            if (value instanceof String)
                this.öffneLieferung(value.toString());
        });
    }
/**
 * Erstellt den Lieferungskontroller für die Auslieferung
 */
    public void öffneAuslieferung() {
    	LieferungController lieferungController= new LieferungController(model, ui.Lieferung.AUSLIEFERUNG);
    }
/**
 * Erstellt den Lieferungskontroller für die Zulierferung
 */
    public void öffneZulieferung() {
        LieferungController lieferungController= new LieferungController(model, ui.Lieferung.ZULIEFERUNG);
    }
/**
 * Die Methode erzeugt eine ANsicht auf das Übergebene Lager
 * @param lager Das zu öffnende Lager
 */
    public void öffneLagerX(Lager lager) {
        LagerAnsicht lagerAnsicht = new LagerAnsicht(lager);
        lagerAnsicht.update(this.getModel(), lager);
        lagerAnsicht.geklicktesDatum.addObserver((view, value) -> {
            if (value instanceof String)
                this.öffneLieferung(value.toString());
        });
        model.addObserver(lagerAnsicht);
    }
/**
 * Die Methode ändert den Namen eines bestimmten Lagers
 * @param neuerName neuer Name des Lagers
 * @param lager Lager, dessen Namen geändert werden soll
 */
    public void ändereLagerName(String neuerName, Lager lager) {
        lager.setName(neuerName);
    }
/**
 * Die Methode öffnet eine Lieferung eines bestimmten Datums
 * @param datum Das Datum an dem die Lieferungen gemacht wurden
 */
    public void öffneLieferung(String datum) {
        LieferungDatum lieferungDatum = new LieferungDatum(datum);
        lieferungDatum.update(this.getModel(), datum);
        lieferungDatum.geklicktesLager.addObserver((view, value) -> {
            if (value instanceof Model.LagerHalle)
                this.öffneLagerX((Model.LagerHalle) value);
        });
        model.addObserver(lieferungDatum);
    }
}
