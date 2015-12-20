/** @author Jona Stubbe */
package model;

import utils.Utils;

import java.util.*;

public class Model extends Observable {
    private Lager[] lager = new Lager[]{
            new OberLager("Deutschland",
                new Lager[]{new OberLager("Niedersachsen",
                        new LagerHalle[]{new LagerHalle("Hannover-Misburg", 50000), new LagerHalle("Nienburg", 50000)}),
                new LagerHalle("NRW", 50000), new LagerHalle("Bremen", 50000), new LagerHalle("Hessen", 50000), new LagerHalle("Sachsen", 50000), new LagerHalle("Brandenburg", 50000), new LagerHalle("MV", 50000)}),
            new OberLager("Europa", new Lager[]{
                    new OberLager("Frankreich",
                            new Lager[]{new LagerHalle("Paris-Nord", 50000), new LagerHalle("Orléans", 50000), new LagerHalle("Marseille", 50000), new LagerHalle("Nîmes", 50000)}),
                    new OberLager("Italien", new Lager[]{new LagerHalle("Mailand", 50000), new LagerHalle("L´Aquila", 50000)}),
                    new LagerHalle("Spanien", 50000)}),
            new LagerHalle("Großbritannien", 50000)};

	/**
	 *  alle bisherigen Lieferungen
	 *  für den Grund, warum die Lieferungen als Map&lt;LagerHalle, Integer&gt; dargestellt werden, siehe https://www.youtube.com/watch?v=o9pEzgHorH0 (insbesondere ab 17:30)
	 */
	private HashMap<String, Map<LagerHalle, Integer>> lieferungen = new HashMap<>();

	public Map<String, Map<LagerHalle, Integer>> getLieferungen() {
		return Collections.unmodifiableMap(lieferungen);
	}

	public Map<String, Map<LagerHalle, Integer>> getBuchungenFürHalle(LagerHalle halle) {
		return Utils.filterMap(lieferungen, (datum, buchungen) -> buchungen.containsKey(halle));
	}

	/** überprüfe, ob eine Lieferung übernommen werden kann, ansonsten werfe die passende Exception */
	public void checkLieferung(Map<LagerHalle, Integer> buchungen, String datum) {
		buchungen.forEach(LagerHalle::dryRunBuchung);
		try{
			int jear=Integer.valueOf(datum.substring(0,4));
			int month=Integer.valueOf(datum.substring(5,7));
			int day=Integer.valueOf(datum.substring(8,10));
			Calendar cal = Calendar.getInstance();
			cal.set(jear,month-1, day);
			if(jear!=cal.get(Calendar.YEAR)|month!=(cal.get(Calendar.MONTH)+1)|day!=cal.get(Calendar.DAY_OF_MONTH))
				throw new UngültigesDatum();
		} catch(IndexOutOfBoundsException e) {
			throw new UngültigesDatum();
		} catch(NumberFormatException nfe) {
			throw new UngültigesDatum();
		}
		if (lieferungen.containsKey(datum))
			throw new LieferungExistiert();
	}

	/** übernehme eine Lieferung; atomisch: änderungen werden entweder alle, oder gar nicht übernommen */
	public void übernehmeLieferung(Map<LagerHalle, Integer> buchungen, String datum) {
		buchungen = Collections.unmodifiableMap(buchungen);
		checkLieferung(buchungen, datum);
		buchungen.forEach(LagerHalle::buchen);
		lieferungen.put(datum, buchungen);
		setChanged();
		notifyObservers();
	}

	/** gibt alle Lager auf oberster Ebene zurück */
	public Lager[] getLager() {
		return Utils.arrayMap(Lager.class, lager, x -> x);
	}

	public interface Lager {
		String getName();
		int getKapazität();
		int getBestand();
		Lager[] getUnterLager();
		/** @return den Namen des Lagers */
		String toString();
		void setName(String name);
	}

	public class LagerHalle implements Lager {
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

		/** überprüfe, ob die Buchung auf dieser LagerHalle gemacht werden kann */
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

		/** führe Buchung auf dieser LagerHalle durch */
		void buchen(int änderung) throws  LagerNichtVollGenug, LagerÜbervoll {
			dryRunBuchung(änderung);
			bestand += änderung;
		}

		@Override
		public Lager[] getUnterLager() {
			return null;
		}

		@Override
		public String toString() {
			return this.name;
		}

		@Override
		public void setName(String name) {
			this.name=name;
			setChanged();
			notifyObservers(this);
		}
	}

	public class OberLager implements Lager {
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

		@Override
		public Lager[] getUnterLager() {
			return unterLager.clone();
		}

		@Override
		public String toString() {
			return this.name;
		}

		@Override
		public void setName(String name) {
			this.name=name;
			setChanged();
			notifyObservers();
		}
	}
}
