package dal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.List;

public class DBHandler extends AbstractDatabaseHandler {
	
	private String connectionString = "";
	private String username = "";
	private String password = "";
	private boolean initialized = false;
	
	public DBHandler(String connectionString, String username, String password) {
		super(connectionString, username, password);
		this.connectionString = connectionString;
		this.username = username;
		this.password = password;
	}
	
	@Override
	public boolean initialize() {
		// Wenn bereits initialisiert wurde, 'true' zurückgeben.
		if(this.initialized) { return true; }
		// TODO: Initialisierung
		return false;
	}
	
	@Override
	public Connection getConnection() {
		if(!this.initialized) { return null; }
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResultSet performSelect(String table, String columns, String condition) {
		if(!this.initialized) { return null; }
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public ResultSet performSimpleSelect(String table, String condition) {
		if(!this.initialized) { return null; }
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> List<T> performExtendedSelect(String table, String columns, String condition) {
		if(!this.initialized) { return null; }
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int performInsert(String table, String columns, String values) {
		if(!this.initialized) { return -1; }
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int performSimpleInsert(String table, String values) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int performUpdate(String table, String columns, String values, String condition) {
		if(!this.initialized) { return -1; }
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int performDelete(String table, String condition) {
		if(!this.initialized) { return -1; }
		// TODO Auto-generated method stub
		return 0;
	}
}
