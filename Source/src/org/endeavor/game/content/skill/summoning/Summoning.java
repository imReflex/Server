package org.endeavor.game.content.skill.summoning;

import java.io.Serializable;

import org.endeavor.GameSettings;
import org.endeavor.engine.task.Task;
import org.endeavor.engine.task.TaskQueue;
import org.endeavor.engine.utility.GameDefinitionLoader;
import org.endeavor.engine.utility.Misc;
import org.endeavor.game.GameConstants;
import org.endeavor.game.content.dialogue.impl.ConfirmDialogue;
import org.endeavor.game.entity.Entity;
import org.endeavor.game.entity.Graphic;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.item.Item;
import org.endeavor.game.entity.item.impl.GroundItemHandler;
import org.endeavor.game.entity.mob.Mob;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.net.out.impl.SendConfig;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;
import org.endeavor.game.entity.player.net.out.impl.SendModelAnimation;
import org.endeavor.game.entity.player.net.out.impl.SendNPCDialogueHead;
import org.endeavor.game.entity.player.net.out.impl.SendRemoveInterfaces;
import org.endeavor.game.entity.player.net.out.impl.SendString;

public class Summoning implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1277967356593897785L;
	private final Player p;
	private FamiliarMob familiar = null;

	private int time = -1;

	private int special = 60;

	private Task removalTask = null;

	private BOBContainer container = null;

	private boolean attack = false;

	private long specialDelay = System.currentTimeMillis();

	public Summoning(Player p) {
		this.p = p;
	}

	public void updateSpecialAmount() {
		for (int i = 0; i < 17; i++)
			p.getClient().queueOutgoingPacket(
					new SendConfig(334 + i, (special == 60) || (special > 3 * (i + 1)) ? 0 : 1));
	}

	public boolean isFamilarBOB() {
		return (familiar != null) && (container != null);
	}

	public void removeForLogout() {
		familiar.remove();
	}

	public void onUpdateInventory() {
		if (familiar != null) {
			int scroll = SummoningConstants.getScrollForFamiliar(familiar);
			int am = p.getInventory().getItemAmount(scroll);

			p.getClient().queueOutgoingPacket(new SendString("" + (scroll == -1 ? 0 : am), 18024));

			p.getClient().queueOutgoingPacket(new SendConfig(330, am > 0 ? 1 : 0));
		} else {
			p.getClient().queueOutgoingPacket(new SendString("0", 18024));
			p.getClient().queueOutgoingPacket(new SendConfig(330, 0));
		}
	}

	public void refreshSidebar() {
		if (familiar == null) {
			p.getClient().queueOutgoingPacket(new SendString(getMinutes(0), 18043));
			p.getClient().queueOutgoingPacket(new SendNPCDialogueHead(4000, 18021));
			p.getClient().queueOutgoingPacket(new SendString("None", 18028));
			p.getClient().queueOutgoingPacket(new SendConfig(333, 0));
		} else {
			p.getClient().queueOutgoingPacket(new SendConfig(333, attack ? 1 : 0));
		}

		updateSpecialAmount();
		onUpdateInventory();
	}

	public void onFamiliarDeath() {
		if (familiar.getData().bobSlots > 0) {
			if (p.getInterfaceManager().hasBOBInventoryOpen()) {
				p.getClient().queueOutgoingPacket(new SendRemoveInterfaces());
			}

			for (Item i : container.getItems()) {
				if (i != null) {
					GroundItemHandler.add(i, familiar.getLocation(), p);
				}
			}

			container.clear();
			container = null;
		}

		familiar = null;
		attack = false;
		refreshSidebar();
	}

	public String getMinutes(int ticks) {
		if ((familiar != null) && (ticks > 0)) {
			int minutes = ticks / 100;
			int seconds = (int) (ticks % 100 * 0.6D);

			return minutes + "." + (seconds < 10 ? "0" + seconds : Integer.valueOf(seconds));
		}
		return "0.0";
	}

	public boolean interact(Mob mob, int option) {
		if ((mob == null) || (mob.getOwner() == null) || !(mob instanceof FamiliarMob)) {
			return false;
		}

		if (((mob instanceof FamiliarMob)) && (!mob.getOwner().equals(p))) {
			p.getClient().queueOutgoingPacket(new SendMessage("This is not your familiar!"));
			return true;
		}

		if ((option == 2) && familiar.getData() != null && (familiar.getData().bobSlots > 0) && (container != null)) {
			container.open();
			return true;
		}

		return false;
	}

	public void doSpecial() {
		if (familiar != null) {
			FamiliarSpecial spec = SummoningConstants.getSpecial(familiar.getData());

			if (spec != null) {
				int scroll = SummoningConstants.getScrollForFamiliar(familiar);

				if (scroll == -1) {
					p.getClient().queueOutgoingPacket(
							new SendMessage("This familiar's scroll could not be found, please report this error."));
					return;
				}

				if ((spec.getSpecialType() == FamiliarSpecial.SpecialType.COMBAT)
						&& (familiar.getCombat().getAttacking() == null)) {
					p.getClient().queueOutgoingPacket(
							new SendMessage("You must be attacking something to use this special move!"));
					return;
				}

				if (System.currentTimeMillis() - specialDelay < 5000L) {
					p.getClient().queueOutgoingPacket(
							new SendMessage("You must let your familiar rest for a few seconds!"));
					return;
				}

				if (special < spec.getAmount()) {
					p.getClient().queueOutgoingPacket(
							new SendMessage("You do not have enough special points to perform that attack."));
					return;
				}

				if (!p.getInventory().hasItemId(scroll)) {
					p.getClient().queueOutgoingPacket(
							new SendMessage("You do not have the scroll required for this special move."));
					return;
				}

				if (spec.execute(p, familiar)) {
					p.getUpdateFlags().sendAnimation(7660, 0);

					specialDelay = System.currentTimeMillis();
					special -= spec.getAmount();
					p.getSkill().addExperience(21, spec.getExperience());

					p.getInventory().remove(scroll, 1);

					updateSpecialAmount();
				}
			} else {
				p.getClient().queueOutgoingPacket(new SendMessage("This special is not supported yet."));
			}
		}
	}

	public boolean click(int button) {
		switch (button) {
		case 70098:
		case 70103:
			doSpecial();
			return true;
		case 70105:
			if (familiar != null) {
				attack = (!attack);
				p.getClient().queueOutgoingPacket(new SendConfig(333, attack ? 1 : 0));
			} else {
				p.getClient().queueOutgoingPacket(new SendMessage("You do not have a familiar."));
			}
			return true;
		case 70112:
			if (familiar != null) {
				if (p.getInventory().hasItemId(familiar.getData().pouch)) {
					p.getInventory().remove(familiar.getData().pouch);
					time = 20;
					renew();
					familiar.getUpdateFlags().sendGraphic(new Graphic(familiar.getSize() == 1 ? 1314 : 1315, 0, false));
				} else {
					p.getClient()
							.queueOutgoingPacket(new SendMessage("You need another pouch to renew this familiar."));
				}
			} else
				p.getClient().queueOutgoingPacket(new SendMessage("You do not have a familiar."));

			return true;
		case 70118:
			if (familiar == null) {
				p.getClient().queueOutgoingPacket(new SendMessage("You do not have a familiar."));
				return true;
			}
			
			p.start(new ConfirmDialogue(p, new String[] {"Are you sure you want to dismiss this familiar?", "Any BoB items will be dropped."}) {

				@Override
				public void onConfirm() {
					if (familiar != null) {
						familiar.remove();
						onFamiliarDeath();
					} else {
						p.getClient().queueOutgoingPacket(new SendMessage("You do not have a familiar."));
					}
				}
				
			});
			return true;
		case 70115:
			if (familiar != null) {
				if (Misc.getManhattanDistance(p.getLocation(), familiar.getLocation()) > 3) {
					if (!p.getSkill().locked())
						p.getSkill().lock(1);
					else {
						return true;
					}

					Location l = GameConstants.getClearAdjacentLocation(p.getLocation(), familiar.getSize());

					familiar.teleport(l != null ? l : p.getLocation());
				}
			} else
				p.getClient().queueOutgoingPacket(new SendMessage("You do not have a familiar."));

			return true;
		case 70109:
			if (familiar != null) {
				if (container != null)
					container.open();
				else
					p.getClient().queueOutgoingPacket(new SendMessage("Your current familiar cannot store items."));
			} else {
				p.getClient().queueOutgoingPacket(new SendMessage("You do not have a familiar."));
			}
			return true;
		}
		return false;
	}

	public boolean summon(int id) {
		Familiar f = SummoningConstants.getFamiliarForPouch(id);

		if (f != null) {
			if ((!GameSettings.DEV_MODE) && (!SummoningConstants.isAllowed(f))) {
				p.getClient().queueOutgoingPacket(new SendMessage("This familiar is not yet functional."));
				return true;
			}

			if (p.getMaxLevels()[21] < f.levelRequired) {
				p.getClient().queueOutgoingPacket(
						new SendMessage("You need a Summoning level of " + f.levelRequired + " to summon this mob."));
				return true;
			}

			if ((familiar != null) || (p.getPets().hasPet())) {
				p.getClient().queueOutgoingPacket(new SendMessage("You already have a familiar."));
				return true;
			}

			if (p.getLevels()[21] < f.pointsForSummon) {
				p.getClient().queueOutgoingPacket(
						new SendMessage("You must recharge your Summoning points to do this!"));
				return true;
			}

			Location l = GameConstants.getClearAdjacentLocation(p.getLocation(),
					GameDefinitionLoader.getNpcDefinition(f.mob).getSize());

			if (l == null) {
				p.getClient().queueOutgoingPacket(new SendMessage("You must make room for your familiar!"));
				return true;
			}
			short[] tmp243_238 = p.getLevels();
			tmp243_238[21] = ((short) (tmp243_238[21] - f.pointsForSummon));
			p.getSkill().update(21);

			p.getInventory().remove(id, 1, true);

			familiar = new FamiliarMob(f, p, l);

			familiar.getUpdateFlags().sendGraphic(new Graphic(familiar.getSize() == 1 ? 1314 : 1315, 0, false));

			p.getClient().queueOutgoingPacket(new SendModelAnimation(18021, -1));
			p.getClient().queueOutgoingPacket(new SendNPCDialogueHead(f.mob, 18021));
			p.getClient().queueOutgoingPacket(
					new SendString(GameDefinitionLoader.getNpcDefinition(f.mob).getName(), 18028));

			time = 20;
			renew();

			onUpdateInventory();

			if (f.bobSlots > 0)
				container = new BOBContainer(p, f.bobSlots);
			else {
				container = null;
			}

			attack = false;
			p.getClient().queueOutgoingPacket(new SendConfig(333, 0));

			return true;
		}

		return false;
	}

	public void onLogin() {
		if (p.getAttributes().get("summoningfamsave") != null) {
			summonOnLogin(p.getAttributes().getInt("summoningfamsave"));

			if (p.getAttributes().get("summoningbobinventory") != null) {
				container.setItems((Item[]) p.getAttributes().get("summoningbobinventory"));
			}
		}

		TaskQueue.queue(new Task(p, 35) {
			/**
			 * 
			 */
			private static final long serialVersionUID = -3381545683668845866L;

			@Override
			public void execute() {
				if (special < 60) {
					special += 5;

					if (special > 60) {
						special = 60;
					}

					updateSpecialAmount();
				}
			}

			@Override
			public void onStop() {
			}
		});
		refreshSidebar();
	}

	public void summonOnLogin(int id) {
		Familiar f = Familiar.forMobId(id);

		if ((f == null) || (!SummoningConstants.isAllowed(f))) {
			return;
		}

		Location l = GameConstants.getClearAdjacentLocation(p.getLocation(),
				GameDefinitionLoader.getNpcDefinition(f.mob).getSize());

		familiar = new FamiliarMob(f, p, l == null ? p.getLocation() : l);

		familiar.getUpdateFlags().sendGraphic(new Graphic(familiar.getSize() == 1 ? 1314 : 1315, 0, false));

		p.getClient().queueOutgoingPacket(new SendModelAnimation(18021, -1));
		p.getClient().queueOutgoingPacket(new SendNPCDialogueHead(f.mob, 18021));
		p.getClient()
				.queueOutgoingPacket(new SendString(GameDefinitionLoader.getNpcDefinition(f.mob).getName(), 18028));
		renew();

		onUpdateInventory();

		if (f.bobSlots > 0)
			container = new BOBContainer(p, f.bobSlots);
		else
			container = null;
	}

	public void renew() {
		if (familiar != null) {
			if (removalTask != null) {
				removalTask.stop();
			}

			p.getClient().queueOutgoingPacket(new SendString(getMinutes(time * (familiar.getData().time / 20)), 18043));

			removalTask = new Task(familiar, familiar.getData().time / 20) {
				/**
				 * 
				 */
				private static final long serialVersionUID = 6543222022965293040L;

				@Override
				public void onStop() {
				}

				@Override
				public void execute() {
					if ((familiar == null) || (familiar.isDead())) {
						stop();
						return;
					}

					time -= 1;

					p.getClient().queueOutgoingPacket(
							new SendString(getMinutes(time * (familiar.getData().time / 20)), 18043));

					if (time == 1) {
						p.getClient().queueOutgoingPacket(
								new SendMessage("Your familiar will dissapear in "
										+ (int) (familiar.getData().time / 20 * 0.6D) + " seconds."));
					}

					if (time == 0) {
						familiar.getUpdateFlags().sendGraphic(
								new Graphic(familiar.getSize() == 1 ? 1314 : 1315, 0, false));
						setTaskDelay(4);
						return;
					}
					if (time < 0) {
						if (familiar != null) {
							familiar.remove();
							onFamiliarDeath();
						}

						stop();
					}
				}
			};
			TaskQueue.queue(removalTask);
		}
	}

	public BOBContainer getContainer() {
		return container;
	}

	public boolean hasFamiliar() {
		return familiar != null;
	}

	public boolean isFamiliar(Entity e) {
		return (familiar != null) && (e.equals(familiar));
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public int getSpecialAmount() {
		return special;
	}

	public void setSpecial(int special) {
		this.special = special;
	}

	public Familiar getFamiliarData() {
		return familiar != null ? familiar.getData() : null;
	}

	public boolean isAttack() {
		return attack;
	}

	public void setAttack(boolean attack) {
		this.attack = attack;
	}
}
