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

import com.sun.rowset.CachedRowSetImpl;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * DatabaseHandler-Klasse
 * <br />
 * Erbt von der {@linkplain AbstractDatabaseHandler} Klasse.
 * @author Joel Strasser
 * @version 2
 * @since 1
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
	 * @param connectionString {@linkplain String} Zeichenkette für die Verbindung
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
	 * @return {@linkplain Boolean} Erfolgreich
	 */
	@Override
	public boolean initialize() {
		// Wenn bereits initialisiert wurde, 'true' zurückgeben.
		if (this.initialized) {
			return true;
		}

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			this.initialized = true;
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
	 * @return {@linkplain Boolean} Erfolgreich
	 */
	@Override
	protected boolean openConnection() {
		if (!this.initialized) {
			return false;
		}

		try {
			this.connection = DriverManager.getConnection(this.connectionString);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Verbindung schließen.
	 * @author Joel Strasser
	 * @version 1
	 * @since 2
	 * @retunr {@linkplain Boolean} Erfolgreich
	 */
	@Override
	protected boolean closeConnection() {
		if (!this.initialized) {
			return false;
		}

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
	 * 
	 * @author Joel Strasser
	 * @version 1
	 * @since 2
	 * @param statement {@linkplain String} Statement
	 * @return {@linkplain Boolean} Erfolgreich
	 */
	@Override
	public boolean perform(String statement) {
		if (!this.initialized) {
			return false;
		}

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
	 * @param table {@linkplain String} Datenbanktabelle
	 * @param columns {@linkplain Set} Spalten
	 * @param condition {@linkplain String} Bedingung
	 * @return {@linkplain ResultSet}, {@linkplain CachedRowSetImpl} Ergebnis
	 */
	@Override
	public ResultSet performSelect(String table, Set<String> columns, String condition) {
		if (!this.initialized) {
			return null;
		}

		CachedRowSetImpl crs = null;
		try {
			crs = new CachedRowSetImpl();
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		Iterator<String> it = columns.iterator();

		String statementString = "";

		while (it.hasNext()) {
			String s = it.next();

			if (it.hasNext()) {
				statementString = String.format("%s %s,", statementString, s);
			} else {
				statementString = String.format("%s %s", statementString, s);
			}
		}
		statementString = String.format("SELECT %s FROM %s WHERE %s", statementString, table, condition);

		this.openConnection();

		try {
			crs.populate(this.connection.createStatement().executeQuery(statementString));
		} catch (SQLException e) {
			e.printStackTrace();

			try {
				crs.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}

			return null;
		} finally {
			this.closeConnection();
		}

		return crs;
	}
	/**
	 * Einfaches SELECT exekutieren.
	 * @author Joel Strasser
	 * @version 2
	 * @since 1
	 * @param table {@linkplain String} Datenbanktabelle
	 * @param condition {@linkplain String} Bedingung
	 * @return {@linkplain ResultSet}, {@linkplain CachedRowSetImpl} Ergebnis
	 */
	@Override
	public ResultSet performSimpleSelect(String table, String condition) {
		if (!this.initialized) {
			return null;
		}
		return performSelect(table, new HashSet<String>(Arrays.asList("*")), condition);
	}
	/**
	 * Erweitertes SELECT exekutieren.
	 * @author Joel Strasser
	 * @version 1
	 * @since 1
	 * @param table {@linkplain String} Datenbanktabelle
	 * @param columns {@linkplain Set} Spalten
	 * @param condition {@linkplain String} Bedingung
	 * @return {@linkplain List} Ergebnis
	 * @deprecated Nicht implementiert.
	 * <br />
	 * Benutzung von {@linkplain DatabaseHandler#performSelect(String, Set, String)} empfohlen.
	 */
	@Override
	public <T> List<T> performExtendedSelect(String table, Set<String> columns, String condition) {
		if (!this.initialized) {
			return null;
		}
		throw new NotImplementedException();
	}
	/**
	 * INSERT exekutieren.
	 * @author Joel Strasser
	 * @version 2
	 * @since 1
	 * @param table {@linkplain String} Datenbanktabelle
	 * @param columns {@linkplain Set} Spalten
	 * @param values {@linkplain Set} Werte
	 * @return {@linkplain Boolean} Erfolgreich
	 */
	@Override
	public boolean performInsert(String table, Set<String> columns, Set<String> values) {
		if (!this.initialized) {
			return false;
		}

		boolean result = false;

		Iterator<String> it = columns.iterator();
		Iterator<String> it2 = values.iterator();

		String statementColumns = "";
		String statementValues = "";

		String statementString = "";

		while (it.hasNext() && it2.hasNext()) {
			String s = it.next();
			String s2 = it2.next();

			if (it.hasNext() && it2.hasNext()) {
				statementColumns = String.format("%s %s,", statementColumns, s);
				statementValues = String.format("%s %s,", statementValues, s2);
			} else {
				statementColumns = String.format("%s %s", statementColumns, s);
				statementValues = String.format("%s %s", statementValues, s2);
			}
		}

		statementString = String.format("INSERT INTO %s(%s) VALUES(%s)", table, statementColumns, statementValues);

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
	 * Einfaches INSERT exekutieren.
	 * @author Joel Strasser
	 * @version 2
	 * @since 1
	 * @param table {@linkplain String} Datenbanktabelle
	 * @param values {@linkplain Set} Werte
	 * @return {@linkplain Boolean} Erfolgreich
	 */
	@Override
	public boolean performSimpleInsert(String table, Set<String> values) {
		if (!this.initialized) {
			return false;
		}

		boolean result = false;

		Iterator<String> it = values.iterator();

		String statementString = "";

		while (it.hasNext()) {
			String s = it.next();

			if (it.hasNext()) {
				statementString = String.format("%s %s,", statementString, s);
			} else {
				statementString = String.format("%s %s", statementString, s);
			}
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
	 * @param table {@linkplain String} Datenbanktabelle
	 * @param columns {@linkplain Set} Spalten
	 * @param values {@linkplain Set} Werte
	 * @param condition {@linkplain String} Bedingung
	 * @return {@linkplain Boolean} Erfolgreich
	 */
	@Override
	public boolean performUpdate(String table, Set<String> columns, Set<String> values, String condition) {
		if (!this.initialized) {
			return false;
		}

		if (columns.size() != values.size()) {
			return false;
		}

		boolean result = false;

		Iterator<String> it = columns.iterator();
		Iterator<String> it2 = values.iterator();

		String statementString = "";

		while (it.hasNext() && it2.hasNext()) {
			String s = it.next();
			String s2 = it2.next();

			if (it.hasNext() && it2.hasNext()) {
				statementString = String.format("%s %s = %s,", statementString, s, s2);
			} else {
				statementString = String.format("%s %s = %s", statementString, s, s2);
			}
		}

		statementString = String.format("UPDATE %s SET %s WHERE %s", table, statementString, condition);

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
	 * @param table {@linkplain String} Datenbanktabelle
	 * @param condition {@linkplain String} Bedingung
	 * @return {@linkplain Boolean} Erfolgreich
	 */
	@Override
	public boolean performDelete(String table, String condition) {
		if (!this.initialized) {
			return false;
		}

		boolean result = false;

		this.openConnection();

		try {
			result = this.connection.prepareStatement(String.format("DELETE FROM %s WHERE %s", table, condition))
					.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection();
		}

		return result;
	}
}
