package de.hpi.idd.dysni.util;

public class Tuple<LEFT, RIGHT> {

	private final LEFT left;
	private final RIGHT right;

	public Tuple(final LEFT left, final RIGHT right) {
		this.left = left;
		this.right = right;
	}

	public LEFT getLeft() {
		return left;
	}

	public RIGHT getRight() {
		return right;
	}
}