package nf.fr.eraasoft.pool.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;

import nf.fr.eraasoft.pool.ObjectPool;
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
public abstract class AbstractPool<T> implements ObjectPool<T>, Controlable {
	final PoolSettings<T> settings;
	final PoolableObject<T> poolableObject;
	Queue<T> queue;
	final AtomicInteger totalSize = new AtomicInteger();

	public AbstractPool(final PoolableObject<T> poolableObject, final PoolSettings<T> settings) {
		this.poolableObject = poolableObject;
		this.settings = settings;

	}
	protected void init() throws PoolException {
		for (int n = 0; n < settings.min(); n++) {
			create();
		}

	}

	protected void create() throws PoolException {
		T t = poolableObject.make();
		totalSize.incrementAndGet();
		queue.add(t);

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
			T newT;
			try {
				newT = poolableObject.make();
				queue.add(newT);
			} catch (PoolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
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
		for (int n=0;n<queue.size();n++) {
			T t = queue.poll();
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
		b.append(this.getClass().getSimpleName());
		b.append(",  totalSize: ").append(total);
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
