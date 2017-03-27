package ch.pentago.core;

public abstract class GameMove {
	
	private int player;
	private Placement placement;
	private Rotation rotation;
	
	public GameMove(int player, Placement placement, Rotation rotation) {
		this.player = player;
		this.placement = placement;
		this.rotation = rotation;
	}
	
	public void doGameMove() {
		placement.place(player);
		rotation.rotate();
	}

	public Placement getPlacement() {
		return placement;
	}

	public Rotation getRotation() {
		return rotation;
	}

	public int getPlayer() {
		return player;
	}

}
