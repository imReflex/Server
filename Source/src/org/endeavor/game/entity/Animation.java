package org.endeavor.game.entity;

import java.io.Serializable;

/**
 * Represents a single animation
 * 
 * @author Michael Sasse
 * 
 */
public final class Animation implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8845090347809744811L;

	/**
	 * The id of the animation
	 */
	private short id;

	/**
	 * The delay to perform the animation
	 */
	private byte delay;

	/**
	 * Constructs a new animation with an id and a delay
	 * 
	 * @param id
	 *            The id of the animation
	 * @param delay
	 *            The delay of the animation
	 */
	public Animation(int id, int delay) {
		this.id = ((short) id);
		this.delay = ((byte) delay);
	}

	/**
	 * Constructs a new animation with an id and no delay
	 * 
	 * @param id
	 */
	public Animation(int id) {
		this.id = ((short) id);
		delay = 0;
	}

	/**
	 * Gets the id of the animation
	 * 
	 * @return
	 */
	public int getId() {
		return id;
	}

	/**
	 * Sets the id of the animation
	 * 
	 * @param id
	 *            The id of the animation
	 */
	public void setId(int id) {
		this.id = ((short) id);
	}

	/**
	 * Gets the delay of the animation
	 * 
	 * @return
	 */
	public int getDelay() {
		return delay;
	}

	/**
	 * Sets the delay of the animation
	 * 
	 * @param delay
	 *            The delay of the animation
	 */
	public void setDelay(int delay) {
		this.delay = ((byte) delay);
	}
}
