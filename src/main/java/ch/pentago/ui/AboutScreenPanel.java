package ch.pentago.ui;

/*
 * AboutScreen.java
 *
 * Created on __DATE__, __TIME__
 */

import javax.swing.ImageIcon;

/**
 *
 * @author  __USER__
 */
public class AboutScreenPanel extends javax.swing.JPanel {

	private static final long serialVersionUID = -1018090713348138809L;

	/** Creates new form AboutScreen */
	public AboutScreenPanel() {
		initComponents();
		ImageIcon about = new ImageIcon("themes/about.png");
		about_image.setIcon(about);
	}

	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
	//GEN-BEGIN:initComponents
	// <editor-fold defaultstate="collapsed" desc=" Generated Code ">
	private void initComponents() {
		about_image = new javax.swing.JLabel();

		setPreferredSize(new java.awt.Dimension(675, 256));

		org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(
				this);
		this.setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(
				org.jdesktop.layout.GroupLayout.LEADING).add(about_image,
				org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 675,
				org.jdesktop.layout.GroupLayout.PREFERRED_SIZE));
		layout.setVerticalGroup(layout.createParallelGroup(
				org.jdesktop.layout.GroupLayout.LEADING).add(about_image,
				org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 256,
				org.jdesktop.layout.GroupLayout.PREFERRED_SIZE));
	}// </editor-fold>//GEN-END:initComponents

	//GEN-BEGIN:variables
	// Variables declaration - do not modify
	private javax.swing.JLabel about_image;
	// End of variables declaration//GEN-END:variables

}