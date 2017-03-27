package ch.pentago.core;

import java.util.Observable;

public class ObservableBoolean extends Observable {
	
	private boolean value;
	
	public ObservableBoolean(boolean value) {
		this.value = value;
	}
	
	public void set(boolean value) {
		if(this.value !=  value) {
			this.value = value;
			setChanged();
			notifyObservers();
		}
	}
	
	public boolean get() {
		return value;
	}

}
