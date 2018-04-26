package gui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.text.MaskFormatter;

import bll.Task;

public class EditDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = -9502059696809342L;
	private JLabel lbStatus = null;
	private JPanel radioButtons = null;
	private JRadioButton rbFertig = null;
	private JRadioButton rbUnFertig = null;
	private ButtonGroup group = new ButtonGroup();
	private JLabel lbVon = null;
	private JFormattedTextField tfVon = null;
	private JLabel lbFach = null;
	private JTextField tfFach = null;
	private JLabel lbTyp = null;
	private JComboBox<String> cbTyp = null;
	private JLabel lbBis = null;
	private JFormattedTextField tfBis = null;
	private JLabel lbText = null;
	private JTextField tfText = null;
	private JButton btnOk = null;
	private JButton btnCancel = null;
	private Task task = null;
	private TaskTable taskTable = null;
	private Object[] columNames = { "Status", "Von", "Fach", "Typ", "Bis", "Text" };

	public EditDialog(int selectedIndex, Task task) throws ParseException {
		this.task = task;
		this.initializeControls();
		this.fillControls();
		this.pack();
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.setVisible(true);
	}

	private void fillControls() {
		if(task.isErledigt())
			this.rbFertig.setSelected(true);
		else 
			this.rbUnFertig.setSelected(true);
		this.tfVon.setText(task.getDatumVon().toString());
	}

	private void initializeControls() throws ParseException {
		GridLayout grid = new GridLayout(7, 2);
		this.setLayout(grid);

		this.lbStatus = new JLabel("Status");
		this.radioButtons = new JPanel(new GridLayout(1, 2));
		this.rbFertig = new JRadioButton("Fertig");
		this.rbUnFertig = new JRadioButton("Unfertig");
		this.group.add(rbFertig);
		this.group.add(rbUnFertig);
		this.radioButtons.add(rbFertig);
		this.radioButtons.add(rbUnFertig);

		this.lbVon = new JLabel("Von");
		this.tfVon = new JFormattedTextField(new MaskFormatter("##.##.####"));

		this.lbFach = new JLabel("Fach");
		this.tfFach = new JTextField();

		this.lbTyp = new JLabel("Typ");
		this.cbTyp = new JComboBox<String>(new String[] { "Schularbeit", "Test", "Hausübung" });

		this.lbBis = new JLabel("Bis");
		this.tfBis = new JFormattedTextField(new MaskFormatter("##.##.####"));

		this.lbText = new JLabel("Text");
		this.tfText = new JTextField();

		this.btnOk = new JButton("OK");
		this.btnOk.addActionListener(this);

		this.btnCancel = new JButton("Cancel");
		this.btnCancel.addActionListener(this);

		this.add(this.lbStatus);
		this.add(radioButtons);
		this.add(lbVon);
		this.add(tfVon);
		this.add(lbFach);
		this.add(tfFach);
		this.add(lbTyp);
		this.add(cbTyp);
		this.add(lbBis);
		this.add(tfBis);
		this.add(lbText);
		this.add(tfText);
		this.add(this.btnOk);
		this.add(this.btnCancel);

		this.setModal(true);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println(taskTable.getTasks());
		if(e.getSource().equals(this.btnOk)) {
			
		}
		this.dispose();
	}

//	private boolean writeValuesToStudent() {
//		boolean isValid = false;
//		try {
//			if (!this.tfNumber.getText().trim().isEmpty() && !this.tfFirstName.getText().trim().isEmpty()
//					&& !this.tfLastName.getText().trim().isEmpty()) {
//				this.task.setKatalognummer(Integer.parseInt(this.tfNumber.getText()));
//				this.task.setVorname(this.tfFirstName.getText());
//				this.task.setNachname(this.tfLastName.getText());
//				isValid = true;
//			}
//
//		} catch (NumberFormatException ex) {
//			System.out.println("Textfield Number has the wrong value!" + ex.toString());
//		}
//		return isValid;
//	}
}
