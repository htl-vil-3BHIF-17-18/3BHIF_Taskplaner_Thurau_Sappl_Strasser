package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.DefaultListModel;
import javax.swing.JList;
//import javax.swing.JMenuItem;
//import javax.swing.JPopupMenu;

import bll.Task;

public class TaskList extends JList<Task> implements ActionListener{

	private static final long serialVersionUID = 6296981809654802779L;
	private DefaultListModel<Task> model = new DefaultListModel<Task>();
//	private JPopupMenu popup;
//	private JMenuItem edit;

	public TaskList(TreeSet<Task> treeSet) {
		this.setData(treeSet);
	}

	private void setData(TreeSet<Task> tasks) {
		model.removeAllElements();
		for (Task t : tasks) {
			model.addElement(t);
		}
		this.setModel(model);
	}
	
	public Set<Task> getData() {
		Set<Task> rgw = new TreeSet<Task>();
		for (int i = 0; i < model.size(); i++) {
			rgw.add(model.getElementAt(i));
		}
		return rgw;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO implement list selection, insertion and changes
		
	}

}
