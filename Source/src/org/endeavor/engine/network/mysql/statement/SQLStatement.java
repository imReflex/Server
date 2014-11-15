package org.endeavor.engine.network.mysql.statement;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Logger;

import org.endeavor.engine.network.mysql.Database;

public abstract class SQLStatement {

	protected static final Logger log = Logger.getLogger(Database.class.getName());
	
	/**
	 * The statement to execute.
	 */
	protected PreparedStatement statement;
	
	/**
	 * The Constructor
	 * @param player
	 * @throws SQLException 
	 */
	public SQLStatement(String query) {
		if(Database.getConnection() == null)
			System.out.println("Database is null!");
		try {
			statement = Database.getConnection().prepareStatement(query);
		} catch (SQLException e) {
			statement = null;
		}
	}
	
	/**
	 * Execute the prepared statement.
	 */
	public abstract void execute();
	
	public PreparedStatement getStatement() {
		return statement;
	}
	
}
