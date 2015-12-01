package ui;

import model.Model;

import javax.swing.*;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LagerTree extends JTree {
	private class LagerTreeModel implements TreeModel {	// <- ADAPTER PATTERN? DECORATOR?
		Model model;
		public LagerTreeModel(Model m) {model = m;}
		private Model.Lager[] getLager(Object parent) {
			if (parent == "")
				return model.getLager();
			return ((Model.Lager)parent).getUnterLager();
		}
		@Override
		public Object getRoot() {
			return "";
		}
		@Override
		public int getChildCount(Object parent) {
			Model.Lager[] lager = getLager(parent);
			if (lager == null)
				return 0;
			return lager.length;
		}

		@Override
		public Object getChild(Object parent, int index) {
			return getLager(parent)[index];
		}

		@Override
		public int getIndexOfChild(Object parent, Object child) {
			Model.Lager[] lager = getLager(parent);
			for (int i = 0; i < lager.length; i++) {
				if (child == lager[i])
					return i;
			}
			return -1;
		}

		@Override
		public boolean isLeaf(Object node) {
			return getLager(node) == null;
		}

		@Override
		public void addTreeModelListener(TreeModelListener l) {}

		@Override
		public void removeTreeModelListener(TreeModelListener l) {}

		@Override
		public void valueForPathChanged(TreePath path, Object newValue) {}
	}
	public LagerTree(Model m) {
		setModel(new LagerTreeModel(m));
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int row = getRowForLocation(e.getX(), e.getY());
			}
		});
	}
}
