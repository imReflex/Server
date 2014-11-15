package org.endeavor.game.content.pets;

import java.io.Serializable;

import org.endeavor.engine.task.Task;
import org.endeavor.engine.task.TaskQueue;
import org.endeavor.game.content.minigames.dungeoneering.DungConstants;
import org.endeavor.game.entity.item.Item;
import org.endeavor.game.entity.mob.Mob;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;

public class Pets implements Serializable {
	private static final long serialVersionUID = 1915833321517272577L;
	private transient Player p;
	private boolean petTut = false;

	private Pet pet = null;

	public Pets(Player p) {
		this.p = p;
	}

	public void init(int id) {
		PetData.PetStage data = PetData.getPetDataForItem(id);

		if (data == null) {
			return;
		}

		if ((pet != null) || (p.getSummoning().hasFamiliar())) {
			p.getClient().queueOutgoingPacket(new SendMessage("You already have a familiar."));
			return;
		}
		p.getInventory().remove(new Item(id, 1));

		final Mob mob = new Mob(p, data.npcId, false, false, true, p.getLocation());
		mob.getFollowing().setIgnoreDistance(true);
		mob.getFollowing().setFollow(p);
		chat(mob);

		pet = new Pet(mob, data.stage, (byte) 0);

		TaskQueue.queue(new Task(mob, 100) {
			/**
			 * 
			 */
			private static final long serialVersionUID = -5691323711385473855L;

			@Override
			public void execute() {
				if (pet == null) {
					stop();
					return;
				}

				Pets.chat(mob);
				checkForGrowth();
			}

			@Override
			public void onStop() {
			}
		});
	}

	public void checkForGrowth() {
		if (pet == null) {
			return;
		}

		if (System.currentTimeMillis() - pet.start >= 3600000L) {
			grow(false);
			pet.start = System.currentTimeMillis();
		}
	}

	public void grow(boolean overgrow) {
		PetData.PetStage stage = PetData.getPetDataForMob(pet.mob.getId());
		if ((stage.next == null) || ((!overgrow) && (stage.petType.overgrows) && (pet.stage == 1))) {
			return;
		}

		pet.mob.transform(stage.next.npcId);

		p.getClient().queueOutgoingPacket(new SendMessage("Your pet has grown!"));

		pet.start = System.currentTimeMillis();
		Pet tmp97_94 = pet;
		tmp97_94.stage = ((byte) (tmp97_94.stage + 1));
	}

	public boolean remove() {
		if (pet != null) {
			if (System.currentTimeMillis() - pet.start >= 3600000L) {
				grow(false);
				pet.start = System.currentTimeMillis();
			}

			Item item = new Item(PetData.getPetDataForMob(pet.mob.getId()).itemId, 1);

			if (p.getController().equals(DungConstants.DUNG_CONTROLLER)) {
				p.getBank().add(item);
				p.getClient().queueOutgoingPacket(new SendMessage("Your pet has been added to your bank."));
				return false;
			}

			if (!p.getInventory().hasSpaceFor(item)) {
				if (p.getBank().hasSpaceFor(item)) {
					p.getBank().add(item);
					p.getClient().queueOutgoingPacket(new SendMessage("Your pet has been added to your bank."));
				} else {
					p.getClient().queueOutgoingPacket(
							new SendMessage("You must free some inventory space to pick up your pet."));
					return false;
				}
			} else
				p.getInventory().add(item);

			pet.mob.remove();
			pet = null;
		}

		return true;
	}

	public boolean hasPet() {
		return pet != null;
	}

	public Pet getPet() {
		return pet;
	}

	public boolean hadPetTut() {
		return petTut;
	}

	public void setPetTut(boolean petTut) {
		this.petTut = petTut;
	}

	public static String[] getPetChat(int id) {
		return PetData.getPetDataForMob(id).messages;
	}

	public static final void chat(Mob mob) {
		String[] messages = PetData.getPetDataForMob(mob.getId()).messages;
		if (messages != null)
			mob.getUpdateFlags().sendForceMessage(
					messages[org.endeavor.engine.utility.Misc.randomNumber(messages.length)]);
	}

	public static final boolean isItemPet(int id) {
		return PetData.getPetDataForItem(id) != null;
	}

	public static final boolean isMobPet(int id) {
		return PetData.getPetDataForMob(id) != null;
	}

	public class Pet implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = -7082888780452953667L;
		protected Mob mob = null;
		protected byte stage = 0;
		protected long start = System.currentTimeMillis();

		public Pet(Mob mob, byte stage, byte hunger) {
			this.mob = mob;
			this.stage = stage;
		}
		
		public Mob getMob() {
			return mob;
		}
	}
	
	public void setPlayer(Player player) {
		this.p = player;
	}
}
