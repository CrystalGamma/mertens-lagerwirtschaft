import model.LagerHalle;
import model.LagerNichtVollGenug;
import model.LagerÜbervoll;
import org.junit.Test;

public class ModelTests {
	@Test
	public void lager() {
		LagerHalle halle = new LagerHalle("Halle", 10);
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
			halle.buchen(-10);
		} catch (LagerNichtVollGenug e) {
			noexcept = false;
		}
		if (noexcept)
			assert false;
		assert halle.getBestand() == 0;
		halle.dryRunBuchung(5);
		halle.buchen(5);
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
			halle.buchen(10);
		} catch (LagerÜbervoll e) {
			noexcept = false;
		}
		if (noexcept)
			assert false;
		assert halle.getBestand() == 5;
	}
}
