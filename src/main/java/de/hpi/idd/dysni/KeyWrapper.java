package de.hpi.idd.dysni;

import de.hpi.idd.dysni.avl.HasKey;
import de.hpi.idd.dysni.avl.KeyComparator;

public class KeyWrapper<T> implements HasKey<String> {
	
	protected final T object;
	private final String key;
	private final KeyComparator<String> comp;
	
	public KeyWrapper(T object, String key, KeyComparator<String> comp) {
		this.object = object;
		this.key = key;
		this.comp = comp;
	}

	public T getObject() {
		return object;
	}
	
	@Override
	public final String getKey() {
		return key;
	}

	@Override
	public KeyComparator<String> getComparator() {
		return comp;
	}

}
