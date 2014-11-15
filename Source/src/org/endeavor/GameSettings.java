package org.endeavor;

/**
 * The game settings for the server
 * 
 * @author Michael Sasse
 * 
 */
public class GameSettings {

	/**
	 * Development mode enables options that shouldn't be added to the live mode
	 */
	public static boolean DEV_MODE = true;

	/**
	 * Debugs the outgoing packets
	 */
	public static final boolean OUT_GOING_PACKET_DEBUG = false;

	/**
	 * Prints the tick time
	 */
	public static final boolean PRINT_TICK_TIME = false;

	/**
	 * Debugs the threads for errors
	 */
	public static final boolean DEBUG_THREADS = false;

	/**
	 * Enables mysql connections, should be false when dev mode is enabled
	 */
	public static final boolean MYSQL_ENABLED = false;
}
