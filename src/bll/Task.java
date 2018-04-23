package bll;

import java.util.Date;

import java.util.HashMap;
import java.util.Map;

import dal.DatabaseMapable;

public class Task implements DatabaseMapable, Comparable<Task> {
	private String text;
	private Date datumBis;
	private Date datumVon;
	private String typ;
	private String fach;
	private boolean erledigt;

	public Task(String text, Date datumVon, Date datumBis, String typ, String fach) {
		super();
		this.text = text;
		this.datumVon = datumVon;
		this.datumBis = datumBis;
		this.typ = typ;
		this.fach = fach;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Date getDatumBis() {
		return datumBis;
	}

	public void setDatumBis(Date datumBis) {
		this.datumBis = datumBis;
	}

	public Date getDatumVon() {
		return datumVon;
	}

	public void setDatumVon(Date datumVon) {
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
	 * Gibt eine Map (Key: Feld, Value: Spaltenname) zurück.
	 * 
	 * @return {@link Map}
	 */
	@Override
	public Map<String, String> columnFieldMap() {
		Map<String, String> columnField = new HashMap<String, String>();
		columnField.put("text", "text");
		columnField.put("datum", "date");
		columnField.put("datumVon", "date");
		columnField.put("datumBis", "date");
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

}
