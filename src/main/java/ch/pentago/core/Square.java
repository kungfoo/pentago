package ch.pentago.core;

import java.util.Observable;

public class Square extends Observable {

	private int[][] board = new int[3][3];

	private double rotation = 0;

	public void placeMarble(int x, int y, int player) {
		assert( x >= 0 && x <= board.length && y >= 0 && y <= board[0].length):"illegal marble placement";
		board[x][y] = player;
		setChanged();
		notifyObservers();
	}

	public int getField(int x, int y) {
		return board[x][y];
	}
	
	public double getRotation() {
		return rotation;
	}

	public Square() {
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				board[i][j] = 0;
			}
		}
	}

	public void rotateRight() {
		int[][] temp = new int[3][3];

		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board.length; j++) {
				temp[2 - j][i] = board[i][j];
			}
		}

		board = temp;
		rotation = (rotation + Math.PI / 2) % (2 * Math.PI);
		setChanged();
		notifyObservers();
	}

	public void rotateLeft() {
		int[][] temp = new int[3][3];

		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board.length; j++) {
				temp[j][2 - i] = board[i][j];
			}
		}

		board = temp;
		rotation = (rotation - Math.PI / 2) % (2 * Math.PI);
		setChanged();
		notifyObservers();
	}
}
