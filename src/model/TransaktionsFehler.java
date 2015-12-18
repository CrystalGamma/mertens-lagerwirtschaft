package model;

public class TransaktionsFehler extends RuntimeException {
    public TransaktionsFehler(String str) {
        super(str);
    }
}
