package de.hpi.idd.dysni.avl;

import de.hpi.idd.dysni.comp.LevenshteinComparator;

enum Foo implements HasKey<String> {
	A("a"), B("b"), C("c"), D("d"), DE("de"), E("e"), F("f"), G("g"), H("h"), I("i");

	private static final LevenshteinComparator COMPARATOR = new LevenshteinComparator(0.5);
	private final String key;

	private Foo(final String key) {
		this.key = key;
	}

	@Override
	public KeyComparator<String> getComparator() {
		return COMPARATOR;
	}

	@Override
	public String getKey() {
		return key;
	};
}