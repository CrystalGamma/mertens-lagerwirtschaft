package model;

public class OberLager extends Lager {
	private Lager[] unterLager;

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
}
