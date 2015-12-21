import model.*;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class ModelTests {
	@Test
	public void transaktion() {
		Model model = new Model();
		model.getLieferungen();
		Model.LagerHalle halle1 = (Model.LagerHalle)model.getLager()[0].getUnterLager()[0].getUnterLager()[0];
		assert halle1.getBestand() == 0;
		Map<Model.LagerHalle, Integer> buchungen = new HashMap<>();
		buchungen.put(halle1, 10);
		model.übernehmeLieferung(buchungen, "2015-11-24");
		try {
			model.übernehmeLieferung(buchungen, "2015-11-24");
		} catch (Exception e) {
			assert e instanceof LieferungExistiert;
		}
		assert halle1.getBestand() == 10;
		assert model.getBuchungenFürHalle(halle1).get("2015-11-24").get(halle1) == 10;
		Map<Model.LagerHalle, Integer> ungültig = new HashMap<>();
		ungültig.put(halle1, -15);
		boolean noexcept = true;
		try {
			model.übernehmeLieferung(ungültig, "2015-11-25");
		} catch (LagerNichtVollGenug e) {
			noexcept = false;
		}
		assert !noexcept;
		assert halle1.getBestand() == 10;
		model.checkLieferung(buchungen, "2035-06-25");
		noexcept = true;
		try {
			model.checkLieferung(buchungen, "2035-0-1");
		} catch (UngültigesDatum üd) {
			noexcept = false;
		}
		assert !noexcept;

		// IndexOutOfBoundsException
		noexcept = true;
		try {
			model.checkLieferung(buchungen, "2010");
		} catch (UngültigesDatum e) {
			noexcept = false;
		}
		assert !noexcept;

		// Wirklich ungültiges Datum
		noexcept = true;
		try {
			model.checkLieferung(buchungen, "2015-13-01");
		} catch (UngültigesDatum e) {
			noexcept = false;
		}
		assert !noexcept;

		// LagerÜbervoll
		noexcept = true;
		try {
			buchungen.put(halle1, halle1.getKapazität()+1);
			model.checkLieferung(buchungen, "2015-13-01");
		} catch (LagerÜbervoll e) {
			noexcept = false;
		}
		assert !noexcept;

		noexcept = true;
		try {
			model.checkLieferung(new HashMap<>(), "1994-12-14");
		} catch (LieferungZuFrüh e) {
			noexcept = false;
		}
		assert !noexcept;
	}

	@Test
	public void OberLager() {
		Model model = new Model();
		Model.OberLager deutschland = (Model.OberLager)model.getLager()[0];
		assert deutschland.toString().equals("Deutschland");
		deutschland.setName("Neuer Name");
		assert deutschland.getName().equals("Neuer Name");
		assert deutschland.getBestand() == 0;
		assert deutschland.getKapazität() == 400000;
	}

	@Test
	public void LagerHalle() {
		Model model = new Model();
		Model.LagerHalle nienburg = (Model.LagerHalle)model.getLager()[0].getUnterLager()[0].getUnterLager()[1];
		assert nienburg.toString().equals("Nienburg");
		nienburg.setName("Neuer Name");
		assert nienburg.getName().equals("Neuer Name");
		assert nienburg.getBestand() == 0;
		assert nienburg.getKapazität() == 50000;
		assert nienburg.getUnterLager() == null;
	}
}
