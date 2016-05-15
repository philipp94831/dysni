package de.hpi.idd.dysni;

import de.hpi.idd.dysni.avl.Element;

public abstract class ElementWrapper<T> implements Element<String> {
	
	protected final T object;
	private final String key;	
	
	public ElementWrapper(T object) {
		this.object = object;
		this.key = computeKey();
	}

	public T getObject() {
		return object;
	}
	
	protected abstract String computeKey();
	
	@Override
	public final String getKey() {
		return key;
	}

}
