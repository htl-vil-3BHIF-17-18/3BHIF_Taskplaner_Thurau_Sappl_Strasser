package dal;

import java.util.Set;

import bll.Task;

public class DatabaseWrapper {

	DatabaseHandler databaseHandler = null;
	
	public DatabaseWrapper(DatabaseHandler databaseHandler) {
		this.databaseHandler = databaseHandler;
	}
	
	public boolean createTaskTable() {
		return this.databaseHandler.perform("");
	}
	
	public boolean setTasks(Set<Task> tasks) {
		return false;
	}
	
	public Set<Task> getTasks() {
		return null;
	}
}
