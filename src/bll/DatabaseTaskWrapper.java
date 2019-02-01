package bll;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.LinkedHashSet;
import java.util.Set;

import dal.DatabaseHandler;

/**
 * DatabaseWrapper-Klasse
 * <br />
 * Baut auf den {@linkplain DatabaseHandler} auf.
 * @author Joel Strasser
 */
public class DatabaseTaskWrapper {

	DatabaseHandler databaseHandler = null;
	
	/**
	 * DatabaseWrapper-Konstruktur
	 * @author Joel Strasser
	 * @param databaseHandler {@linkplain DatabaseHandler} DatabaseHandler
	 */
	public DatabaseTaskWrapper(DatabaseHandler databaseHandler) {
		this.databaseHandler = databaseHandler;
	}
	
	/**
	 * DatabaseWrapper-Konstruktur
	 * @author Joel Strasser
	 * @param connectionString {@linkplain DatabaseHandler} Verbindungsstring
	 */
	public DatabaseTaskWrapper(String connectionString) {
		this.databaseHandler = new DatabaseHandler(connectionString);
		this.databaseHandler.initialize();
	}
	
	/**
	 * Datenbanktabelle für Klasse {@linkplain Task} erstellen.
	 * @author Joel Strasser
	 * @return {@linkplain Boolean} Erfolgreich
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
	 * Ein {@linkplain Set} mit {@linkplain Task}s in die Datenbanktabelle speichern.
	 * @author Joel Strasser
	 * @param tasks {@linkplain Set} Set mit Tasks
	 * @return {@linkplain Boolean} Erfolgreich
	 */
	public boolean setTasks(Set<Task> tasks) {
		
		boolean result = false;
		
		//result = this.databaseHandler.perform("TRUNCATE TABLE Tasks");
		this.databaseHandler.perform("TRUNCATE TABLE Tasks");
		
		//if(result) {
			for(Task t : tasks) {
				result = this.databaseHandler.performSimpleInsert("Tasks", t.databaseValues());
			}
		//}
		
		return result;
	}
	
	/**
	 * Ein {@linkplain Set} mit {@linkplain Task}s von der Datenbank bekommen.
	 * @author Joel Strasser
	 * @return tasks {@linkplain Set} Set mit Tasks
	 */
	public Set<Task> getTasks() {
		
		Set<Task> result = new LinkedHashSet<Task>();
		
		ResultSet rs = this.databaseHandler.performSelect(
				"Tasks",
				new LinkedHashSet<String>(Arrays.asList("done", "text", "type", "subject", "dateFrom", "dateTo")),
				"1 = 1"
				);
		
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
	
	/**
	 * Ein {@linkplain Set} mit {@linkplain Task}s von der Datenbank bekommen,
	 * welche eine bestimmte Bedingung erfüllen.
	 * @author Joel Strasser
	 * @param condition {@linkplain String} Bedingung
	 * @return tasks {@linkplain Set} Set mit Tasks
	 */
	public Set<Task> getTasks(String condition) {
		
		Set<Task> result = new LinkedHashSet<Task>();
		
		ResultSet rs = this.databaseHandler.performSelect(
				"Tasks",
				new LinkedHashSet<String>(Arrays.asList("done", "text", "type", "subject", "dateFrom", "dateTo")),
				condition
				);
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
