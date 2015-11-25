import model.LagerNichtVollGenug;
import model.LagerÜbervoll;
import model.Model;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class ModelTests {
	static class Halle extends Model.LagerHalle {
		public Halle(String s, int i) {super(s, i);}
		public void buchen_(int x) {buchen(x);}
	}
	@Test
	public void lager() {
		Halle halle = new Halle("Halle", 10);
		assert halle.getKapazität() == 10;
		assert halle.getBestand() == 0;
		boolean noexcept = true;
		try {
			halle.dryRunBuchung(-10);
		} catch (LagerNichtVollGenug e) {
			noexcept = false;
		}
		if (noexcept)
			assert false;
		noexcept = true;
		try {
			halle.buchen_(-10);
		} catch (LagerNichtVollGenug e) {
			noexcept = false;
		}
		if (noexcept)
			assert false;
		assert halle.getBestand() == 0;
		halle.dryRunBuchung(5);
		halle.buchen_(5);
		assert halle.getBestand() == 5;
		noexcept = true;
		try {
			halle.dryRunBuchung(10);
		} catch (LagerÜbervoll e) {
			noexcept = false;
		}
		if (noexcept)
			assert false;
		noexcept = true;
		try {
			halle.buchen_(10);
		} catch (LagerÜbervoll e) {
			noexcept = false;
		}
		if (noexcept)
			assert false;
		assert halle.getBestand() == 5;
	}
	@Test
	public void transaktion() {
		Model model = new Model();
		Model.LagerHalle halle1 = (Model.LagerHalle)model.getLager()[0].getUnterLager()[0].getUnterLager()[0];
		Map<Model.LagerHalle, Integer> buchungen = new HashMap<>();
		buchungen.put(halle1, 10);
		model.übernehmeLieferung(buchungen, "2015-11-24");
		assert halle1.getBestand() == 10;
	}
}
