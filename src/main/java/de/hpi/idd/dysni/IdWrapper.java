package de.hpi.idd.dysni;

import java.util.Map;

class IdWrapper {

	private final Map<String, String> object;
	private final String id;

	public IdWrapper(final Map<String, String> object, final String idField) {
		this.object = object;
		id = object.get(idField);
	}

	public String getId() {
		return id;
	}

	public Map<String, String> getObject() {
		return object;
	}

}
