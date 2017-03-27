/*
 * LoginPanel.java
 *
 * Created on __DATE__, __TIME__
 */

package ch.pentago.ui;

import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import ch.pentago.client.AuthenticationHandler;
import ch.pentago.xml.UserConfigManager;

/**
 *
 * @author __USER__
 */
public class LoginPanel extends javax.swing.JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7488047859000914763L;

	private java.util.List<String> userlist;
	private JFrame parent;
	private AuthenticationHandler handler = new AuthenticationHandler(this);

	/** Creates new form LoginPanel */
	public LoginPanel(JFrame parent) {
		this.parent = parent;
		initComponents();
		ImageIcon logoimg = new ImageIcon("themes/logosmall.png");
		pentago_icon.setIcon(logoimg);
	}

	// GEN-BEGIN:initComponents
	// <editor-fold defaultstate="collapsed" desc=" Generated Code ">
	private void initComponents() {
		pentago_icon = new javax.swing.JLabel();
		jPanel1 = new javax.swing.JPanel();
		username_label = new javax.swing.JLabel();
		password_label = new javax.swing.JLabel();
		password_text = new javax.swing.JPasswordField();
		username_combobox = new javax.swing.JComboBox();
		login_button = new javax.swing.JButton();
		jLabel1 = new javax.swing.JLabel();
		jLabel2 = new javax.swing.JLabel();
		
		pentago_icon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		pentago_icon.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

		username_label.setText("Username");

		password_label.setText("Password");

		password_text.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyPressed(java.awt.event.KeyEvent evt) {
				password_textKeyPressed(evt);
			}
		});

		username_combobox.setEditable(true);
		userlist = UserConfigManager.getUserNames();
		for (String user:userlist) {
			username_combobox.addItem(user);
			if (user.equals(UserConfigManager.getLastLogin()))
				username_combobox.setSelectedItem(user);
		}
		login_button.setText("login");
		login_button.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				login_buttonActionPerformed(evt);
			}
		});

		org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(
				jPanel1);
		jPanel1.setLayout(jPanel1Layout);
		jPanel1Layout
				.setHorizontalGroup(jPanel1Layout
						.createParallelGroup(
								org.jdesktop.layout.GroupLayout.LEADING)
						.add(
								jPanel1Layout
										.createSequentialGroup()
										.addContainerGap()
										.add(
												jPanel1Layout
														.createParallelGroup(
																org.jdesktop.layout.GroupLayout.LEADING,
																false).add(
																username_label)
														.add(password_label))
										.addPreferredGap(
												org.jdesktop.layout.LayoutStyle.RELATED)
										.add(
												jPanel1Layout
														.createParallelGroup(
																org.jdesktop.layout.GroupLayout.LEADING,
																false)
														.add(
																org.jdesktop.layout.GroupLayout.TRAILING,
																login_button,
																org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
																105,
																Short.MAX_VALUE)
														.add(password_text)
														.add(
																username_combobox,
																org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
																95,
																Short.MAX_VALUE))
										.addContainerGap(
												org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)));
		jPanel1Layout
				.setVerticalGroup(jPanel1Layout
						.createParallelGroup(
								org.jdesktop.layout.GroupLayout.LEADING)
						.add(
								jPanel1Layout
										.createSequentialGroup()
										.addContainerGap()
										.add(
												jPanel1Layout
														.createParallelGroup(
																org.jdesktop.layout.GroupLayout.BASELINE)
														.add(username_label)
														.add(
																username_combobox,
																org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
																org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
																org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
										.addPreferredGap(
												org.jdesktop.layout.LayoutStyle.RELATED)
										.add(
												jPanel1Layout
														.createParallelGroup(
																org.jdesktop.layout.GroupLayout.BASELINE)
														.add(password_label)
														.add(
																password_text,
																org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
																org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
																org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
										.addPreferredGap(
												org.jdesktop.layout.LayoutStyle.RELATED)
										.add(login_button).addContainerGap(38,
												Short.MAX_VALUE)));

		org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(
				this);
		this.setLayout(layout);
		layout
				.setHorizontalGroup(layout
						.createParallelGroup(
								org.jdesktop.layout.GroupLayout.LEADING)
						.add(
								org.jdesktop.layout.GroupLayout.TRAILING,
								layout
										.createSequentialGroup()
										.addContainerGap()
										.add(
												layout
														.createParallelGroup(
																org.jdesktop.layout.GroupLayout.TRAILING)
														.add(
																org.jdesktop.layout.GroupLayout.LEADING,
																pentago_icon,
																org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
																356,
																Short.MAX_VALUE)
														.add(
																org.jdesktop.layout.GroupLayout.LEADING,
																layout
																		.createSequentialGroup()
																		.add(
																				jLabel1,
																				org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
																				68,
																				Short.MAX_VALUE)
																		.addPreferredGap(
																				org.jdesktop.layout.LayoutStyle.RELATED)
																		.add(
																				jPanel1,
																				org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
																				org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
																				org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
																		.addPreferredGap(
																				org.jdesktop.layout.LayoutStyle.RELATED)
																		.add(
																				jLabel2,
																				org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
																				61,
																				Short.MAX_VALUE)))
										.addContainerGap()));
		layout
				.setVerticalGroup(layout
						.createParallelGroup(
								org.jdesktop.layout.GroupLayout.LEADING)
						.add(
								layout
										.createSequentialGroup()
										.addContainerGap()
										.add(
												pentago_icon,
												org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
												324, Short.MAX_VALUE)
										.addPreferredGap(
												org.jdesktop.layout.LayoutStyle.RELATED)
										.add(
												layout
														.createParallelGroup(
																org.jdesktop.layout.GroupLayout.TRAILING,
																false)
														.add(
																jLabel2,
																org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
																org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
																Short.MAX_VALUE)
														.add(
																jLabel1,
																org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
																org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
																Short.MAX_VALUE)
														.add(
																jPanel1,
																org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
																org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
																Short.MAX_VALUE))
										.addContainerGap()));
	}// </editor-fold>//GEN-END:initComponents

	// GEN-FIRST:event_login_buttonActionPerformed
	private void login_buttonActionPerformed(java.awt.event.ActionEvent evt) {
		
			String username = (String)username_combobox.getSelectedItem();
			String password = new String(password_text.getPassword());
			
			boolean result = handler.authorizeUser(username, password);
			if (result) {
				
				parent.setContentPane(new ChatPanel());
				parent.pack();
				
				UserConfigManager.setLastLogin(username);
				if (!userlist.contains(username)) {
					UserConfigManager.addNewUserName(username);
				}
			}
		
		// parent.setContentPane(new ChatPanel());
		// parent.pack();
	}// GEN-LAST:event_login_buttonActionPerformed

	// GEN-FIRST:event_password_textKeyPressed
	private void password_textKeyPressed(KeyEvent evt) {
		if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
			login_button.doClick();
		}
	}// GEN-LAST:event_password_textKeyPressed

	// GEN-BEGIN:variables
	// Variables declaration - do not modify
	private javax.swing.JLabel jLabel1;

	private javax.swing.JLabel jLabel2;

	private javax.swing.JPanel jPanel1;

	private javax.swing.JButton login_button;

	private javax.swing.JLabel password_label;

	private javax.swing.JPasswordField password_text;

	private javax.swing.JLabel pentago_icon;

	private javax.swing.JLabel username_label;

	private javax.swing.JComboBox username_combobox;
	// End of variables declaration//GEN-END:variables

}