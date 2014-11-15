package org.endeavor.game.entity.player;

import org.endeavor.game.entity.player.net.Client;

public class PlayerDetails {

	public String username;
	public String password;
	public Client client;
	
	public PlayerDetails(String username, String password, Client client) {
		this.username = username;
		this.password = password;
		this.client = client;
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	public Client getClient() {
		return client;
	}
	
}
