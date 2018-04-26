package dal;

import java.util.Set;

import bll.Task;

public class DatabaseWrapper {
	
	DatabaseHandler databaseHandler = null;
	
	public DatabaseWrapper(DatabaseHandler databaseHandler) {
		this.databaseHandler = databaseHandler;
	}
	
	public boolean createTasksTable() {
		return this.databaseHandler.perform("");
	}
	
	public Set<Task> getTasks() {
		return null;
	}
	
	public boolean setTasks(Set<Task> tasks) {
		return true;
	}
}
