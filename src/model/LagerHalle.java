package model;

public class LagerHalle extends Lager {
	private int bestand;
	private int kapazität;

	@Override
	final public int getKapazität() {
		return 0;
	}

	@Override
	final public int getBestand() {
		return 0;
	}
}
