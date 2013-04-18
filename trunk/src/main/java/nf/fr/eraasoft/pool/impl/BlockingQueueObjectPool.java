package nf.fr.eraasoft.pool.impl;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import nf.fr.eraasoft.pool.PoolException;
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
		try {
			init();
		} catch (PoolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	@Override
	public synchronized T getObj() throws PoolException {
		if (queue.size() == 0 && totalSize.get() < settings.max()) {
			create();
		}
		T t = null;
		try {
			t = linkQueue.poll(settings.maxWait(), TimeUnit.SECONDS);
			poolableObject.activate(t);
		} catch (InterruptedException e) {
			throw new PoolException(e);
		}
		
		return t;
	}



}
