package nf.fr.eraasoft.pool.impl;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import nf.fr.eraasoft.pool.PoolSettings;
import nf.fr.eraasoft.pool.PoolableObject;


/**
 * 
 * Object pool implementation based on LinkedBlockingQueue<br>
 * Use PoolSettings class to obtain an instance of this class
 * 
 * @see PoolSettings
 * 
 * @author eddie
 * 
 * @param <T>
 */
public abstract class BlockingQueueObjectPool<T> extends AbstractPool<T> {
	LinkedBlockingQueue<T> linkQueue;
	public BlockingQueueObjectPool(final PoolableObject<T> poolableObject, final PoolSettings<T> settings) {
		super(poolableObject,settings);
		queue = new LinkedBlockingQueue<T>();
		linkQueue = (LinkedBlockingQueue<T>) queue;
		init();
	}


	@Override
	public T getObj() throws InterruptedException {
		if (queue.size() == 0 && totalSize.get() < settings.max()) {
			create();
		}
		T t = linkQueue.poll(settings.maxWait(), TimeUnit.SECONDS);
		poolableObject.activate(t);
		return t;
	}



}
