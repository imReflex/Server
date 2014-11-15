package org.endeavor.game.content.clans.clanwars;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.endeavor.engine.TasksExecutor;
import org.endeavor.engine.cache.map.RSObject;
import org.endeavor.game.entity.object.GameObject;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.net.out.impl.SendAnimateObject;
import org.endeavor.game.entity.player.net.out.impl.SendObject;

/**
 * 
 * @author allen_000
 * Credits to matrix devs for the design of this class.
 */
public class WallHandler {

	private ClanWar war;
	private ArrayList<GameObject> objectList = new ArrayList<GameObject>();
	
	public WallHandler(ClanWar war) {
		this.war = war;
	}
	
	public void spawnWalls() {
		//System.out.println("Spawning walls!");
		TasksExecutor.slowExecutor.schedule(new Runnable() {

			@Override
			public void run() {
				sendWall();
			}
			
		}, 0, TimeUnit.MILLISECONDS);
	}
	
	public void dropWalls() {
		System.out.println("Dropping walls!");
		final ArrayList<Player> requesters = war.getRequesterPlayers();
		final ArrayList<Player> accepters = war.getAccepterPlayers();
		TasksExecutor.slowExecutor.schedule(new Runnable() {

			@Override
			public void run() {
				for(Player player : requesters) { 
					dropWall(player);
				}
				for(Player player : accepters)
					dropWall(player);
			}
			
		}, 0, TimeUnit.MILLISECONDS);
	}
	
	public void dropWall(Player player) {
		for(GameObject obj : objectList)
			player.send(new SendAnimateObject(new RSObject(obj.getLocation().getX(), obj.getLocation().getY(), obj.getLocation().getZ(), obj.getId(), 10,0), 
					war.getRules().getArena().getAnimationID()));
	}
	
	public void sendWall() {
		ClanArena arena = this.war.getRules().getArena();
		final ArrayList<Player> requesters = war.getRequesterPlayers();
		final ArrayList<Player> accepters = war.getAccepterPlayers();
		if(arena == ClanArena.CLASSIC) {
			int objectIndex = 0;
			for(int start = 3268; start <= (3317); start++) {
				objectIndex = (objectIndex + 1) % 3;
				GameObject obj = new GameObject(arena.getWallID() + objectIndex, start, 3775, war.getHeight(), 10, 0);
				for(Player player : requesters)
					player.send(new SendObject(player, obj));
				for(Player player : accepters)
					player.send(new SendObject(player, obj));
				objectList.add(obj);
			}
		} else if(arena == ClanArena.PLATEAU) {
			for(int start = 2856; start <= 2907; start++) {
				GameObject obj = new GameObject(arena.getWallID(), start, 5921, war.getHeight(), 10, 0);
				for(Player player : requesters)
					player.send(new SendObject(player, obj));
				for(Player player : accepters)
					player.send(new SendObject(player, obj));
				objectList.add(obj);
			}
		} else if(arena == ClanArena.FORSAKEN_QUARRY) {
			for(int start = 2885; start <= 2938; start++) {
				GameObject obj = new GameObject(arena.getWallID(), start, 5537, war.getHeight(), 10, 0);
				for(Player player : requesters)
					player.send(new SendObject(player, obj));
				for(Player player : accepters)
					player.send(new SendObject(player, obj));
				objectList.add(obj);
			}
		} else if(arena == ClanArena.BLASTED_FORREST) {
			for(int start = 2884; start <= 2939; start++) {
				GameObject obj = new GameObject(arena.getWallID(), start, 5666, war.getHeight(), 10, 0);
				for(Player player : requesters)
					player.send(new SendObject(player, obj));
				for(Player player : accepters)
					player.send(new SendObject(player, obj));
				objectList.add(obj);
			}
		} else if(arena == ClanArena.TURRETS) {
			for(int start = 2692; start <= 2748; start++) {
				GameObject obj = new GameObject(arena.getWallID(), start, 5568, war.getHeight(), 10, 0);
				for(Player player : requesters)
					player.send(new SendObject(player, obj));
				for(Player player : accepters)
					player.send(new SendObject(player, obj));
				objectList.add(obj);
			}
		} else {
			System.out.println("none!");
		}
	}
	
	public void removeWalls() {
		final ArrayList<Player> requesters = war.getRequesterPlayers();
		final ArrayList<Player> accepters = war.getAccepterPlayers();
		TasksExecutor.slowExecutor.schedule(new Runnable() {

			@Override
			public void run() {
				for(Player player : requesters) { 
					removeWall(player);
				}
				for(Player player : accepters)
					removeWall(player);
			}
			
		}, 0, TimeUnit.MILLISECONDS);
	}
	
	public void removeWall(Player player) {
		ClanArena arena = this.war.getRules().getArena();
		if(arena == ClanArena.CLASSIC) {
			int objectIndex = 0;
			for(int start = 3268; start <= (3317); start++) {
				objectIndex = (objectIndex + 1) % 3;
				player.send(new SendObject(player, new GameObject(2376, start, 3775, war.getHeight(), 10, 0)));
			}
		} else if(arena == ClanArena.PLATEAU) {
			for(int start = 2856; start <= 2907; start++)
				player.send(new SendObject(player, new GameObject(2376, start, 5921, war.getHeight(), 10, 0)));
		} else if(arena == ClanArena.FORSAKEN_QUARRY) {
			for(int start = 2885; start <= 2938; start++)
				player.send(new SendObject(player, new GameObject(2376, start, 5537, war.getHeight(), 10, 0)));
		} else if(arena == ClanArena.BLASTED_FORREST) {
			for(int start = 2884; start <= 2939; start++)
				player.send(new SendObject(player, new GameObject(2376, start, 5666, war.getHeight(), 10, 0)));
		} else if(arena == ClanArena.TURRETS) {
			for(int start = 2692; start <= 2748; start++)
				player.send(new SendObject(player, new GameObject(2376, start, 5568, war.getHeight(), 10, 0)));
		}
	}
	
}
