package model;

public class LagerÜbervoll extends TransaktionsFehler {
    public LagerÜbervoll() {
        super("Die angegebene Menge überschreitetet die Lagerkapazität.");
    }
}
