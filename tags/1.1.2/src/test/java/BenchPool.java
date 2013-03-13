import java.util.concurrent.atomic.AtomicInteger;

import junit.framework.TestCase;

import nf.fr.eraasoft.test.Bench;
import nf.fr.eraasoft.test.Job;

import org.apache.commons.pool.ObjectPool;
import org.apache.commons.pool.PoolableObjectFactory;
import org.apache.commons.pool.impl.GenericKeyedObjectPool.Config;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.apache.commons.pool.impl.GenericObjectPoolFactory;
import nf.fr.eraasoft.pool.PoolSettings;
import nf.fr.eraasoft.pool.PoolableObjectBase;


public class BenchPool extends TestCase {

	public void testPool() {
		Bench bench = new Bench().minthreads(0).maxthreads(50).maxiteration(2000).pause(50);

		final ObjectPool commonBlockingPool = getCommonBlockingPool();
		final ObjectPool commonGrowingPool = getCommonGrowingPool();

		bench.addJob(new Job() {
			
			
			AtomicInteger totalCalled = new AtomicInteger();

			public void go() {
				StringBuilder b = null;
				try {
					totalCalled.incrementAndGet();
					b = (StringBuilder) commonBlockingPool.borrowObject();
					for (int n = 0; n < 100; n++) {
						b.append("sdfsdqfjsdkfhjksdfh jkhjkhjk");
					}

				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					try {
						if (b != null)
							commonBlockingPool.returnObject(b);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

			@Override
			public String toString() {
				return "ApacheCommon Generic Bloking";
			}
		});
		
		bench.addJob(new Job() {
			
			
			AtomicInteger totalCalled = new AtomicInteger();

			public void go() {
				StringBuilder b = null;
				try {
					totalCalled.incrementAndGet();
					b = (StringBuilder) commonGrowingPool.borrowObject();
					for (int n = 0; n < 100; n++) {
						b.append("sdfsdqfjsdkfhjksdfh jkhjkhjk");
					}

				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					try {
						if (b != null)
							commonGrowingPool.returnObject(b);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

			@Override
			public String toString() {
				return "ApacheCommon Generic-Growing";
			}
		});

		final nf.fr.eraasoft.pool.ObjectPool<StringBuilder> poolB = getffPool();
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
				return "FuriousPoolBlocking";
			}
		});
		
		final nf.fr.eraasoft.pool.ObjectPool<StringBuilder> furiousNonBlockingPool = getFuriousNonBlockingPool();
		bench.addJob(new Job() {
			AtomicInteger totalCalled = new AtomicInteger();

			public void go() {
				StringBuilder b = null;
				try {
					totalCalled.incrementAndGet();
					b = furiousNonBlockingPool.getObj();
					for (int n = 0; n < 100; n++) {
						b.append("sdfsdqfjsdkfhjksdfh jkhjkhjk");
					}

				} catch (Exception e) {
					// TODO: handle exception
				} finally {
					furiousNonBlockingPool.returnObj(b);
				}

			}

			@Override
			public String toString() {
				return "FuriousNonBlocking";
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

	private ObjectPool getCommonBlockingPool() {
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
	private ObjectPool getCommonGrowingPool() {
		Config poolConfig = new Config();
		poolConfig.maxActive = MAX_ACTIVE;
		poolConfig.whenExhaustedAction = GenericObjectPool.WHEN_EXHAUSTED_GROW;
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

	private nf.fr.eraasoft.pool.ObjectPool<StringBuilder> getffPool() {
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
	
	private nf.fr.eraasoft.pool.ObjectPool<StringBuilder> getFuriousNonBlockingPool() {
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
		poolSettings.max(-1);
		return poolSettings.pool();
	}

}
