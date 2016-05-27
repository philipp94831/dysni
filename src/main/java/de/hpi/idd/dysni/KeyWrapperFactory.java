package de.hpi.idd.dysni;


public interface KeyWrapperFactory<K, T> {

	KeyWrapper<T> wrap(K rec);

}
