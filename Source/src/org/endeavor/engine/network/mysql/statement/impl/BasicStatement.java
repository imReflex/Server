package org.endeavor.engine.network.mysql.statement.impl;

import org.endeavor.engine.network.mysql.statement.SQLStatement;

/**
 * Basic statement, CAN NOT BE SELECT. 
 * TODO: Make a SelectStatement & UpdateStatement instead of BasicStatement
 * @author Allen
 *
 */
public class BasicStatement extends SQLStatement {

	/**
	 * An array of ? values.
	 */
	Object[] statements;
	
	/**
	 * The constructor.
	 * @param query
	 * @param statements
	 */
	public BasicStatement(String query, Object[] statements) {
		super(query);
		this.statements = statements;
	}

	@Override
	public void execute() {
		try {
			for(int id = 0; id < statements.length; id++) {
				statement.setObject(id, statements[id]);
				statement.execute();
			}
			statement.close();
		} catch (Exception e) {
			System.out.println("Error executing BasicStatement query.");
		}
	}

	
	
}