package com.eraasoftware.pool.impl;

import com.eraasoftware.pool.ObjectPool;
import com.eraasoftware.pool.PoolSettings;
import com.eraasoftware.pool.PoolableObject;

public class PoolFactory<T> {
	final PoolSettings<T> settings;

	BlockingQueueObjectPool<T> pool;
	final PoolableObject<T> poolableObject;

	public PoolFactory(PoolSettings<T> settings, PoolableObject<T> poolableObject) {
		this.settings = settings;
		this.poolableObject = poolableObject;
	}

	public ObjectPool<T> getPool() {
		if (pool == null)
			createPoolInstance();
		return pool;
	}

	private static class BBObjectPool<T> extends BlockingQueueObjectPool<T> {

		public BBObjectPool(PoolableObject<T> poolableObject, PoolSettings<T> settings) {
			super(poolableObject, settings);

		}

	}

	private synchronized void createPoolInstance() {
		if (pool == null)
			pool = new BBObjectPool<T>(poolableObject, settings);

	}

	public PoolSettings<T> settings() {
		return settings;
	}

}