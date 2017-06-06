package org.scenarioo.rest.step.logic;

import java.io.File;
import java.util.List;

import org.scenarioo.business.builds.BuildLink;
import org.scenarioo.dao.aggregates.AggregatedDocuDataReader;
import org.scenarioo.model.docu.aggregates.branches.BuildImportSummary;
import org.scenarioo.model.docu.aggregates.objects.CustomObjectTabTree;
import org.scenarioo.model.docu.aggregates.objects.LongObjectNamesResolver;
import org.scenarioo.model.docu.aggregates.objects.ObjectIndex;
import org.scenarioo.model.docu.aggregates.scenarios.ScenarioPageSteps;
import org.scenarioo.model.docu.aggregates.steps.StepLink;
import org.scenarioo.model.docu.aggregates.steps.StepNavigation;
import org.scenarioo.model.docu.aggregates.features.FeatureScenarios;
import org.scenarioo.model.docu.entities.generic.ObjectDescription;
import org.scenarioo.model.docu.entities.generic.ObjectList;
import org.scenarioo.model.docu.entities.generic.ObjectReference;
import org.scenarioo.rest.base.BuildIdentifier;
import org.scenarioo.rest.base.ScenarioIdentifier;

public class DummyAggregatedDataReader implements AggregatedDocuDataReader {

	@Override
	public String loadVersion(final BuildIdentifier buildIdentifier) {
		return null;
	}

	@Override
	public List<FeatureScenarios> loadFeatureScenariosList(final BuildIdentifier buildIdentifier) {
		return null;
	}

	@Override
	public FeatureScenarios loadFeatureScenarios(final BuildIdentifier buildIdentifier, final String featureName) {
		return null;
	}

	@Override
	public ScenarioPageSteps loadScenarioPageSteps(final ScenarioIdentifier scenarioIdentifier) {
		if (StepTestData.SCENARIO_IDENTIFIER_VALID.equals(scenarioIdentifier)) {
			return StepTestData.SCENARIO;
		} else if (StepTestData.SCENARIO_IDENTIFIER_VALID_2.equals(scenarioIdentifier)) {
			return StepTestData.SCENARIO_FALLBACK_IN_SAME_FEATURE;
		} else if (StepTestData.SCENARIO_IDENTIFIER_LOGIN_WITH_MATCHING_LABELS.equals(scenarioIdentifier)) {
			return StepTestData.SCENARIO_WITH_MATCHING_LABELS;
		} else if (StepTestData.SCENARIO_IDENTIFIER_FALLBACK_WITH_LABELS_IN_SAME_FEATURE.equals(scenarioIdentifier)) {
			return StepTestData.SCENARIO_WITH_MATCHING_LABELS;
		} else {
			return null;
		}
	}

	@Override
	public ObjectDescription loadObjectDescription(final BuildIdentifier buildIdentifier,
			final ObjectReference objectRef) {
		return null;
	}

	@Override
	public ObjectDescription loadObjectDescription(final File file) {
		return null;
	}

	@Override
	public ObjectIndex loadObjectIndex(final BuildIdentifier buildIdentifier, final String objectType,
			final String objectName) {
		if (!StepTestData.BUILD_IDENTIFIER_VALID.equals(buildIdentifier)) {
			return null;
		}

		if ("page".equals(objectType) && StepTestData.PAGE_NAME_VALID_1.equals(objectName)) {
			return StepTestData.OBJECT_INDEX_FOR_PAGE_1;
		}

		return null;
	}

	@Override
	public ObjectList<ObjectDescription> loadObjectsList(final BuildIdentifier buildIdentifier, final String type) {
		return null;
	}

	@Override
	public CustomObjectTabTree loadCustomObjectTabTree(final BuildIdentifier buildIdentifier, final String tabId) {
		return null;
	}

	@Override
	public ObjectIndex loadObjectIndexIfExistant(final BuildIdentifier buildIdentifier, final String objectType,
			final String objectName) {
		return null;
	}

	@Override
	public List<BuildImportSummary> loadBuildImportSummaries() {
		return null;
	}

	@Override
	public LongObjectNamesResolver loadLongObjectNamesIndex(final BuildIdentifier buildIdentifier) {
		return null;
	}

	@Override
	public StepNavigation loadStepNavigation(final BuildIdentifier build, final StepLink step) {
		return null;
	}

	@Override
	public StepNavigation loadStepNavigation(final ScenarioIdentifier scenarioIdentifier, final int stepIndex) {
		return null;
	}

	@Override
	public List<BuildLink> loadBuildLinks(final String branchName) {
		return null;
	}

}
