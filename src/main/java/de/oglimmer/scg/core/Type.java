package de.oglimmer.scg.core;

public enum Type {

	HAND(1), DRAWN(2), OPEN(3), IN_PLAY(4);

	private int ordinal;

	private Type(int ordinal) {
		this.ordinal = ordinal;
	}

	public int toOrdinal() {
		return ordinal;
	}

	public static Type fromOrdinal(int ordinal) {
		for (Type type : values()) {
			if (type.toOrdinal() == ordinal) {
				return type;
			}
		}
		assert false;
		return null;
	}
}
