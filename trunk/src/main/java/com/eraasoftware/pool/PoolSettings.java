package com.eraasoftware.pool;

import com.eraasoftware.pool.impl.PoolControler;
import com.eraasoftware.pool.impl.PoolFactory;

/**
 * 
 * @author eddie
 * 
 * @param <T>
 */
public class PoolSettings<T> {
	/**
	 * Wait (in second) before
	 */
	public static final int DEFAUL_MAX_WAIT = 5;
	public static final int DEFAULT_MIN = 1;
	public static final int DEFAULT_MAX = 10;

	private int maxWait = DEFAUL_MAX_WAIT;
	private int min = DEFAULT_MIN;
	private int max = DEFAULT_MAX;
	private int maxIdle = min;

	private final PoolFactory<T> poolFactory;

	public PoolSettings(final PoolableObject<T> poolableObject) {
		this.poolFactory = new PoolFactory<T>(this, poolableObject);
		PoolControler.addPoolSettings(this);
	}

	public ObjectPool<T> pool() {
		return poolFactory.getPool();
	}

	public PoolSettings<T> maxIdle(final int maxIdle) {
		this.maxIdle = maxIdle < min ? min : maxIdle;
		return this;
	}

	public int maxIdle() {
		return this.maxIdle;
	}

	public PoolSettings<T> maxWait(final int maxWait) {
		this.maxWait = maxWait;
		return this;
	}

	public PoolSettings<T> min(final int min) {
		this.min = min;
		if (maxIdle < min) {
			maxIdle = min;
		}
		if (min > max) {
			max(min);
		}
		return this;
	}

	public PoolSettings<T> max(final int max) {
		this.max = max;
		if (max < min) {
			min(max);
		}
		return this;
	}

	public int min() {
		return min;
	}

	public int maxWait() {
		return maxWait;
	}

	public int max() {
		return max;
	}

	public static void shutdown() {
		PoolControler.shutdown();
	}

}
