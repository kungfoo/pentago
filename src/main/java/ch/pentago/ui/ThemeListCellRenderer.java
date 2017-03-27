package ch.pentago.ui;

import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import ch.pentago.core.Theme;

public class ThemeListCellRenderer extends JLabel implements ListCellRenderer {

	private static final long serialVersionUID = -1978382334488604066L;

	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {
		if (isSelected) {
			setBackground(list.getSelectionBackground());
			setForeground(list.getSelectionForeground());
		} else {
			setBackground(list.getBackground());
			setForeground(list.getForeground());
		}

		Theme theme = (Theme) value;
		if (theme != null) {
			ImageIcon icon = new ImageIcon("themes/" + theme.getName()
					+ "/screenshot.png");
			if (icon != null) {
				setIcon(icon);
			}
			setText(theme.getDescription());
		}
		return this;
	}

}
