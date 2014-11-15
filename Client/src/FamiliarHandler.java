
public class FamiliarHandler {

	/**
	 * Creates a new handler.
	 */
	public FamiliarHandler() {
		setDefaults();
	}

	/**
	 * Sets the familiar defaults.
	 */
	public void setDefaults() {
		familiarName = "";
		specialAttack = "";
		familiarActive = false;
	}

	/**
	 * Sets the familiar name and special attack name.
	 * @param name
	 * @param special
	 */
	public void setFamiliar(String name, String special) {
		familiarName = name;
		specialAttack = special;
	}

	/**
	 * Sets whether or not a familiar is active.
	 * @param active
	 */
	public void setActive(boolean active) {
		familiarActive = active;
	}

	/**
	 * Gets the current familiar's name.
	 * @return
	 */
	public String getName() {
		return familiarName;
	}

	/**
	 * Gets the current familiar's special attack name.
	 */
	public String getSpecialAttack() {
		return specialAttack;
	}

	/**
	 * Gets the activity of the familar.
	 */
	public boolean isActive() {
		return familiarActive;
	}

	/**
	 * The familiar's name.
	 */
	public String familiarName;

	/**
	 * The familiar's special attack name.
	 */
	public String specialAttack;

	/**
	 * Is a familiar active?
	 */
	public boolean familiarActive = false;
}
