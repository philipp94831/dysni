package de.hpi.idd.store;

import java.io.IOException;

public class StoreException extends IOException {

	private static final long serialVersionUID = -2689744478775670177L;

	public StoreException() {
		super();
	}

	public StoreException(String message) {
		super(message);
	}

	public StoreException(String message, Throwable cause) {
		super(message, cause);
	}

	public StoreException(Throwable cause) {
		super(cause);
	}
}
