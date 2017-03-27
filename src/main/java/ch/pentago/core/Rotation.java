package ch.pentago.core;

public class Rotation {
	
	/**
	 * Declare Constants
	 */
	public static final int RIGHT = 1;
	public static final int LEFT = -1;
	
	private Square square;
	private int direction;
	
	public Rotation (Square square, int direction) {
		assert (direction == 1 || direction == -1): "direction not in valid range";
		this.square = square;
		this.direction = direction;
	}
	
	public void rotate() {
		if (direction == Rotation.RIGHT) {
			square.rotateRight();
		} else {
			square.rotateLeft();
		}
	}
	

}
