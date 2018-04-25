package dal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class DatabaseHandler extends AbstractDatabaseHandler {
	
	private String connectionString = "";
	private boolean initialized = false;
	private Connection connection = null;
	
	/**
	 * DatabaseHandler-Konstruktor
	 * @author Joel Strasser
	 * @version 1
	 * @since 1
	 * @param connectionString Zeichenkette für die Verbindung.
	 */
	public DatabaseHandler(String connectionString) {
		super(connectionString);
		this.connectionString = connectionString;
	}
	
	/**
	 * DatabaseHandler initialisieren.
	 * @author Joel Strasser
	 * @version 1
	 * @since 1
	 */
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

	/**
	 * SELECT exekutieren.
	 * @author Joel Strasser
	 * @version 1
	 * @since 1
	 * @param table Datenbanktabelle
	 * @param columns Spalten
	 * @param condition Bedingung
	 * @return {@link ResultSet} Ergebnis
	 */
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
	
	/**
	 * Einfaches SELECT exekutieren.
	 * @author Joel Strasser
	 * @version 1
	 * @since 1
	 * @param table Datenbanktabelle
	 * @param condition Bedingung
	 * @return {@link ResultSet} Ergebnis
	 */
	@Override
	public ResultSet performSimpleSelect(String table, String condition) {
		if(!this.initialized) { return null; }
		return performSelect(table, "*", condition);
	}

	/**
	 * Erweitertes SELECT exekutieren.
	 * @author Joel Strasser
	 * @version 1
	 * @since 1
	 * @param table Datenbanktabelle
	 * @param columns Spalten
	 * @param condition Bedingung
	 * @return {@link List} Ergebnis
	 * @deprecated
	 * @see performSelect
	 */
	@Override
	public <T> List<T> performExtendedSelect(String table, String columns, String condition) {
		if(!this.initialized) { return null; }
		throw new NotImplementedException();
	}

	/**
	 * INSERT exekutieren.
	 * @author Joel Strasser
	 * @version 1
	 * @since 1
	 * @param table Datenbanktabelle
	 * @param columns Spalten
	 * @param values Werte
	 * @return {@link Boolean} Erfolgreich
	 */
	@Override
	public boolean performInsert(String table, String columns, String values) {
		if(!this.initialized) { return false; }
		
		boolean result = false;
		
		try {
            result = this.connection.prepareStatement(String.format("INSERT INTO %s(%s) VALUES(%s)", table, columns, values)).execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
		
		return result;
	}

	/**
	 * Einfaches INSERT exekutieren.
	 * @author Joel Strasser
	 * @version 1
	 * @since 1
	 * @param table Datenbanktabelle
	 * @param values Werte
	 * @return {@link Boolean} Erfolgreich
	 */
	@Override
	public boolean performSimpleInsert(String table, String values) {
		if(!this.initialized) { return false; }
		
		boolean result = false;
		
		try {
            result = this.connection.prepareStatement(String.format("INSERT INTO %s VALUES(%s)", table, values)).execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
		
		return result;
	}

	/**
	 * UPDATE exekutieren.
	 * @author Joel Strasser
	 * @version 1
	 * @since 1
	 * @param table Datenbanktabelle
	 * @param columns Spalten
	 * @param values Werte
	 * @param condition Bedingung
	 * @return {@link Boolean} Erfolgreich
	 */
	@Override
	public boolean performUpdate(String table, String[] columns, String[] values, String condition) {
		if(!this.initialized) { return false; }
		
		if(columns.length != values.length) {
			return false;
		}
		
		boolean result = false;
		
		String statementString = "";
		
		for(int i = 0; i < columns.length - 1; i++) {
			statementString = String.format("%s, %s = %s", statementString, columns[i], values[i]);
		}
		
		statementString = String.format("UPDATE %S SET %s WHERE %s", table, statementString, condition);
		
		try {
            result = this.connection.prepareStatement(statementString).execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
		
		return result;
	}

	/**
	 * DELETE exekutieren.
	 * @author Joel Strasser
	 * @version 1
	 * @since 1
	 * @param table Datenbanktabelle
	 * @param condition Bedingung
	 * @return {@link Boolean} Erfolgreich
	 */
	@Override
	public boolean performDelete(String table, String condition) {
		if(!this.initialized) { return false; }
		
		boolean result = false;
		
		try {
            result = this.connection.prepareStatement(String.format("DELETE FROM %s WHERE %s", table, condition)).execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
		
		return result;
	}
}
