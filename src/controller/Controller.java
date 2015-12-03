package controller;

import model.Model;
import model.Model.Lager;
import model.Model.LagerHalle;
import model.Model.OberLager;
import ui.AlleBuchungen;
import ui.LagerAnsicht;
import ui.LieferungDatum;
import ui.StartAnsicht;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class Controller {
    Model model;

    public Controller() {
        this.model = new Model();
        //StartAnsicht startAnsicht =new StartAnsicht(this.model, this);
        this.generateDummyData();
        model.addObserver(new StartAnsicht(this.model, this));
    }

    public static void main(String[] args) {
        Controller controller = new Controller();
    }

    public void generateDummyData() {
        Map<Model.LagerHalle, Integer> buchung;
        Vector<LagerHalle> lagerHallen = new Vector<>();

        int day = 1;
        int amount = 15;
        for (Model.Lager tier1 : model.getLager()) {
            if (tier1 instanceof LagerHalle) {
                lagerHallen.add((LagerHalle) tier1);
                continue;
            }

            for (Model.Lager tier2 : tier1.getUnterLager()) {
                if (tier2 instanceof LagerHalle) {
                    lagerHallen.add((LagerHalle) tier2);
                    continue;
                }

                for (Model.Lager tier3 : tier2.getUnterLager())
                    if (tier3 instanceof LagerHalle)
                        lagerHallen.add((LagerHalle) tier3);
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

    public Model getModel() {
        return this.model;
    }

    public void öffneAlleBuchungen() {
        AlleBuchungen alleBuchungen = AlleBuchungen.getInstance();
        alleBuchungen.update(this.getModel(), null);
        alleBuchungen.geklicktesDatum.addObserver((view, value) -> {
            if (value instanceof String)
                this.öffneLieferung(value.toString());
        });
    }

    public void öffneAuslieferung() {
        //Auslieferung auslieferung = new Auslieferung(model);
        System.out.println("öffneAuslieferung");
    }

    public void öffneZulieferung() {
        //Zulieferung zulieferung = new Zulieferung(model);
        System.out.println("öffneZulieferung");
    }

    public void öffneLagerX(LagerHalle lager) {
        LagerAnsicht lagerAnsicht = new LagerAnsicht(lager);
        lagerAnsicht.update(this.getModel(), lager);
        lagerAnsicht.geklicktesDatum.addObserver((view, value) -> {
            if (value instanceof String)
                this.öffneLieferung(value.toString());
        });
        model.addObserver(lagerAnsicht);
    }

    public void ändereLagerName(String neuerName, Lager lager) {
        lager.setName(neuerName);
    }

    public void öffneLieferung(String datum) {
        LieferungDatum lieferungDatum = new LieferungDatum(datum);
        lieferungDatum.update(this.getModel(), datum);
        lieferungDatum.geklicktesLager.addObserver((view, value) -> {
            if (value instanceof Model.LagerHalle)
                this.öffneLagerX((Model.LagerHalle) value);
        });
        model.addObserver(lieferungDatum);
    }

    public void setZeigeUnterlager(OberLager oberLager) {
        oberLager.setZeigeUnterlager();
    }
}
