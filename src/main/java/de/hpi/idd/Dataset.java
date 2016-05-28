package de.hpi.idd;

public enum Dataset {
	CD;

	public static Dataset getForName(final String name) {
		switch (name.toLowerCase()) {
		case "cd":
			return CD;
		default:
			return null;
		}
	}
}
