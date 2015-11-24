package model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Model {
    private Lager[] lager = new Lager[]{
            new OberLager("Deutschland",
                new Lager[]{new OberLager("Niedersachsen",
                        new LagerHalle[]{new LagerHalle("Hannover-Misburg", 50), new LagerHalle("Nienburg", 50)}),
                new LagerHalle("NRW", 50), new LagerHalle("Bremen", 50), new LagerHalle("Hessen", 50), new LagerHalle("Sachsen", 50), new LagerHalle("Brandenburg", 50), new LagerHalle("MV", 50)}),
            new OberLager("Europa", new Lager[]{
                    new OberLager("Frankreich",
                            new Lager[]{new LagerHalle("Paris-Nord", 50), new LagerHalle("Orléans", 50), new LagerHalle("Marseille", 50), new LagerHalle("Nîmes", 50)}),
                    new OberLager("Italien", new Lager[]{new LagerHalle("Mailand", 50), new LagerHalle("L´Aquila", 50)}),
                    new LagerHalle("Spanien", 50)}),
            new LagerHalle("Großbritannien", 50)};

    /*
     * String: Datum
     * Integer: Zulieferung
     */
	private HashMap<String, Map<LagerHalle, Integer>> lieferungen = new HashMap<>();

	public Map<String, Map<LagerHalle, Integer>> getLieferungen() {
		return Collections.unmodifiableMap(lieferungen);
	}

	public Map<String, Map<LagerHalle, Integer>> getBuchungenFürHalle(LagerHalle halle) {
		return Utils.filterMap(lieferungen, (datum, buchungen) -> buchungen.containsKey(halle));
	}

	
	public void übernehmeLieferung(Map<LagerHalle, Integer> buchungen, String datum) {
		buchungen.forEach(LagerHalle::dryRunBuchung);
		buchungen.forEach(LagerHalle::buchen);
	}
	public Lager[] getlager()
	{
		return lager;
	}
}
