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

	private HashMap<String, Map<LagerView, Integer>> lieferungen = new HashMap<>();

	public Map<String, Map<LagerView, Integer>> getLieferungen() {
		return Collections.unmodifiableMap(lieferungen);
	}

	public Map<String, Map<LagerView, Integer>> getBuchungenFürHalle(LagerHalle halle) {
		return Utils.filterMap(lieferungen, (datum, buchungen) -> buchungen.containsKey(halle));
	}

	public void übernehmeLieferung(Map<LagerView, Integer> buchungen, String datum) {
		buchungen = Collections.unmodifiableMap(buchungen);
		buchungen.forEach((key, value) -> {	// <- VISITOR PATTERN!  ☺
			if (!(key.inner instanceof LagerHalle)) {
				throw new RuntimeException("Lager ist keine Halle");
			} else {
				((LagerHalle)key.inner).dryRunBuchung(value);
			}
		});
		buchungen.forEach((x, y) -> ((LagerHalle)x.inner).buchen(y));
		lieferungen.put(datum, buchungen);
		setChanged();
		notifyObservers();
	}

	// DECORATOR PATTERN ☺
	static final public class LagerView extends Lager {
		private final Lager inner;
		LagerView(Lager lager) {inner = lager;}

		@Override
		public int getBestand() {
			return inner.getBestand();
		}

		@Override
		public int getKapazität() {
			return inner.getKapazität();
		}

		@Override
		public String getName() {
			return inner.getName();
		}

		public LagerView[] getUnterLager() {
			if (inner instanceof OberLager) {
				return Utils.arrayMap(LagerView.class, ((OberLager) inner).getUnterLager(), LagerView::new);
			}
			return null;
		}
	}

	public LagerView[] getLager() {
		return Utils.arrayMap(LagerView.class, lager, x -> new LagerView(x));
	}
}
