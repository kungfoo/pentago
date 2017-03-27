package ch.pentago.ui;

import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import ch.pentago.core.Theme;

public class ColorListCellRenderer extends JLabel implements ListCellRenderer {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4995707276797574740L;
	JComboBox theme_combobox;
	
	public ColorListCellRenderer(JComboBox theme) {
		theme_combobox = theme;
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

		String color = (String) value;
		Theme thm = (Theme)theme_combobox.getSelectedItem();
		
		if (color != null) {
			ImageIcon icon = new ImageIcon("themes/" + thm.getName()+ "/" + color +".png");
	
			if (icon != null)
				setIcon(icon);
			if (color == "white")
				setText("Color one");
			else
				setText("Color two");
		}
		return this;
	}

}
