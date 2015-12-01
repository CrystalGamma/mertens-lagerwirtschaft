package controller;

import model.Model;
import ui.Lieferung;

public class LieferungController {
	Model model;

	public LieferungController(Model model) {
		this.model = model;
		new Lieferung(model).setVisible(true);
	}
	public static void main(String[] args) {
		new LieferungController(new Model());
	}
}
