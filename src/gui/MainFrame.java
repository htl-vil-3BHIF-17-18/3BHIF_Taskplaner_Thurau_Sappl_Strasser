package gui;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.util.ArrayList;
import java.util.TreeSet;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.text.DateFormatter;

import bll.Task;

public class MainFrame extends JFrame {

	private static final long serialVersionUID = -8026416994513756565L;
	private JMenuBar menuBar;
	private JMenu start;
	private JMenuItem load;
	private JMenuItem save;
	private JPanel inputFields;
	private JComboBox<String> taskType;
	private JFormattedTextField fromDate;
	private JFormattedTextField toDate;
	private JButton showTasks;
	private TaskList list;
	private static String[] comboBoxTypes = { "Schularbeit", "Test", "Hausübung" };

	public MainFrame(String title) {
		super();
		this.setTitle(title);
		this.initializeControls();
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.pack();
		this.setVisible(true);
	}

	private void initializeControls() {
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
		this.fromDate = new JFormattedTextField(new DateFormatter());
		this.toDate = new JFormattedTextField(new DateFormatter());
		this.taskType = new JComboBox<String>(comboBoxTypes);
		this.showTasks = new JButton("Tasks anzeigen");
		this.inputFields = new JPanel(new FlowLayout());
		this.inputFields.add(fromDate);
		this.inputFields.add(toDate);
		this.inputFields.add(taskType);
		this.inputFields.add(showTasks);

		this.list = new TaskList(new TreeSet<Task>());
		
		this.setLayout(new GridLayout(2, 1));
		this.add(inputFields);
		this.add(list);
		
		//TODO: add eventListeners
	}

}
