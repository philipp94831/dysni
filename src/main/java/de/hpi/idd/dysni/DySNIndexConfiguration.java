package de.hpi.idd.dysni;

import de.hpi.idd.dysni.window.WindowBuilder;

public class DySNIndexConfiguration<RECORD, KEY extends Comparable<KEY>, ID> {

	private final WindowBuilder<RECORD, KEY, ID> builder;
	private final KeyHandler<RECORD, KEY> handler;

	public DySNIndexConfiguration(KeyHandler<RECORD, KEY> handler, WindowBuilder<RECORD, KEY, ID> builder) {
		this.handler = handler;
		this.builder = builder;
	}

	public WindowBuilder<RECORD, KEY, ID> getBuilder() {
		return builder;
	}

	public KeyHandler<RECORD, KEY> getHandler() {
		return handler;
	}

}
