package org.scenarioo.model.docu.entities;

import org.scenarioo.api.rules.CharacterChecker;

/**
 * Object that can be labeled.
 *
 * A label is a string to categorize objects. Their goal is an easy distinction of features, scenarios etc. in the UI.
 * Comprehensive metadata should be added to the details of an object {@link Detailable}. Each object should only have a
 * small number of labels, ideally around 2 to 5.
 */
public interface Labelable {

	/**
	 * (optional) Add a label to this object.
	 *
	 * @param label
	 *            A label string, which must only contain alpha-numeric characters, spaces, underscores and dashes. See
	 *            {@link CharacterChecker#checkLabel(String)}.
	 * @return The set containing all the labels, can be used to chain additional method calls for adding multiple
	 *         labels.
	 */
	public Labels addLabel(final String label);

	/**
	 * Get all labels of this object.
	 *
	 * @return The set of labels, never null!
	 */
	public Labels getLabels();

	/**
	 * (optional) Can be used to replace the whole labels object (which is not allowed to be null).
	 *
	 * We recommend to use the {@link #addLabel} method directly instead to add labels.
	 *
	 * @param labels
	 *            The labels to set (never null!). Furthermore, only certain characters are allowed. See
	 *            {@link #addLabel(String)}.
	 */
	public void setLabels(final Labels labels);

}
