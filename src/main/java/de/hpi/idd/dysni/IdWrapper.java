package de.hpi.idd.dysni;

class IdWrapper<K, V> {

	private final K id;
	private final V object;

	public IdWrapper(final V object, final K id) {
		this.object = object;
		this.id = id;
	}

	public K getId() {
		return id;
	}

	public V getObject() {
		return object;
	}
}
