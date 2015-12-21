package model;

public abstract class TransaktionsFehler extends RuntimeException {
    public TransaktionsFehler(String str) {
        super(str);
    }
}
