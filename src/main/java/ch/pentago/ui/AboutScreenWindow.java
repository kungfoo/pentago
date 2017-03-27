package ch.pentago.ui;

import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JWindow;

import ch.pentago.orb.OrbPanel;

public class AboutScreenWindow extends JWindow{
	
	private static final long serialVersionUID = -1990196865418243848L;

	public AboutScreenWindow(){
		this.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				if (e.getX() >= 540 && e.getX() <= 655 && e.getY() >= 120
						&& e.getY() <= 220) {
					new OrbPanel().setVisible(true);
					dispose();	
				}
				else{
					dispose();
				}
			}
		});
		int x = Toolkit.getDefaultToolkit().getScreenSize().width>>1;
		int y = Toolkit.getDefaultToolkit().getScreenSize().height>>1;
		x -= 675>>1;
		y -= 256>>1;
		this.setLocation(x, y);
		this.getContentPane().add(new AboutScreenPanel());
		this.pack();
		this.setVisible(true);
	}
	
	public static void main(String[] args) {
		new AboutScreenWindow();
	}
}
