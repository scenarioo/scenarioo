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
	
	public LoadScenarioResult loadScenario(final StepIdentifier stepIdentifier) {
		ScenarioPageSteps requestedScenario = aggregatedDataReader.loadScenarioPageSteps(stepIdentifier
				.getScenarioIdentifier());
		if (requestedScenario != null) {
			return LoadScenarioResult.foundRequestedScenario(requestedScenario);
		}
		
		return findFallbackScenario(stepIdentifier);
	}
	
	private LoadScenarioResult findFallbackScenario(final StepIdentifier stepIdentifier) {
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
		// TODO Auto-generated method stub
		return null;
	}
	
	private StepIdentifier findPageInRequestedUseCase(final ObjectIndex objectIndex, final StepIdentifier stepIdentifier) {
		String useCaseToFind = stepIdentifier.getUsecaseName();
		
		if (objectIndex.getReferenceTree() == null) {
			return null;
		}
		
		List<ObjectTreeNode<ObjectReference>> useCases = objectIndex.getReferenceTree().getChildren();
		
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
	
	private StepIdentifier getFirstScenarioInUseCase(final ObjectTreeNode<ObjectReference> useCaseNode,
			final StepIdentifier stepIdentifier) {
		List<ObjectTreeNode<ObjectReference>> scenarios = useCaseNode.getChildren();
		
		if (scenarios == null) {
			return null;
		}
		
		for (ObjectTreeNode<ObjectReference> scenarioNode : scenarios) {
			if (TYPE_SCENARIO.equals(scenarioNode.getItem().getType())) {
				return StepIdentifier.forFallBackScenario(stepIdentifier, scenarioNode.getItem().getName());
			}
		}
		
		return null;
	}
}
