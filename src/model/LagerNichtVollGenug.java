package model;

public class LagerNichtVollGenug extends TransaktionsFehler {
    public LagerNichtVollGenug() {
        super("Der Bestand im ausgewählten Lager ist nicht ausreichend.");
    }
}
