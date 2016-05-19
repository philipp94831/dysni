package de.hpi.idd.dysni;

import de.hpi.idd.dysni.avl.Element;

public abstract class ElementWrapper<T> implements Element<String> {
	
	protected final T object;
	private final String key;	
	
	public ElementWrapper(T object, String key) {
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