package model;

public class OberLager extends Lager {
	private Lager[] unterLager;

	@Override
	public int getKapazität() {
		return 0;
	}

	@Override
	public int getBestand() {
		return 0;
	}
}
