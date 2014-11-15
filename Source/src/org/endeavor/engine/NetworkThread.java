package org.endeavor.engine;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.endeavor.game.content.sounds.MusicPlayer;
import org.endeavor.game.entity.World;
import org.endeavor.game.entity.object.GameObject;
import org.endeavor.game.entity.object.ObjectManager;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.net.in.IncomingPacket;

public class NetworkThread extends Thread {
	public static NetworkThread singleton;
	private static Queue<long[]> ticklog = new ConcurrentLinkedQueue<long[]>();

	private static Queue<PacketLog> packetLog = new ConcurrentLinkedQueue<PacketLog>();

	public static int cycles = 0;
	public static final String PACKET_LOG_DIR = "./data/logs/packets/";

	public NetworkThread() {
		singleton = this;

		setName("Network Thread");

		setPriority(Thread.MAX_PRIORITY - 1);

		start();
	}

	public static void queueLog(long[] log) {
		//ticklog.add(log);
	}

	@Override
	public void run() {
		while (!Thread.interrupted())
			cycle();
	}

	public static void cycle() {
		long start = System.nanoTime();
		
		
		GameObject r;
		while ((r = ObjectManager.getSend().poll()) != null) {
			try {
				ObjectManager.send(r);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		for (Player k : World.getPlayers()) {
			try {
				if ((k != null) && (k.isActive())) {
					try {
						/*if (k.getPlayerShop().hasSearch()) {
							k.getPlayerShop().doSearch();
							k.getPlayerShop().resetSearch();
						}*/

						k.getGroundItems().process();
						k.getObjects().process();
						MusicPlayer.play(k);
						k.getClient().processOutgoingPackets();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		/*PacketLog l = null;*/

		/*if (packetLog.size() > 100)
			while ((l = packetLog.poll()) != null)
				try {
					BufferedWriter writer = new BufferedWriter(new FileWriter("./data/logs/packets/" + l.username
							+ ".txt", true));

					writer.newLine();
					writer.write(l.packet);

					writer.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
		try {
			long[] log;
			if ((log = ticklog.poll()) != null) {
				BufferedWriter writer = null;
				try {
					writer = new BufferedWriter(new FileWriter("./data/logs/ticks/tick-"
							+ Calendar.getInstance().getTime().getTime() + ".txt"));

					writer.write(" ----- " + Calendar.getInstance().getTime());

					writer.newLine();

					for (int i = 0; i < log.length; i++) {
						writer.write("e" + (i + 1) + ": " + log[i] / 1000000.0D);
						writer.newLine();
					}
				} catch (Exception e) {
					e.printStackTrace();

					if (writer != null)
						try {
							writer.close();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
				} finally {
					if (writer != null) {
						try {
							writer.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}

				log = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}*/

		long elapsed = (System.nanoTime() - start) / 1000000;

		if (elapsed < 200) {
			try {
				Thread.sleep(200 - elapsed);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("network thread overflow: " + elapsed);
		}
	}

	public static NetworkThread getSingleton() {
		return singleton;
	}

	public static void createLog(String username, IncomingPacket packet, int opcode) {
		packetLog.add(new PacketLog(username, packet.getClass().getSimpleName() + " : " + opcode));
	}

	public static class PacketLog {
		public final String username;
		public final String packet;

		public PacketLog(String username, String packet) {
			this.username = username;
			this.packet = packet;
		}
	}
}
