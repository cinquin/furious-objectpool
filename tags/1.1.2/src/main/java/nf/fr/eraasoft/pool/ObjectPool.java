package nf.fr.eraasoft.pool;

public interface ObjectPool<T> {
	/**
	 * <pre> {@literal
		PoolSettings<MyType> poolSettings = ....;
		MyType myType = poolSettings.pool().getObj();
	  }</pre>
	 * 
	 * @return an instance of T object
	 * @throws InterruptedException
	 */
	public T getObj() throws PoolException;

	/**
	 * Return object to the pool
	 * @param object
	 */
	public void returnObj(T object);

}
