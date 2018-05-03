package dal;

import java.util.Map;
import java.util.Set;

/**
 * DatabaseMapable-Interface
 * <br />
 * Wird von der Klasse {@linkplain DatabaseWrapper} gebraucht.
 */
public interface DatabaseMapable {

	/**
	 * Gibt eine {@linkplain Map} zurück, welche Datenbankspaltennamen zum internen Feldnamen auflöst.
	 * @deprecated Ist nicht mehr zu gebrauchen.
	 * <br />
	 * Benutzung von {@linkplain #databaseColumns()} und  {@linkplain #databaseValues()} empfohlen.
	 * @return {@linkplain Map} Datenbankspaltennamen, interner Feldname
	 */
	public Map<String, String> columnFieldMap();
	/**
	 * Gibt die Spaltenbezeichnungen für die Datenbanktabellen für die impementierende Klasse vor.
	 * @return {@linkplain Set} Spaltenbezeichnungen für die Datenbanktabellen
	 */
	public Set<String> databaseColumns();
	/**
	 * Gibt die Werte für die Saplten für die Datenbanktabellen für die impementierende Klasse vor.
	 * (Der Name der Methode ist nicht gut gewählt.)
	 * @return {@linkplain Set} Werte für die Spalten für die Datenbanktabellen
	 */
	public Set<String> databaseValues();
}