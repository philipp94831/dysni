package de.hpi.idd.dysni.avl;

import de.hpi.idd.dysni.comp.LevenshteinComparator;

enum Foo implements Element<String> {
	A("a"), B("b"), C("c"), D("d"), E("e"), F("f"), G("g"), H("h"), I("i"), CD("cd");

	private final String key;
	private static final LevenshteinComparator COMPARATOR = new LevenshteinComparator(0.5);

	private Foo(final String key) {
		this.key = key;
	}

	@Override
	public String getKey() {
		return key;
	}

	@Override
	public KeyComparator<String> getComparator() {
		return COMPARATOR;
	};
}