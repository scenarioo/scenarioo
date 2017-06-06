package org.scenarioo.dao.aggregates;

import java.io.File;
import java.util.List;

import org.scenarioo.business.builds.BuildLink;
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

/**
 * Contains all the read operation that can be done on the aggregated documentation data.
 */
public interface AggregatedDocuDataReader {

	public abstract String loadVersion(final BuildIdentifier buildIdentifier);

	public abstract List<FeatureScenarios> loadFeatureScenariosList(final BuildIdentifier buildIdentifier);

	public abstract FeatureScenarios loadFeatureScenarios(final BuildIdentifier buildIdentifier,
														  final String featureName);

	public abstract ScenarioPageSteps loadScenarioPageSteps(final ScenarioIdentifier scenarioIdentifier);

	public abstract ObjectDescription loadObjectDescription(final BuildIdentifier buildIdentifier,
			final ObjectReference objectRef);

	public abstract ObjectDescription loadObjectDescription(final File file);

	/**
	 * @param resolvedObjectName
	 *            Object name, if too long, shortened using {@link LongObjectNamesResolver}
	 */
	public abstract ObjectIndex loadObjectIndex(final BuildIdentifier buildIdentifier, final String objectType,
			final String objectName);

	public abstract ObjectList<ObjectDescription> loadObjectsList(final BuildIdentifier buildIdentifier,
			final String type);

	public abstract CustomObjectTabTree loadCustomObjectTabTree(final BuildIdentifier buildIdentifier,
			final String tabId);

	public abstract ObjectIndex loadObjectIndexIfExistant(final BuildIdentifier buildIdentifier,
			final String objectType, final String objectName);

	public abstract List<BuildImportSummary> loadBuildImportSummaries();

	public abstract LongObjectNamesResolver loadLongObjectNamesIndex(final BuildIdentifier buildIdentifier);

	public abstract StepNavigation loadStepNavigation(final BuildIdentifier build, final StepLink step);

	public abstract StepNavigation loadStepNavigation(final ScenarioIdentifier scenarioIdentifier, final int stepIndex);

	public abstract List<BuildLink> loadBuildLinks(final String branchName);

}
