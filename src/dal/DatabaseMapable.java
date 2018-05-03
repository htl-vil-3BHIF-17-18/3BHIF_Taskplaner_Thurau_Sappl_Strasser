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
	 * Gibt eine {@linkplain Map} zur�ck, welche Datenbankspaltennamen zum internen Feldnamen aufl�st.
	 * @deprecated Ist nicht mehr zu gebrauchen.
	 * <br />
	 * Benutzung von {@linkplain #databaseColumns()} und  {@linkplain #databaseValues()} empfohlen.
	 * @return {@linkplain Map} Datenbankspaltennamen, interner Feldname
	 */
	public Map<String, String> columnFieldMap();
	/**
	 * Gibt die Spaltenbezeichnungen f�r die Datenbanktabellen f�r die impementierende Klasse vor.
	 * @return {@linkplain Set} Spaltenbezeichnungen f�r die Datenbanktabellen
	 */
	public Set<String> databaseColumns();
	/**
	 * Gibt die Werte f�r die Saplten f�r die Datenbanktabellen f�r die impementierende Klasse vor.
	 * (Der Name der Methode ist nicht gut gew�hlt.)
	 * @return {@linkplain Set} Werte f�r die Spalten f�r die Datenbanktabellen
	 */
	public Set<String> databaseValues();
}