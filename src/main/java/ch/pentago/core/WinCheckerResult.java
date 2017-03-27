package ch.pentago.core;

/**
 * result class enclosing results of the WinChecker Algorithm
 * @author kungfoo
 *
 */
public class WinCheckerResult {
	private int[][] winconfig = new int[6][6];
	private boolean won = false;
	
	public WinCheckerResult(boolean result, int[][] configuration){
		this.won = result;
		this.winconfig = configuration;
	}
	
	public boolean hasWon(){
		return won;
	}
	
	public int[][] getWinconfig() {
		return winconfig;
	}
}
