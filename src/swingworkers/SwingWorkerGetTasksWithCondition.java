package swingworkers;

import java.util.Set;

import javax.swing.SwingWorker;

import bll.DatabaseTaskWrapper;
import bll.Task;

public class SwingWorkerGetTasksWithCondition extends SwingWorker<Set<Task>, String> {

	DatabaseTaskWrapper dbw = null;
	String condition = "";
	
	public SwingWorkerGetTasksWithCondition(final DatabaseTaskWrapper dbw, final String condition) {
		this.dbw = dbw;
		this.condition = condition;
	}
	
	@Override
	protected Set<Task> doInBackground() throws Exception {
		return this.dbw.getTasks(condition);
	}

}
