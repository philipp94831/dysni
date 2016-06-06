package de.hpi.idd.dysni.store;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Map.Entry;

public abstract class JDBCStore<K, V> implements RecordStore<K, V> {

	protected final Connection conn;

	protected JDBCStore() {
		this.conn = establishConnection();
	}

	@Override
	public void close() throws SQLException {
		conn.close();
	}

	protected abstract V deserialize(ResultSet rs);

	protected abstract Connection establishConnection();

	@Override
	public V getRecord(K id) throws StoreException {
		try {
			ResultSet rs = prepareSelect(id).executeQuery();
			return deserialize(rs);
		} catch (SQLException e) {
			throw new StoreException("Error retrieving record from database", e);
		}
	}

	@Override
	public Iterator<Entry<K, V>> iterator() {
		throw new UnsupportedOperationException();
	}

	protected abstract PreparedStatement prepareInsert(K id, V record);

	protected abstract PreparedStatement prepareSelect(K id);

	@Override
	public void storeRecord(K id, V record) throws StoreException {
		try {
			prepareInsert(id, record).executeQuery();
		} catch (SQLException e) {
			throw new StoreException("Error storing record in database", e);
		}
	}

}
