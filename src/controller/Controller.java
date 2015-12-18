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
        this.generateDummyData();
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
        AlleBuchungen alleBuchungen = AlleBuchungen.getInstance(this.model);
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
        LagerAnsicht lagerAnsicht = new LagerAnsicht(this.model, lager);
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
        LieferungDatum lieferungDatum = new LieferungDatum(this.model, datum);
        lieferungDatum.update(this.getModel(), datum);
        lieferungDatum.geklicktesLager.addObserver((view, value) -> {
            if (value instanceof Model.LagerHalle)
                this.öffneLagerX((Model.LagerHalle) value);
        });
        model.addObserver(lieferungDatum);
    }
}
