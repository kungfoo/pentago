/*
 * MainWindow.java
 *
 * Created on __DATE__, __TIME__
 */

package ch.pentago.ui;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.jdom.Document;
import org.jdom.Element;

import ch.pentago.client.ClientState;
import ch.pentago.network.NetworkException;

import com.pagosoft.plaf.PgsLookAndFeel;

/**
 * 
 * @author __USER__
 */
public class MainWindow extends javax.swing.JFrame {

	private static final long serialVersionUID = 2117018732674344501L;

	/** Creates new form MainWindow */
	public MainWindow() {

		try {
			UIManager.setLookAndFeel(new PgsLookAndFeel());
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		initComponents();
		this.setContentPane(new LoginPanel(this));

	}

	// GEN-BEGIN:initComponents
	// <editor-fold defaultstate="collapsed" desc=" Generated Code ">
	private void initComponents() {

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setTitle("Pentago");
		addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent evt) {
				formWindowClosing(evt);
			}
		});

		org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(
				getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(
				org.jdesktop.layout.GroupLayout.LEADING).add(0, 340,
				Short.MAX_VALUE));
		layout.setVerticalGroup(layout.createParallelGroup(
				org.jdesktop.layout.GroupLayout.LEADING).add(0, 424,
				Short.MAX_VALUE));
		pack();
	}// </editor-fold>//GEN-END:initComponents

	// GEN-FIRST:event_formWindowClosing
	/*
	 * disconnect from server, if connection exists
	 */
	private void formWindowClosing(java.awt.event.WindowEvent evt) {
		if (ClientState.currentUser != null) {
			Document packet = new Document(new Element("packet"));
			Element request = new Element("request");
			request.setAttribute("type", "disconnect");
			request.setAttribute("sessionid", ClientState.currentUser
					.getSessionId());
			packet.getRootElement().addContent(request);
			try {
				ClientState.currentUser.getStream().sendPacket(packet);
			} catch (NetworkException e) {
				e.printStackTrace();
			}
		}
	}// GEN-LAST:event_formWindowClosing

	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String args[]) {
		try {
			java.awt.EventQueue.invokeLater(new Runnable() {
				public void run() {
					new MainWindow().setVisible(true);
				}
			});
		} finally {
			if (ClientState.currentUser != null) {
				Document packet = new Document(new Element("packet"));
				Element request = new Element("request");
				request.setAttribute("type", "disconnect");
				request.setAttribute("sessionid", ClientState.currentUser
						.getSessionId());
				packet.getRootElement().addContent(request);
				try {
					ClientState.currentUser.getStream().sendPacket(packet);
				} catch (NetworkException e) {
					e.printStackTrace();
				}
			}
		}
	}

	// GEN-BEGIN:variables
	// Variables declaration - do not modify
	// End of variables declaration//GEN-END:variables

}