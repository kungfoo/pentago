package ch.pentago.core;


public class WinChecker {
	private static int[][] state = new int[6][6];
	/*
	 * masks for the win situations
	 * ----------------------------------------------------------------------------
	 */
	
	/*
	 * straight diagonal
	 */
	private static int[][] diagonalMask = {	{1,0,0,0,0,0},
											{0,1,0,0,0,0},
											{0,0,1,0,0,0},
											{0,0,0,1,0,0},
											{0,0,0,0,1,0},
											{0,0,0,0,0,0}};
	/*
	 * straight five
	 */
	private static int[][] straightmask1 ={	{1,0,0,0,0,0},
											{1,0,0,0,0,0},
											{1,0,0,0,0,0},
											{1,0,0,0,0,0},
											{1,0,0,0,0,0},
											{0,0,0,0,0,0}};
	
	private static int[][] straightmask11 ={{0,0,0,0,0,0},
											{1,0,0,0,0,0},
											{1,0,0,0,0,0},
											{1,0,0,0,0,0},
											{1,0,0,0,0,0},
											{1,0,0,0,0,0}};
	
	private static int[][] straightmask2 ={	{0,1,0,0,0,0},
											{0,1,0,0,0,0},
											{0,1,0,0,0,0},
											{0,1,0,0,0,0},
											{0,1,0,0,0,0},
											{0,0,0,0,0,0}};
	
	private static int[][] straightmask21 ={{0,0,0,0,0,0},
											{0,1,0,0,0,0},
											{0,1,0,0,0,0},
											{0,1,0,0,0,0},
											{0,1,0,0,0,0},
											{0,1,0,0,0,0}};
	
	private static int[][] straightmask3 ={	{0,0,1,0,0,0},
											{0,0,1,0,0,0},
											{0,0,1,0,0,0},
											{0,0,1,0,0,0},
											{0,0,1,0,0,0},
											{0,0,0,0,0,0}};
	
	private static int[][] straightmask31 ={{0,0,0,0,0,0},
											{0,0,1,0,0,0},
											{0,0,1,0,0,0},
											{0,0,1,0,0,0},
											{0,0,1,0,0,0},
											{0,0,1,0,0,0}};
	/*
	 * monicas five
	 */
	private static int[][] monicas1 = 	{	{0,0,0,0,1,0},
											{0,0,0,1,0,0},
											{0,0,1,0,0,0},
											{0,1,0,0,0,0},
											{1,0,0,0,0,0},
											{0,0,0,0,0,0}};
	/*
	 * all masks together
	 */
	private static int[][][] patterns = {diagonalMask,straightmask1,straightmask11,straightmask2,straightmask21,straightmask3,straightmask31,monicas1};
	
	/*
	 * end of masks
	 * -----------------------------------------------------------------------------------
	 */
	public static WinCheckerResult checkWin(int color, Square[] squares){
		assert (color == 1 || color == 2): "color must be in {1,2}";
		assert (squares != null && squares.length == 4): "wrong number of squares given";
		int[][] result;
		if((result = checkWin(color, squares[0], squares[1], squares[2], squares[3])) != null){
			return new WinCheckerResult(true,result);
		}
		else{
			return new WinCheckerResult(false,null);
		}
	}
	
	
	private static int[][] checkWin(int color, Square topleft, Square topright, Square bottomleft, Square bottomright){
		// copy over values for all squares
		for(int square = 0; square < 4; square++){
			Square temp = null;
			int dx = 0;
			int dy = 0;
			switch(square){
			case 0:
				temp = topleft;
				break;
			case 1:
				temp = topright;
				dx = 3;
				break;
			case 2:
				temp = bottomleft;
				dy = 3;
				break;
			case 3:
				temp = bottomright;
				dx = 3;
				dy = 3;
				break;
			default:
				break;
			}
			for(int i = 0; i < 3; i++){
				for(int j = 0; j < 3; j++){
					state[dx+i][dy+j] = temp.getField(i, j);
				}
			}
		}
		return checkWin(color);
	}
	
	private static int[][] checkWin(int color){
		for(int mask = 0; mask < 8; mask++){
			int[][] pattern = patterns[mask];
			for(int i = 0; i < 4; i++){
				pattern = rotateRight(pattern);
				// apply pattern
				int[][] result = and(state,pattern);
				int count = 0;
				// count remaining balls
				for(int x = 0; x < 6; x++){
					for(int y = 0; y < 6; y++){
						count += (result[x][y]==color)?1:0;
					}
				}
				if(count >= 5){
					printstate(result);
					System.out.println("player "+color+" has won the game!!");
					return result;
				}
			}
		}
		return null;
	}
	
	/**
	 * rotates the given pattern clockwise, 90 degrees
	 * @param pattern
	 * @return
	 */
	private static int[][] rotateRight(int [][] pattern) {
		int[][] temp = new int[6][6];

		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 6; j++) {
				temp[5 - j][i] = pattern[i][j];
			}
		}

		return temp;
	}
	
	/**
	 * extracts all the fields from state that are 1's in the mask
	 * @param state
	 * @param pattern
	 * @return resulting state
	 */
	private static int[][] and(int[][] state, int[][]pattern){
		int[][] temp = new int[6][6];
		for(int i = 0; i < 6; i++){
			for(int j = 0; j < 6; j++){
				temp[i][j] = (pattern[i][j]==1)? state[i][j] :0;
			}
		}
		return temp;
	}
	
	private static void printstate(int[][] state){
		for(int i = 0; i < 6; i++){
			for(int j = 0; j < 6;j++){
				System.out.print("["+state[j][i]+"]");
			}
			System.out.println("");
		}
		System.out.println("------------------------------------------------");
	}
	
}
