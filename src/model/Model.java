package model;

import java.util.*;

public class Model extends Observable {
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

	private HashMap<String, Map<LagerHalle, Integer>> lieferungen = new HashMap<>();

	public Map<String, Map<LagerHalle, Integer>> getLieferungen() {
		return Collections.unmodifiableMap(lieferungen);
	}

	public Map<String, Map<LagerHalle, Integer>> getBuchungenFürHalle(LagerHalle halle) {
		return Utils.filterMap(lieferungen, (datum, buchungen) -> buchungen.containsKey(halle));
	}

	public void übernehmeLieferung(Map<LagerHalle, Integer> buchungen, String datum) {
		buchungen = Collections.unmodifiableMap(buchungen);
		buchungen.forEach(LagerHalle::dryRunBuchung);	// <- VISITOR PATTERN!  ☺
		buchungen.forEach(LagerHalle::buchen);
		lieferungen.put(datum, buchungen);
		setChanged();
		notifyObservers();
	}

	public Lager[] getLager() {
		return Utils.arrayMap(Lager.class, lager, x -> x);
	}

	public interface Lager {
		String getName();
		int getKapazität();
		int getBestand();
		Lager[] getUnterLager();
	}

	public static class LagerHalle implements Lager {
		private String name;
		private int bestand;
		private int kapazität;

		public LagerHalle(String name, int kapazität) {
			this.name = name;
			this.kapazität = kapazität;
		}

		@Override
		public String getName() {
			return this.name;
		}

		@Override
		final public int getKapazität() {
			return this.kapazität;
		}

		@Override
		final public int getBestand() {
			return this.bestand;
		}

		public void dryRunBuchung(int änderung) throws  LagerNichtVollGenug, LagerÜbervoll{
			if (änderung > 0) {
				if (bestand + änderung < bestand || bestand + änderung > kapazität) {
					throw new LagerÜbervoll();
				}
			} else {
				if (-bestand > änderung) {
					throw new LagerNichtVollGenug();
				}
			}
		}

		protected void buchen(int änderung) throws  LagerNichtVollGenug, LagerÜbervoll {	// protected um besser die Klasse einzeln testen zu können
			dryRunBuchung(änderung);
			bestand += änderung;
		}

		@Override
		public Lager[] getUnterLager() {
			return null;
		}
	}

	public static class OberLager implements Lager {
		private String name;
		private Lager[] unterLager;

		public OberLager(String name, Lager[] unterLager){
			this.name = name;
			this.unterLager = unterLager;
		}

		@Override
		public String getName() {
			return this.name;
		}

		@Override
		public int getKapazität() {
			int kapazität = 0;
			for (Lager lagerHalle : unterLager) {
				kapazität += lagerHalle.getKapazität();
			}
			return kapazität;
		}

		@Override
		public int getBestand() {
			int bestand = 0;
			for (Lager lagerHalle : unterLager) {
				bestand += lagerHalle.getBestand();
			}
			return bestand;
		}

		public Lager[] getUnterLager() {
			return unterLager.clone();
		}
	}
}
