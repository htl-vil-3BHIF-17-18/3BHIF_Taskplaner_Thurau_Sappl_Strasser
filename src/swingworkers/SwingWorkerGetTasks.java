package swingworkers;

import java.util.Set;

import javax.swing.SwingWorker;

import bll.Task;
import dal.DatabaseWrapper;

public class SwingWorkerGetTasks extends SwingWorker<Set<Task>, String> {

	DatabaseWrapper dbw = null;
	
	public SwingWorkerGetTasks(final DatabaseWrapper dbw) {
		this.dbw = dbw;
	}
	
	@Override
	protected Set<Task> doInBackground() throws Exception {
		return this.dbw.getTasks();
	}

}
