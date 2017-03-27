package ch.pentago.orb;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.VolatileImage;

import javax.swing.JFrame;
/**
 * OrbPanel
 * @author kungfoo
 *
 */
public class OrbPanel extends JFrame implements MouseMotionListener{
	private static final long serialVersionUID = 1L;
	private final static int maxOrbs = 144;
	
	/**
	 * orb stuff
	 */
	private Orb[] orbs = new Orb[maxOrbs];
	// the last orb in the array
	private int lastOrb = 0;
	private int orbcount = 0;
	private Dimension dim;
	private Point mouseposition = new Point();
	private int initialOrbs = 4;
	
	private int score = 0;
	
	/**
	 * square stuff
	 */
	private int maxSquares = 17;
	private int lastSquare = 0;
	// suqares
	private Square[] squares = new Square[maxSquares];
	
	/**
	 * plop stuff
	 */
	private final static int maxPlops = maxOrbs;
	private Plop[] plops = new Plop[maxOrbs*2];
	private int lastPlop = 0;
	/*
	 * offscreen ressources for double buffering
	 */
	VolatileImage offImage;
	Graphics2D offGraphics;
	/*
	 * other cached-for-speed values
	 */
	Color color = new Color(20,20,20);
	private int health = 10;
	// background flashing
	private int red = 255;
	
	private AlphaComposite alphas[] = new AlphaComposite[10];
	
	// running boolean to stop game
	private boolean running = true;
	private int even = 0;
	
	public OrbPanel(){
		dim = new Dimension(600,600);
		
		for (int i = 0; i< 10; i++) {
			alphas[i] = AlphaComposite.getInstance(AlphaComposite.SRC_OVER,i/10);
		}
		squares[lastSquare++] = new Square(dim.width,dim.height);
		
		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setTitle("Orbs");
		setSize(dim);
		setResizable(false);
		this.addMouseMotionListener(this);
		new Thread(new Runnable(){
			public void run(){
			while(running){
				repaint();
			try {
				Thread.sleep(30);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			}}
		}).start();
	}
	
	public void offPaint(){
		tick();
		if(red <= 255){
			offGraphics.setBackground(new Color(255,red,red));
			red+=25;
		}else{
			offGraphics.setBackground(Color.WHITE);
		}
		offGraphics.clearRect(0, 0, dim.width, dim.height);
		offGraphics.setRenderingHint( RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON );
		// paint all orbs that are not yet null
		for(int i = 0; i < lastOrb; i++){
			if(orbs[i] != null){
				offGraphics.setColor(orbs[i].color());
				offGraphics.fillOval(orbs[i].x(), orbs[i].y(),orbs[i].radius()<<1,orbs[i].radius()<<1);
			}
		}
		
		// paint all squares
		for(int i = 0; i < lastSquare; i++){
			offGraphics.setColor(Color.GRAY);
			offGraphics.fillRect(squares[i].x(),squares[i].y(),squares[i].side(),squares[i].side());
			offGraphics.setColor(Color.BLACK);
			offGraphics.setStroke(new BasicStroke(2));
			offGraphics.drawRect(squares[i].x()-1,squares[i].y(), squares[i].side()+1, squares[i].side()+1);
		}
		
		// paint plops
		for(int i = 0; i < lastPlop; i++){
			if(plops[i] != null){
				offGraphics.setStroke(new BasicStroke(2));
				offGraphics.setColor(plops[i].color());
				offGraphics.drawOval(plops[i].x(), plops[i].y(), plops[i].size(), plops[i].size());
			}
		}
		offGraphics.setColor(Color.DARK_GRAY);
		offGraphics.setFont(new Font("Arial",Font.BOLD,14));
		offGraphics.drawString("level: "+initialOrbs+" score: "+score +"  health: "+health, 10, 40);
	}
	
	public void paint(Graphics g){
		offImage = createVolatileImage(dim.width, dim.height);
		offGraphics = (Graphics2D)offImage.getGraphics();
		if(offImage != null){
			offPaint();
			g.drawImage(offImage,0,0,this);
		}
	}
	
	private void tick(){
		// animate orbs
		for(int i = 0; i < lastOrb; i++){
			if(orbs[i] != null){
				orbs[i].move(mouseposition.x,mouseposition.y);
				for(int j = 0; j < lastSquare; j++){
					if(orbs[i] != null && orbs[i].intersects(squares[j])){
						// generate a plop here
						plops[(lastPlop++)%maxPlops] = new Plop(orbs[i].x(),orbs[i].y(),orbs[i].color());
						score += orbs[i].weight()+initialOrbs;
						orbs[i] = null;
						orbcount--;
						lastPlop = lastPlop%maxPlops;
					}
				}
			}
		}
		
		// animate squares
		for(int i = 0; i < lastSquare; i++){
			squares[i].move(mouseposition.x,mouseposition.y);
			if(squares[i].intersects(mouseposition)){
				health--;
				red = 0;
			}
			if(health == 0){
				running = false;
			}
		}
		
		// animate plops
		for(int i = 0; i < maxPlops; i++){
			if(plops[i] != null){
				plops[i].move();
				if(plops[i].isDead()){
					plops[i] = null;
				}
			}
		}
		
		// create new orbs if all are gone
		if(orbcount == 0){
			switch(even){
			case 0:
				initialOrbs += 1;
				even = 2;
				break;
			case 1:
				initialOrbs += 2;
				even = 0;
				break;
			case 2:
				initialOrbs += 2;
				even = 1;
				break;
			}
			
			lastOrb = 0;
			// decide whether to spawn at left/right border or top/bottom
			int spawn = (OrbRandomizer.getDouble() <= 0.5)?1:0;
			for(int i = 0; i < initialOrbs; i++){
				orbs[lastOrb++] = new Orb(dim.width,dim.height,spawn);
				orbcount++;
			}
			
			// create new square every 5 initial orbs
			if(initialOrbs %5 == 0){
				if(lastSquare+1 < maxSquares){
					squares[lastSquare++] = new Square(dim.width,dim.height);
				}
			}
		}
	}
	
	public static void main(String[] args) {
		new OrbPanel().setVisible(true);
	}
	
	public void mouseMoved(MouseEvent e) {
		mouseposition = e.getPoint();
	}
	
	public void mouseDragged(MouseEvent e) {
		mouseposition = e.getPoint();
	}
}
