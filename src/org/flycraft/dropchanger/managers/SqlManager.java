package org.flycraft.dropchanger.managers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.PriorityQueue;
import java.util.Queue;

import org.flycraft.dropchanger.thingsdoers.Sender;

/**
 * Класс, работающий в отдельном потоке. Соединяется с базой данных, работает с соединением, шлет запросы из очереди.
 * @author Alexandr
 *
 */

public class SqlManager extends Thread {
	
	private Connection connection;
	private Statement statement;
	
	private volatile Queue<String> queries = new PriorityQueue();
	
	private final String url;
	private final String user;
	private final String password;
	
	private volatile boolean isRunning;
	
	public SqlManager(String[] mySqlInfo) {
		url = "jdbc:" + mySqlInfo[0];
		user = mySqlInfo[1];
		password = mySqlInfo[2];
	}
	
    @Override
    public void run() {
    	openConnection();
    	isRunning = true;
    	queriesSender();
    }
	
    private void openConnection() {
			try {
				Sender.sendWML("sql.info.connection.open.start");
				
				try {
					Class.forName("com.mysql.jdbc.Driver").newInstance();
				} catch(java.lang.ClassNotFoundException e) {
					e.printStackTrace();
				}
				connection = DriverManager.getConnection(url, user, password);
				statement = connection.createStatement();
				
				Sender.sendWML("sql.info.connection.open.end");
			} catch (SQLException e) { Sender.sendWML("sql.error.connection.open"); e.printStackTrace(); } catch (InstantiationException e) { e.printStackTrace();
			} catch (IllegalAccessException e) { e.printStackTrace(); }
	}
	
    public void addQuery(String queryCode) {
    	queries.add(queryCode);
    }
    
	private void queriesSender() {
		try {
			while(isRunning || !queries.isEmpty()) {
				if(!queries.isEmpty()) {
					Sender.sendDebugWMLWWithCustomPostfix("sql.debug.connection.sending", ": " + queries.element());
					statement.executeUpdate(queries.remove());
				}
			}
			if(connection != null) {
				connection.close();
			}
			Sender.sendWML("sql.info.connection.close.end");
		} catch (SQLException e) { e.printStackTrace(); }
	}
	
	public void kill() {
		Sender.sendWML("sql.info.connection.close.start");
		isRunning = false;
	}
	
}
