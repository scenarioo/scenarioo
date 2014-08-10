package org.scenarioo.rest.util;

import java.util.List;

import org.scenarioo.dao.aggregates.AggregatedDataReader;
import org.scenarioo.model.docu.aggregates.objects.ObjectIndex;
import org.scenarioo.model.docu.aggregates.scenarios.ScenarioPageSteps;
import org.scenarioo.model.docu.entities.generic.ObjectReference;
import org.scenarioo.model.docu.entities.generic.ObjectTreeNode;
import org.scenarioo.rest.request.StepIdentifier;

public class ScenarioLoader {
	
	private static final String TYPE_USECASE = "usecase";
	private static final String TYPE_SCENARIO = "scenario";
	
	private final AggregatedDataReader aggregatedDataReader;
	
	public ScenarioLoader(final AggregatedDataReader aggregatedDataReader) {
		this.aggregatedDataReader = aggregatedDataReader;
	}
	
	/**
	 * Tries to load the scenario. If it is not found, a fallback to a different scenario (or even use case) with the
	 * same page happens.
	 */
	public LoadScenarioResult loadScenario(final StepIdentifier stepIdentifier) {
		ScenarioPageSteps requestedScenario = aggregatedDataReader.loadScenarioPageSteps(stepIdentifier
				.getScenarioIdentifier());
		if (requestedScenario != null) {
			return LoadScenarioResult.foundRequestedScenario(requestedScenario);
		}
		
		return findPageInRequestedUseCaseAndInAllUseCases(stepIdentifier);
	}
	
	public LoadScenarioResult findPageInRequestedUseCaseAndInAllUseCases(final StepIdentifier stepIdentifier) {
		ObjectIndex objectIndex = aggregatedDataReader.loadObjectIndex(stepIdentifier.getBuildIdentifier(), "page",
				stepIdentifier.getPageName());
		
		StepIdentifier redirect = findPageInRequestedUseCase(objectIndex, stepIdentifier);
		
		if (redirect == null) {
			redirect = findPageInAllUseCases(objectIndex, stepIdentifier);
		}
		
		if (redirect == null) {
			return LoadScenarioResult.foundNothing();
		}
		
		return LoadScenarioResult.foundFallback(redirect);
	}
	
	private StepIdentifier findPageInAllUseCases(final ObjectIndex objectIndex, final StepIdentifier stepIdentifier) {
		List<ObjectTreeNode<ObjectReference>> useCases = getChildren(objectIndex);
		
		if (useCases == null) {
			return null;
		}
		
		for (ObjectTreeNode<ObjectReference> useCaseNode : useCases) {
			if (TYPE_USECASE.equals(useCaseNode.getItem().getType())) {
				// TODO [fallback with labels] Find best matching scenario using labels
				return getFirstScenarioInUseCase(useCaseNode, stepIdentifier);
			}
		}
		
		return null;
	}
	
	private StepIdentifier findPageInRequestedUseCase(final ObjectIndex objectIndex, final StepIdentifier stepIdentifier) {
		String useCaseToFind = stepIdentifier.getUsecaseName();
		
		List<ObjectTreeNode<ObjectReference>> useCases = getChildren(objectIndex);
		
		if (useCases == null) {
			return null;
		}
		
		for (ObjectTreeNode<ObjectReference> useCaseNode : useCases) {
			if (TYPE_USECASE.equals(useCaseNode.getItem().getType())
					&& useCaseToFind.equals(useCaseNode.getItem().getName())) {
				// TODO [fallback with labels] Find best matching scenario using labels
				return getFirstScenarioInUseCase(useCaseNode, stepIdentifier);
			}
		}
		
		return null;
	}
	
	private List<ObjectTreeNode<ObjectReference>> getChildren(final ObjectIndex objectIndex) {
		if (objectIndex.getReferenceTree() == null) {
			return null;
		}
		
		return objectIndex.getReferenceTree().getChildren();
	}
	
	private StepIdentifier getFirstScenarioInUseCase(final ObjectTreeNode<ObjectReference> useCaseNode,
			final StepIdentifier stepIdentifier) {
		List<ObjectTreeNode<ObjectReference>> scenarios = useCaseNode.getChildren();
		
		String useCaseName = useCaseNode.getItem().getName();
		
		if (scenarios == null) {
			return null;
		}
		
		for (ObjectTreeNode<ObjectReference> scenarioNode : scenarios) {
			if (TYPE_SCENARIO.equals(scenarioNode.getItem().getType())) {
				return StepIdentifier
						.forFallBackScenario(stepIdentifier, useCaseName, scenarioNode.getItem().getName());
			}
		}
		
		return null;
	}
}
