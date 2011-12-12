package org.eraasoftware.pool;

public abstract class PoolableObjectBase<T> implements PoolableObject<T> {

	@Override
	public boolean validate(T t) {
		return true;
	}

	@Override
	public void destroy(T t) {

	}

	@Override
	public void passivate(T t) {

	}

}
