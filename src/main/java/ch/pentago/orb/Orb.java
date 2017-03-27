package ch.pentago.orb;

import java.awt.Color;
/**
 * Orbs displayed in the Orb Panel
 * @author kungfoo
 *
 */
public class Orb {
	private int x;
	private int y;
	private int radius;
	private int weight;
	private Color color;
	
	/*
	 * speed vector
	 */
	private float dx = 0;
	private float dy = 0;
	/**
	 * create new orb, within width and height
	 * @param width
	 * @param height
	 */
	public Orb(int width, int height,int spawn){
		switch(spawn){
		case 0:
			y = OrbRandomizer.getRandom(height);
			x = (OrbRandomizer.getDouble() <= 0.5) ? 0: width;
			break;
		case 1:
			y = (OrbRandomizer.getDouble() <= 0.5) ? 20: height;
			x = (OrbRandomizer.getRandom(width));
			break;
		}
		
		weight = OrbRandomizer.getRandom(50)+10;
		radius = 5;
		/*
		 * SMOOOOOOOOOOTH colors!!!
		 */
		if(weight <= 35){
			// transition from green to yellow
			color = (new Color((int)Math.floor(weight*7.2),255,0));
		}
		else{
			// transition from yellow to red
			color = (new Color(255,(int)Math.floor((255-((weight-35)*7.2))),0));
		}
	}
	
	
	/**
	 * move one tick towards mouse pointer, according to weight
	 * @param x
	 * @param y
	 */
	public void move(int x, int y){
		float ddx = this.x - x;
		float ddy = this.y - y;
		dx += (ddx)*weight*0.0001;
		dy += (ddy)*weight*0.0001;
		//System.out.println("dx,dy="+dx+","+dy);
		this.x -= dx;
		this.x -= ddx*0.032;
		this.y -= dy;
		this.y -= ddy*0.032;
	}
	
	public int weight(){
		return weight;
	}
	
	public int x(){
		return x;
	}
	
	public int y(){
		return y;
	}
	
	public Color color(){
		return color;
	}
	
	public int radius(){
		return radius;
	}
	
	/**
	 * returns true if the orb intersects this square
	 */
	public boolean intersects(Square sq){
		int dx = x + radius;
		int dy = y + radius;
		return (dx > sq.x() && dy > sq.y() && dx < sq.x()+sq.side() && dy < sq.y()+sq.side());
	}
}
