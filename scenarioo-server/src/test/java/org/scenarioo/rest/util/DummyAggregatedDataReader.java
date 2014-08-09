package org.scenarioo.rest.util;

import java.io.File;
import java.util.List;

import org.scenarioo.TestData;
import org.scenarioo.dao.aggregates.AggregatedDataReader;
import org.scenarioo.model.docu.aggregates.branches.BuildImportSummary;
import org.scenarioo.model.docu.aggregates.objects.CustomObjectTabTree;
import org.scenarioo.model.docu.aggregates.objects.LongObjectNamesResolver;
import org.scenarioo.model.docu.aggregates.objects.ObjectIndex;
import org.scenarioo.model.docu.aggregates.scenarios.ScenarioPageSteps;
import org.scenarioo.model.docu.aggregates.steps.StepLink;
import org.scenarioo.model.docu.aggregates.steps.StepNavigation;
import org.scenarioo.model.docu.aggregates.usecases.UseCaseScenarios;
import org.scenarioo.model.docu.entities.generic.ObjectDescription;
import org.scenarioo.model.docu.entities.generic.ObjectList;
import org.scenarioo.model.docu.entities.generic.ObjectReference;
import org.scenarioo.rest.request.BuildIdentifier;
import org.scenarioo.rest.request.ScenarioIdentifier;

public class DummyAggregatedDataReader implements AggregatedDataReader {
	
	@Override
	public String loadVersion(final String branchName, final String buildName) {
		return null;
	}
	
	@Override
	public List<UseCaseScenarios> loadUseCaseScenariosList(final BuildIdentifier buildIdentifier) {
		return null;
	}
	
	@Override
	public UseCaseScenarios loadUseCaseScenarios(final String branchName, final String buildName,
			final String usecaseName) {
		return null;
	}
	
	@Override
	public ScenarioPageSteps loadScenarioPageSteps(final ScenarioIdentifier scenarioIdentifier) {
		if (TestData.SCENARIO_IDENTIFIER_VALID.equals(scenarioIdentifier)) {
			return TestData.SCENARIO;
		} else if (TestData.SCENARIO_IDENTIFIER_VALID_2.equals(scenarioIdentifier)) {
			return TestData.SCENARIO_FALLBACK_IN_SAME_USECASE;
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
		if (!TestData.BUILD_IDENTIFIER_VALID.equals(buildIdentifier)) {
			return null;
		}
		
		if ("page".equals(objectType) && TestData.PAGE_NAME_VALID_1.equals(objectName)) {
			return TestData.OBJECT_INDEX_FOR_PAGE_1;
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
	public LongObjectNamesResolver loadLongObjectNamesIndex(final String branchName, final String buildName) {
		return null;
	}
	
	@Override
	public StepNavigation loadStepNavigation(final BuildIdentifier build, final StepLink step) {
		return null;
	}
	
	@Override
	public StepNavigation loadStepNavigation(final BuildIdentifier build, final String useCaseName,
			final String scenarioName, final int stepIndex) {
		return null;
	}
	
}
