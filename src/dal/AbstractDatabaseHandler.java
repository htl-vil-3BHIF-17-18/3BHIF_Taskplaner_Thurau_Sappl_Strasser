package dal;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.List;

public abstract class AbstractDatabaseHandler {
	
	private String connectionString = "";
	private String username = "";
	private String password = "";
	private boolean initialized = false;
	
	public AbstractDatabaseHandler(String connectionString, String username, String password) {
		this.connectionString = connectionString;
		this.username = username;
		this.password = password;
	}
	
	public abstract boolean initialize();
	
	public abstract Connection getConnection();
	
	public abstract ResultSet performSelect(String table, String columns, String condition);
	
	public abstract ResultSet performSimpleSelect(String table, String condition);
	
	public abstract <T> List<T> performExtendedSelect(String table, String columns, String condition);
	
	public abstract int performInsert(String table, String columns, String values);
	
	public abstract int performSimpleInsert(String table, String values);
	
	public abstract int performUpdate(String table, String columns, String values, String condition);
	
	public abstract int performDelete(String table, String condition);
}