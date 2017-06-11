package org.scenarioo.rest.step.logic;

import java.util.LinkedList;
import java.util.List;

import org.scenarioo.api.exception.ResourceNotFoundException;
import org.scenarioo.dao.aggregates.AggregatedDocuDataReader;
import org.scenarioo.model.docu.aggregates.objects.ObjectIndex;
import org.scenarioo.model.docu.aggregates.scenarios.ScenarioPageSteps;
import org.scenarioo.model.docu.entities.generic.ObjectReference;
import org.scenarioo.model.docu.entities.generic.ObjectTreeNode;
import org.scenarioo.rest.base.StepIdentifier;

public class ScenarioLoader {

	private static final String TYPE_FEATURE = "feature";
	private static final String TYPE_SCENARIO = "scenario";

	private final AggregatedDocuDataReader aggregatedDataReader;

	public ScenarioLoader(final AggregatedDocuDataReader aggregatedDataReader) {
		this.aggregatedDataReader = aggregatedDataReader;
	}

	/**
	 * Tries to load the scenario. If it is not found, a fallback to a different scenario (or even feature) with the
	 * same page happens.
	 */
	public LoadScenarioResult loadScenario(final StepIdentifier stepIdentifier) {
		ScenarioPageSteps requestedScenario = aggregatedDataReader.loadScenarioPageSteps(stepIdentifier
				.getScenarioIdentifier());
		if (requestedScenario != null) {
			return LoadScenarioResult.foundRequestedScenario(requestedScenario);
		}

		return findPageInRequestedFeatureOrInAllFeatures(stepIdentifier);
	}

	public LoadScenarioResult findPageInRequestedFeatureOrInAllFeatures(final StepIdentifier stepIdentifier) {
		ObjectIndex objectIndex = null;
		try {
			objectIndex = aggregatedDataReader.loadObjectIndex(stepIdentifier.getBuildIdentifier(), "page",
					stepIdentifier.getPageName());
		} catch (ResourceNotFoundException e) {
			return LoadScenarioResult.foundNothing();
		}

		StepIdentifier redirect = findPageInRequestedFeature(objectIndex, stepIdentifier);

		if (redirect == null) {
			redirect = findPageInAllFeatures(objectIndex, stepIdentifier);
		}

		if (redirect == null) {
			return LoadScenarioResult.foundNothing();
		}

		ScenarioPageSteps fallbackScenario = aggregatedDataReader.loadScenarioPageSteps(redirect
				.getScenarioIdentifier());
		return LoadScenarioResult.foundFallback(fallbackScenario, redirect);
	}

	private StepIdentifier findPageInRequestedFeature(final ObjectIndex objectIndex, final StepIdentifier stepIdentifier) {
		String featureToFind = stepIdentifier.getFeatureName();

		List<ObjectTreeNode<ObjectReference>> featureNodes = getChildren(objectIndex);

		if (featureNodes == null) {
			return null;
		}

		ObjectTreeNode<ObjectReference> requestedFeatureNode = getNodeOfRequestedFeature(featureNodes, featureToFind);

		if (requestedFeatureNode == null) {
			return null;
		}

		return getBestMatchingScenarioAndStepInFeature(requestedFeatureNode, stepIdentifier);
	}

	private ObjectTreeNode<ObjectReference> getNodeOfRequestedFeature(
			final List<ObjectTreeNode<ObjectReference>> featureNodes, final String featureToFind) {
		for (ObjectTreeNode<ObjectReference> featureNode : featureNodes) {
			if (TYPE_FEATURE.equals(featureNode.getItem().getType())
					&& featureToFind.equals(featureNode.getItem().getName())) {
				return featureNode;
			}
		}
		return null;
	}

	private StepIdentifier findPageInAllFeatures(final ObjectIndex objectIndex, final StepIdentifier stepIdentifier) {
		List<ObjectTreeNode<ObjectReference>> featureNodes = getChildren(objectIndex);
		return getBestMatchingScenarioAndStepInAllFeatures(stepIdentifier, featureNodes);
	}

	private List<ObjectTreeNode<ObjectReference>> getChildren(final ObjectIndex objectIndex) {
		if (objectIndex.getReferenceTree() == null) {
			return null;
		}

		return objectIndex.getReferenceTree().getChildren();
	}

	private StepIdentifier getBestMatchingScenarioAndStepInAllFeatures(final StepIdentifier stepIdentifier,
			final List<ObjectTreeNode<ObjectReference>> featureNodes) {
		if (featureNodes == null) {
			return null;
		}

		List<StepCandidate> stepCandidates = new LinkedList<StepCandidate>();

		for (ObjectTreeNode<ObjectReference> featureNode : featureNodes) {
			stepCandidates.addAll(collectStepCandidates(featureNode, stepIdentifier));
		}

		return getStepCandidateWithHighestNumberOfMatchingLabels(stepCandidates, stepIdentifier);
	}

	private StepIdentifier getBestMatchingScenarioAndStepInFeature(final ObjectTreeNode<ObjectReference> featureNode,
			final StepIdentifier stepIdentifier) {
		List<StepCandidate> stepCandidates = collectStepCandidates(featureNode, stepIdentifier);
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
				.forFallBackScenario(stepIdentifier, stepCandidate.getFeature(), stepCandidate.getScenario(),
						stepCandidate.getPageOccurrence(), stepCandidate.getStepInPageOccurrence());
	}

	private List<StepCandidate> collectStepCandidates(final ObjectTreeNode<ObjectReference> featureNode,
			final StepIdentifier stepIdentifier) {
		List<ObjectTreeNode<ObjectReference>> scenarios = featureNode.getChildren();
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
						.add(StepCandidate.create(featureNode, scenarioNode, stepNode, stepIdentifier.getLabels()));

			}
		}

		return stepCandidates;
	}

}
