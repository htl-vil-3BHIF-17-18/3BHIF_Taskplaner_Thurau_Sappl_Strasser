package dal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class DBHandler extends AbstractDatabaseHandler {
	
	private String connectionString = "";
	private boolean initialized = false;
	private Connection connection = null;
	
	public DBHandler(String connectionString) {
		super(connectionString);
		this.connectionString = connectionString;
	}
	
	@Override
	public boolean initialize() {
		// Wenn bereits initialisiert wurde, 'true' zurückgeben.
		if(this.initialized) { return true; }
		
		try {
            Class.forName("oracle.jdbc.driver.OracleDriver"); 
            this.connection = DriverManager.getConnection(this.connectionString);
            return true;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (SQLException e) {
        	e.printStackTrace();
        	return false;
        }
	}

	@Override
	public ResultSet performSelect(String table, String columns, String condition) {
		if(!this.initialized) { return null; }

        ResultSet rs;
		try {
			rs = this.connection.createStatement().executeQuery(String.format("SELECT %s FROM %s WHERE %s", columns, table, condition));
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
        
       return rs;
	}
	
	@Override
	public ResultSet performSimpleSelect(String table, String condition) {
		if(!this.initialized) { return null; }
		return performSelect(table, "*", condition);
	}

	@Override
	public <T> List<T> performExtendedSelect(String table, String columns, String condition) {
		if(!this.initialized) { return null; }
		throw new NotImplementedException();
	}

	@Override
	public int performInsert(String table, String columns, String values) {
		if(!this.initialized) { return -1; }
		throw new NotImplementedException();
	}

	@Override
	public int performSimpleInsert(String table, String values) {
		if(!this.initialized) { return -1; }
		throw new NotImplementedException();
	}

	@Override
	public int performUpdate(String table, String columns, String values, String condition) {
		if(!this.initialized) { return -1; }
		throw new NotImplementedException();
	}

	@Override
	public int performDelete(String table, String condition) {
		if(!this.initialized) { return -1; }
		throw new NotImplementedException();
	}
}
