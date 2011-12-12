package org.eraasoftware.pool.impl;

import java.util.HashSet;
import java.util.Set;

import org.eraasoftware.pool.PoolSettings;


public class PoolControler extends Thread {
	static PoolControler instance = null;
	static int interval = 1000;
	boolean alive = false;
	Set<PoolSettings<?>> listPoolSettings = new HashSet<PoolSettings<?>>();

	private static void launch() {
		if (instance == null) {
			instance = new PoolControler();
		}
		if (!instance.alive)
			instance.start();
	}

	public static void addPoolSettings(PoolSettings<?> poolSettings) {
		launch();
		instance.listPoolSettings.add(poolSettings);
	}

	public static void shutdown() {
		instance.alive = false;
		for (PoolSettings<?> poolSettings : instance.listPoolSettings) {

			if (poolSettings.pool() instanceof Controlable) {
				Controlable controlable = (Controlable) poolSettings.pool();
				controlable.destroy();

			}

		}
		instance.listPoolSettings.clear();

	}

	private PoolControler() {
		setName("PoolControler");
	}

	@Override
	public void run() {
		alive = true;
		while (alive) {
			try {
				sleep(interval);
				checkPool();
			} catch (InterruptedException e) {
				System.out.println("Interupted " + e.getMessage());
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

		for (PoolSettings<?> poolSettings : listPoolSettings) {

			if (poolSettings.pool() instanceof Controlable) {
				Controlable controlable = (Controlable) poolSettings.pool();
				System.out.println(controlable.toString());

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
