package ch.pentago.ui;

import java.util.ArrayList;

import javax.swing.AbstractListModel;

import ch.pentago.core.User;

public class UserListModel extends AbstractListModel {

	private static final long serialVersionUID = -8309659254344720889L;
	
	private ArrayList<User> users = new ArrayList<User>();
	
	public void add(Object o) {
		users.add((User)o);
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				fireContentsChanged(this, 0, users.size());
			}
		});
		
	}
	
	public void clear() {
		users.clear();
	}

	public Object getElementAt(int index) {
		return users.get(index);
	}

	public int getSize() {
		return users.size();
	}

}
