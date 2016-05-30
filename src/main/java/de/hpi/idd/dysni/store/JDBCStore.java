package de.hpi.idd.dysni.store;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class JDBCStore<K, V> implements RecordStore<K, V> {

	protected final Connection conn;

	public JDBCStore() {
		this.conn = establishConnection();
	}

	@Override
	public void close() throws SQLException {
		conn.close();
	}

	protected abstract V deserialize(ResultSet rs);

	protected abstract Connection establishConnection();

	@Override
	public V getRecord(final K id) throws StoreException {
		try {
			final ResultSet rs = prepareSelect(id).executeQuery();
			return deserialize(rs);
		} catch (final SQLException e) {
			throw new StoreException("Error retrieving record from database", e);
		}
	}

	protected abstract PreparedStatement prepareInsert(K id, V record);

	protected abstract PreparedStatement prepareSelect(K id);

	@Override
	public void storeRecord(final K id, final V record) throws StoreException {
		try {
			prepareInsert(id, record).executeQuery();
		} catch (final SQLException e) {
			throw new StoreException("Error storing record in database", e);
		}
	}

}
