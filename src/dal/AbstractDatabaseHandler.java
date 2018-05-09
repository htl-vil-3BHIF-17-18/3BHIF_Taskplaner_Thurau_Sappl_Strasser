package dal;

import java.sql.ResultSet;
import java.util.List;
import java.util.Set;

public abstract class AbstractDatabaseHandler {
		
	public AbstractDatabaseHandler(String connectionString) { };
	public abstract boolean initialize();
	
	protected abstract boolean openConnection();
	
	protected abstract boolean closeConnection();
	
	public abstract boolean perform(String statement);
	
	public abstract ResultSet performSelect(String table, Set<String> columns, String condition);
	
	public abstract ResultSet performSimpleSelect(String table, String condition);
	
	public abstract <T> List<T> performExtendedSelect(String table, Set<String> columns, String condition);
	
	public abstract boolean performInsert(String table, Set<String> columns, Set<String> values);
	
	public abstract boolean performSimpleInsert(String table, Set<String> values);
	
	public abstract boolean performUpdate(String table, Set<String> columns, Set<String> values, String condition);
	
	public abstract boolean performDelete(String table, String condition);
}