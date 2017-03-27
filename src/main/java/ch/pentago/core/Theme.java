package ch.pentago.core;

public class Theme {
	private String name;
	private String description;
	
	public Theme(String name, String desc){
		this.name = name;
		description = desc;
	}
	
	public String getName() {
		return name;
	}
	public String getDescription() {
		return description;
	}
}
