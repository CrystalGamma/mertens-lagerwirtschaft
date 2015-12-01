package ui;

import model.Model;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LagerTree extends JTree {
	public LagerTree(Model m) {
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int row = getRowForLocation(e.getX(), e.getY());
			}
		});
	}
}
