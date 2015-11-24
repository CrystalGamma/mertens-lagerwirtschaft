package model;

public class LagerHalle extends Lager {
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

	public void buchen(int änderung) throws  LagerNichtVollGenug, LagerÜbervoll {
		dryRunBuchung(änderung);
		bestand += änderung;
	}
}
