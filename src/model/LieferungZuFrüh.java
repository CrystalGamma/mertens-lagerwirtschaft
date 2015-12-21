package model;

public class LieferungZuFrüh extends TransaktionsFehler {
	public LieferungZuFrüh() {
		super("Datum liegt vor der letzten übernommenen Lieferung");
	}
}
