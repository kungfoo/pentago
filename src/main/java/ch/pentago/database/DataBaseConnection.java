package ch.pentago.database;


import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.Semaphore;

import ch.pentago.xml.ServerConfigManager;


/**
 * singleton class executing database queries and such
 * @author kungfoo
 *
 */
public final class DataBaseConnection {
	private static boolean initialized = false;
	private static java.sql.Connection connection;
	private static Semaphore mutex = new Semaphore(1);
	
	private static void init() throws DataBaseException{
		if(!initialized){
			try{
				mutex.acquire();
				if(ServerConfigManager.getDataBaseType().equals("mysql")){
					Class.forName("com.mysql.jdbc.Driver");
					connection = DriverManager.getConnection("jdbc:mysql://"+ServerConfigManager.getDataBaseHost()+
							":"+ServerConfigManager.getDataBasePort()+"/"+ServerConfigManager.getDataBaseName(),
							ServerConfigManager.getDataBaseUser(), ServerConfigManager.getDataBasePassword());
					initialized = true;
				}
				if(ServerConfigManager.getDataBaseType().equals("h2")) {
					Class.forName("org.h2.Driver");
					connection = DriverManager.getConnection("jdbc:h2:~/pentago.db");
					initialized = true;
				}
				else{
					throw(new DataBaseException("unknown database type: "+ServerConfigManager.getDataBaseType()));
				}


			}
			catch(SQLException e){
				throw(new DataBaseException("credentials not accepted by database"));
			}
			catch(ClassNotFoundException e){
				throw(new DataBaseException("could not load database driver"));
			}
			catch(InterruptedException e){
				throw(new DataBaseException("thread interrupted during mutex acquire"));
			}
			finally{
				mutex.release();
			}
		}
	}
	
	@SuppressWarnings("finally")
	private static Statement getStatement() throws DataBaseException{
		init();
		try{
			return connection.createStatement();
		}
		catch(SQLException e){
			throw(new DataBaseException("getStatement(): could not create statement"));
		}
	}
	
	public static ResultSet executeQuery(String query) throws DataBaseException{
		init();
		ResultSet set = null;
		Statement statement = getStatement();
		try{
			set = statement.executeQuery(query);
		}
		
		catch(SQLException e){
			e.printStackTrace();
			throw(new DataBaseException("executeQuery(): could not execute query"));
		}
		return set;
	}
	
	public static int executeUpdate(String query) throws DataBaseException{
		init();
		Statement state = getStatement();
		int affectedRows = 0;
		try{
			affectedRows = state.executeUpdate(query);
		}
		catch(SQLException e){
			throw(new DataBaseException("executeUpdate(): could not execute update"));
		}
		finally{}
		return affectedRows;
	}
}
