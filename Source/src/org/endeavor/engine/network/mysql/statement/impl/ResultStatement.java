package org.endeavor.engine.network.mysql.statement.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.endeavor.engine.network.mysql.statement.SQLStatement;

/**
 * A connection and query to MySQL
 * @author Allen
 *
 */
public class ResultStatement extends SQLStatement {

	private boolean result;
	
	private String results;
	
	private String string;
	
	public ResultStatement(String query, String string) {
		super(query);
		this.string = string;
	}
	
	public ResultStatement(String query) {
		this(query, null);
	}

	@Override
	public void execute() {
		try {
			if(string != null)
				statement.setString(1, string);
			ResultSet result = statement.executeQuery();
			this.result = result.next() ? true : false;
		} catch (SQLException e) {
			log.info("Error executing result statement.");
		}
	}
	
	/**
	 * Does the statement have a result?
	 */
	public boolean hasResult() {
		return this.result;
	}

}

