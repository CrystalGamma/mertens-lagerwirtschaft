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
}
