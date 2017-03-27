package ch.pentago.orb;

import java.util.Random;
/**
 * reduce overhead on creation of the orbs....
 * @author kungfoo
 *
 */
public class OrbRandomizer {
	private static boolean initialized = false;
	private static Random rand = new Random(System.nanoTime());
	public static int getRandom(int x){
		if(init()){
			return rand.nextInt(x);
		}
		else{
			return 10;
		}
	}
	
	public static double getDouble(){
		return rand.nextDouble();
	}
	
	private static boolean init(){
		if(!initialized){
			synchronized(rand){
				rand = new Random(System.nanoTime());
				initialized = true;
				return true;
			}
		}
		else{
			return true;
		}
	}
}
