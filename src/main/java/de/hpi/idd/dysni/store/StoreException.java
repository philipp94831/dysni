package de.hpi.idd.dysni.store;

public class StoreException extends Exception {

	private static final long serialVersionUID = 6061576405698881106L;

	public StoreException() {
		super();
	}

	public StoreException(final String message) {
		super(message);
	}

	public StoreException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public StoreException(final Throwable cause) {
		super(cause);
	}
}
