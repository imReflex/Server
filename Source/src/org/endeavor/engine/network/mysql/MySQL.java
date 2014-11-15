package org.endeavor.engine.network.mysql;

import static org.endeavor.engine.network.mysql.DatabaseConstants.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.endeavor.game.content.QuestTab;
import org.endeavor.game.content.skill.SkillConstants;
import org.endeavor.game.entity.World;
import org.endeavor.game.entity.item.Item;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.Player.ExperienceType;
import org.endeavor.game.entity.player.PlayerConstants;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;

/**
 * UNUSED - ALLEN
 *
 */
public class MySQL {

	private static ThreadLocal<Connection> donationDatabase = new DonationDabaseConnection();
	private static ThreadLocal<Connection> voteDatabase = new VoteDabaseConnection();
	private static ThreadLocal<Connection> highscoreDatabase = new HighscoreDabaseConnection();

	private static Statement donatingStatement = null;
	private static Statement votingStatement = null;
	private static Statement highscoreStatement = null;

	private static boolean connected = false;

	public static Connection getDonationConnection() {
		return donationDatabase.get();
	}

	public static Connection getVoteConnection() {
		return voteDatabase.get();
	}

	public static boolean connect() {
		try {
			//highscoreStatement = highscoreDatabase.get().createStatement();
			votingStatement = voteDatabase.get().createStatement();
			donatingStatement = donationDatabase.get().createStatement();
			connected = true;
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static boolean isConnected() {
		return connected;
	}

	public static void release() throws SQLException {
		if (donationDatabase != null) {
			donationDatabase.get().close();
			donationDatabase.remove();
		}

		if (voteDatabase != null) {
			voteDatabase.get().close();
			voteDatabase.remove();
		}

		if (highscoreDatabase != null) {
			highscoreDatabase.get().close();
			highscoreDatabase.remove();
		}
	}

	public static void writeToHighscores(final Player p) {
		try {
			if (PlayerConstants.isOwner(p)) {
				return;
			}

			if (highscoreStatement.isClosed()) {
				highscoreStatement = highscoreDatabase.get().createStatement();

				if (highscoreStatement.isClosed()) {
					Exception e = new IllegalArgumentException("Could not create highscores statement (MySql).");
					e.printStackTrace();
					return;
				}
			}

			highscoreDatabase.get().setAutoCommit(false);

			ResultSet rs = highscoreStatement.executeQuery("SELECT * FROM hs_users WHERE username = '"
					+ p.getUsername() + "'");

			if (rs.next()) {
				PreparedStatement s = highscoreDatabase.get().prepareStatement(DELETE_HIGHSCORE_STATEMENT);
				s.setString(1, p.getUsername());
				s.executeUpdate();
			}

			PreparedStatement s = highscoreDatabase.get().prepareStatement(INSERT_HIGHSCORE_STATEMENT);

			s.setString(1, null);
			s.setString(2, p.getUsername());
			s.setString(3, "none");
			s.setString(4, "0");
			s.setString(5, "" + p.getSkill().getTotalExperience());

			for (int i = 0; i <= SkillConstants.SKILL_COUNT; i++) {
				s.setString(i, "" + p.getSkill().getExperience()[i]);
			}

			s.executeUpdate();

			highscoreDatabase.get().commit();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final void checkForVote(final Player p) {
		try {
			if (votingStatement.isClosed()) {
				votingStatement = voteDatabase.get().createStatement();

				if (votingStatement.isClosed()) {
					Exception e = new IllegalArgumentException("Could not create voting statement (MySql).");
					e.printStackTrace();
					return;
				}
			}

			ResultSet rs = votingStatement.executeQuery("SELECT * FROM has_voted WHERE username = '" + p.getUsername().replaceAll(" ", "_") + "'");

			int voted = 0;

			while (rs.next()) {
				voted++;
				PreparedStatement s = voteDatabase.get().prepareStatement(DELETE_VOTE_STATEMENT);
				s.setString(1, p.getUsername().replaceAll(" ", "_"));
				s.executeUpdate();
			}

			if (voted > 0) {
				int coins = 0;
				int points = 0;
				
				for (int i = 0; i < voted; i++) {
					coins += 500000;
					points += 5;
					p.addToVotePoints(5);
				}
				
				
				p.getClient().queueOutgoingPacket(new SendMessage("Thank you for voting!"));
				p.getClient().queueOutgoingPacket(new SendMessage("You have been given " + points + " new vote points and " + (coins / 1000) + "k has been added to your bank."));

				p.getBank().add(new Item(995, coins));
				
				QuestTab.updateVotePoints(p);
				
				World.sendGlobalMessage(p.getUsername() + " has received rewards for voting! Use ::vote for rewards!", true);
			} else {
				p.getClient().queueOutgoingPacket(
						new SendMessage(
								"Have you voted today? Use ::vote now for rewards!"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void checkForDonation(final Player p) {
		try {
			if (donatingStatement.isClosed()) {
				donatingStatement = donationDatabase.get().createStatement();

				if (donatingStatement.isClosed()) {
					Exception e = new IllegalArgumentException("Could not create donation statement (MySql).");
					e.printStackTrace();
					return;
				}
			}

			ResultSet rs = donatingStatement.executeQuery("SELECT * FROM donation WHERE username = '"
					+ p.getUsername() + "'");

			boolean don = false;
			boolean underscoreInUsername = false;
			
			if (!rs.next()) {
				rs = donatingStatement.executeQuery("SELECT * FROM donation WHERE username = '"
						+ p.getUsername().replaceAll(" ", "_") + "'");
				underscoreInUsername = true;
			}
			
			while (rs.next()) {
				if (rs.getInt("tickets") != -10) {
					int product = rs.getInt("productid");
					int donated = rs.getInt("price");

					p.getClient().queueOutgoingPacket(
							new SendMessage("Thank you for donating $" + donated + " for the product with id: "
									+ product + "!"));

					if (doActionsForDonationProduct(p, product, donated)) {

						// update index
						PreparedStatement s = donationDatabase.get().prepareStatement(UPDATE_DONATION_STATEMENT);
						s.setString(1, underscoreInUsername ? p.getUsername().replaceAll(" ", "_") : p.getUsername());
						s.executeUpdate();

						if (!don) {
							World.sendGlobalMessage(p.getUsername()
									+ " has just received in-game rewards for donating!", true);
						}
					}
				}

				don = true;
			}

			if (!don) {
				p.getClient().queueOutgoingPacket(new SendMessage("You've never donated. Did you know you can recieve rewards in-game for donating?"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static boolean doActionsForDonationProduct(Player p, int product, int price) {
		String message = null;

		switch (product) {
		case 1:
			if (price != 3) {
				p.getClient().queueOutgoingPacket(new SendMessage("<shad=57457><col=24247>" + "You have paid an incorrect price. Please contact support."));
				return false;
			} else {
			
				if (p.getBank().hasSpaceFor(new Item(13664))) {
					message = "You have been given a Donator rank ticket.";
					p.getBank().add(new Item(13664));
				} else {
					message = "You must clear some bank space to get your Donator rank ticket!";
				}
			}
			break;
		case 2:
			if (price != 5) {
				p.getClient().queueOutgoingPacket(new SendMessage("<shad=57457><col=24247>" + "You have paid an incorrect price. Please contact support."));
				return false;
			} else {
				if (p.getBank().hasSpaceFor(new Item(13663))) {
					message = "A Premium Membership Ticket has been added to your bank.";
					p.getBank().add(new Item(13663));
				} else {
					message = "You must clear some bank space to get your Premium ticket!";
				}
			}
			break;
		case 3:
			if (price != 10) {
				p.getClient().queueOutgoingPacket(new SendMessage("<shad=57457><col=24247>" + "You have paid an incorrect price. Please contact support."));
				return false;
			} else {
				giveDonationPoints(p, 5);
			}
			break;
		case 4:
			if (price != 18) {
				p.getClient().queueOutgoingPacket(new SendMessage("<shad=57457><col=24247>" + "You have paid an incorrect price. Please contact support."));
				return false;
			} else {
				giveDonationPoints(p, 10);
			}
			break;
		case 5:
			if (price != 34) {
				p.getClient().queueOutgoingPacket(new SendMessage("<shad=57457><col=24247>" + "You have paid an incorrect price. Please contact support."));
				return false;
			} else {
				giveDonationPoints(p, 20);
			}
			break;
		case 6:
			if (price != 39) {
				p.getClient().queueOutgoingPacket(new SendMessage("<shad=57457><col=24247>" + "You have paid an incorrect price. Please contact support."));
				return false;
			} else {
				giveDonationPoints(p, 30);
			}
			break;
		case 7:
			if (price != 48) {
				p.getClient().queueOutgoingPacket(new SendMessage("<shad=57457><col=24247>" + "You have paid an incorrect price. Please contact support."));
				return false;
			} else {
				giveDonationPoints(p, 40);
			}
			break;
			
		default:
			Exception e = new IllegalArgumentException("Unknown product id attempted to be used (MySQL)! id: " + product);
			e.printStackTrace();
			return false;
		}

		if (message != null) {
			p.getClient().queueOutgoingPacket(new SendMessage("<shad=57457><col=24247>" + message));
		}

		return true;
	}
	
	private static void giveDonationPoints(Player player, int amount) {
		player.incrDonationPoints(amount);
		player.getClient().queueOutgoingPacket(new SendMessage("<shad=57457><col=24247>" + amount + " gold points have been added to your account."));
	}

	private static class HighscoreDabaseConnection extends ThreadLocal<Connection> {
		static {
			try {
				Class.forName("com.mysql.jdbc.Driver");
			} catch (ClassNotFoundException e) {
				System.out.println("[SQL] Could not locate the JDBC mysql driver.");
			}
		}

		@Override
		protected Connection initialValue() {
			return getConnection();
		}

		private Connection getConnection() {
			DriverManager.setLoginTimeout(15);
			try {
				return DriverManager.getConnection(MYSQL_CONNECT_PREFIX + HIGHSCORE_DATABASE_NAME, HIGHSCORE_USERNAME,
						HS_PASSWORD);
			} catch (SQLException sql) {
				System.out
						.println("[SQL] Error establishing connection. Please make sure you've correctly configured your mysql database information.");
				return null;
			}
		}

		@Override
		public Connection get() {
			try {
				Connection con = super.get();
				if (con != null && !con.isClosed()) {
					return con;
				}
				con = getConnection();
				super.set(con);
				return con;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
	}

	private static class DonationDabaseConnection extends ThreadLocal<Connection> {
		static {
			try {
				Class.forName("com.mysql.jdbc.Driver");
			} catch (ClassNotFoundException e) {
				System.out.println("[SQL] Could not locate the JDBC mysql driver.");
			}
		}

		@Override
		protected Connection initialValue() {
			return getConnection();
		}

		private Connection getConnection() {
			DriverManager.setLoginTimeout(15);
			try {
				return DriverManager.getConnection(MYSQL_CONNECT_PREFIX + DONATE_DATABASE_NAME, DONATE_USERNAME,
						DONATE_PASSWORD);
			} catch (SQLException sql) {
				System.out.println("[SQL] Error establishing connection. Please make sure you've correctly configured your mysql database information.");
				return null;
			}
		}

		@Override
		public Connection get() {
			try {
				Connection con = super.get();
				if (!con.isClosed()) {
					return con;
				}
				con = getConnection();
				super.set(con);
				return con;
			} catch (Exception e) {
				return null;
			}
		}
	}

	private static class VoteDabaseConnection extends ThreadLocal<Connection> {
		static {
			try {
				Class.forName("com.mysql.jdbc.Driver");
			} catch (ClassNotFoundException e) {
				System.out.println("[SQL] Could not locate the JDBC mysql driver.");
			}
		}

		@Override
		protected Connection initialValue() {
			return getConnection();
		}

		private Connection getConnection() {
			DriverManager.setLoginTimeout(15);
			try {
				return DriverManager.getConnection(MYSQL_CONNECT_PREFIX + VOTE_DATABASE_NAME, VOTE_USERNAME,
						VOTE_PASSWORD);
			} catch (SQLException sql) {
				System.out
						.println("[SQL] Error establishing connection. Please make sure you've correctly configured your mysql database information.");
				return null;
			}
		}

		@Override
		public Connection get() {
			try {
				Connection con = super.get();
				if (!con.isClosed()) {
					return con;
				}
				con = getConnection();
				super.set(con);
				return con;
			} catch (Exception e) {
				return null;
			}
		}
	}
}
