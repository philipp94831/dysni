package de.hpi.idd.dysni;


public interface WrapperFactory<T> {

	ElementWrapper<T> wrap(T rec);

}
