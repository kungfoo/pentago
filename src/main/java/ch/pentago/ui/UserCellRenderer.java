package ch.pentago.ui;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import ch.pentago.client.ImageLoader;
import ch.pentago.core.User;

public class UserCellRenderer extends JLabel implements ListCellRenderer {

	private static final long serialVersionUID = -2589470757400266691L;
	
	public UserCellRenderer() {
		this.setOpaque(true);
	}

	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {
		if (isSelected) {
			setBackground(list.getSelectionBackground());
			setForeground(list.getSelectionForeground());
		} else {
			setBackground(list.getBackground());
			setForeground(list.getForeground());
		}
		
		User user = (User)value;
		this.setText(user.getUserName());
		this.setIcon(ImageLoader.getIcon("user.png"));
		this.setPreferredSize(new Dimension(0,30));
		return this;
	}

}
