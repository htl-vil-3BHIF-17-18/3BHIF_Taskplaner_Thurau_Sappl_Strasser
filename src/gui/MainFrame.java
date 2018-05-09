package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.time.ZoneId;
import java.util.GregorianCalendar;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ExecutionException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.SwingWorker.StateValue;

import bll.DatabaseTaskWrapper;
import bll.Task;
import swingworkers.SwingWorkerGetTasks;
import swingworkers.SwingWorkerGetTasksWithCondition;
import swingworkers.SwingWorkerSetTasks;
import util.PropertyManager;

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
	private DateTextField fromDate;
	private DateTextField toDate;
	private JButton showTasks;
	private JScrollPane scrollPane;
	private TaskTable table;
	private JPopupMenu popup;
	private JMenuItem newItem;
	private JMenuItem edit;
	private JMenuItem delete;
	private static String[] comboBoxTypes = { "Alle", "Schularbeit", "Test", "Hausübung" };
	private DatabaseTaskWrapper dbw = null;

	public MainFrame(String title) {
		super();
		this.setTitle(title);
		try {
			this.initializeControls();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		try {
			this.dbw = new DatabaseTaskWrapper(PropertyManager.getInstance().readProperty("ConnectionString"));
		} catch (FileNotFoundException e) {
			System.out.println("Config-File not found!");
		} catch (IOException e) {
			System.out.println("An IO-Exception occured!");
		}
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
		this.fromDate = new DateTextField();
		this.toDate = new DateTextField();
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

		this.table = new TaskTable(set);
		this.scrollPane = new JScrollPane(table);
		this.setLayout(new BorderLayout());
		this.add(inputFields, BorderLayout.PAGE_START);
		this.add(scrollPane, BorderLayout.CENTER);
		
		table.setAutoCreateRowSorter(true);

		// Rechtsklick-Menü:
		popup = new JPopupMenu();
		delete = new JMenuItem("Löschen");
		edit = new JMenuItem("Bearbeiten");
		newItem = new JMenuItem("Neu");
		popup.add(newItem);
		popup.add(edit);
		popup.add(delete);

		// EventListener für Rechtsklick:
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
			if (this.table.getSelectedRow() < 0 || this.table.getSelectedRow() > this.table.getModel().getRowCount()) {
				return;
			}

			try {
				new EditDialog(this.table, (Task) this.table.getTasks().toArray()[this.table.getSelectedRow()], false);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		} else if (arg0.getSource().equals(this.load)) {
			SwingWorkerGetTasks swgt = new SwingWorkerGetTasks(dbw);
			swgt.addPropertyChangeListener(new PropertyChangeListener() {
				@Override
				public void propertyChange(PropertyChangeEvent evt) {
					switch (evt.getPropertyName()) {
						case "progress":
							showTasks.setEnabled(false);
							break;
						case "state":
							switch ((StateValue) evt.getNewValue()) {
								case DONE:
									try {
										table.setTasks(swgt.get());
										showTasks.setEnabled(true);
										JOptionPane.showMessageDialog(
												mainframe,
												"Laden von DB erfolgreich.",
												"Information",
												JOptionPane.INFORMATION_MESSAGE
										);
									} catch (InterruptedException e) {
										JOptionPane.showMessageDialog(
												mainframe,
												"Laden von DB NICHT erfolgreich.",
												"Information",
												JOptionPane.ERROR_MESSAGE
										);
									} catch (ExecutionException e) {
										JOptionPane.showMessageDialog(
												mainframe,
												"Laden von DB NICHT erfolgreich.",
												"Information",
												JOptionPane.ERROR_MESSAGE
										);
									}
									break;
								case STARTED:
									showTasks.setEnabled(false);
									break;
								case PENDING:
									showTasks.setEnabled(false);
									break;
							}
							break;
					}
				}
			});
			swgt.execute();
		} else if (arg0.getSource().equals(this.save)) {
			SwingWorkerSetTasks swst = new SwingWorkerSetTasks(dbw, table.getTasks());
			swst.addPropertyChangeListener(new PropertyChangeListener() {
				@Override
				public void propertyChange(PropertyChangeEvent evt) {
					switch (evt.getPropertyName()) {
						case "progress":
							showTasks.setEnabled(false);
							break;
						case "state":
							switch ((StateValue) evt.getNewValue()) {
								case DONE:
									try {
										swst.get();
										showTasks.setEnabled(true);
										JOptionPane.showMessageDialog(
												mainframe,
												"Speichern in DB erfolgreich.",
												"Information",
												JOptionPane.INFORMATION_MESSAGE
										);
									} catch (InterruptedException e) {
										JOptionPane.showMessageDialog(
												mainframe,
												"Speichern in DB NICHT erfolgreich.",
												"Information",
												JOptionPane.ERROR_MESSAGE
										);
									} catch (ExecutionException e) {
										JOptionPane.showMessageDialog(
												mainframe,
												"Speichern in DB NICHT erfolgreich.",
												"Information",
												JOptionPane.ERROR_MESSAGE
										);
									}
									break;
								case STARTED:
									showTasks.setEnabled(false);
									break;
								case PENDING:
									showTasks.setEnabled(false);
									break;
							}
							break;
					}
				}
			});
			swst.execute();
		} else if (arg0.getSource().equals(this.showTasks)) {
			SwingWorkerGetTasksWithCondition swgt = new SwingWorkerGetTasksWithCondition(dbw, processUserInput());
			swgt.addPropertyChangeListener(new PropertyChangeListener() {
				@Override
				public void propertyChange(PropertyChangeEvent evt) {
					switch (evt.getPropertyName()) {
						case "progress":
							showTasks.setEnabled(false);
							break;
						case "state":
							switch ((StateValue) evt.getNewValue()) {
								case DONE:
									try {
										table.setTasks(swgt.get());
										showTasks.setEnabled(true);
										JOptionPane.showMessageDialog(
												mainframe,
												"Laden von DB erfolgreich.",
												"Information",
												JOptionPane.INFORMATION_MESSAGE
										);
									} catch (InterruptedException e) {
										JOptionPane.showMessageDialog(
												mainframe,
												"Laden von DB NICHT erfolgreich.",
												"Information",
												JOptionPane.ERROR_MESSAGE
										);
									} catch (ExecutionException e) {
										JOptionPane.showMessageDialog(
												mainframe,
												"Laden von DB NICHT erfolgreich.",
												"Information",
												JOptionPane.ERROR_MESSAGE
										);
									}
									break;
								case STARTED:
									showTasks.setEnabled(false);
									break;
								case PENDING:
									showTasks.setEnabled(false);
									break;
							}
							break;
					}
				}
			});
			swgt.execute();
		} else if (arg0.getSource().equals(newItem)) {
			try {
				new EditDialog(table,
						new Task(false, new GregorianCalendar(), "", "Hausübung", new GregorianCalendar(), ""), true);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		} else if (arg0.getSource().equals(delete)) {
			if (this.table.getSelectedRow() < 0 || this.table.getSelectedRow() > this.table.getModel().getRowCount()) {
				return;
			}
			this.table.removeSelectedTask();
		}

	}

	private String processUserInput() {
		GregorianCalendar von = new GregorianCalendar(
				fromDate.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getYear(),
				fromDate.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getMonthValue() - 1,
				fromDate.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getDayOfMonth()
		);
		String typ = (String) this.taskType.getSelectedItem();
		GregorianCalendar bis = new GregorianCalendar(
				toDate.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getYear(),
				toDate.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getMonthValue() - 1,
				toDate.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getDayOfMonth()
		);
		
		System.out.println(
				"GC: " + von.toInstant().toString()
				+ "SQLD: " + new java.sql.Date(von.getTimeInMillis()).toString()
		);
		
		if (typ != "Alle")
			return "type = '" + typ + "' AND "
					+ "dateFrom > to_date('"
						+ new java.sql.Date(von.getTimeInMillis()).toString()
					+ "', 'yyyy-mm-dd') AND "
					+ "dateFrom < to_date('"
						+ new java.sql.Date(bis.getTimeInMillis()).toString()
					+ "', 'yyyy-mm-dd')";
		else
			return "dateFrom > to_date('"
							+ new java.sql.Date(von.getTimeInMillis()).toString()
					+ "', 'yyyy-mm-dd') AND "
					+ "dateFrom < to_date('"
						+ new java.sql.Date(bis.getTimeInMillis()).toString()
					+ "', 'yyyy-mm-dd')";
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