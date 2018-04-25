package gui;

import java.util.GregorianCalendar;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import bll.Task;

public class TaskTable extends JTable {

	private static final long serialVersionUID = -3013523607123464946L;
	private DefaultTableModel model;
	private Object[] columNames = { "Status", "Von", "Fach", "Typ", "Bis", "Text" };

	public TaskTable(Set<Task> tasks) {
		this.setTasks(tasks);
	}

	public void setTasks(Set<Task> students) {
		this.model = new DefaultTableModel(){

			
			private static final long serialVersionUID = -736270369587528844L;

			@Override
		    public boolean isCellEditable(int row, int column) {
		       return false;
		    }
		};

		this.model.setColumnIdentifiers(columNames);
		this.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
		for (Task student : students) {
			this.model.addRow(student.toVector());
		}
		this.setModel(model);
	}

	public Set<Task> getTasks() {
		Set<Task> rgw = new TreeSet<Task>();
		for (int row = 0; row < this.model.getRowCount(); row++) {
			boolean status = this.model.getValueAt(row, 0) == "erledigt" ? true : false;
			GregorianCalendar datumVon = new GregorianCalendar(
					Integer.parseInt(((String) model.getValueAt(row, 1)).split(".")[2]),
					Integer.parseInt(((String) model.getValueAt(row, 1)).split(".")[1]),
					Integer.parseInt(((String) model.getValueAt(row, 1)).split(".")[0]));
			String fach = (String) this.model.getValueAt(row, 2);
			String typ = (String) this.model.getValueAt(row, 3);
			GregorianCalendar datumBis = new GregorianCalendar(
					Integer.parseInt(((String) model.getValueAt(row, 4)).split(".")[2]),
					Integer.parseInt(((String) model.getValueAt(row, 4)).split(".")[1]),
					Integer.parseInt(((String) model.getValueAt(row, 4)).split(".")[0]));
			String text = (String) this.model.getValueAt(row, 5);
			rgw.add(new Task(status, datumVon, fach, typ, datumBis, text));
		}
		return rgw;

	}
}
