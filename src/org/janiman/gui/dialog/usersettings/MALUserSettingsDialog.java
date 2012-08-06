package org.janiman.gui.dialog.usersettings;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SwingWorker;

import org.janiman.parser.myanimelist.MALApi;

public class MALUserSettingsDialog extends JDialog {

	JPanel contentPanel;
	JLabel labelName, labelPassword;
	JTextField fieldName;
	JPasswordField fieldPassword;
	JButton buttonSave, buttonBack;
	JLabel labelInfo;
	JProgressBar progressBar;
	SwingWorker worker;

	public MALUserSettingsDialog(JFrame owner) {
		super(owner,true);
		initComponents();
		setUp();
	}

	private void initComponents() {
		contentPanel = new JPanel();

		labelName = new JLabel("My Anime List Username:");
		labelPassword = new JLabel("Password");

		fieldName = new JTextField();
		fieldPassword = new JPasswordField();
		labelInfo = new JLabel();

		buttonSave = new JButton("Save");
		buttonSave.setActionCommand("save");
		buttonSave.addActionListener(new MALUserSettingsViewController());

		buttonBack = new JButton("Back");
		buttonBack.setActionCommand("back");
		buttonBack.addActionListener(new MALUserSettingsViewController());
		
		progressBar = new JProgressBar();

		progressBar.setIndeterminate(false);
	}

	private void setUp() {
		contentPanel.setLayout(new GridLayout(0, 2));
		contentPanel.add(labelName);
		contentPanel.add(fieldName);

		contentPanel.add(labelPassword);
		contentPanel.add(fieldPassword);

		contentPanel.add(labelInfo);
		contentPanel.add(progressBar);

		contentPanel.add(buttonBack);
		contentPanel.add(buttonSave);

		super.add(contentPanel);

	}

	private void close() {
		super.dispose();
	}

	class MALUserSettingsViewController implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getActionCommand()=="save")
			{
				worker = new SwingWorker(){

					@Override
					protected Object doInBackground() throws Exception {
						String name = fieldName.getText();
						String pw = String.valueOf(fieldPassword.getPassword());
						name=name.trim();
						pw=pw.trim();
						
						//Beginn
						progressBar.setIndeterminate(true);
						buttonSave.setEnabled(false);
						buttonBack.setEnabled(false);
						
						if(!name.isEmpty() && !pw.isEmpty())
						{
						//Test if input Correct
						labelInfo.setText("verifying credentials");
						progressBar.setEnabled(true);
						MALApi api = MALApi.getInstance();
						int malId = api.verifyCredentials(name, pw);
						
						if(malId==-1)
						{
							labelInfo.setText("Invalid Credentials");
						}
						else
						{
							 MALUserFactory.getInstance().saveUserData(name,pw,malId);
							 labelInfo.setText("Success");
							 System.out.println(malId);
							
						}
						}
						
						else
						{
							labelInfo.setText("Invalid Input");
						}
						//After work+
						progressBar.setIndeterminate(false);
						buttonSave.setEnabled(true);
						buttonBack.setEnabled(true);
						return null;
					}
					
					
				
		
				};
				worker.execute();
			}
			if(e.getActionCommand()=="back")
			{
				String name = fieldName.getText();
				String pw = String.valueOf(fieldPassword.getPassword());
				pw=name.trim();
				System.out.println(name);
				System.out.println(pw);
				close();
			}
			
		}
	}

}
