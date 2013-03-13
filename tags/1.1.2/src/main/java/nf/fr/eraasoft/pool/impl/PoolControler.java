package nf.fr.eraasoft.pool.impl;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import nf.fr.eraasoft.pool.PoolSettings;


public class PoolControler extends Thread {
	static PoolControler instance = null;
	
	boolean alive = false;
	Set<PoolSettings<?>> listPoolSettings = Collections.synchronizedSet(new HashSet<PoolSettings<?>>());

	private PoolControler() {
		setName("PoolControler");
	}
	/**
	 * 
	 */
	private static synchronized void  launch() {
		if (instance == null) {
			instance = new PoolControler();
		}
		if (!instance.alive) {
			instance.alive = true;
			instance.start();
		}
	}
	
	
	public static synchronized void addPoolSettings(PoolSettings<?> poolSettings) {
		launch();
		instance.listPoolSettings.add(poolSettings);
	}

	public static void shutdown() {
		if (instance !=null) {
			instance.alive = false;
			
			for (PoolSettings<?> poolSettings : instance.listPoolSettings) {
				if (poolSettings.pool() instanceof Controlable) {
					Controlable controlable = (Controlable) poolSettings.pool();
					controlable.destroy();

				}

			}
			instance.listPoolSettings.clear();
			instance.interrupt();
			instance = null;
		}
	}



	@Override
	public void run() {
		System.out.println("Starting "+getName());
		alive = true;
		while (alive) {
			try {
				sleep(PoolSettings.timeBetweenTwoControls()*1000);
				checkPool();
			} catch (InterruptedException e) {
				System.out.println("PoolControler " + e.getMessage());
				alive = false;
			}
		}

	}

	/**
	 * Remove idle <br>
	 * Validate idle
	 * 
	 * 
	 */
	private void checkPool() {
		synchronized (listPoolSettings) {
			for (PoolSettings<?> poolSettings : listPoolSettings) {

				if (poolSettings.pool() instanceof Controlable) {
					Controlable controlable = (Controlable) poolSettings.pool();
					if (poolSettings.debug()) System.out.println(controlable.toString());

					/*
					 * Remove idle
					 */
					int idleToRemoves = controlable.idles() - poolSettings.maxIdle();
					if (idleToRemoves > 0)
						controlable.remove(idleToRemoves);

					/*
					 * Check idle
					 */
					controlable.validateIdles();

				}
			}	
		}
		

	}

}
