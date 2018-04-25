package dal;

import java.sql.ResultSet;
import java.util.List;

public abstract class AbstractDatabaseHandler {
		
	public AbstractDatabaseHandler(String connectionString) { };
	
	public abstract boolean initialize();
	
	public abstract ResultSet performSelect(String table, String columns, String condition);
	
	public abstract ResultSet performSimpleSelect(String table, String condition);
	
	public abstract <T> List<T> performExtendedSelect(String table, String columns, String condition);
	
	public abstract boolean performInsert(String table, String columns, String values);
	
	public abstract boolean performSimpleInsert(String table, String values);
	
	public abstract boolean performUpdate(String table, String[] columns, String[] values, String condition);
	
	public abstract boolean performDelete(String table, String condition);
}