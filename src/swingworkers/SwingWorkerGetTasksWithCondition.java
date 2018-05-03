package swingworkers;

import java.util.Set;

import javax.swing.SwingWorker;

import bll.Task;
import dal.DatabaseWrapper;

public class SwingWorkerGetTasksWithCondition extends SwingWorker<Set<Task>, String> {

	DatabaseWrapper dbw = null;
	String condition = "";
	
	public SwingWorkerGetTasksWithCondition(final DatabaseWrapper dbw, final String condition) {
		this.dbw = dbw;
		this.condition = condition;
	}
	
	@Override
	protected Set<Task> doInBackground() throws Exception {
		return this.dbw.getTasks(condition);
	}

}
