package swingworkers;

import javax.swing.SwingWorker;

import bll.DatabaseTaskWrapper;

public class SwingWorkerCreateTasksTable extends SwingWorker<Boolean, String> {

	DatabaseTaskWrapper dbw = null;
	
	public SwingWorkerCreateTasksTable(final DatabaseTaskWrapper dbw) {
		this.dbw = dbw;
	}
	
	@Override
	protected Boolean doInBackground() throws Exception {
		return this.dbw.createTasksTable();
	}

}
