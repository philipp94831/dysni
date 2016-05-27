package de.hpi.idd.dysni;

import de.hpi.idd.dysni.avl.HasKey;

public class KeyWrapper<T> implements HasKey<String> {

	private final T object;
	private final String key;

	public KeyWrapper(final T object, final String key) {
		this.object = object;
		this.key = key;
	}

	@Override
	public final String getKey() {
		return key;
	}

	public T getObject() {
		return object;
	}

}
