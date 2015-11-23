package model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Model {
	private HashMap<String, Map<LagerHalle, Integer>> lieferungen = new HashMap<>();

	public Map<String, Map<LagerHalle, Integer>> getLieferungen() {
		return Collections.unmodifiableMap(lieferungen);
	}

	public Map<String, Map<LagerHalle, Integer>> getBuchungenFürHalle(LagerHalle halle) {
		return Utils.filterMap(lieferungen, (datum, buchungen) -> buchungen.containsKey(halle));
	}

	public void übernehmeLieferung(Map<LagerHalle, Integer> buchungen, String datum) {
	}
}
