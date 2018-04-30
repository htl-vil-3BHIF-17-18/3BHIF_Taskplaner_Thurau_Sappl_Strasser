package dal;

import java.util.Map;
import java.util.Set;

public interface DatabaseMapable {

	/**
	 * Gibt eine {@link Map} zurück, welche Datenbankspaltennamen zum internen Feldnamen auflöst.
	 * @deprecated Ist nicht mehr zu gebrauchen.
	 * @see {@link databaseColumns} und  {@link databaseValues}
	 * @return {@link Map} Datenbankspaltennamen, interner Feldname
	 */
	@Deprecated
	public Map<String, String> columnFieldMap();
	
	/**
	 * Gibt die Spaltenbezeichnungen für die Datenbanktabellen für die impementierende Klasse vor.
	 * @return {@link Set} Spaltenbezeichnungen für die Datenbanktabellen
	 */
	public Set<String> databaseColumns();
	
	/**
	 * Gibt die Werte für die Saplten für die Datenbanktabellen für die impementierende Klasse vor.
	 * (Der Name der Methode ist nicht gut gewählt.)
	 * @return {@link Set} Werte für die Spalten für die Datenbanktabellen
	 */
	public Set<String> databaseValues();
}
