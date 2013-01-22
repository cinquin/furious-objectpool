package nf.fr.eraasoft.pool;

import nf.fr.eraasoft.pool.impl.PoolControler;
import nf.fr.eraasoft.pool.impl.PoolFactory;

/**
 * Classe used to configure your object pooling. Instance of this classes must be used in a Singleton context (static variable).<br>
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
	/**
	 * Control thread 
	 */
	public static final int DEFAULT_TIME_BETWEEN_TWO_CONTROLE = 30;
	private static int TIME_BETWEEN_TWO_CONTROLE = DEFAULT_TIME_BETWEEN_TWO_CONTROLE;

	private int maxWait = DEFAUL_MAX_WAIT;
	private int min = DEFAULT_MIN;
	private int max = DEFAULT_MAX;
	private int maxIdle = min;
	private boolean validateWhenReturn = false;
	private boolean debug = false;
	
	public static void timeBetweenTwoControls(int time) {
		TIME_BETWEEN_TWO_CONTROLE = time;
	}
	
	public static int timeBetweenTwoControls() {
		return TIME_BETWEEN_TWO_CONTROLE;
	}
	

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

	/**
	 * Define the minimum number of element in the pool
	 * @param min
	 * @return 
	 */
	public PoolSettings<T> min(final int min) {
		this.min = min;
		maxIdle = min;
		if (max>0 && min > max) {
			max(min);
		}
		return this;
	}

	/**
	 * if  
	 * @param max
	 * @return
	 */
	public PoolSettings<T> max(final int max) {
		this.max = max;
		if (max>0 && max < min) {
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
	
	/**
	 * if true invoke PoolableObject.validate() method
	 * @param validateWhenReturn
	 * @return
	 */
	public PoolSettings<T> validateWhenReturn(boolean validateWhenReturn) {
		this.validateWhenReturn = validateWhenReturn;
		return this;
	}

	public boolean validateWhenReturn() {
		return validateWhenReturn;
	}
	
	public PoolSettings<T> debug(boolean debug) {
		this.debug = debug;
		return this;
	}
	
	public boolean debug() {
		return debug;
	}
	
	public void clearCurrentPool() {
		poolFactory.clear();
	}
	

}
 