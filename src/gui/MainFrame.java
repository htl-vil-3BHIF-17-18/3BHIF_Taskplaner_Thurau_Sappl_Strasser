package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.ParseException;
import java.util.GregorianCalendar;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.text.MaskFormatter;

import bll.Task;
import dal.DatabaseHandler;
import dal.DatabaseWrapper;

public class MainFrame extends JFrame implements ActionListener {

	private static final long serialVersionUID = -8026416994513756565L;
	private MainFrame mainframe = this;
	private JMenuBar menuBar;
	private JMenu start;
	private JMenuItem load;
	private JMenuItem save;
	private JPanel inputFields;
	private JComboBox<String> taskType;
	private JLabel lbFromDate, lbToDate;
	private JFormattedTextField fromDate;
	private JFormattedTextField toDate;
	private JButton showTasks;
	private JScrollPane scrollPane;
	private TaskTable table;
	private JPopupMenu popup;
	private JMenuItem newItem;
	private JMenuItem edit;
	private JMenuItem delete;
	private static String[] comboBoxTypes = { "Alle", "Schularbeit", "Test", "Haus�bung" };
	private DatabaseHandler dbh = null;
	private DatabaseWrapper dbw = null;

	public MainFrame(String title) {
		super();
		this.setTitle(title);
		try {
			this.initializeControls();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		this.dbh = new DatabaseHandler("jdbc:oracle:thin:d3b20/d3b@212.152.179.117:1521:ora11g");
		this.dbh.initialize();
		this.dbw = new DatabaseWrapper(dbh);
		// --- [Begin] Debug
		// System.out.println(this.dbw.createTasksTable());
		// --- [End ] Debug
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setPreferredSize(new Dimension(700, 500));
		this.pack();
		this.setVisible(true);

	}

	private void initializeControls() throws ParseException {
		// Men�-Kram
		this.menuBar = new JMenuBar();
		this.start = new JMenu("Start");
		this.load = new JMenuItem("Von DB laden");
		this.save = new JMenuItem("In DB speichern");
		this.start.add(load);
		this.start.add(save);
		this.menuBar.add(start);
		this.setJMenuBar(menuBar);

		// Input-Fields
		this.lbFromDate = new JLabel("von: ");
		this.lbToDate = new JLabel("bis: ");
		this.fromDate = new JFormattedTextField(new MaskFormatter("##.##.####"));
		this.toDate = new JFormattedTextField(new MaskFormatter("##.##.####"));
		this.taskType = new JComboBox<String>(comboBoxTypes);
		this.showTasks = new JButton("Tasks anzeigen");
		this.inputFields = new JPanel(new FlowLayout());
		this.inputFields.add(lbFromDate);
		this.inputFields.add(fromDate);
		this.inputFields.add(lbToDate);
		this.inputFields.add(toDate);
		this.inputFields.add(taskType);
		this.inputFields.add(showTasks);

		// Testdaten f�r GUI:
		Set<Task> set = new TreeSet<Task>();
		set.add(new Task(false, new GregorianCalendar(2018, 4, 20), "POS", "Haus�bung",
				new GregorianCalendar(2018, 5, 5), "Taskplaner implementieren"));
		set.add(new Task(false, new GregorianCalendar(2018, 4, 25), "TINF", "Haus�bung",
				new GregorianCalendar(2018, 4, 26), "�bung 13785"));
		set.add(new Task(true, new GregorianCalendar(2018, 4, 26), "Deutsch", "Schularbeit",
				new GregorianCalendar(2018, 4, 26), "Textbezogene Er�rterung"));
		set.add(new Task(false, new GregorianCalendar(2018, 5, 26), "SYP", "Test", new GregorianCalendar(2018, 5, 26),
				"nix"));

		this.table = new TaskTable(set);
		this.scrollPane = new JScrollPane(table);
		this.setLayout(new BorderLayout());
		this.add(inputFields, BorderLayout.PAGE_START);
		this.add(scrollPane, BorderLayout.CENTER);

		// Rechtsklick-Men�:
		popup = new JPopupMenu();
		delete = new JMenuItem("L�schen");
		edit = new JMenuItem("Bearbeiten");
		newItem = new JMenuItem("Neu");
		popup.add(newItem);
		popup.add(edit);
		popup.add(delete);

		// EventListener f�r Rechtsklick:
		MouseListener popupListener = new PopupListener(popup);
		this.table.addMouseListener(popupListener);

		// DONE: add eventListeners
		delete.addActionListener(this);
		edit.addActionListener(this);
		newItem.addActionListener(this);
		showTasks.addActionListener(this);
		load.addActionListener(this);
		save.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getSource().equals(edit)) {
			try {
				new EditDialog(this.table, (Task) this.table.getTasks().toArray()[this.table.getSelectedRow()], false);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		} else if (arg0.getSource().equals(this.load)) {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
			    public void run() {
					table.setTasks(dbw.getTasks());
					JOptionPane.showMessageDialog(
							mainframe,
							"Laden von DB erfolgreich.",
							"Information",
							JOptionPane.INFORMATION_MESSAGE
					);
				}
			});
		} else if (arg0.getSource().equals(this.save)) {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
			    public void run() {
					dbw.setTasks(table.getTasks());
					JOptionPane.showMessageDialog(
							mainframe,
							"Speichern in DB erfolgreich.",
							"Information",
							JOptionPane.INFORMATION_MESSAGE
					);
				}
			});
		} else if (arg0.getSource().equals(this.showTasks)) {
			this.table.setTasks(this.processUserInput());
		} else if (arg0.getSource().equals(newItem)) {
			try {
				new EditDialog(table,
						new Task(false, new GregorianCalendar(), "", "Haus�bung", new GregorianCalendar(), ""), true);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		} else if (arg0.getSource().equals(delete)) {
			this.table.removeSelectedTask();
		}

	}

	private Set<Task> processUserInput() {
		GregorianCalendar von = new GregorianCalendar(Integer.valueOf(this.fromDate.getText().split("\\.")[2]),
				Integer.valueOf(this.fromDate.getText().split("\\.")[1]),
				Integer.valueOf(this.fromDate.getText().split("\\.")[0]));
		String typ = (String) this.taskType.getSelectedItem();
		GregorianCalendar bis = new GregorianCalendar(Integer.valueOf(this.toDate.getText().split("\\.")[2]),
				Integer.valueOf(this.toDate.getText().split("\\.")[1]),
				Integer.valueOf(this.toDate.getText().split("\\.")[0]));
		//Monat wird nullbasiert abgespeichert, deswegen die Eingabe um 1 vermindern
		von.set(GregorianCalendar.MONTH, von.get(GregorianCalendar.MONTH) - 1);
		bis.set(GregorianCalendar.MONTH, von.get(GregorianCalendar.MONTH) - 1);
		// das da funktioniert:
		if (typ != "alle")
			return this.dbw.getTasks("type = '" + typ + "' AND " + "dateFrom > to_date('"
					+ new java.sql.Date(von.getTimeInMillis()).toString() + "', 'yyyy-mm-dd') AND "
					+ "dateFrom < to_date('" + new java.sql.Date(bis.getTimeInMillis()).toString()
					+ "', 'yyyy-mm-dd')");
		else
			//das nicht
			//TODO: alle Tasks zwischen 2 Zeitpunkten unabh�ngig vom Typ aus der DB herauskitzeln
			return this.dbw.getTasks(
					// joestr an  @thuraua: type = ... weglossn?
					//"type = '%' AND " + 
							"dateFrom > to_date('" + new java.sql.Date(von.getTimeInMillis()).toString()
							+ "', 'yyyy-mm-dd') AND " + "dateFrom < to_date('"
							+ new java.sql.Date(bis.getTimeInMillis()).toString() + "', 'yyyy-mm-dd')");
	}

	class PopupListener extends MouseAdapter {
		JPopupMenu popup;

		PopupListener(JPopupMenu popupMenu) {
			popup = popupMenu;
		}

		public void mousePressed(MouseEvent e) {
			maybeShowPopup(e);
		}

		public void mouseReleased(MouseEvent e) {
			maybeShowPopup(e);
		}

		private void maybeShowPopup(MouseEvent e) {
			if (e.isPopupTrigger()) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		}
	}
}