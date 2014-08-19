package org.scenarioo.rest.util;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.scenarioo.model.docu.entities.generic.ObjectList;
import org.scenarioo.model.docu.entities.generic.ObjectReference;
import org.scenarioo.model.docu.entities.generic.ObjectTreeNode;

import com.google.common.base.Preconditions;

/**
 * Used for the fallback mechanism if the requested step is not found.
 */
public class StepCandidate {
	
	private final String usecase;
	private final String scenario;
	private final int pageOccurrence;
	private final int stepInPageOccurrence;
	private int numberOfMatchingLabels = 0;
	
	/**
	 * @param step
	 *            Short form of the step identifier (e.g. "searchPage.jsp/3/0").
	 */
	private StepCandidate(final String usecase, final String scenario, final int pageOccurrence,
			final int stepInPageOccurrence, final int numberOfMatchingLabels) {
		super();
		
		Preconditions.checkNotNull(usecase);
		Preconditions.checkNotNull(scenario);
		Preconditions.checkArgument(pageOccurrence >= 0);
		Preconditions.checkArgument(stepInPageOccurrence >= 0);
		Preconditions.checkArgument(numberOfMatchingLabels >= 0);
		
		this.usecase = usecase;
		this.scenario = scenario;
		this.pageOccurrence = pageOccurrence;
		this.stepInPageOccurrence = stepInPageOccurrence;
		this.numberOfMatchingLabels = numberOfMatchingLabels;
	}
	
	public static StepCandidate create(final ObjectTreeNode<ObjectReference> useCaseNode,
			final ObjectTreeNode<ObjectReference> scenarioNode, final ObjectTreeNode<ObjectReference> stepNode,
			final Set<String> stepIdentifierLabels) {
		Set<String> labelsOfStepAndParentObjects = collectAllLabelsOfStepAndParentObjects(useCaseNode, scenarioNode,
				stepNode);
		
		int numberOfMatchingLabel = getMatchingLabelsCount(labelsOfStepAndParentObjects, stepIdentifierLabels);
		
		String stepId = stepNode.getItem().getName();
		String[] stepIdParts = stepId.split("/");
		
		return new StepCandidate(useCaseNode.getItem().getName(), scenarioNode.getItem().getName(),
				Integer.parseInt(stepIdParts[1]), Integer.parseInt(stepIdParts[2]), numberOfMatchingLabel);
	}
	
	private static Set<String> collectAllLabelsOfStepAndParentObjects(
			final ObjectTreeNode<ObjectReference> useCaseNode, final ObjectTreeNode<ObjectReference> scenarioNode,
			final ObjectTreeNode<ObjectReference> stepNode) {
		Set<String> labelsOfStepAndParentObjects = new HashSet<String>();
		labelsOfStepAndParentObjects.addAll(getLabelsOfNode(useCaseNode));
		labelsOfStepAndParentObjects.addAll(getLabelsOfNode(scenarioNode));
		labelsOfStepAndParentObjects.addAll(getLabelsOfNode(stepNode));
		return labelsOfStepAndParentObjects;
	}
	
	private static Collection<String> getLabelsOfNode(final ObjectTreeNode<ObjectReference> treeNode) {
		if (treeNode == null) {
			return Collections.emptySet();
		}
		
		Object labelsDetail = treeNode.getDetails().getDetail("labels");
		
		if (labelsDetail == null || !(labelsDetail instanceof ObjectList)) {
			return Collections.emptySet();
		}
		
		@SuppressWarnings("unchecked")
		ObjectList<String> stepLabels = (ObjectList<String>) labelsDetail;
		
		return stepLabels;
	}
	
	private static int getMatchingLabelsCount(final Set<String> labelsOfStepAndParentObjects,
			final Set<String> stepIdentifierLabels) {
		if (labelsOfStepAndParentObjects == null || stepIdentifierLabels == null) {
			return 0;
		}
		
		Set<String> intersection = new HashSet<String>(labelsOfStepAndParentObjects);
		intersection.retainAll(stepIdentifierLabels);
		
		return intersection.size();
	}
	
	public String getUsecase() {
		return usecase;
	}
	
	public String getScenario() {
		return scenario;
	}
	
	public int getNumberOfMatchingLabels() {
		return numberOfMatchingLabels;
	}
	
	public int getPageOccurrence() {
		return pageOccurrence;
	}
	
	public int getStepInPageOccurrence() {
		return stepInPageOccurrence;
	}
	
}
