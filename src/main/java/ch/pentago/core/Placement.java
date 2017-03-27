package ch.pentago.core;

public class Placement {
	
	private Square square;
	private int x,y;
	
	public Placement(Square square, int x, int y) {
		assert (x>=0 && x<=2 && y<=2 && y>=0): "illegal placment";
		
		this.square = square;
		this.x = x;
		this.y = y;
	}
	
	public void place(int player) {
		square.placeMarble(x, y, player);
	}
}
