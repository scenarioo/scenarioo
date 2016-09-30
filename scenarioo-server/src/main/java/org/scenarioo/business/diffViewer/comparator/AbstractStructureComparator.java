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
 * @param <ELEMENT_TYPE>
 *            Represents the element type. For example a use case.
 * @param <ADDED_ELEMENT_TYPE>
 *            Represents the added element type in {@link StructureDiffInfo}.
 * @param <REMOVED_ELEMENT_TYPE>
 *            Represents the removed element type in {@link StructureDiffInfo}.
 */
public abstract class AbstractStructureComparator<ELEMENT_TYPE, ADDED_ELEMENT_TYPE, REMOVED_ELEMENT_TYPE> extends AbstractComparator {

	private static final double ADDED_REMOVED_CHANGE_RATE = 100.0;

	public AbstractStructureComparator(final String baseBranchName, final String baseBuildName,
			final ComparisonConfiguration comparisonConfiguration) {
		super(baseBranchName, baseBuildName, comparisonConfiguration);
	}

	protected abstract double compareElementAndWrite(ELEMENT_TYPE baseElement, ELEMENT_TYPE comparisonElement, StructureDiffInfo<ADDED_ELEMENT_TYPE, REMOVED_ELEMENT_TYPE> diffInfo);

	protected abstract String getElementIdentifier(ELEMENT_TYPE element);

	protected abstract ADDED_ELEMENT_TYPE getAddedElementValue(ELEMENT_TYPE element);

	protected abstract List<REMOVED_ELEMENT_TYPE> getRemovedElementValues(List<ELEMENT_TYPE> removedElements);

	protected void handleAddedElements(final List<ELEMENT_TYPE> baseElements, final List<ELEMENT_TYPE> comparisonElements,
									   final StructureDiffInfo<ADDED_ELEMENT_TYPE, REMOVED_ELEMENT_TYPE> diffInfo) {
		final List<ELEMENT_TYPE> addedElements = getAddedElements(baseElements, comparisonElements);
		setAddedDiffInfo(addedElements, diffInfo);
	}

	protected void calculateDiffInfo(final List<ELEMENT_TYPE> baseElements,
									 final List<ELEMENT_TYPE> comparisonElements, final StructureDiffInfo<ADDED_ELEMENT_TYPE, REMOVED_ELEMENT_TYPE> diffInfo) {
		handleAddedElements(baseElements, comparisonElements, diffInfo);

		final double elementChangeRateSum = compareElementsAndWrite(baseElements, comparisonElements, diffInfo);

		handleRemovedElements(baseElements, comparisonElements, diffInfo);

		diffInfo.setChangeRate(calculateChangeRate(baseElements.size(), diffInfo.getAdded(),
				diffInfo.getRemoved(), elementChangeRateSum));
	}

	protected double compareElementsAndWrite(final List<ELEMENT_TYPE> baseElements, final List<ELEMENT_TYPE> comparisonElements,
											 final StructureDiffInfo<ADDED_ELEMENT_TYPE, REMOVED_ELEMENT_TYPE> diffInfo) {
		double elementChangeRateSum = 0;
		for (final ELEMENT_TYPE baseElement : baseElements) {
			final ELEMENT_TYPE comparisonElement = getElementByIdentifier(comparisonElements, getElementIdentifier(baseElement));
			elementChangeRateSum += compareElementAndWrite(baseElement, comparisonElement, diffInfo);
		}
		return elementChangeRateSum;
	}

	protected void handleRemovedElements(final List<ELEMENT_TYPE> baseElements,
										 final List<ELEMENT_TYPE> comparisonElements, final StructureDiffInfo<ADDED_ELEMENT_TYPE, REMOVED_ELEMENT_TYPE> diffInfo) {
		final List<ELEMENT_TYPE> removedElements = getRemovedElements(baseElements, comparisonElements);
		setRemovedDiffInfo(removedElements, diffInfo);
	}

	private List<ADDED_ELEMENT_TYPE> getAddedElementValues(final List<ELEMENT_TYPE> addedElements) {
		final List<ADDED_ELEMENT_TYPE> addedElementNames = new LinkedList<ADDED_ELEMENT_TYPE>();
		for (final ELEMENT_TYPE addedElement : addedElements) {
			addedElementNames.add(getAddedElementValue(addedElement));
		}
		return addedElementNames;
	}

	protected List<ELEMENT_TYPE> getAddedElements(final List<ELEMENT_TYPE> baseElements, final List<ELEMENT_TYPE> comparisonElements) {
		return getRelativeComplement(baseElements, comparisonElements);
	}

	protected List<ELEMENT_TYPE> getRemovedElements(final List<ELEMENT_TYPE> baseElements, final List<ELEMENT_TYPE> comparisonElements) {
		return getRelativeComplement(comparisonElements, baseElements);
	}

	protected void setAddedDiffInfo(final List<ELEMENT_TYPE> addedElements,
			final StructureDiffInfo<ADDED_ELEMENT_TYPE, REMOVED_ELEMENT_TYPE> diffInfo) {
		diffInfo.setAdded(addedElements.size());
		diffInfo.setAddedElements(getAddedElementValues(addedElements));
	}

	protected void setRemovedDiffInfo(final List<ELEMENT_TYPE> removedElements,
			final StructureDiffInfo<ADDED_ELEMENT_TYPE, REMOVED_ELEMENT_TYPE> diffInfo) {
		diffInfo.setRemoved(removedElements.size());
		diffInfo.setRemovedElements(getRemovedElementValues(removedElements));
	}

	protected ELEMENT_TYPE getElementByIdentifier(final List<ELEMENT_TYPE> elements, final String identifier) {
		for (final ELEMENT_TYPE element : elements) {
			if (identifier.equals(getElementIdentifier(element))) {
				return element;
			}
		}
		return null;
	}

	protected double calculateChangeRate(final int numberOfBaseElements, final int numberOfAddedElements,
			final int numberOfRemovedElements,
			final double childChangeRateSum) {

		double changeRateSum = 0.0;
		changeRateSum += numberOfAddedElements * ADDED_REMOVED_CHANGE_RATE;
		changeRateSum += childChangeRateSum;
		changeRateSum += numberOfRemovedElements * ADDED_REMOVED_CHANGE_RATE;

		if (numberOfBaseElements + numberOfRemovedElements == 0) {
			return 0;
		}
		return changeRateSum / (numberOfBaseElements + numberOfRemovedElements);
	}

	protected String getLogMessage(final StructureDiffInfo<ADDED_ELEMENT_TYPE, REMOVED_ELEMENT_TYPE> diffInfo, final String identifier) {
		final StringBuilder logMessage = new StringBuilder(identifier).append(" has");
		logMessage.append(" addedElements: ").append(diffInfo.getAdded());
		logMessage.append(" changedElements: ").append(diffInfo.getChanged());
		logMessage.append(" removedElements: ").append(diffInfo.getRemoved());
		logMessage.append(" changeRate: ").append(diffInfo.getChangeRate());
		return logMessage.toString();
	}

	/**
	 * Returns all elements of ADDED_ELEMENT_TYPE which aren't in elements B. --> (ADDED_ELEMENT_TYPE \ B) = {x is element of ADDED_ELEMENT_TYPE | x is not element of B}
	 */
	private List<ELEMENT_TYPE> getRelativeComplement(final List<ELEMENT_TYPE> elementsA, final List<ELEMENT_TYPE> elementsB) {
		final List<ELEMENT_TYPE> relativeComplement = new LinkedList<ELEMENT_TYPE>();

		for (final ELEMENT_TYPE elementA : elementsA) {
			final ELEMENT_TYPE elementB = getElementByIdentifier(elementsB,
					getElementIdentifier(elementA));
			if (elementB == null) {
				relativeComplement.add(elementA);
			}
		}

		return relativeComplement;
	}
}
