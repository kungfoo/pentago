package ch.pentago.network;

public class NetworkException extends Exception {
	private static final long serialVersionUID = -2415657417718568137L;
	private String msg;
	public NetworkException(String message) {
		msg = message;
	}
	
	@Override
	public StackTraceElement[] getStackTrace() {
		return super.getStackTrace();
	}
	
	@Override
	public String getMessage() {
		return super.getMessage() + "\n" + msg + "\n";
	}
}
