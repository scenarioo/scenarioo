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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

/**
 * @param <A>
 *            Represents the added element type.
 * @param <R>
 *            Represents the removed element type.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class StructureDiffInfo<A, R> extends AbstractDiffInfo {

	private String name;
	private int added;
	private int changed;
	private int removed;

	@XmlElementWrapper(name = "addedElements")
	@XmlElement(name = "addedElement")
	private List<A> addedElements = new LinkedList<A>();

	@XmlElementWrapper(name = "removedElements")
	@XmlElement(name = "removedElemet")
	private List<R> removedElements = new LinkedList<R>();

	public StructureDiffInfo() {
		// Used for JAXB
	}

	public StructureDiffInfo(final String name) {
		this.name = name;
	}

	@Override
	public boolean hasChanges() {
		return getChangeRate() != 0 || added != 0 || changed != 0 || removed != 0;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public int getAdded() {
		return added;
	}

	public void setAdded(final int added) {
		this.added = added;
	}

	public int getChanged() {
		return changed;
	}

	public void setChanged(final int changed) {
		this.changed = changed;
	}

	public int getRemoved() {
		return removed;
	}

	public void setRemoved(final int removed) {
		this.removed = removed;
	}

	public List<A> getAddedElements() {
		return addedElements;
	}

	public void setAddedElements(final List<A> addedElements) {
		this.addedElements = addedElements;
	}

	public List<R> getRemovedElements() {
		return removedElements;
	}

	public void setRemovedElements(final List<R> removedElements) {
		this.removedElements = removedElements;
	}
}
