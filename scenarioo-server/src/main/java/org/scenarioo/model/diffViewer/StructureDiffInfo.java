/* scenarioo-server
 * Copyright (C) 2014, scenarioo.org Development Team
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.scenarioo.model.diffViewer;

import java.util.LinkedList;
import java.util.List;

/**
 * Contains the diff information for a structure object.
 */
public class StructureDiffInfo<T> extends AbstractDiffInfo {

	private String name;
	private int added;
	private int changed;
	private int removed;
	private List<T> addedElements = new LinkedList<T>();
	private List<T> removedElements = new LinkedList<T>();

	public StructureDiffInfo() {
		// Used for JAXB
	}

	public StructureDiffInfo(final String name) {
		this.name = name;
	}

	/**
	 * @see org.scenarioo.model.diffViewer.AbstractDiffInfo#hasChanges()
	 */
	@Override
	public boolean hasChanges() {
		return getChangeRate() != 0 || added != 0 || changed != 0 || removed != 0;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * @return the added
	 */
	public int getAdded() {
		return added;
	}

	/**
	 * @param added
	 *            the added to set
	 */
	public void setAdded(final int added) {
		this.added = added;
	}

	/**
	 * @return the changed
	 */
	public int getChanged() {
		return changed;
	}

	/**
	 * @param changed
	 *            the changed to set
	 */
	public void setChanged(final int changed) {
		this.changed = changed;
	}

	/**
	 * @return the removed
	 */
	public int getRemoved() {
		return removed;
	}

	/**
	 * @param removed
	 *            the removed to set
	 */
	public void setRemoved(final int removed) {
		this.removed = removed;
	}

	/**
	 * @return the addedElements
	 */
	public List<T> getAddedElements() {
		return addedElements;
	}

	/**
	 * @param addedElements
	 *            the addedElements to set
	 */
	public void setAddedElements(List<T> addedElements) {
		this.addedElements = addedElements;
	}

	/**
	 * @return the removedElements
	 */
	public List<T> getRemovedElements() {
		return removedElements;
	}

	/**
	 * @param removedElements
	 *            the removedElements to set
	 */
	public void setRemovedElements(List<T> removedElements) {
		this.removedElements = removedElements;
	}

}
