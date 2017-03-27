package ch.pentago.core;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import ch.pentago.client.ClientState;
import ch.pentago.database.DataBaseConnection;
import ch.pentago.database.DataBaseException;
import ch.pentago.server.ServerState;
import ch.pentago.server.jobs.SendInTurnJob;
import ch.pentago.server.jobs.SendMessageJob;
import ch.pentago.server.receivers.GameMessageReceiver;

public class Game {
	/**
	 * The two players participating 
	 **/
	private User player1;
	private User player2;
	// reference to user who has the current turn
	private User turn;
	private Map<String, Integer> colormap = new HashMap<String, Integer>();
	public final static int ROTATING = 1;
	public final static int PLACING = 2;
	public final static int FINISHED = 3;
	private int state = PLACING;
	private int moves = 0;
	
	// squares to hold the data
	private Square squares[] = new Square[4];
	
	// GameMessageReceiver subscribing to the two players publishers
	private GameMessageReceiver messageReceiver;
	
	public Game(User opponent) {
		player1 = ClientState.currentUser;
		player2 = opponent;
		resetSquares();
	}
	
	public Game(User player1, User player2){
		assert(player1 != null && player2 != null): "players cannot be null";
		resetSquares();
		this.player1 = player1;
		this.player2 = player2;
		// give one of the two user the first turn, fair distribution
		turn = (new Random(System.nanoTime()).nextDouble() <= 0.5)? player1 : player2;
		colormap.put(player1.getSessionId(), 1);
		colormap.put(player2.getSessionId(), 2);
		
		ServerState.submitToPool(new SendInTurnJob(getInTurn(),getNotInTurn()));
		
		messageReceiver = new GameMessageReceiver(this);
		player1.getPublisher().subscribe(messageReceiver, "game");
		player2.getPublisher().subscribe(messageReceiver, "game");
	}
	
	public Square[] getSquares() {
		return squares;
	}

	public int resolveColor(String sessionid){
		return colormap.get(sessionid);
	}
	
	public int getState() {
		return state;
	}

	public User getPlayer1() {
		return player1;
	}
	
	public User getPlayer2() {
		return player2;
	}
	
	public User getInTurn() {
		return turn;
	}
	
	public User getNotInTurn(){
		return (player1 == turn) ? player2 : player1;
	}
	
	/**
	 * this method will advance the state transition graph of the game one step.
	 * MUST NOT be used except by GameMessageReceiver class!
	 */
	public void doStateTransition(){
		WinCheckerResult result1 = WinChecker.checkWin(1, squares);
		WinCheckerResult result2 = WinChecker.checkWin(2, squares);
		switch(state){
		case PLACING:
			if(result1.hasWon() || result2.hasWon()){
				state = FINISHED;
				finishgame(result1, result2);
			}
			else{
				state = ROTATING;
			}
			break;
		case ROTATING:
			if(result1.hasWon() || result2.hasWon()){
				state = FINISHED;
				finishgame(result1, result2);
			}
			else{
				moves++;
				state = PLACING;
				turn = (turn == player1)? player2: player1;
			}
			break;
		case FINISHED:
			break;
		}
	}
	
	private void finishgame(WinCheckerResult result1, WinCheckerResult result2){
		if(result1.hasWon() && result2.hasWon()){
			sendDrawMessage();
			addToDB(player1, 0.5);
			addToDB(player2, 0.5);
		}
		else if(result1.hasWon()){
			sendWinMessage(result1,player1.getSessionId());
			addToDB(player1, 1);
			addToDB(player2, 0);
		}
		else if(result2.hasWon()){
			sendWinMessage(result2,player2.getSessionId());
			addToDB(player1, 0);
			addToDB(player2, 1);
		}
		else{
			System.out.println(this.getClass()+".finishgame(): something went terribly wrong, nobody has won and we're still here...");
		}
		// unsubscribe from game messages
		player1.getPublisher().unsubscribe(messageReceiver, "game");
		player2.getPublisher().unsubscribe(messageReceiver, "game");
	}
	
	
	private void sendDrawMessage(){
		Document packet = new Document(new Element("packet"));
		Element game = new Element("game");
		game.setAttribute("type", "draw");
		packet.getRootElement().addContent(game);
		ServerState.submitToPool(new SendMessageJob(packet,player1));
		ServerState.submitToPool(new SendMessageJob(packet,player2));
	}
	
	private void sendWinMessage(WinCheckerResult result, String sessionid){
		Document packet = new Document(new Element("packet"));
		Element game = new Element("game");
		game.setAttribute("type", "win");
		game.setAttribute("sessionid",sessionid);
		packet.getRootElement().addContent(game);
		Element board = new Element("board");
		game.addContent(board);
		for(int sq = 0; sq < 4; sq++){
			Element square = new Element("square");
			square.setAttribute("number", ""+(sq+1));
			int dx = 0;
			int dy = 0;
			switch(sq){
			case 0:
				dx = 0;
				dy = 0;
				break;
			case 1:
				dx = 3;
				dy = 0;
				break;
			case 2:
				dx = 0;
				dy = 3;
				break;
			case 3:
				dx = 3;
				dy = 3;
				break;
			}
			for(int i = 0; i < 3; i++){
				for(int j = 0; j < 3; j++){
					if(result.getWinconfig()[i+dx][j+dy] != 0){
						Element win = new Element("marble");
						win.setAttribute("x", ""+i);
						win.setAttribute("y",""+j);
						square.addContent(win);
					}
				}
			}
			board.addContent(square);
		}
		ServerState.submitToPool(new SendMessageJob(packet,player1));
		ServerState.submitToPool(new SendMessageJob(packet,player2));
	}
	
	public Square getSquare(int x){
		return squares[x-1];
	}
	
	private void resetSquares(){
		for(int habba = 0; habba < 4; habba++){
			squares[habba] = new Square();
		}
	}
	
	public Document getEntireGameState(){
		Document document = new Document(new Element("packet"));
		Element element = new Element("game");
		element.setAttribute("type", "initial");
		element.setAttribute("turn", turn.getSessionId());
		document.getRootElement().addContent(element);
		Element board = new Element("board");
		element.addContent(board);
		
		// now encoding the game board
		for(int square = 0; square < 4; square++){
			Element sq = new Element("square");
			sq.setAttribute("number", ""+(square+1));
			for(int i = 0; i < 3; i++){
				for(int j = 0; j < 3; j++){
					Element marble = new Element("marble");
					marble.setAttribute("x",""+i);
					marble.setAttribute("y",""+j);
					marble.setAttribute("color",""+squares[square].getField(i, j));
					sq.addContent(marble);
				}
			}
			board.addContent(sq);
		}
		
		try {
			new XMLOutputter(Format.getPrettyFormat()).output(document,System.out);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return document;
	}
	
	public void addToDB(User player, double value){
		try{
			ResultSet userset = DataBaseConnection.executeQuery("select * from users where username=\""+player.getUserName()+"\"");
			userset.first();
			int playerid = userset.getInt("id");
			// now add the entry to the database
			DataBaseConnection.executeUpdate("insert into games (moves,player,outcome) values ("+moves+","+playerid+",'"+value+"')");
		}
		catch(DataBaseException e){
			System.out.println("could not inserts stats into db");
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
