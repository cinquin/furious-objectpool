package nf.fr.eraasoft.pool.impl;

public interface Controlable {

	/**
	 * 
	 * @return number of idles objects
	 */
	public int idles();

	/**
	 * remove idle objects
	 * 
	 * @param nbObjects
	 *            number of idle objects to remove
	 */
	public void remove(int nbObjects);

	/**
	 * Clear the object pool
	 */
	public void clear();

	/**
	 * Destroy the pool
	 */

	public void destroy();

	/**
	 * 
	 * @return number of active objects
	 */

	public int actives();

	/**
	 * validate idle objects
	 */

	public void validateIdles();

}
