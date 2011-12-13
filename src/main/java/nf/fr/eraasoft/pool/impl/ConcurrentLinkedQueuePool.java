package nf.fr.eraasoft.pool.impl;

import java.util.concurrent.ConcurrentLinkedQueue;
import nf.fr.eraasoft.pool.PoolSettings;
import nf.fr.eraasoft.pool.PoolableObject;

public class ConcurrentLinkedQueuePool<T> extends AbstractPool<T> {



	public ConcurrentLinkedQueuePool(final PoolableObject<T> poolableObject, final PoolSettings<T> settings) {
		super(poolableObject, settings);
		queue = new ConcurrentLinkedQueue<T>();
		init();

	}

	@Override
	public T getObj() throws InterruptedException {
		T t = queue.poll();
		if (t==null) {
			t = poolableObject.make();
			totalSize.incrementAndGet();
		}
		poolableObject.activate(t);
		return t;
	}

}
