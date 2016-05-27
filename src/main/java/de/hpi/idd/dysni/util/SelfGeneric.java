package de.hpi.idd.dysni.util;

public interface SelfGeneric<T extends SelfGeneric<T>> {

	@SuppressWarnings("unchecked")
	default T getThis() {
		return (T) this;
	}

}
