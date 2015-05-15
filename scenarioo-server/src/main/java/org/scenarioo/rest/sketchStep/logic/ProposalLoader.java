package org.scenarioo.rest.sketchStep.logic;

import java.util.LinkedList;
import java.util.List;

import org.scenarioo.api.exception.ResourceNotFoundException;
import org.scenarioo.dao.design.aggregates.ScenarioSketchAggregationDAO;
import org.scenarioo.model.docu.aggregates.objects.ObjectIndex;
import org.scenarioo.model.docu.aggregates.scenarios.ScenarioPageSteps;
import org.scenarioo.model.docu.entities.generic.ObjectReference;
import org.scenarioo.model.docu.entities.generic.ObjectTreeNode;
import org.scenarioo.rest.base.StepIdentifier;

public class ProposalLoader {
	
	private static final String TYPE_USECASE = "usecase";
	private static final String TYPE_SCENARIO = "scenario";
	
	private final ScenarioSketchAggregationDAO aggregatedDataReader;
	
	public ProposalLoader(final ScenarioSketchAggregationDAO aggregatedDataReader) {
		this.aggregatedDataReader = aggregatedDataReader;
	}
	
	/**
	 * Tries to load the scenario. If it is not found, a fallback to a different scenario (or even use case) with the
	 * same page happens.
	 */
	public LoadProposalResult loadScenario(final StepIdentifier stepIdentifier) {
		ScenarioPageSteps requestedScenario = aggregatedDataReader.loadScenarioPageSteps(stepIdentifier
				.getScenarioIdentifier());
		if (requestedScenario != null) {
			return LoadProposalResult.foundRequestedScenario(requestedScenario);
		}
		
		return findPageInRequestedUseCaseOrInAllUseCases(stepIdentifier);
	}
	
	public LoadProposalResult findPageInRequestedUseCaseOrInAllUseCases(final StepIdentifier stepIdentifier) {
		ObjectIndex objectIndex = null;
		try {
			objectIndex = aggregatedDataReader.loadObjectIndex(stepIdentifier.getBuildIdentifier(), "page",
					stepIdentifier.getPageName());
		} catch (ResourceNotFoundException e) {
			return LoadProposalResult.foundNothing();
		}
		
		StepIdentifier redirect = findPageInRequestedUseCase(objectIndex, stepIdentifier);
		
		if (redirect == null) {
			redirect = findPageInAllUseCases(objectIndex, stepIdentifier);
		}
		
		if (redirect == null) {
			return LoadProposalResult.foundNothing();
		}
		
		ScenarioPageSteps fallbackScenario = aggregatedDataReader.loadScenarioPageSteps(redirect
				.getScenarioIdentifier());
		return LoadProposalResult.foundFallback(fallbackScenario, redirect);
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
		
		List<SketchStepCandidate> stepCandidates = new LinkedList<SketchStepCandidate>();
		
		for (ObjectTreeNode<ObjectReference> useCaseNode : useCaseNodes) {
			stepCandidates.addAll(collectStepCandidates(useCaseNode, stepIdentifier));
		}
		
		return getStepCandidateWithHighestNumberOfMatchingLabels(stepCandidates, stepIdentifier);
	}
	
	private StepIdentifier getBestMatchingScenarioAndStepInUseCase(final ObjectTreeNode<ObjectReference> useCaseNode,
			final StepIdentifier stepIdentifier) {
		List<SketchStepCandidate> stepCandidates = collectStepCandidates(useCaseNode, stepIdentifier);
		return getStepCandidateWithHighestNumberOfMatchingLabels(stepCandidates, stepIdentifier);
	}
	
	private StepIdentifier getStepCandidateWithHighestNumberOfMatchingLabels(final List<SketchStepCandidate> stepCandidates,
			final StepIdentifier stepIdentifier) {
		if (stepCandidates == null || stepCandidates.size() == 0) {
			return null;
		}
		
		if (stepCandidates.size() == 1) {
			return createStepIdentifierFromCandidate(stepCandidates.get(0), stepIdentifier);
		}
		
		SketchStepCandidate stepWithMostMatchingLabels = stepCandidates.get(0);
		
		for (SketchStepCandidate candidate : stepCandidates) {
			if (candidate.getNumberOfMatchingLabels() > stepWithMostMatchingLabels.getNumberOfMatchingLabels()) {
				stepWithMostMatchingLabels = candidate;
			}
		}
		
		return createStepIdentifierFromCandidate(stepWithMostMatchingLabels, stepIdentifier);
	}
	
	private StepIdentifier createStepIdentifierFromCandidate(final SketchStepCandidate stepCandidate,
			final StepIdentifier stepIdentifier) {
		
		return StepIdentifier
				.forFallBackScenario(stepIdentifier, stepCandidate.getUsecase(), stepCandidate.getScenario(),
						stepCandidate.getPageOccurrence(), stepCandidate.getStepInPageOccurrence());
	}
	
	private List<SketchStepCandidate> collectStepCandidates(final ObjectTreeNode<ObjectReference> useCaseNode,
			final StepIdentifier stepIdentifier) {
		List<ObjectTreeNode<ObjectReference>> scenarios = useCaseNode.getChildren();
		if (scenarios == null) {
			return null;
		}
		
		List<SketchStepCandidate> stepCandidates = new LinkedList<SketchStepCandidate>();
		
		for (ObjectTreeNode<ObjectReference> scenarioNode : scenarios) {
			if (!TYPE_SCENARIO.equals(scenarioNode.getItem().getType())) {
				continue;
			}
			List<ObjectTreeNode<ObjectReference>> steps = scenarioNode.getChildren();
			for (ObjectTreeNode<ObjectReference> stepNode : steps) {
				stepCandidates
						.add(SketchStepCandidate.create(useCaseNode, scenarioNode, stepNode, stepIdentifier.getLabels()));
				
			}
		}
		
		return stepCandidates;
	}
	
}
