package org.eraasoftware.pool;

public interface ObjectPool<T> {
	/**
	 * <pre>
	 * PoolSettings<MyType> poolSettings = ....
	 * 
	 * MyType myType = poolSettings.pool().getObj();
	 * </pre>
	 * 
	 * @return an instance of T object
	 * @throws InterruptedException
	 */
	public T getObj() throws InterruptedException;

	public void returnObj(T object);

}
