package ch.pentago.orb;

import java.awt.Color;

public class Plop {
	private int x;
	private int y;
	private int size;
	private boolean dead = false;
	private Color color;
	
	public Plop(int x, int y,Color color){
		this.x = x;
		this.y = y;
		size = 3;
		this.color = color;
	}
	
	public void move(){
		if(size <= 40){
			size++;
			dead = false;
		}
		else{
			dead = true;
		}
	}
	
	public boolean isDead(){
		return dead;
	}
	
	public int x(){
		return x;
	}
	
	public int y(){
		return y;
	}
	
	public int size(){
		return size;
	}
	
	public Color color(){
		return color;
	}
}
