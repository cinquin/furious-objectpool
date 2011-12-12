import java.util.concurrent.atomic.AtomicInteger;

import junit.framework.TestCase;

import org.apache.commons.pool.ObjectPool;
import org.apache.commons.pool.PoolableObjectFactory;
import org.apache.commons.pool.impl.GenericKeyedObjectPool.Config;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.apache.commons.pool.impl.GenericObjectPoolFactory;
import org.eraasoftware.test.Bench;
import org.eraasoftware.test.Job;

import com.eraasoftware.pool.PoolSettings;
import com.eraasoftware.pool.PoolableObjectBase;

public class BenchPool extends TestCase {

	public void testPool() {
		Bench bench = new Bench().minthreads(1).maxthreads(80).maxiteration(10000).pause(50);

		final ObjectPool pool = getCommonPool();

		bench.addJob(new Job() {
			AtomicInteger totalCalled = new AtomicInteger();

			public void go() {
				StringBuilder b = null;
				try {
					totalCalled.incrementAndGet();
					b = (StringBuilder) pool.borrowObject();
					for (int n = 0; n < 100; n++) {
						b.append("sdfsdqfjsdkfhjksdfh jkhjkhjk");
					}

				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					try {
						if (b != null)
							pool.returnObject(b);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

			@Override
			public String toString() {
				return "Apache Common";
			}
		});

		final com.eraasoftware.pool.ObjectPool<StringBuilder> poolB = getffPool();
		bench.addJob(new Job() {
			AtomicInteger totalCalled = new AtomicInteger();

			public void go() {
				StringBuilder b = null;
				try {
					totalCalled.incrementAndGet();
					b = poolB.getObj();
					for (int n = 0; n < 100; n++) {
						b.append("sdfsdqfjsdkfhjksdfh jkhjkhjk");
					}

				} catch (Exception e) {
					// TODO: handle exception
				} finally {
					poolB.returnObj(b);
				}

			}

			@Override
			public String toString() {
				return "Furious Pool";
			}
		});

		try {
			bench.launch();
			bench.displayStat();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	final int MAX_ACTIVE = 10;

	private ObjectPool getCommonPool() {
		Config poolConfig = new Config();
		poolConfig.maxActive = MAX_ACTIVE;
		poolConfig.whenExhaustedAction = GenericObjectPool.WHEN_EXHAUSTED_BLOCK;
		poolConfig.maxTotal = MAX_ACTIVE;
		GenericObjectPoolFactory poolFactory = new GenericObjectPoolFactory(new PoolableObjectFactory() {
			public boolean validateObject(Object arg0) {
				return true;
			}

			public void passivateObject(Object arg0) throws Exception {
			}

			public Object makeObject() throws Exception {
				return new StringBuilder();
			}

			public void destroyObject(Object arg0) throws Exception {
			}

			public void activateObject(Object arg0) throws Exception {
				((StringBuilder) arg0).setLength(0);
			}
		});

		return poolFactory.createPool();
	}

	private com.eraasoftware.pool.ObjectPool<StringBuilder> getffPool() {
		PoolSettings<StringBuilder> poolSettings = new PoolSettings<StringBuilder>(
				new PoolableObjectBase<StringBuilder>() {

					public void activate(StringBuilder arg0) {
						arg0.setLength(0);
					}

					@Override
					public boolean validate(StringBuilder t) {
						return true;
					}

					public StringBuilder make() {
						return new StringBuilder();
					}

					@Override
					public void destroy(StringBuilder t) {
					}
				});
		poolSettings.max(MAX_ACTIVE);
		return poolSettings.pool();
	}

}
