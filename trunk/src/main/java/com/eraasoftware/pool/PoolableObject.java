package com.eraasoftware.pool;

public interface PoolableObject<T> {
	/**
	 * is called whenever a new instance is needed.
	 * 
	 * @return
	 */
	public T make();

	/**
	 * is invoked on activated instances to make sure they can be borrowed from
	 * the pool. validateObject may also be used to test an instance being
	 * returned to the pool before it is passivated. It will only be invoked on
	 * an activated instance.
	 * 
	 * @param t
	 * @return
	 */
	public boolean validate(T t);

	/**
	 * is invoked on every instance when it is being "dropped" from the pool
	 * (whether due to the response from validateObject, or for reasons specific
	 * to the pool implementation.) There is no guarantee that the instance
	 * being destroyed will be considered active, passive or in a generally
	 * consistent state.
	 * 
	 * @param t
	 */
	public void destroy(T t);

	/**
	 * is invoked on every instance that has been passivated before it is
	 * borrowed from the pool.
	 * 
	 * @param t
	 */
	public void activate(T t);

	/**
	 * is invoked on every instance when it is returned to the pool.
	 * 
	 * @param t
	 */
	public void passivate(T t);

}
