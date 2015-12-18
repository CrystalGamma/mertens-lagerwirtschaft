package model;

public class LieferungExistiert extends TransaktionsFehler {
    public LieferungExistiert() {
        super("Zu diesem Tag existiert schon eine Lieferung im System.");
    }
}
