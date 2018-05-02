package gui;

import java.awt.Color;
import java.awt.Component;
import java.util.GregorianCalendar;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class TaskTableCellRenderer extends DefaultTableCellRenderer {

	private static final long serialVersionUID = 6204386928982458463L;

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int col) {
		Component comp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);

		String typ = table.getModel().getValueAt(row, 3).toString();
		String status = table.getModel().getValueAt(row, 0).toString();
		GregorianCalendar datumBis = new GregorianCalendar(
				Integer.parseInt(((String) table.getModel().getValueAt(row, 4)).split("\\.")[2]),
				Integer.parseInt(((String) table.getModel().getValueAt(row, 4)).split("\\.")[1]),
				Integer.parseInt(((String) table.getModel().getValueAt(row, 4)).split("\\.")[0]));
		GregorianCalendar now = new GregorianCalendar();
		now.set(GregorianCalendar.MONTH, now.get(GregorianCalendar.MONTH) + 1);

		// Unterscheidung bei Hausübungen, ob sie gemacht sind, noch gemacht werden
		// können oder ob die Abgabefrist abgelaufen ist
		if (typ.equalsIgnoreCase("hausübung")) {
			if (status.equalsIgnoreCase("erledigt")) {
				comp.setBackground(new Color(126, 247, 134));
			} else if (status.equalsIgnoreCase("offen") && datumBis.before(now)) {
				comp.setBackground(new Color(255, 158, 119));
			} else {
				comp.setBackground(new Color(247, 243, 126));
			}
			// Unterscheidung bei Schularbeiten und Tests, ob man diese hinter sich hat oder
			// nicht
		} else {
			if (datumBis.before(now)) {
				comp.setBackground(new Color(126, 247, 134));
			} else {
				comp.setBackground(new Color(247, 243, 126));
			}
		}

		return (comp);
	}
}