package dal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * DatabaseHandler-Klasse
 * @author Joel Strasser
 * @version 2
 * @since 1
 * @param connectionString Zeichenkette f�r die Verbindung.
 */
public class DatabaseHandler extends AbstractDatabaseHandler {
	
	private String connectionString = "";
	private boolean initialized = false;
	private Connection connection = null;
	
	/**
	 * DatabaseHandler-Konstruktor
	 * @author Joel Strasser
	 * @version 1
	 * @since 1
	 * @param connectionString Zeichenkette f�r die Verbindung.
	 */
	public DatabaseHandler(String connectionString) {
		super(connectionString);
		this.connectionString = connectionString;
	}
	
	/**
	 * DatabaseHandler initialisieren.
	 * @author Joel Strasser
	 * @version 2
	 * @since 1
	 */
	@Override
	public boolean initialize() {
		// Wenn bereits initialisiert wurde, 'true' zurückgeben.
		if(this.initialized) { return true; }
		
		try {
            Class.forName("oracle.jdbc.driver.OracleDriver"); 
            return true;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
	}
	
	/**
	 * Verbindung öffnen.
	 * @author Joel Strasser
	 * @version 1
	 * @since 2
	 */
	@Override
	protected boolean openConnection() {
		if(!this.initialized) { return false; }
		
		try {
            this.connection = DriverManager.getConnection(this.connectionString);
            return true;
        } catch (SQLException e) {
        	e.printStackTrace();
        	return false;
        }
	}
	
	/**
	 * Verbindung öffnen.
	 * @author Joel Strasser
	 * @version 1
	 * @since 2
	 */
	@Override
	protected boolean closeConnection() {
		if(!this.initialized) { return false; }
		
		try {
            this.connection.close();
            return true;
        } catch (SQLException e) {
        	e.printStackTrace();
        	return false;
        }
	}
	
	/**
	 * Exekutieren.
	 * @author Joel Strasser
	 * @version 1
	 * @since 2
	 * @param {@link String} statement Statement
	 * @return {@link Boolean} Erfolgreich
	 */
	@Override
	public boolean perform(String statement) {
		if(!this.initialized) { return false; }
		
		boolean result = false;
		
		this.openConnection();
		
		try {
            result = this.connection.prepareStatement(statement).execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
        	this.closeConnection();
        }
		
		return result;
	}
	
	/**
	 * SELECT exekutieren.
	 * @author Joel Strasser
	 * @version 2
	 * @since 1
	 * @param {@link String} table Datenbanktabelle
	 * @param {@link Set} columns Spalten
	 * @param {@link String} condition Bedingung
	 * @return {@link ResultSet} Ergebnis
	 */
	@SuppressWarnings("null")
	@Override
	public ResultSet performSelect(String table, Set<String> columns, String condition) {
		if(!this.initialized) { return null; }

        ResultSet rs = null;
        
        this.openConnection();
        
		try {
			rs = this.connection.createStatement().executeQuery(String.format("SELECT %s FROM %s WHERE %s", columns, table, condition));
		} catch (SQLException e) {
			e.printStackTrace();
			
			try {
				rs.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			
			return null;
		} finally {
			this.closeConnection();
		}
        
       return rs;
	}
	
	/**
	 * Einfaches SELECT exekutieren.
	 * @author Joel Strasser
	 * @version 2
	 * @since 1
	 * @param {@link String} table Datenbanktabelle
	 * @param {@link String} condition Bedingung
	 * @return {@link ResultSet} Ergebnis
	 */
	@Override
	public ResultSet performSimpleSelect(String table, String condition) {
		if(!this.initialized) { return null; }
		return performSelect(table, new HashSet<String>(Arrays.asList("*")), condition);
	}

	/**
	 * Erweitertes SELECT exekutieren.
	 * @author Joel Strasser
	 * @version 1
	 * @since 1
	 * @param {@link String} table Datenbanktabelle
	 * @param {@link Set} columns Spalten
	 * @param {@link String} condition Bedingung
	 * @return {@link List} Ergebnis
	 * @deprecated
	 * @see performSelect
	 */
	@Override
	public <T> List<T> performExtendedSelect(String table, Set<String> columns, String condition) {
		if(!this.initialized) { return null; }
		throw new NotImplementedException();
	}

	/**
	 * INSERT exekutieren.
	 * @author Joel Strasser
	 * @version 2
	 * @since 1
	 * @param {@link String} table Datenbanktabelle
	 * @param {@link Set} columns Spalten
	 * @param {@link Set} values Werte
	 * @return {@link Boolean} Erfolgreich
	 */
	@Override
	public boolean performInsert(String table, Set<String> columns, Set<String> values) {
		if(!this.initialized) { return false; }
		
		boolean result = false;
		
		Iterator<String> it = columns.iterator();
		Iterator<String> it2 = values.iterator();
		
		String statementColumns = "";
		String statementValues = "";
		
		String statementString = "";
		
		while(it.hasNext() && it2.hasNext()) {
			statementColumns = String.format("%s, %s", statementColumns, it.next());
			statementValues = String.format("%s, %s", statementValues, it2.next());
		}
		
		statementString = String.format("INSERT INTO %s(%s) VALUES(%s)", table, statementColumns, statementValues);
		
		this.openConnection();
		
		try {
            result = this.connection.prepareStatement(statementString).execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
        	this.openConnection();
        }
		
		return result;
	}

	/**
	 * Einfaches INSERT exekutieren.
	 * @author Joel Strasser
	 * @version 2
	 * @since 1
	 * @param {@link String} table Datenbanktabelle
	 * @param {@link Set} values Werte
	 * @return {@link Boolean} Erfolgreich
	 */
	@Override
	public boolean performSimpleInsert(String table, Set<String> values) {
		if(!this.initialized) { return false; }
		
		boolean result = false;
		
		Iterator<String> it = values.iterator();
		
		String statementString = "";
		
		while(it.hasNext()) {
			statementString = String.format("%s, %s", statementString, it.next());
		}
		
		statementString = String.format("INSERT INTO %s VALUES(%s)", table, statementString);
		
		this.openConnection();
		
		try {
            result = this.connection.prepareStatement(statementString).execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
        	this.closeConnection();
        }
		
		return result;
	}

	/**
	 * UPDATE exekutieren.
	 * @author Joel Strasser
	 * @version 2
	 * @since 1
	 * @param {@link String} table Datenbanktabelle
	 * @param {@link Set} columns Spalten
	 * @param {@link Set} values Werte
	 * @param {@link String} condition Bedingung
	 * @return {@link Boolean} Erfolgreich
	 */
	@Override
	public boolean performUpdate(String table, Set<String> columns, Set<String> values, String condition) {
		if(!this.initialized) { return false; }
		
		if(columns.size() != values.size()) {
			return false;
		}
		
		boolean result = false;
		
		Iterator<String> it = columns.iterator();
		Iterator<String> it2 = values.iterator();
		
		String statementString = "";
		
		while(it.hasNext() && it2.hasNext()) {
			statementString = String.format("%s, %s = %s", statementString, it.next(), it2.next());
		}
		
		statementString = String.format("UPDATE %S SET %s WHERE %s", table, statementString, condition);
		
		this.openConnection();
		
		try {
            result = this.connection.prepareStatement(statementString).execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
        	this.closeConnection();
        }
		
		return result;
	}

	/**
	 * DELETE exekutieren.
	 * @author Joel Strasser
	 * @version 2
	 * @since 1
	 * @param {@link String} table Datenbanktabelle
	 * @param {@link String} condition Bedingung
	 * @return {@link Boolean} Erfolgreich
	 */
	@Override
	public boolean performDelete(String table, String condition) {
		if(!this.initialized) { return false; }
		
		boolean result = false;
		
		this.openConnection();
		
		try {
            result = this.connection.prepareStatement(String.format("DELETE FROM %s WHERE %s", table, condition)).execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
        	this.closeConnection();
        }
		
		return result;
	}
}
