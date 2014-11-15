package org.endeavor.game.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * An entities attributes
 * 
 * @author Michael Sasse
 * 
 */
public final class Attributes implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3789174691800579830L;
	/**
	 * A map of all the stored attributes
	 */
	private Map<Object, Object> attributes = new HashMap<Object, Object>();

	/**
	 * Gets an attribute from the map
	 * 
	 * @param key
	 *            The key of the attribute
	 * @return
	 */
	public Object get(Object key) {
		return attributes.get(key);
	}

	/**
	 * Gets an attribute, the fuck is fail?
	 * 
	 * @param key
	 *            The key of the attribute
	 * @param fail
	 *            The fuck is this?
	 * @return
	 */
	public Object get(Object key, Object fail) {
		Object value = attributes.get(key);
		if (value == null) {
			return fail;
		}
		return value;
	}

	/**
	 * Gets a byte attribute from the map
	 * 
	 * @param key
	 *            The byte key
	 * @return
	 */
	public byte getByte(Object key) {
		Number n = (Number) get(key);
		if (n == null) {
			return 0;
		}
		return n.byteValue();
	}

	/**
	 * Gets a double attribute from the map
	 * 
	 * @param key
	 *            The double key
	 * @return
	 */
	public double getDouble(Object key) {
		Number n = (Number) get(key);
		if (n == null) {
			return 0.0D;
		}
		return n.doubleValue();
	}

	/**
	 * Gets an integer attribute from the map
	 * 
	 * @param key
	 *            The integer key
	 * @return
	 */
	public int getInt(Object key) {
		Number n = (Number) get(key);
		if (n == null) {
			return -1;
		}
		return n.intValue();
	}

	/**
	 * Gets an long attribute from the map
	 * 
	 * @param key
	 *            The long key
	 * @return
	 */
	public long getLong(Object key) {
		Number n = (Number) get(key);
		if (n == null) {
			return 0L;
		}
		return n.longValue();
	}

	/**
	 * Gets an short attribute from the map
	 * 
	 * @param key
	 *            The short key
	 * @return
	 */
	public short getShort(Object key) {
		Number n = (Number) get(key);
		if (n == null) {
			return 0;
		}
		return n.shortValue();
	}

	/**
	 * Gets a boolean attribute from the map
	 * 
	 * @param key
	 *            The boolean key
	 * @return
	 */
	public boolean is(Object key) {
		Boolean b = (Boolean) get(key);
		if (b == null) {
			return false;
		}
		return b.booleanValue();
	}

	/**
	 * Sets an attribute with a key and value
	 * 
	 * @param key
	 *            The key of the attribute
	 * @param value
	 *            The value of an attribute
	 */
	public void set(Object key, Object value) {
		if (attributes.containsKey(key)) {
			attributes.remove(key);
		}

		attributes.put(key, value);
	}

	/**
	 * Gets if an attribute is added
	 * 
	 * @param key
	 *            The key to check if eixists
	 * @return
	 */
	public boolean isSet(Object key) {
		return attributes.containsKey(key);
	}

	/**
	 * Removes an attribute from the map
	 * 
	 * @param key
	 */
	public void remove(Object key) {
		attributes.remove(key);
	}
}
