package de.hpi.idd;

public enum Dataset {
	CD, CORA, MOVIES, NCVOTERS;

	public static Dataset getForName(final String name) {
		switch (name.toLowerCase()) {
		case "cd":
			return CD;
		case "cora":
			return CORA;
		case "movies":
			return MOVIES;
		case "ncvoters":
			return NCVOTERS;
		default:
			return null;
		}
	}
}
