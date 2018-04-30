package dal;

import java.util.Map;
import java.util.Set;

public interface DatabaseMapable {

	/**
	 * Gibt eine {@link Map} zur�ck, welche Datenbankspaltennamen zum internen Feldnamen aufl�st.
	 * @deprecated Ist nicht mehr zu gebrauchen.
	 * @see {@link databaseColumns} und  {@link databaseValues}
	 * @return {@link Map} Datenbankspaltennamen, interner Feldname
	 */
	@Deprecated
	public Map<String, String> columnFieldMap();
	
	/**
	 * Gibt die Spaltenbezeichnungen f�r die Datenbanktabellen f�r die impementierende Klasse vor.
	 * @return {@link Set} Spaltenbezeichnungen f�r die Datenbanktabellen
	 */
	public Set<String> databaseColumns();
	
	/**
	 * Gibt die Werte f�r die Saplten f�r die Datenbanktabellen f�r die impementierende Klasse vor.
	 * (Der Name der Methode ist nicht gut gew�hlt.)
	 * @return {@link Set} Werte f�r die Spalten f�r die Datenbanktabellen
	 */
	public Set<String> databaseValues();
}
