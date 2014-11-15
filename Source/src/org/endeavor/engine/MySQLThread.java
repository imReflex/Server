package org.endeavor.engine;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.endeavor.engine.network.mysql.Database;
import org.endeavor.engine.network.mysql.MySQL;
import org.endeavor.engine.network.mysql.statement.SQLStatement;
import org.endeavor.game.entity.player.Player;

public class MySQLThread extends Thread {

	private static final Queue<SQLStatement> queryQueue = new ConcurrentLinkedQueue<SQLStatement>();

	public MySQLThread() {
		setPriority(MIN_PRIORITY);

		start();
	}

	@Override
	public void run() {
		while (!Thread.interrupted()) {
			try {
				cycle();
			} catch (Exception e) {
				e.printStackTrace();
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	public static void cycle() throws Exception {
		SQLStatement statement = null;

		while ((statement = queryQueue.poll()) != null) {
			long s = System.currentTimeMillis();
			
			statement.execute();

			/**
			 * Let's be safe just in case..
			 */
			if (System.currentTimeMillis() - s > 5000) {
				System.out.println("SQL Thread Shutdown!");
				break;
			}
		}

		//Thread.sleep(10000);
	}

	public static void queue(SQLStatement statement) {
		if (Database.connected()) {
			queryQueue.add(statement);
		}
	}

}
