package org.endeavor.engine.network.mysql;

/**
 * 
 * @author Allen K.
 *
 */
public class SQLConstants {

	/**
	 * Database link
	 */
	public final static String DB_URL = "jdbc:mysql://localhost/";
	
	/**
	 * Database Username
	 */
	public final static String DB_USER = "";
	
	/**
	 * Database name
	 */
	public final static String DB_NAME = "";
	
	/**
	 * Database Password
	 */
	public final static String DB_PASS = "";
	
	/**
	 * Main Identifier
	 */
	public final static String DEFAULT_IDENTIFIER = "main";
	
	public final static String LOGIN_QUERY = "SELECT * FROM characters WHERE username = ? AND password = ? LIMIT 1";
	
	public final static String FORUM_SALT_QUERY = "SELECT members_pass_salt FROM members WHERE members_l_username = ? OR members_l_display_name = ?";
	
	public final static String FORUM_LOGIN_QUERY = "SELECT * FROM members WHERE (members_l_display_name = ? OR members_l_username = ?) AND members_pass_hash = ?";
	
}
