package model;

public class UngültigesDatum extends TransaktionsFehler {
    public UngültigesDatum() {
        super("Das eingegebe Datum weist nicht die vorgegebene Struktur (JJJJ-MM-TT) auf.");
    }
}
