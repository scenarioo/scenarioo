package org.scenarioo.model.docu.entities;

import org.scenarioo.model.docu.entities.generic.Details;

/**
 * Scenarioo documentation object that may be enriched by adding applications specific key value properties to its
 * details content.
 *
 * See {@link Details} for documentation about what can be used as values for details properties.
 */
public interface Detailable {

	/**
	 * (optional) Add application specific details as key-value-data-items.
	 *
	 * See {@link Details} for what can be used as values.
	 */
	public Details addDetail(final String key, final Object value);

	/**
	 * Get the key value map containing all the application specific details properties.
	 *
	 * @return The details, never null!
	 */
	public Details getDetails();

	/**
	 * (optional) Can be used to replace the whole details object (which is not allowed to be null).
	 *
	 * We recommend to use the {@link #addDetail} method directly instead to add detail items.
	 *
	 * @param details
	 *            The details to set (never null!)
	 */
	public void setDetails(final Details details);

}
