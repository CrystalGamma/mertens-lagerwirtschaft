package model;

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

	public Lager[] getUnterLager() {
		return unterLager;
	}
}
