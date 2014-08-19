package org.scenarioo.rest.util;

import java.util.LinkedList;
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
		
		return findPageInRequestedUseCaseOrInAllUseCases(stepIdentifier);
	}
	
	public LoadScenarioResult findPageInRequestedUseCaseOrInAllUseCases(final StepIdentifier stepIdentifier) {
		ObjectIndex objectIndex = aggregatedDataReader.loadObjectIndex(stepIdentifier.getBuildIdentifier(), "page",
				stepIdentifier.getPageName());
		
		StepIdentifier redirect = findPageInRequestedUseCase(objectIndex, stepIdentifier);
		
		if (redirect == null) {
			redirect = findPageInAllUseCases(objectIndex, stepIdentifier);
		}
		
		if (redirect == null) {
			return LoadScenarioResult.foundNothing();
		}
		
		ScenarioPageSteps fallbackScenario = aggregatedDataReader.loadScenarioPageSteps(stepIdentifier
				.getScenarioIdentifier());
		return LoadScenarioResult.foundFallback(fallbackScenario, redirect);
	}
	
	private StepIdentifier findPageInRequestedUseCase(final ObjectIndex objectIndex, final StepIdentifier stepIdentifier) {
		String useCaseToFind = stepIdentifier.getUsecaseName();
		
		List<ObjectTreeNode<ObjectReference>> useCaseNodes = getChildren(objectIndex);
		
		if (useCaseNodes == null) {
			return null;
		}
		
		ObjectTreeNode<ObjectReference> requestedUseCaseNode = getNodeOfRequestedUseCase(useCaseNodes, useCaseToFind);
		
		if (requestedUseCaseNode == null) {
			return null;
		}
		
		return getBestMatchingScenarioAndStepInUseCase(requestedUseCaseNode, stepIdentifier);
	}
	
	private ObjectTreeNode<ObjectReference> getNodeOfRequestedUseCase(
			final List<ObjectTreeNode<ObjectReference>> useCaseNodes, final String useCaseToFind) {
		for (ObjectTreeNode<ObjectReference> useCaseNode : useCaseNodes) {
			if (TYPE_USECASE.equals(useCaseNode.getItem().getType())
					&& useCaseToFind.equals(useCaseNode.getItem().getName())) {
				return useCaseNode;
			}
		}
		return null;
	}
	
	private StepIdentifier findPageInAllUseCases(final ObjectIndex objectIndex, final StepIdentifier stepIdentifier) {
		List<ObjectTreeNode<ObjectReference>> useCaseNodes = getChildren(objectIndex);
		return getBestMatchingScenarioAndStepInAllUseCases(stepIdentifier, useCaseNodes);
	}
	
	private List<ObjectTreeNode<ObjectReference>> getChildren(final ObjectIndex objectIndex) {
		if (objectIndex.getReferenceTree() == null) {
			return null;
		}
		
		return objectIndex.getReferenceTree().getChildren();
	}
	
	private StepIdentifier getBestMatchingScenarioAndStepInAllUseCases(final StepIdentifier stepIdentifier,
			final List<ObjectTreeNode<ObjectReference>> useCaseNodes) {
		if (useCaseNodes == null) {
			return null;
		}
		
		List<StepCandidate> stepCandidates = new LinkedList<StepCandidate>();
		
		for (ObjectTreeNode<ObjectReference> useCaseNode : useCaseNodes) {
			stepCandidates.addAll(collectStepCandidates(useCaseNode, stepIdentifier));
		}
		
		return getStepCandidateWithHighestNumberOfMatchingLabels(stepCandidates, stepIdentifier);
	}
	
	private StepIdentifier getBestMatchingScenarioAndStepInUseCase(final ObjectTreeNode<ObjectReference> useCaseNode,
			final StepIdentifier stepIdentifier) {
		List<StepCandidate> stepCandidates = collectStepCandidates(useCaseNode, stepIdentifier);
		return getStepCandidateWithHighestNumberOfMatchingLabels(stepCandidates, stepIdentifier);
	}
	
	private StepIdentifier getStepCandidateWithHighestNumberOfMatchingLabels(final List<StepCandidate> stepCandidates,
			final StepIdentifier stepIdentifier) {
		if (stepCandidates == null || stepCandidates.size() == 0) {
			return null;
		}
		
		if (stepCandidates.size() == 1) {
			return createStepIdentifierFromCandidate(stepCandidates.get(0), stepIdentifier);
		}
		
		StepCandidate stepWithMostMatchingLabels = stepCandidates.get(0);
		
		for (StepCandidate candidate : stepCandidates) {
			if (candidate.getNumberOfMatchingLabels() > stepWithMostMatchingLabels.getNumberOfMatchingLabels()) {
				stepWithMostMatchingLabels = candidate;
			}
		}
		
		return createStepIdentifierFromCandidate(stepWithMostMatchingLabels, stepIdentifier);
	}
	
	private StepIdentifier createStepIdentifierFromCandidate(final StepCandidate stepCandidate,
			final StepIdentifier stepIdentifier) {
		
		return StepIdentifier
				.forFallBackScenario(stepIdentifier, stepCandidate.getUsecase(), stepCandidate.getScenario(),
						stepCandidate.getPageOccurrence(), stepCandidate.getStepInPageOccurrence());
	}
	
	private List<StepCandidate> collectStepCandidates(final ObjectTreeNode<ObjectReference> useCaseNode,
			final StepIdentifier stepIdentifier) {
		List<ObjectTreeNode<ObjectReference>> scenarios = useCaseNode.getChildren();
		if (scenarios == null) {
			return null;
		}
		
		List<StepCandidate> stepCandidates = new LinkedList<StepCandidate>();
		
		for (ObjectTreeNode<ObjectReference> scenarioNode : scenarios) {
			if (!TYPE_SCENARIO.equals(scenarioNode.getItem().getType())) {
				continue;
			}
			List<ObjectTreeNode<ObjectReference>> steps = scenarioNode.getChildren();
			for (ObjectTreeNode<ObjectReference> stepNode : steps) {
				stepCandidates
						.add(StepCandidate.create(useCaseNode, scenarioNode, stepNode, stepIdentifier.getLabels()));
				
			}
		}
		
		return stepCandidates;
	}
	
}
