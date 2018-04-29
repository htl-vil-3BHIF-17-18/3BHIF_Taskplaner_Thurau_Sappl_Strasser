package dal;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;

import bll.Task;

public class DatabaseWrapper {

	DatabaseHandler databaseHandler = null;
	
	public DatabaseWrapper(DatabaseHandler databaseHandler) {
		this.databaseHandler = databaseHandler;
	}
	
	/**
	 * Tabelle {@code Tasks} erstellen.
	 * @return {@link Boolean} Erfolgreich
	 */
	public boolean createTasksTable() {
		return this.databaseHandler.perform(
				"CREATE TABLE Tasks ("
				+ "done INTEGER,"
				+ "text VARCHAR2(100),"
				+ "type VARCHAR2(100),"
				+ "subject VARCHAR2(100),"
				+ "dateFrom DATE,"
				+ "dateTo DATE"
				+ ")");
	}
	
	/**
	 * Setzte {@code Tasks}.
	 * @param tasks {@link Set} Tasks
	 * @return {@link Boolean} Erfolgreich
	 */
	public boolean setTasks(Set<Task> tasks) {
		
		boolean result = false;
		
		result = this.databaseHandler.perform("TRUNCATE TABLE Tasks;");
		
		if(result) {
			for(Task t : tasks) {
				result = this.databaseHandler.performInsert("Task", t.databaseColumns(), t.databaseValues());
			}
		}
		
		return result;
	}
	
	/**
	 * Bekomme {@code Tasks}.
	 * @return {@link Set} Tasks
	 */
	public Set<Task> getTasks() {
		
		Set<Task> result = new HashSet<Task>();
		
		ResultSet rs = this.databaseHandler.performSelect(
				"Tasks",
				new HashSet<String>(Arrays.asList("done", "text", "type", "subject", "dateFrom", "dateTo")),
				"1 = 1"
				);
		
		// --- [Start] Debug
		System.out.println(rs);
		// --- [End  ] Debug
		
		try {
			while(rs.next()) {
				result.add(
						new Task(
								rs.getBoolean(1), // erledigt
								new GregorianCalendar( // von
										rs.getDate(5).toLocalDate().getYear(),
										rs.getDate(5).toLocalDate().getMonthValue(),
										rs.getDate(5).toLocalDate().getDayOfMonth()
										),
								rs.getString(4), // fach
								rs.getString(3), // typ
								new GregorianCalendar( // bis
										rs.getDate(6).toLocalDate().getYear(),
										rs.getDate(6).toLocalDate().getMonthValue(),
										rs.getDate(6).toLocalDate().getDayOfMonth()
										),
								rs.getString(2)
								) // text
						);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
		return result;
	}
}
