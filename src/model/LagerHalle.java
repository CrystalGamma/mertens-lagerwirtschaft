package model;

public class LagerHalle extends Lager {
	private int bestand;
	private int kapazität;

	public LagerHalle(int kapazität) {
		this.kapazität = kapazität;
	}

	@Override
	final public int getKapazität() {
		return kapazität;
	}

	@Override
	final public int getBestand() {
		return bestand;
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

	public void buchen(int änderung) throws  LagerNichtVollGenug, LagerÜbervoll {
		dryRunBuchung(änderung);
		bestand += änderung;
	}
}
