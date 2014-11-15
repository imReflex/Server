package org.endeavor;

import org.endeavor.engine.GameThread;
import org.endeavor.engine.ShutdownHook;

/**
 * Initializes the server
 * 
 * @author Michael Sasse
 * 
 */
public class Server {
	
	/**
	 * The main method of the server that initializes everything
	 * 
	 * @param args
	 *            The startup arguments
	 */
	public static void main(String[] args) {
		GameThread.init();
		Runtime.getRuntime().addShutdownHook(new ShutdownHook());
	}
}
