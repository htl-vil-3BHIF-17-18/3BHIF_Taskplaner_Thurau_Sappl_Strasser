package swingworkers;

import javax.swing.SwingWorker;

import dal.DatabaseWrapper;

public class SwingWorkerCreateTasksTable extends SwingWorker<Boolean, String> {

	DatabaseWrapper dbw = null;
	
	public SwingWorkerCreateTasksTable(final DatabaseWrapper dbw) {
		this.dbw = dbw;
	}
	
	@Override
	protected Boolean doInBackground() throws Exception {
		return this.dbw.createTasksTable();
	}

}
