package ch.pentago.database;


public class DataBaseException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3395169348210789643L;
	private String message;
	
	public DataBaseException(String message){
		this.message = message;
	}
	@Override
	public String getMessage() {
		return "DataBaseConnection: " + this.message;
	}
}
