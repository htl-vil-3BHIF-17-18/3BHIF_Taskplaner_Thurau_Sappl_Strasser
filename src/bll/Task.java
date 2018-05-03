package bll;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import dal.DatabaseMapable;

public class Task implements DatabaseMapable, Comparable<Task> {
	private boolean erledigt;
	private GregorianCalendar datumVon;
	private String fach;
	private String typ;
	private GregorianCalendar datumBis;
	private String text;

	public Task(boolean erledigt, GregorianCalendar datumVon, String fach, String typ, GregorianCalendar datumBis,
			String text) {
		super();
		this.erledigt = erledigt;
		this.datumVon = datumVon;
		this.fach = fach;
		this.typ = typ;
		this.datumBis = datumBis;
		this.text = text;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public GregorianCalendar getDatumBis() {
		return datumBis;
	}

	public void setDatumBis(GregorianCalendar datumBis) {
		this.datumBis = datumBis;
	}

	public GregorianCalendar getDatumVon() {
		return datumVon;
	}

	public void setDatumVon(GregorianCalendar datumVon) {
		this.datumVon = datumVon;
	}

	public String getTyp() {
		return typ;
	}

	public void setTyp(String typ) {
		this.typ = typ;
	}

	public String getFach() {
		return fach;
	}

	public void setFach(String fach) {
		this.fach = fach;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((datumBis == null) ? 0 : datumBis.hashCode());
		result = prime * result + ((datumVon == null) ? 0 : datumVon.hashCode());
		result = prime * result + (erledigt ? 1231 : 1237);
		result = prime * result + ((fach == null) ? 0 : fach.hashCode());
		result = prime * result + ((text == null) ? 0 : text.hashCode());
		result = prime * result + ((typ == null) ? 0 : typ.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Task other = (Task) obj;
		if (datumBis == null) {
			if (other.datumBis != null)
				return false;
		} else if (!datumBis.equals(other.datumBis))
			return false;
		if (datumVon == null) {
			if (other.datumVon != null)
				return false;
		} else if (!datumVon.equals(other.datumVon))
			return false;
		if (erledigt != other.erledigt)
			return false;
		if (fach == null) {
			if (other.fach != null)
				return false;
		} else if (!fach.equals(other.fach))
			return false;
		if (text == null) {
			if (other.text != null)
				return false;
		} else if (!text.equals(other.text))
			return false;
		if (typ == null) {
			if (other.typ != null)
				return false;
		} else if (!typ.equals(other.typ))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Task [text=" + text + ", datumBis=" + datumBis + ", datumVon=" + datumVon + ", typ=" + typ + ", fach="
				+ fach + ", erledigt=" + erledigt + "]";
	}

	/**
	 * Gibt eine Map (Key: Feld, Value: Spaltenname) zur�ck.
	 * 
	 * @return {@link Map}
	 */
	@Override
	public Map<String, String> columnFieldMap() {
		Map<String, String> columnField = new HashMap<String, String>();
		columnField.put("erledigt", "done");
		columnField.put("text", "text");
		columnField.put("datumVon", "dateFrom");
		columnField.put("datumBis", "dateTo");
		columnField.put("typ", "type");
		columnField.put("fach", "subject");
		return columnField;
	}

	@Override
	public int compareTo(Task o) {
		return this.datumVon.compareTo(o.getDatumVon());
	}

	public boolean isErledigt() {
		return erledigt;
	}

	public void setErledigt(boolean erledigt) {
		this.erledigt = erledigt;
	}

	public Vector<String> toVector() {
		Vector<String> rgw = new Vector<String>();
		rgw.add(erledigt ? "erledigt" : "offen");
		rgw.add(datumVon.get(GregorianCalendar.DAY_OF_MONTH) + "." + datumVon.get(GregorianCalendar.MONTH) + "."
				+ datumVon.get(GregorianCalendar.YEAR));
		rgw.add(fach);
		rgw.add(typ);
		rgw.add(datumBis.get(GregorianCalendar.DAY_OF_MONTH) + "." + datumBis.get(GregorianCalendar.MONTH) + "."
				+ datumBis.get(GregorianCalendar.YEAR));
		rgw.add(text);
		return rgw;
	}

	@Override
	public Set<String> databaseColumns() {
		return new LinkedHashSet<String>(Arrays.asList("done", "text", "type", "subject", "dateFrom", "dateTo"));
	}
	
	@Override
	public Set<String> databaseValues() {
		Set<String> result = new LinkedHashSet<String>();
		result.add(this.erledigt ? "'1'" : "'0'");
		result.add("'" + this.text + "'");
		result.add("'" + this.typ + "'");
		result.add("'" + this.fach + "'");
		result.add("to_date('" + this.toVector().get(1) + "', 'dd.mm.yyyy')");
		LocalDate d = new java.sql.Date(this.datumBis.getTimeInMillis()).toLocalDate();
		result.add("to_date('" + d.getYear() + "-" + (d.getMonthValue() - 1) + "-" + d.getDayOfMonth()
				+ "', 'yyyy-mm-dd')");
		return result;
	}
}