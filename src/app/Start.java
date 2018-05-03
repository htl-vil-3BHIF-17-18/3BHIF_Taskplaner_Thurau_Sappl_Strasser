package app;

import javax.swing.SwingUtilities;

import gui.MainFrame;

public class Start {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new MainFrame("Taskplaner"); // Mainframe
			}
		});
	}
}