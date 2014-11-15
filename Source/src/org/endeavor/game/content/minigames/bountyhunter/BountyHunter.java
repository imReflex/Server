package org.endeavor.game.content.minigames.bountyhunter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.endeavor.engine.TasksExecutor;
import org.endeavor.engine.utility.Misc;
import org.endeavor.game.content.Inventory;
import org.endeavor.game.content.combat.Combat.CombatTypes;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.item.Item;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.controllers.ControllerManager;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;
import org.endeavor.game.entity.player.net.out.impl.SendPlayerHint;
import org.endeavor.game.entity.player.net.out.impl.SendRemoveInterfaces;
import org.endeavor.game.entity.player.net.out.impl.SendString;
import org.endeavor.game.entity.player.net.out.impl.SendWalkableInterface;

/**
 * 
 * @author Allen K.
 *
 */
public class BountyHunter {

	private ArrayList<Player> lowCrater = new ArrayList<Player>();
	private ArrayList<Player> mediumCrater = new ArrayList<Player>();
	private ArrayList<Player> highCrater = new ArrayList<Player>();
	private ArrayList<TargetMatch> targetMatches = new ArrayList<TargetMatch>();
	private ArrayList<Player> targetQueue = new ArrayList<Player>();
	private static BountyHunter instance = new BountyHunter();
	private static ArrayList<Integer> blocked = new ArrayList<Integer>();
	
	private final static int TARGET_NAME = 25350;
	private final static int PENALTY_TYPE = 25351;
	private final static int PENALTY_TIME = 25352;
	
	private final BountyHunterController controller;
	
	public BountyHunter() {
		controller = new BountyHunterController();
		/*for(int id : UNALLOWED_ITEMS)
			blocked.add(id);*/
	}
	
	public void enterCrater(Craters crater, Player player) {
		if(!canEnterCrater(crater, player)) {
			player.send(new SendMessage("Your combat level is too high to enter this crater."));
			return;
		}
		if(player.getPenaltyTimer() != null) {
			if(player.getPenaltyTimer().hasEnterTime()) {
				player.send(new SendMessage("You cannot enter the a bounty hunter crater yet, please wait."));
				return;
			}
		}
		if(!doesFollowRules(player))
			return;
		int r = Misc.randomNumber(CRATER_CAVES.length);
		int x = CRATER_CAVES[r][0];
		int y = CRATER_CAVES[r][1];
		if(crater == Craters.LOW) {
			lowCrater.add(player);
			player.teleport(new Location(x,y,4));
			player.setController(controller);
		} else if(crater == Craters.MEDIUM) {
			mediumCrater.add(player);
			player.teleport(new Location(x,y,8));
			player.setController(controller);
		} else if(crater == Craters.HIGH) {
			highCrater.add(player);
			player.teleport(new Location(x,y,12));
			player.setController(controller);
		}
		if(this.isInBountyHunter(player)) {
			this.sentBountyInterface(player);
			this.scheduleTargetSearch(player, true);
		}
	}
	
	public void exitCrater(Player player) {
		if(!isInBountyHunter(player) || !(player.getController() instanceof BountyHunterController)) {
			player.setController(ControllerManager.DEFAULT_CONTROLLER);
			player.teleport(new Location(3152, 3672, 0));
		}
		if(player.getPenaltyTimer() != null) {
			if(player.getPenaltyTimer().hasExitTime()) {
				player.send(new SendMessage("You cannot leave this crater, you have a leave penalty."));
				return;
			}
		}
		Location togo = new Location(3166, 3679, 0);
		if(lowCrater.contains(player)) {
			togo = new Location(3152, 3672, 0);
			lowCrater.remove(player);
		} else if(mediumCrater.contains(player)) {
			togo = new Location(3158, 3680, 0);
			mediumCrater.remove(player);
		} else if(highCrater.contains(player)) {
			togo = new Location(3164, 3685, 0);
			highCrater.remove(player);
		}
		player.teleport(togo);
		player.setController(ControllerManager.DEFAULT_CONTROLLER);
		player.send(new SendRemoveInterfaces());
		if(player.getTargetMatch() != null) {
			Player target = player.getTargetMatch().getTargetForPlayer(player);
			if(target.getTargetMatch() == player.getTargetMatch()) {
				target.setTargetMatch(null);
				this.sentBountyInterface(target);
				target.send(new SendPlayerHint(true, -1));
			}
			player.setTargetMatch(null);
			player.send(new SendPlayerHint(true, -1));
		}
	}
	
	public void forceExitCrater(Player player) {
		Location togo = new Location(3166, 3679, 0);
		if(lowCrater.contains(player)) {
			togo = new Location(3152, 3672, 0);
			lowCrater.remove(player);
		} else if(mediumCrater.contains(player)) {
			togo = new Location(3158, 3680, 0);
			mediumCrater.remove(player);
		} else if(highCrater.contains(player)) {
			togo = new Location(3164, 3685, 0);
			highCrater.remove(player);
		}
		player.teleport(togo);
		player.setController(ControllerManager.DEFAULT_CONTROLLER);
		if(player.getTargetMatch() != null) {
			Player target = player.getTargetMatch().getTargetForPlayer(player);
			if(target.getTargetMatch() == player.getTargetMatch()) {
				target.setTargetMatch(null);
				this.sentBountyInterface(target);
				target.send(new SendPlayerHint(true, -1));
			}
			player.setTargetMatch(null);
			player.send(new SendPlayerHint(true, -1));
		}
	}
	
	public void onDeath(Player player) {
		lowCrater.remove(player);
		mediumCrater.remove(player);
		highCrater.remove(player);
		player.setController(ControllerManager.DEFAULT_CONTROLLER);
		player.send(new SendRemoveInterfaces());
	}
	
	public void addPickupPenalty(Player player) {
		if(this.isInBountyHunter(player)) {
			if(player.getPenaltyTimer() != null) {
				player.getPenaltyTimer().setPickupTime(180);
			} else {
				player.setPenaltyTimer(new PenatlyTimer(player, 180, 0, 0));
				TasksExecutor.fastExecutor.scheduleAtFixedRate(player.getPenaltyTimer(), 0, 1000);
			}
			player.send(new SendMessage("You now have a pickup penalty of 180 seconds."));
		}
	}
	
	public void addLeavePenalty(Player player) {
		if(this.isInBountyHunter(player) && (player.getPenaltyTimer() != null && player.getPenaltyTimer().hasPickupTime())) {
			if(player.getPenaltyTimer() != null) {
				player.getPenaltyTimer().setExitTime(180);
			}
			player.send(new SendMessage("You now have a pickup penalty of 180 seconds."));
		}
	}
	
	public boolean handleObjectClick(Player player, int objectId) {
		Craters crater = Craters.getCraterForId(objectId);
		if(crater != null) {
			enterCrater(crater, player);
			return true;
		}
		if(objectId == 28122) {
			exitCrater(player);
			return true;
		}
		return false;
	}
	
	public void sentBountyInterface(Player player) {
		if(isInBountyHunter(player)) {
			String name = "Searching";
			if(player.getTargetMatch() != null)
				name = player.getTargetMatch().getTargetForPlayer(player).getUsername();
			player.send(new SendString(name, TARGET_NAME));
			if(player.getPenaltyTimer() == null) {
				player.send(new SendString("No Penalty", PENALTY_TYPE));
				player.send(new SendString("", PENALTY_TIME));
			} else {
				if(player.getPenaltyTimer().getExitTime() > 0) {
					player.send(new SendString("Leave Penalty: ", PENALTY_TYPE));
					player.send(new SendString(player.getPenaltyTimer().getExitTime() + " sec", PENALTY_TIME));
				} else {
					if(player.getPenaltyTimer().getPickupTime() > 0) {
						player.send(new SendString("Pickup Penalty: ", PENALTY_TYPE));
						player.send(new SendString(player.getPenaltyTimer().getPickupTime() + " sec", PENALTY_TIME));
					}
				}
			}
			player.send(new SendWalkableInterface(25347));
		}
	}
	
	public Player getTargetForPlayer(Player player) {
		for(TargetMatch match : targetMatches)
			if(match.contains(player))
				return match.getTargetForPlayer(player);
		return null;
	}
	
	public void scheduleTargetSearch(final Player player, boolean newBH) {
		if(targetQueue.contains(player) || player.getTargetMatch() != null)
			return;
		targetQueue.add(player);
		TasksExecutor.slowExecutor.schedule(new Runnable() {
			@Override
			public void run() {
				if(player.getTargetMatch() == null) {
					BountyHunter.getSingleton().assignSuitableTarget(player);
					removeTargetQueue(player);
				}
			}
		}, newBH ? 7 : 16, TimeUnit.SECONDS);
	}
	
	public void removeTargetQueue(Player player) {
		targetQueue.remove(player);
	}
	
	public void assignSuitableTarget(Player player) {
		System.out.println("Assigning target: " + player.getUsername());
		if(player.getTargetMatch() != null || stillHasTarget(player)) {
			System.out.println(player.getUsername() + " already has a target."); 
			return;
		}
		if(isInBountyHunter(player) && player.getController() instanceof BountyHunterController) {
			ArrayList<Player> possibleTargets = new ArrayList<>();
			System.out.println("Building possible targets...");
			if(lowCrater.contains(player)) {
				for(Player target : lowCrater)
					if((target.getTargetMatch() == null || !stillHasTarget(target)) && target.getController() instanceof BountyHunterController && target != player)
						possibleTargets.add(target);
			} else if(mediumCrater.contains(player)) {
				for(Player target : mediumCrater)
					if((target.getTargetMatch() == null || !stillHasTarget(target)) && target.getController() instanceof BountyHunterController && target != player)
						possibleTargets.add(target);
			} else if(highCrater.contains(player)) {
				for(Player target : highCrater)
					if((target.getTargetMatch() == null || !stillHasTarget(target)) && target.getController() instanceof BountyHunterController && target != player)
						possibleTargets.add(target);
			}
			System.out.println("Assigning possible target...");
			if(possibleTargets.size() == 0) {
				player.send(new SendMessage("There are currently no active targets... trying again."));
				scheduleTargetSearch(player, false);
				return;
			}
			Player target = possibleTargets.get(Misc.randomNumber(possibleTargets.size()));
			System.out.println("Target: " + target.getUsername());
			TargetMatch match = new TargetMatch(player, target);
			player.setTargetMatch(match);
			target.setTargetMatch(match);
			player.send(new SendMessage("Your target is now: " + target.getUsername()));
			target.send(new SendMessage("Your target is now: " + player.getUsername()));
			player.send(new SendPlayerHint(true, target.getIndex()));
			target.send(new SendPlayerHint(true, player.getIndex()));
			this.sentBountyInterface(player);
			this.sentBountyInterface(target);
			
		} else {
			System.out.println(player.getUsername() + " is not in bounty hunter.");
		}
	}
	
	public boolean stillHasTarget(Player player) {
		if(player.getTargetMatch() == null)
			return false;
		return player.getTargetMatch().getTargetForPlayer(player).getTargetMatch() == player.getTargetMatch();
	}
	
	public boolean isInBountyHunter(Player player) {
		return lowCrater.contains(player) || mediumCrater.contains(player) || highCrater.contains(player);
	}
	
	private boolean canEnterCrater(Craters crater, Player player) {
		int combatLevel = player.getSkill().getCombatLevel();
		return (crater.getLowRange() <= combatLevel && crater.getHighRange() >= combatLevel) || 
				(combatLevel <= crater.getLowRange());
	}
	
	private boolean doesFollowRules(Player player) {
		Inventory inventory = player.getInventory();
		//EquipmentPiece<Integer,CombatTypes> equipmentPiece = new EquipmentPiece<Integer,CombatTypes>();
		for(Item item : inventory.getItems()) {
			/*int equipSlot = item.getEquipmentDefinition().getSlot();
			*/
			/*if(blocked.contains(item.getId())) {
				player.send(new SendMessage("You cannot bring a " + item.getDefinition().getName() + " into a crater."));
				return false;
			}*/
			if(item == null)
				continue;
			if(item.getAmount() > 1000) {
				player.send(new SendMessage("You cannot bring more than 1000 of one item into a crater."));
				return false;
			}
			if(item.getDefinition().isNote()) {
				player.send(new SendMessage("You cannot bring noted items into a crater."));
				return false;
			}
			String name = item.getDefinition().getName();
			if(name.contains("log") || name.contains("raw") || name.contains("unfinished") || name.contains("coin")) {
				player.send(new SendMessage("You may only bring combat items into a crater."));
				return false;
			}
		}
		return true;
	}
	
	public static BountyHunter getSingleton() {
		return instance;
	}
	
	/**
	 * The list of items which are unallowed in bounty hunter.
	 */
	public static final int[] UNALLOWED_ITEMS = { 995, 996, 13734, 13736, 13738, 13740, 13742, 13744, 18335, 18349, 18351, 18353, 18355, 
				18357, 18359, 18361, 18363, 1037, 1038, 1039, 1040, 1041, 1042, 1043, 1044, 1045, 1046, 1047, 1048, 1049, 
				1050, 1051, 1052, 1053, 1054, 1055, 1056, 1057, 1058, 20072, 19780, 20135, 20139, 20143, 20147, 20151, 20155, 
				20159, 20163, 20167 };
	
	public static final int[][] CRATER_CAVES = {{ 3163, 3696 }, { 3171, 3701 },
		{ 3180, 3708 }, { 3181, 3720 }, { 3171, 3737 }, { 3170, 3746 },
		{ 3163, 3753 }, { 3147, 3758 }, { 3135, 3758 }, { 3121, 3754 },
		{ 3110, 3747 }, { 3091, 3735 }, { 3086, 3717 }, { 3091, 3706 },
		{ 3096, 3692 }, { 3101, 3682 }, { 3108, 3671 }, { 3124, 3665 },
		{ 3138, 3669 }, { 3146, 3681 }};
	
}
