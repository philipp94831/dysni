package de.hpi.idd.dysni;

import java.util.Map;

public class IdWrapper implements HasId<String> {

	private final Map<String, String> object;

	public IdWrapper(final Map<String, String> object) {
		this.object = object;
	}

	@Override
	public String getId() {
		return object.get("did");
	}

	public Map<String, String> getObject() {
		return object;
	}

}
