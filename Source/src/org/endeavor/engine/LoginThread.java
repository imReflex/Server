package org.endeavor.engine;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.endeavor.GameSettings;
import org.endeavor.engine.network.StreamBuffer;
import org.endeavor.engine.network.mysql.MySQL;
import org.endeavor.engine.network.mysql.SQLConstants;
import org.endeavor.engine.network.mysql.statement.impl.login.LoginPlayer;
import org.endeavor.engine.network.mysql.statement.impl.login.LoginSalt;
import org.endeavor.engine.utility.Encryption;
import org.endeavor.engine.utility.SerializeableFileManager;
import org.endeavor.game.content.io.PlayerSave;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.PlayerDetails;
import org.endeavor.game.entity.player.net.Client;

public class LoginThread extends Thread {
	public static LoginThread singleton;
	private static final Queue<PlayerDetails> login = new ConcurrentLinkedQueue<PlayerDetails>();

	private static Thread log = null;

	private static final Queue<String> publicChat = new ConcurrentLinkedQueue<String>();
	private static final Queue<String> privateChat = new ConcurrentLinkedQueue<String>();
	public static final String PUBLIC_CHAT_LOG = "./data/logs/public.txt";
	public static final String PRIVATE_CHAT_LOG = "./data/logs/private.txt";

	public LoginThread() {
		singleton = this;

		setName("Login Thread");

		setPriority(Thread.MAX_PRIORITY - 2);

		start();
	}

	public static void queueLogin(PlayerDetails player) {
		login.add(player);
	}

	public static void queuePublicChatLog(Player p, String message) {
		synchronized (publicChat) {
			publicChat.add(p.getUsername() + ": " + message);
		}
	}

	public static void queuePrivateChatLog(Player p, Player to, String message) {
		synchronized (privateChat) {
			privateChat.add("(" + p.getUsername() + " to " + to.getUsername() + "): " + message);
		}
	}

	@Override
	public void run() {
		while (!Thread.interrupted())
			cycle();
	}

	public static void cycle() {
		synchronized (singleton) {
		}

		long start = System.currentTimeMillis();

		PlayerDetails pd = null;

		if ((pd = login.poll()) != null) {
			System.out.println("Logging in: " + pd.getUsername());

			boolean starter = false;
			boolean wasLoaded = false;
			Player player;
			if (!SerializeableFileManager.containsPlayer(pd.getUsername())) {
				player = new Player(pd);
				starter = true;
				wasLoaded = true;
			} else {
				player = SerializeableFileManager.loadPlayer(pd.getUsername());
				if(player == null) {
					StreamBuffer.OutBuffer resp = StreamBuffer.newOutBuffer(3);
					resp.writeByte(11);
					resp.writeByte(0);
					resp.writeByte(0);
					pd.getClient().send(resp.getBuffer());
					return;
				}
				player.setClient(pd.getClient());
				wasLoaded = true;
				starter = false;
			}
			if(GameSettings.DEV_MODE) {
				/*LoginSalt loginSalt = new LoginSalt(SQLConstants.FORUM_SALT_QUERY, player.getUsername().toLowerCase());
				loginSalt.execute();
				String salt = "";
				if(loginSalt.hasResult()) {
					salt = loginSalt.getSalt();
					//System.out.println("Found Salt: " + salt + " Gen Salt: " + Encryption.ipbEncrypt(player.getClient().getEnteredPassword(), salt));
					LoginPlayer login = new LoginPlayer(player.getUsername().toLowerCase(), Encryption.ipbEncrypt(player.getClient().getEnteredPassword(), salt), SQLConstants.FORUM_LOGIN_QUERY);
					login.execute();
					if(login.hasResult()) {
						//System.out.println("Has Result!");
						int rights = login.getRights();
						player.setRights(player.getRightsForIPB(rights));
						player.setMemberRank(player.getRankForIPB(rights));
						player.setCanLogin(true);
					}
				} else {
					System.out.println("Salt not found!");
				}*/
				player.setCanLogin(true);
			} else {
				long timeStart = System.currentTimeMillis();
				try {
					URL url = new URL("http://localhost/verify.php?username="+player.getUsername().toLowerCase().replaceAll(" ", "%20")+"&password="+player.getClient().getEnteredPassword()+"&key=RXKey2014");
					BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
					String line = reader.readLine();
					if(line.startsWith("true")) {
						String[] lines = line.split("-");
						int rights = Integer.parseInt(lines[1]);
						player.setRights(player.getRightsForIPB(rights));
						player.setMemberRank(player.getRankForIPB(rights));
						String displayName = lines[2];
						player.setDisplayName(displayName);
						player.setCanLogin(true);
					}
				} catch (Exception e) {
					e.printStackTrace();
					StreamBuffer.OutBuffer resp = StreamBuffer.newOutBuffer(3);
					resp.writeByte(8);
					resp.writeByte(0);
					resp.writeByte(0);
					pd.getClient().send(resp.getBuffer());
					return;
				}
				long timeEnd = System.currentTimeMillis() - timeStart;
				System.out.println("Time taken: " + timeEnd);
			}

			if (wasLoaded) {
				try {
					boolean login = player.login(starter, pd);

					if (login) {
						player.getClient().setStage(Client.Stages.LOGGED_IN);
					}
				} catch (Exception e) {
					e.printStackTrace();
					player.logout(true);
				}
			}

		}

		long elapsed = System.currentTimeMillis() - start;

		/*if (elapsed < 700L) {
			try {
				if ((log == null) && ((publicChat.size() > 500) || (privateChat.size() > 500))) {
					log = new LogThread();
					log.start();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				Thread.sleep(700L);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("Login thread overflow: " + elapsed);
		}*/
	}

	public static class LogThread extends Thread {
		@Override
		public void run() {
			try {
				String[] privateLogs;
				String[] publicLogs;

				synchronized (LoginThread.publicChat) {
					publicLogs = new String[LoginThread.publicChat.size()];

					for (int i = 0; i < publicLogs.length; i++)
						publicLogs[i] = (LoginThread.publicChat.poll());
				}

				synchronized (LoginThread.privateChat) {
					privateLogs = new String[LoginThread.privateChat.size()];

					for (int i = 0; i < privateLogs.length; i++)
						privateLogs[i] = (LoginThread.privateChat.poll());
				}

				writeLogs(publicLogs, "./data/logs/public.txt");
				writeLogs(privateLogs, "./data/logs/private.txt");
			} catch (Exception e) {
				e.printStackTrace();
			}

			LoginThread.log = null;
		}

		public void writeLogs(String[] log, String location) {
			try {
				BufferedWriter writer = new BufferedWriter(new FileWriter(location, true));

				for (String i : log) {
					writer.write(i);
					writer.newLine();
				}

				writer.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
