package model;

public interface Lager {
	String getName();
	int getKapazität();
	int getBestand();
	Lager[] getUnterLager();
}
