package org.endeavor.game.entity;

/**
 * Represents a single sound
 * 
 * @author Michael Sasse
 * 
 */
public class Sound {

	/**
	 * The id of the sound
	 */
	public final short id;

	/**
	 * The delay of the sound
	 */
	public final byte delay;

	/**
	 * The type of sound
	 */
	public final byte type;

	/**
	 * Constructs a new sound with a delay of 0
	 * 
	 * @param id
	 *            THe id of the sound
	 */
	public Sound(int id) {
		this.id = ((short) id);
		delay = 0;
		type = 10;
	}

	/**
	 * Constructs a sound with an id and delay
	 * 
	 * @param id
	 *            The id of the sound
	 * @param delay
	 *            The delay of the sound
	 */
	public Sound(int id, int delay) {
		this.id = ((short) id);
		this.delay = ((byte) delay);
		type = 10;
	}

	/**
	 * Gets the id of the sound
	 * 
	 * @return
	 */
	public short getId() {
		return id;
	}

	/**
	 * Gets the delay of the sound
	 * 
	 * @return
	 */
	public byte getDelay() {
		return delay;
	}

	/**
	 * Gets the type of the sound
	 * 
	 * @return
	 */
	public byte getType() {
		return type;
	}
}
