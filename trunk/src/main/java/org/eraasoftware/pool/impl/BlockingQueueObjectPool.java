package org.eraasoftware.pool.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.eraasoftware.pool.ObjectPool;
import org.eraasoftware.pool.PoolSettings;
import org.eraasoftware.pool.PoolableObject;


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
public abstract class BlockingQueueObjectPool<T> implements ObjectPool<T>, Controlable {
	final LinkedBlockingQueue<T> queue;
	final PoolSettings<T> settings;
	final PoolableObject<T> poolableObject;
	final AtomicInteger totalSize = new AtomicInteger();

	public BlockingQueueObjectPool(final PoolableObject<T> poolableObject, final PoolSettings<T> settings) {
		queue = new LinkedBlockingQueue<T>();
		this.poolableObject = poolableObject;
		this.settings = settings;
		for (int n = 0; n < settings.min(); n++) {
			create();
		}

	}

	private void create() {
		T t = poolableObject.make();
		totalSize.incrementAndGet();
		queue.add(t);

	}

	@Override
	public T getObj() throws InterruptedException {
		if (queue.size() == 0 && totalSize.get() < settings.max()) {
			create();
		}
		T t = queue.poll(settings.maxWait(), TimeUnit.SECONDS);
		poolableObject.activate(t);
		return t;
	}

	@Override
	public void returnObj(final T t) {
		if (t == null)
			return;
		if (poolableObject.validate(t)) {
			poolableObject.passivate(t);
			queue.add(t);
		} else {
			destroyObject(t);
			T newT = poolableObject.make();
			queue.add(newT);
		}

	}

	private void destroyObject(final T t) {

		poolableObject.destroy(t);
		totalSize.decrementAndGet();
	}

	@Override
	public int idles() {
		return queue.size();
	}

	@Override
	public void remove(int nbObjects) {
		for (int n = 0; n < nbObjects; n++) {
			T t = queue.poll();
			if (t == null) {
				break;
			}
			destroyObject(t);

		}

	}

	@Override
	public void clear() {
		for (T t = queue.poll(); t != null;) {
			destroyObject(t);
		}
		totalSize.set(0);

	}

	@Override
	public void destroy() {
		clear();

	}

	@Override
	public int actives() {
		return totalSize.get() - queue.size();
	}

	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		int total = totalSize.get();
		b.append(" totalSize: ").append(total);
		b.append(", numActive: ").append(actives());
		b.append(", numIdle: ").append(idles());
		b.append(", max: ").append(settings.max());
		b.append(", queueSize: ").append(queue.size());
		return b.toString();
	}

	@Override
	public void validateIdles() {
		List<T> listT = new ArrayList<T>(queue.size());
		int queueSise = queue.size();

		for (int n = 0; n < queueSise; n++) {
			T t = queue.poll();
			if (t == null)
				break;
			if (poolableObject.validate(t)) {
				listT.add(t);
			} else {
				destroyObject(t);
			}

		}

		for (T t : listT) {
			queue.add(t);
		}

	}

}
