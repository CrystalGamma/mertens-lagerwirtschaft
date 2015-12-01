import model.LagerNichtVollGenug;
import model.LagerÜbervoll;
import model.Model;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class ModelTests {
	@Test
	public void transaktion() {
		Model model = new Model();
		Model.LagerHalle halle1 = (Model.LagerHalle)model.getLager()[0].getUnterLager()[0].getUnterLager()[0];
		assert halle1.getBestand() == 0;
		Map<Model.LagerHalle, Integer> buchungen = new HashMap<>();
		buchungen.put(halle1, 10);
		model.übernehmeLieferung(buchungen, "2015-11-24");
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
 	}
}
