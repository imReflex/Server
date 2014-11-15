package org.endeavor.engine.network.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.endeavor.engine.network.mysql.statement.SQLStatement;

/**
 * 
 * @author Allen
 *
 */
public class Database {

	private static final Logger log = Logger.getLogger(Database.class.getName());
	private static HashMap<String, Connection> connections = new HashMap<String, Connection>();
	
	/**
	 * Connect to the MySQL database.
	 */
	public static void connect(String identifier, String url, String name, String user, String pass) {
		//log.info("Connecting to MySQL...");
		System.out.println("Connecting to MySQL...");
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			Connection connection = DriverManager.getConnection(url + name, user, pass);
			connections.put(identifier, connection);
		} catch (Exception e) {
			log.log(Level.SEVERE, "Error connecting to MySQL...");
			e.printStackTrace();
		}
	}
	
	/**
	 * Retrieve the connection.
	 * @return	connection
	 */
	public static Connection getConnection(String identifier) {
		return connections.get(identifier);
	}
	
	/**
	 * Retrieve the connection.
	 * @return	connection
	 */
	public static Connection getConnection() {
		return connections.get(SQLConstants.DEFAULT_IDENTIFIER);
	}
	
	/**
	 * Is there a connection?
	 * @return
	 */
	public static boolean connected() {
		return !connections.isEmpty();
	}
	
}
