import model.LagerHalle;
import org.junit.Test;

public class ModelTests {
	@Test
	public void lager() {
		LagerHalle halle = new LagerHalle(10);
		assert halle.getKapazität() == 10;
		assert halle.getBestand() == 0;
	}
}
