package de.hpi.idd.dysni;

import de.hpi.idd.dysni.avl.HasKey;

public class KeyWrapper<T> implements HasKey<String> {
	
	private final T object;
	private final String key;
	
	public KeyWrapper(T object, String key) {
		this.object = object;
		this.key = key;
	}

	public T getObject() {
		return object;
	}
	
	@Override
	public final String getKey() {
		return key;
	}

}
