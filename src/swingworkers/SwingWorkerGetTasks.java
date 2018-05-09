package swingworkers;

import java.util.Set;

import javax.swing.SwingWorker;

import bll.DatabaseTaskWrapper;
import bll.Task;

public class SwingWorkerGetTasks extends SwingWorker<Set<Task>, String> {

	DatabaseTaskWrapper dbw = null;
	
	public SwingWorkerGetTasks(final DatabaseTaskWrapper dbw) {
		this.dbw = dbw;
	}
	
	@Override
	protected Set<Task> doInBackground() throws Exception {
		return this.dbw.getTasks();
	}

}
