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

package org.scenarioo.business.diffViewer.comparator;

import java.util.LinkedList;
import java.util.List;

import org.scenarioo.model.configuration.ComparisonConfiguration;
import org.scenarioo.model.diffViewer.StructureDiffInfo;

/**
 * @param <E>
 *            Represents the element type. For example a use case.
 * @param <A>
 *            Represents the added element type in {@link StructureDiffInfo}.
 * @param <R>
 *            Represents the removed element type in {@link StructureDiffInfo}.
 */
public abstract class AbstractStructureComparator<E, A, R> extends AbstractComparator {

	public AbstractStructureComparator(final String baseBranchName, final String baseBuildName,
			final ComparisonConfiguration comparisonConfiguration) {
		super(baseBranchName, baseBuildName, comparisonConfiguration);
	}

	protected abstract double compareElement(E baseElement, E comparisonElement, StructureDiffInfo<A, R> diffInfo);

	protected abstract String getElementIdentifier(E element);

	protected abstract A getAddedElementValue(E element);

	protected abstract List<R> getRemovedElementValues(List<E> removedElements);

	protected void handleAddedElements(final List<E> baseElements, final List<E> comparisonElements,
			final StructureDiffInfo<A, R> diffInfo) {
		final List<E> addedElements = getAddedElements(baseElements, comparisonElements);
		setAddedDiffInfo(addedElements, diffInfo);
	}

	protected void calculateDiffInfo(final List<E> baseElements,
			final List<E> comparisonElements, final StructureDiffInfo<A, R> diffInfo) {
		handleAddedElements(baseElements, comparisonElements, diffInfo);

		final double stepChangeRateSum = compareElements(baseElements, comparisonElements, diffInfo);

		handleRemovedElements(baseElements, comparisonElements, diffInfo);

		diffInfo.setChangeRate(calculateChangeRate(baseElements.size(), diffInfo.getAdded(),
				diffInfo.getRemoved(), stepChangeRateSum));
	}

	/**
	 * Compares the base elements with the comparison elements.
	 * 
	 * @return the change rate sum of all use cases
	 */
	protected double compareElements(final List<E> baseElements, final List<E> comparisonElements,
			final StructureDiffInfo<A, R> diffInfo) {
		double useCaseChangeRateSum = 0;
		for (final E baseElement : baseElements) {
			final E comparisonElement = getElementByIdentifier(comparisonElements, getElementIdentifier(baseElement));
			useCaseChangeRateSum += compareElement(baseElement, comparisonElement, diffInfo);
		}
		return useCaseChangeRateSum;
	}

	protected void handleRemovedElements(final List<E> baseElements,
			final List<E> comparisonElements, final StructureDiffInfo<A, R> diffInfo) {
		final List<E> removedElements = getRemovedElements(baseElements, comparisonElements);
		setRemovedDiffInfo(removedElements, diffInfo);
	}

	private List<A> getAddedElementValues(final List<E> addedElements) {
		final List<A> addedElementNames = new LinkedList<A>();
		for (final E addedElement : addedElements) {
			addedElementNames.add(getAddedElementValue(addedElement));
		}
		return addedElementNames;
	}

	protected List<E> getAddedElements(final List<E> baseElements, final List<E> comparisonElements) {
		return getRelativeComplement(baseElements, comparisonElements);
	}

	protected List<E> getRemovedElements(final List<E> baseElements, final List<E> comparisonElements) {
		return getRelativeComplement(comparisonElements, baseElements);
	}

	protected void setAddedDiffInfo(final List<E> addedElements,
			final StructureDiffInfo<A, R> diffInfo) {
		diffInfo.setAdded(addedElements.size());
		diffInfo.setAddedElements(getAddedElementValues(addedElements));
	}

	protected void setRemovedDiffInfo(final List<E> removedElements,
			final StructureDiffInfo<A, R> diffInfo) {
		diffInfo.setRemoved(removedElements.size());
		diffInfo.setRemovedElements(getRemovedElementValues(removedElements));
	}

	protected E getElementByIdentifier(final List<E> elements, final String identifier) {
		for (final E element : elements) {
			if (identifier.equals(getElementIdentifier(element))) {
				return element;
			}
		}
		return null;
	}

	protected String getLogMessage(final StructureDiffInfo<A, R> diffInfo, final String identifier) {
		final StringBuilder logMessage = new StringBuilder(identifier).append(" has");
		logMessage.append(" addedElements: ").append(diffInfo.getAdded());
		logMessage.append(" changedElements: ").append(diffInfo.getChanged());
		logMessage.append(" removedElements: ").append(diffInfo.getRemoved());
		logMessage.append(" changeRate: ").append(diffInfo.getChangeRate());
		return logMessage.toString();
	}

	private List<E> getRelativeComplement(final List<E> elementsA, final List<E> elementsB) {
		final List<E> relativeComplement = new LinkedList<E>();

		for (final E elementA : elementsA) {
			final E elementB = getElementByIdentifier(elementsB,
					getElementIdentifier(elementA));
			if (elementB == null) {
				relativeComplement.add(elementA);
			}
		}

		return relativeComplement;
	}
}
