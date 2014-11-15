package org.endeavor.engine.network.mysql.statement.impl.login;

import java.sql.ResultSet;

import org.endeavor.engine.SerializedTask;
import org.endeavor.engine.TasksExecutor;
import org.endeavor.engine.network.mysql.SQLConstants;
import org.endeavor.engine.network.mysql.statement.SQLStatement;
import org.endeavor.engine.utility.Encryption;
import org.endeavor.game.entity.player.Player;

/**
 * 
 * @author Allen K.
 *
 */
public class LoginPlayer extends SQLStatement {

	private String username,password;
	
	private boolean hasResult = false;
	
	private int rights = 0;
	private int id = 0;
	
	public LoginPlayer(String username, String password, String query) {
		super(query);
		this.username = username;
		this.password = password;
	}

	@Override
	public void execute() {
		try {
			statement.setString(1, username);
			statement.setString(2, username);
			statement.setString(3, password);
			ResultSet result = statement.executeQuery();
			hasResult = result.next();
			if(hasResult) {
				rights = result.getInt("member_group_id");
				id = result.getInt("member_id");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean hasResult() {
		return hasResult;
	}
	
	public int getRights() {
		return this.rights;
	}
	
	public int getId() {
		return id;
	}
	
	public static void login(final Player player) {
		TasksExecutor.slowExecutor.submit(new SerializedTask() {

			@Override
			public void run() {
				LoginSalt loginSalt = new LoginSalt(SQLConstants.FORUM_SALT_QUERY, player.getUsername().toLowerCase());
				loginSalt.execute();
				String salt = "";
				if(loginSalt.hasResult()) {
					salt = loginSalt.getSalt();
					System.out.println("Found Salt: " + salt + " Gen Salt: " + Encryption.ipbEncrypt(player.getClient().getEnteredPassword(), salt));
					LoginPlayer login = new LoginPlayer(player.getUsername().toLowerCase(), Encryption.ipbEncrypt(player.getClient().getEnteredPassword(), salt), SQLConstants.FORUM_LOGIN_QUERY);
					login.execute();
					if(login.hasResult()) {
						System.out.println("Has Result!");
						int rights = login.getRights();
						player.setRights(player.getRightsForIPB(rights));
						player.setMemberRank(player.getRankForIPB(rights));
						player.setCanLogin(true);
					}
				} else {
					System.out.println("Salt not found!");
				}
			}
		});
	}
	
}
