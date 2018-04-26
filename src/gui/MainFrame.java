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
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.text.MaskFormatter;

import bll.Task;
import dal.DatabaseHandler;
import dal.DatabaseWrapper;

public class MainFrame extends JFrame implements ActionListener {

	private static final long serialVersionUID = -8026416994513756565L;
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
	private TaskTable table;
	private JPopupMenu popup;
	private JMenuItem newItem;
	private JMenuItem edit;
	private JMenuItem delete;
	private static String[] comboBoxTypes = { "Schularbeit", "Test", "Hausübung" };
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
		this.dbw = new DatabaseWrapper(dbh);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setPreferredSize(new Dimension(700, 500));
		this.pack();
		this.setVisible(true);

	}

	private void initializeControls() throws ParseException {
		// Menü-Kram
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

		Set<Task> set = new TreeSet<Task>();
		set.add(new Task(true, new GregorianCalendar(2018, 4, 25), "POS", "PLF", new GregorianCalendar(2018, 4, 26),
				"nix"));
		set.add(new Task(true, new GregorianCalendar(2018, 5, 25), "POS", "PLF", new GregorianCalendar(2018, 4, 26),
				"nix"));
		set.add(new Task(true, new GregorianCalendar(2018, 6, 25), "POS", "PLF", new GregorianCalendar(2018, 4, 26),
				"nix"));

		this.table = new TaskTable(set);

		this.setLayout(new BorderLayout());
		this.add(inputFields, BorderLayout.PAGE_START);
		this.add(table, BorderLayout.CENTER);

		// Rechtsklick-Menü:
		popup = new JPopupMenu();
		delete = new JMenuItem("Löschen");
		edit = new JMenuItem("Bearbeiten");
		newItem = new JMenuItem("Neu");
		delete.addActionListener(this);
		edit.addActionListener(this);
		newItem.addActionListener(this);
		popup.add(edit);
		popup.add(delete);

		// EventListener für Rechtsklick:
		MouseListener popupListener = new PopupListener(popup);
		this.table.addMouseListener(popupListener);

		// TODO: add eventListeners
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getSource().equals(edit)) {
			try {
				EditDialog dialog = new EditDialog(this.table.getSelectedRow(),
						(Task) this.table.getTasks().toArray()[this.table.getSelectedRow()]);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		} else if (arg0.getSource().equals(this.load)) {
			this.table.setTasks(dbw.getTasks());
		} else if (arg0.getSource().equals(this.save)) {
			this.dbw.setTasks(this.table.getTasks());
		} else if (arg0.getSource().equals(this.showTasks)) {
			// TODO: Daten einlesen
		} else if (arg0.getSource().equals(newItem)) {

		} else if (arg0.getSource().equals(delete)) {

		}

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