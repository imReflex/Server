package org.endeavor.engine;

import java.io.FileOutputStream;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.endeavor.GameDataLoader;
import org.endeavor.GameSettings;
import org.endeavor.engine.network.PipelineFactory;
import org.endeavor.engine.network.mysql.Database;
import org.endeavor.engine.network.mysql.MySQL;
import org.endeavor.engine.network.mysql.SQLConstants;
import org.endeavor.engine.task.TaskQueue;
import org.endeavor.engine.utility.LineCounter;
import org.endeavor.game.content.clans.clanwars.ClanWars;
import org.endeavor.game.entity.World;
import org.endeavor.game.entity.item.impl.GroundItemHandler;
import org.endeavor.game.entity.object.ObjectManager;
import org.endeavor.game.entity.player.Player;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelException;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

/**
 * 
 * 
 * @author Mikey`
 * 
 */
public class GameThread extends Thread {

	/**
	 * Create the game thread
	 */
	private GameThread() {
		setName("Main Thread");
		setPriority(Thread.MAX_PRIORITY);
		start();
	}

	public static void init() {
		try {
			/**
			 * Load data, bind ports, init connections, launch worker threads
			 */
			try {
				startup();
			} catch (Exception e) {
				e.printStackTrace();
			}

			/**
			 * Begin game thread
			 */
			new GameThread();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Performs a server cycle.
	 */
	private void cycle() {
		try {
			TaskQueue.process();
			GroundItemHandler.process();
			ObjectManager.process();
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			World.process();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void subcycle() {
		for (Player p : World.getPlayers()) {
			if (p != null && p.isActive()) {
				p.getClient().processIncomingPackets();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		try {
			while (!Thread.interrupted()) {
				long s = System.nanoTime();

				/**
				 * Cycle game
				 */
				cycle();

				/**
				 * Sleep
				 */
				long e = (System.nanoTime() - s) / 1000000;

				/*if (!GameSettings.DEV_MODE) {
					System.out.println("Main cycle time: " + e);
				}*/

				/**
				 * Process incoming packets consecutively throughout the
				 * sleeping cycle *The key to instant switching of equipment
				 */
				if (e < 600) {
					
					if (e < 400) {
						for (int i = 0; i < 30; i++) {
							long sleep = (600 - e) / 30;
							
							Thread.sleep(sleep);
							subcycle();
						}
					} else {
						Thread.sleep(600 - e);
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Starts the server up.
	 * 
	 * @throws Exception
	 */
	private static void startup() throws Exception {
		long startTime = System.currentTimeMillis();

		System.out.println("Launching RevolutionX..");

		/*if (!GameSettings.DEV_MODE) {
			System.setErr(new PrintStream(new FileOutputStream("./data/logs/err.txt", true)));
		}*/

		if (GameSettings.DEV_MODE) {
			LineCounter.run();
		}

		System.out.println("Initialized Tasks Executor!");
		TasksExecutor.init();
		
		System.out.println("Loading game data..");

		GameDataLoader.load();

		while (!GameDataLoader.loaded()) {
			Thread.sleep(200);
		}

		ClanWars.init();
		
		System.out.println("Successfully loaded server in " + Math.round((System.currentTimeMillis() - startTime) / 1000) + " seconds.");
		System.out.println("Binding port and initializing threads..");

		// Initialize netty and begin listening for new clients
		ServerBootstrap serverBootstrap = new ServerBootstrap(new NioServerSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool()));
		serverBootstrap.setPipelineFactory(new PipelineFactory());

		/**
		 * Initialize worker threads
		 */
		new LoginThread();
		new NetworkThread();
		
		Database.connect(SQLConstants.DEFAULT_IDENTIFIER, SQLConstants.DB_URL, SQLConstants.DB_NAME, SQLConstants.DB_USER, SQLConstants.DB_PASS);

		if (Database.connected()) {
			new MySQLThread();
		}

		System.gc();

		while (true) {
			try {
				serverBootstrap.bind(new InetSocketAddress(43594));
				break;
			} catch (ChannelException e2) {
				System.out.println("Server could not bind port - sleeping..");
				Thread.sleep(2000);
			}
		}

		System.out.println("Server successfully launched.");
	}

}
