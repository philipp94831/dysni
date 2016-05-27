package de.hpi.idd.dysni.avl;

enum Foo implements HasKey<String> {
	A("a"), B("b"), C("c"), D("d"), DE("de"), E("e"), F("f"), G("g"), H("h"), I("i");

	private final String key;

	private Foo(final String key) {
		this.key = key;
	}

	@Override
	public String getKey() {
		return key;
	};
}