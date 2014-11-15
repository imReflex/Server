package org.endeavor.game.content.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import org.endeavor.engine.utility.Misc;
import org.endeavor.game.entity.item.Item;
import org.endeavor.game.entity.player.Player;

public class PlayerSaveUtil {
	public static final String IP_MUTE_FILE = "./data/logs/ip mutes.txt";
	public static final String IP_BAN_FILE = "./data/logs/ip bans.txt";
	public static final String STARTER_TRACK_FILE = "./data/logs/starters/";

	public static boolean hasReceived4Starters(Player p) {
		BufferedReader reader = null;
		try {
			if (!new File("./data/logs/starters/" + p.getClient().getHost() + ".txt").exists()) {
				return false;
			}
			
			reader = new BufferedReader(new FileReader("./data/logs/starters/" + p.getClient().getHost() + ".txt"));

			String line = reader.readLine();

			int amount = Integer.parseInt(line.substring(line.indexOf(":") + 1, line.length()));
			
			reader.close();
			
			return amount >= 4;
		} catch (Exception e) {
			e.printStackTrace();

			if (reader != null)
				try {
					reader.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return false;
	}

	public static void setReceivedStarter(Player p) {
		BufferedWriter writer = null;
		BufferedReader reader = null;
		
		final String directory = STARTER_TRACK_FILE + p.getClient().getHost() + ".txt";
		
		try {
			int amount = 1;
			
			if (new File(directory).exists()) {
				reader = new BufferedReader(new FileReader(directory));
				
				try {
					amount += Integer.parseInt(reader.readLine());
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				reader.close();
				new File(directory).delete();
			}
			
			writer = new BufferedWriter(new FileWriter(directory, true));
			writer.write("" + amount);
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();

			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		} finally {
			if (writer != null)
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}

	public static final boolean isIPMuted(Player p) {
		BufferedReader r = null;
		try {
			r = new BufferedReader(new FileReader("./data/logs/ip mutes.txt"));
			String l = null;

			while ((l = r.readLine()) != null) {
				if (l.contains(p.getClient().getHost()))
					return true;
			}
		} catch (Exception e) {
			e.printStackTrace();

			if (r != null)
				try {
					r.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
		} finally {
			if (r != null) {
				try {
					r.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return false;
	}

	public static final boolean isIPBanned(Player p) {
		BufferedReader r = null;
		try {
			r = new BufferedReader(new FileReader("./data/logs/ip bans.txt"));
			String l = null;

			while ((l = r.readLine()) != null) {
				if (l.contains(p.getClient().getHost()))
					return true;
			}
		} catch (Exception e) {
			e.printStackTrace();

			if (r != null)
				try {
					r.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
		} finally {
			if (r != null) {
				try {
					r.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return false;
	}

	public static boolean addToOfflineContainer(String name, Item item) {
		if (!exists(name)) {
			return false;
		}

		Player p = new Player();
		p.setUsername(name);
		try {
			PlayerSave.load(p);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		if (p.getBank().hasSpaceFor(new Item(item)))
			p.getBank().add(item);
		else if (p.getInventory().hasSpaceFor(new Item(item))) {
			p.getInventory().add(item);
		}

		PlayerSave.save(p);
		return true;
	}

	public static final boolean banOfflinePlayer(String name, int length) {
		if (!exists(name)) {
			return false;
		}

		Player p = new Player();
		p.setUsername(name);
		try {
			PlayerSave.PlayerDetails.loadDetails(p);
		} catch (Exception e1) {
			e1.printStackTrace();
			return false;
		}

		p.setBanned(true);
		p.setBanYear(Misc.getYear());
		p.setBanDay(Misc.getDayOfYear());
		p.setBanLength(length);
		try {
			new PlayerSave.PlayerDetails(p).parseDetails();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static final boolean unbanOfflinePlayer(String name) {
		if (!exists(name)) {
			return false;
		}

		Player p = new Player();
		p.setUsername(name);
		try {
			PlayerSave.PlayerDetails.loadDetails(p);
		} catch (Exception e1) {
			e1.printStackTrace();
			return false;
		}

		p.setBanned(false);
		p.setBanDay(0);
		p.setBanYear(0);
		p.setBanLength(0);
		try {
			new PlayerSave.PlayerDetails(p).parseDetails();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static final boolean muteOfflinePlayer(String name, int length) {
		if (!exists(name)) {
			return false;
		}

		Player p = new Player();
		p.setUsername(name);
		try {
			PlayerSave.PlayerDetails.loadDetails(p);
		} catch (Exception e1) {
			e1.printStackTrace();
			return false;
		}
		p.setMuted(true);
		p.setMuteYear(Misc.getYear());
		p.setMuteDay(Misc.getDayOfYear());
		p.setMuteLength(length);
		try {
			new PlayerSave.PlayerDetails(p).parseDetails();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static final boolean yellMuteOfflinePlayer(String name) {
		if (!exists(name)) {
			return false;
		}

		Player p = new Player();
		p.setUsername(name);
		try {
			PlayerSave.PlayerDetails.loadDetails(p);
		} catch (Exception e1) {
			e1.printStackTrace();
			return false;
		}
		p.setYellMuted(true);
		try {
			new PlayerSave.PlayerDetails(p).parseDetails();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static final boolean unYellMuteOfflinePlayer(String name) {
		if (!exists(name)) {
			return false;
		}

		Player p = new Player();
		p.setUsername(name);
		try {
			PlayerSave.PlayerDetails.loadDetails(p);
		} catch (Exception e1) {
			e1.printStackTrace();
			return false;
		}
		p.setYellMuted(false);
		try {
			new PlayerSave.PlayerDetails(p).parseDetails();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static final boolean unmuteOfflinePlayer(String name) {
		if (!exists(name)) {
			return false;
		}

		Player p = new Player();
		p.setUsername(name);
		try {
			PlayerSave.PlayerDetails.loadDetails(p);
		} catch (Exception e1) {
			e1.printStackTrace();
			return false;
		}
		p.setMuted(false);
		p.setMuteYear(0);
		p.setMuteDay(0);
		p.setMuteLength(0);
		try {
			new PlayerSave.PlayerDetails(p).parseDetails();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	private static final boolean exists(String name) {
		try {
			new FileReader("./data/characters/details/" + name + ".json");
			return true;
		} catch (Exception e) {
		}
		return false;
	}

	public static final void setIPMuted(Player p) {
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(new File("./data/logs/ip mutes.txt"), true));
			bw.newLine();
			bw.write(p.getClient().getHost());
		} catch (Exception e) {
			e.printStackTrace();

			if (bw != null)
				try {
					bw.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
		} finally {
			if (bw != null)
				try {
					bw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}

	public static final void setIPBanned(Player p) {
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(new File("./data/logs/ip bans.txt"), true));
			bw.newLine();
			bw.write(p.getClient().getHost());
		} catch (Exception e) {
			e.printStackTrace();

			if (bw != null)
				try {
					bw.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
		} finally {
			if (bw != null)
				try {
					bw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}
}
