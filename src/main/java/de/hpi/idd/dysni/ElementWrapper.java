package de.hpi.idd.dysni;

import de.hpi.idd.dysni.avl.Element;

public abstract class ElementWrapper<T> implements Element<String> {
	
	protected final T object;	
	
	public ElementWrapper(T object) {
		this.object = object;
	}

	public T getObject() {
		return object;
	}

}
