package swingworkers;

import java.util.Set;

import javax.swing.SwingWorker;

import bll.Task;
import dal.DatabaseWrapper;

public class SwingWorkerSetTasks extends SwingWorker<Boolean, String> {

	DatabaseWrapper dbw = null;
	Set<Task> tasks = null;
	
	public SwingWorkerSetTasks(final DatabaseWrapper dbw, final Set<Task> tasks) {
		this.dbw = dbw;
		this.tasks = tasks;
	}
	
	@Override
	protected Boolean doInBackground() throws Exception {
		return this.dbw.setTasks(tasks);
	}

}
