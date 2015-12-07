package ui;

import model.Model;

public class LagerNamensänderung {
	String neuerName;
	Model.Lager lager;
public LagerNamensänderung(String string, Model.Lager lager) {
	this.lager=lager;
	this.neuerName=string;
}
public Model.Lager getLager(){
	return lager;
}
public String getNeuerLagerName()
{
	return neuerName;
}
}
