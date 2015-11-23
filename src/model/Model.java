package model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Model {
	private HashMap<String, Map<LagerHalle, Integer>> lieferungen;

	public Map<String, Map<LagerHalle, Integer>> getLieferungen() {
		return Collections.unmodifiableMap(lieferungen);
	}

	public Map.Entry<String, Map<LagerHalle, Integer>>[] getBuchungenFürHalle(LagerHalle halle) {
		return null;
	}
}
