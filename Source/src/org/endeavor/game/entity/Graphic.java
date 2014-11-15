package org.endeavor.game.entity;

import java.io.Serializable;

/**
 * Represents a single graphic
 * 
 * @author Michael Sasse
 * 
 */
public final class Graphic implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6793795871775585592L;

	/**
	 * The id of the graphic
	 */
	private final int id;

	/**
	 * The height of the graphic
	 */
	private final int height;

	/**
	 * The delay of the graphic
	 */
	private final int delay;

	/**
	 * Constructs a new graphic with a an id
	 * 
	 * @param id
	 *            The id of the graphic
	 */
	public Graphic(int id) {
		this(id, 0, false);
	}

	/**
	 * Constructs a new graphic with an id, delay, and height
	 * 
	 * @param id
	 *            The id of the graphic
	 * @param delay
	 *            The delay of the graphic
	 * @param height
	 *            The height of the graphic
	 */
	public Graphic(int id, int delay, int height) {
		this.id = id;
		this.height = height;
		this.delay = delay;
	}

	/**
	 * Constructs a new graphic with an id and delay, should the delay be high?
	 * 
	 * @param id
	 *            The id of the graphic
	 * @param delay
	 *            The delay of the graphic
	 * @param high
	 *            Should the graphic be high?
	 */
	public Graphic(int id, int delay, boolean high) {
		this.id = id;
		height = (high ? 100 : 0);
		this.delay = delay;
	}

	/**
	 * Constructs a new graphic based on another graphic
	 * 
	 * @param other
	 *            The other graphic
	 */
	public Graphic(Graphic other) {
		id = other.id;
		height = other.height;
		delay = other.delay;
	}

	/**
	 * Creates a new high graphic
	 * 
	 * @param id
	 *            The id of the graphic
	 * @param delay
	 *            The delay of the graphic
	 * @return
	 */
	public static Graphic highGraphic(int id, int delay) {
		return new Graphic(id, delay, 100);
	}

	/**
	 * Creates a new low graphic
	 * 
	 * @param id
	 *            The id of the graphic
	 * @param delay
	 *            The delay of the graphic
	 * @return
	 */
	public static Graphic lowGraphic(int id, int delay) {
		return new Graphic(id, delay, 0);
	}

	/**
	 * Gets the id of the graphic
	 * 
	 * @return
	 */
	public int getId() {
		return id;
	}

	/**
	 * Gets the delay of the graphic
	 * 
	 * @return
	 */
	public int getDelay() {
		return delay;
	}

	/**
	 * Gets the delay and height in one variable to send to the client
	 * 
	 * @return
	 */
	public int getValue() {
		return delay | height << 16;
	}

	/**
	 * Gets the height of the graphic
	 * 
	 * @return
	 */
	public int getHeight() {
		return height;
	}
}
