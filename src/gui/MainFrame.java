package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
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
import javax.swing.text.MaskFormatter;

import bll.Task;

public class MainFrame extends JFrame {

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
	private static String[] comboBoxTypes = { "Schularbeit", "Test", "Haus�bung" };

	public MainFrame(String title) {
		super();
		this.setTitle(title);
		try {
			this.initializeControls();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setPreferredSize(new Dimension(700, 500));
		this.pack();
		this.setVisible(true);
	}

	private void initializeControls() throws ParseException {
		// Men�-Kram
		this.menuBar = new JMenuBar();
		this.start = new JMenu("Start");
		this.load = new JMenuItem("Von DB laden ...");
		this.save = new JMenuItem("In DB speichern ...");
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
		set.add(new Task(true, new GregorianCalendar(2018, 4, 25), "POS", "PLF", new GregorianCalendar(2018, 4, 26), "nix"));
		set.add(new Task(true, new GregorianCalendar(2018, 5, 25), "POS", "PLF", new GregorianCalendar(2018, 4, 26), "nix"));
		set.add(new Task(true, new GregorianCalendar(2018, 6, 25), "POS", "PLF", new GregorianCalendar(2018, 4, 26), "nix"));

		this.table = new TaskTable(set);

		this.setLayout(new BorderLayout());
		this.add(inputFields, BorderLayout.PAGE_START);
		this.add(table, BorderLayout.CENTER);

		// TODO: add eventListeners
	}

}
