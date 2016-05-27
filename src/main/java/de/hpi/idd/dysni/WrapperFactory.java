package de.hpi.idd.dysni;


public interface WrapperFactory<K, T> {

	ElementWrapper<T> wrap(K rec);

}
