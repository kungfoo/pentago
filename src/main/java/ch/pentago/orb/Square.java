package ch.pentago.orb;

import java.awt.Point;

/**
 * squares used in the orb game
 * @author kungfoo
 *
 */
public class Square {
	// position of this square
	private int x;
	private int y;
	
	// motion state
	private final static int IN = 0;
	private final static int OUT = 1;
	private int direction;
	// maximum size of this square, between 20 and 50
	private int maxSize;
	// speed of size change
	private float sizeSpeed;
	
	// length of this squares side
	private int side;
	
	// speed vector of this square
	private float dx;
	private float dy;
	
	// for movers
	private boolean mouseAttached = false;
	
	public Square(int w, int h){
		side = 26;
		x = OrbRandomizer.getRandom(w-side);
		y = OrbRandomizer.getRandom(h-side);
		maxSize = OrbRandomizer.getRandom(70)+30;
		sizeSpeed = (float)OrbRandomizer.getDouble()+0.6f;
		mouseAttached = (OrbRandomizer.getDouble() <= 0.6)?true:false;
		if(!mouseAttached){
			// make static squares bigger
			maxSize *= 1.3;
		}
	}
	
	/**
	 * returns true if the orb intersects this square
	 * @param orb
	 * @return
	 */
	public boolean intersects(Orb orb){
		return ((orb.x() >= x && orb.y() >= y) && orb.x()<=(x+side) && orb.y() <= (y+side));
	}
	
	public boolean intersects(Point p){
		return (p.x >= x && p.y >= y && p.x <= x+side && p.y <= y+side);
	}
	
	public int x(){
		return x;
	}
	
	public int y(){
		return y;
	}
	
	public int side(){
		return side;
	}
	
	/*
	 * move one tick towards the mouse pointer
	 */
	public void move(int mousex, int mousey){
		int oldside = side;
		if(side < 10){
			direction = OUT;
		}
		else if (side >= maxSize){
			direction = IN;
		}
		switch(direction){
		case IN:
			side -= sizeSpeed*3;
			break;
		case OUT:
			side += sizeSpeed*3;
			break;
		}
		
		// movement for mouseAttached Squares
		if(mouseAttached){
			float ddx = this.x - mousex;
			float ddy = this.y - mousey;
			dx += (ddx)*side*0.0001;
			dy += (ddy)*side*0.0001;
			//System.out.println("dx,dy="+dx+","+dy);
			this.x -= dx*1.3;
			this.x -= ddx*0.032;
			this.y -= dy*1.3;
			this.y -= ddy*0.032;
		}
		// we have a static one here :D
		else{
			x = x +(side-oldside);
			y = y -(side-oldside);
		}
	}
}
