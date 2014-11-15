package org.endeavor.engine.network.mysql.statement.impl.login;

import java.sql.ResultSet;

import org.endeavor.engine.network.mysql.statement.SQLStatement;

/**
 * 
 * @author Allen K.
 *
 */
public class LoginSalt extends SQLStatement {

	private String salt = "";
	private boolean hasResult = false;
	
	private String username;
	
	public LoginSalt(String query, String username) {
		super(query);
		this.username = username;
	}

	@Override
	public void execute() {
		 try {
			 statement.setString(1, username);
			 statement.setString(2, username);
			 ResultSet result = statement.executeQuery();
			 hasResult = result.next();
			 if(hasResult) {
				 salt = result.getString("members_pass_salt");
			 }
		 } catch (Exception e) {
			 e.printStackTrace();
		 }
	}
	
	public boolean hasResult() {
		return hasResult;
	}

	public String getSalt() {
		return salt;
	}
	
}
