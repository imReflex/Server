package org.endeavor.engine.definitions;

public class ItemDefinition {

	private String name;
	private short id;
	private int value;
	private boolean members;
	private boolean tradable;
	private boolean stackable;
	private boolean note;
	private short noteId;

	public boolean canNote() {
		return noteId != -1;
	}

	public String getName() {
		return name;
	}

	public int getId() {
		return id;
	}

	public int getValue() {
		return value;
	}

	public boolean isMembers() {
		return members;
	}

	public void setUntradable() {
		tradable = false;
	}

	public boolean isTradable() {
		return tradable;
	}

	public boolean isStackable() {
		return stackable;
	}

	public boolean isNote() {
		return note;
	}

	public int getNoteId() {
		return noteId;
	}

	public void setNoteId(int noteId) {
		this.noteId = (short) noteId;
	}
}
