package org.endeavor.engine.network.mysql;

/**
 * UNUSED - ALLEN
 *
 */
public class DatabaseConstants {

	public static final String HOST = "198.91.94.101";

	public static String DONATE_USERNAME = "";
	public static String VOTE_USERNAME = "";
	public static String HIGHSCORE_USERNAME = "";

	public static String HS_PASSWORD = "";
	public static String VOTE_PASSWORD = "";
	public static String DONATE_PASSWORD = "";

	public static final String MYSQL_CONNECT_PREFIX = "jdbc:mysql://" + HOST + "/";

	/*
	 * 
	 * 
	 * %IZXJ_GX=B5c = pass [11/14/2013 8:17:53 PM] Hank:
	 * wwwendea_donationhere1997_6 = datebase name [11/14/2013 8:18:06 PM] Hank:
	 * wwwendea_don = database user
	 */

	public static final String DONATE_DATABASE_NAME = "wwwendea_donate";
	public static final String VOTE_DATABASE_NAME = "wwwendea_vote";
	public static final String HIGHSCORE_DATABASE_NAME = "wwwendea_high";

	public static final String UPDATE_DONATION_STATEMENT = "UPDATE donation SET tickets = -10 WHERE username = ?";

	public static final String DELETE_VOTE_STATEMENT = "DELETE FROM has_voted WHERE username = ?";

	public static final String DELETE_HIGHSCORE_STATEMENT = "DELETE FROM hs_users WHERE username = ?";

	public static final String INSERT_HIGHSCORE_STATEMENT = "INSERT INTO hs_users VALUES(" + "?," + "?," + "?," + "?,"
			+ "?," + "?," + "?," + "?," + "?," + "?," + "?," + "?," + "?," + "?," + "?," + "?," + "?," + "?," + "?,"
			+ "?," + "?," + "?," + "?," + "?," + "?," + "?," + "?," + "?," + "?," + "?)";
}
