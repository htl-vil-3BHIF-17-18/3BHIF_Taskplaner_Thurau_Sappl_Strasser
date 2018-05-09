package swingworkers;

import java.util.Set;

import javax.swing.SwingWorker;

import bll.DatabaseTaskWrapper;
import bll.Task;

public class SwingWorkerSetTasks extends SwingWorker<Boolean, String> {

	DatabaseTaskWrapper dbw = null;
	Set<Task> tasks = null;
	
	public SwingWorkerSetTasks(final DatabaseTaskWrapper dbw, final Set<Task> tasks) {
		this.dbw = dbw;
		this.tasks = tasks;
	}
	
	@Override
	protected Boolean doInBackground() throws Exception {
		return this.dbw.setTasks(tasks);
	}

}
