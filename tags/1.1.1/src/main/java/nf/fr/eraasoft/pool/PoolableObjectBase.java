package nf.fr.eraasoft.pool;
/**
 * Utility class to create a PoolableObject.<br>
 * Just implement PoolableObject.make and activate method<br> 
 * A sample :
 * <pre>{@code
 		PoolableObject<StringBuilder> poolableStringBuilder = new PoolableObjectBase<StringBuilder>() {
			public StringBuilder make() throws PoolException {
				return new StringBuilder();
			}

			public void activate(StringBuilder t) throws PoolException {
				t.setLength(0);
			}
		}; 
}</pre>
 * @author eddie
 * @see PoolableObject
 *
 * @param <T>
 */

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
