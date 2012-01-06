import nf.fr.eraasoft.pool.ObjectPool;
import nf.fr.eraasoft.pool.PoolException;
import nf.fr.eraasoft.pool.PoolSettings;
import nf.fr.eraasoft.pool.PoolableObjectBase;

import junit.framework.TestCase;


public class TestPool extends TestCase {
	public void testPool() {
		// Create your PoolSettings with an instance of PoolableObject
		PoolSettings<StringBuilder> poolSettings = new PoolSettings<StringBuilder>(
				new PoolableObjectBase<StringBuilder>() {

					@Override
					public StringBuilder make() {
						return new StringBuilder();
					}

					@Override
					public void activate(StringBuilder t) {
						t.setLength(0);
					}
				});
		// Add some settings
		poolSettings.min(0).max(10);

		// Get the objectPool instance using a Singleton Design Pattern is a
		// good idea
		ObjectPool<StringBuilder> objectPool = poolSettings.pool();

		// Use your pool
		StringBuilder buffer = null;
		try {

			buffer = objectPool.getObj();
			// Do something with your object
			buffer.append("yyyy");

			objectPool.returnObj(buffer);

		} catch (PoolException e) {
			e.printStackTrace();
		} finally {
			// Don't forget to return object in the pool
			objectPool.returnObj(buffer);
		}
		PoolSettings.shutdown();

	}
}
