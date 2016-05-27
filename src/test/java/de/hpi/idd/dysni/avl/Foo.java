package de.hpi.idd.dysni.avl;

enum Foo {
	A("a"), B("b"), C("c"), D("d"), DE("de"), E("e"), F("f"), G("g"), H("h"), I("i");

	private final String key;

	Foo(final String key) {
		this.key = key;
	}

	public String getKey() {
		return key;
	}
}