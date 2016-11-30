package de.hpi.idd.dysni;

import de.hpi.idd.dysni.window.WindowBuilder;

/**
 * Configuration to construct a new {@link DySNIndex} from
 *
 * @param <RECORD>
 *            the type of records to be resolved
 * @param <KEY>
 *            the type of the keys the records are sorted by
 * @param <ID>
 *            the type of the ids the records are identified by
 */
public class DySNIndexConfiguration<RECORD, KEY extends Comparable<KEY>, ID> {

	private final WindowBuilder<RECORD, KEY, ID> builder;
	private final KeyHandler<RECORD, KEY> handler;

	/**
	 * Construct a new configuration
	 *
	 * @param handler
	 *            the {@link KeyHandler} to be used in the index
	 * @param builder
	 *            the {@link WindowBuilder} to be used in the index
	 */
	public DySNIndexConfiguration(KeyHandler<RECORD, KEY> handler, WindowBuilder<RECORD, KEY, ID> builder) {
		this.handler = handler;
		this.builder = builder;
	}

	WindowBuilder<RECORD, KEY, ID> getBuilder() {
		return builder;
	}

	KeyHandler<RECORD, KEY> getHandler() {
		return handler;
	}

}
