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

package org.scenarioo.rest.objectRepository;

import org.scenarioo.business.builds.ScenarioDocuBuildsManager;
import org.scenarioo.dao.aggregates.AggregatedDocuDataReader;
import org.scenarioo.dao.aggregates.ScenarioDocuAggregationDao;
import org.scenarioo.model.docu.aggregates.objects.LongObjectNamesResolver;
import org.scenarioo.model.docu.aggregates.objects.ObjectIndex;
import org.scenarioo.model.docu.entities.generic.ObjectList;
import org.scenarioo.model.docu.entities.generic.ObjectReference;
import org.scenarioo.model.docu.entities.generic.ObjectTreeNode;
import org.scenarioo.repository.ConfigurationRepository;
import org.scenarioo.repository.RepositoryLocator;
import org.scenarioo.rest.base.AbstractBuildContentResource;
import org.scenarioo.rest.base.BuildIdentifier;
import org.scenarioo.rest.base.StepIdentifier;
import org.scenarioo.rest.step.logic.ScenarioLoader;
import org.scenarioo.rest.step.logic.StepIndexResolver;
import org.scenarioo.rest.step.logic.StepLoader;
import org.scenarioo.rest.step.logic.StepLoaderResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Resource for getting the URLs for screenshots for a certain object
 */
@RestController
@RequestMapping("/rest/branch/{branchName}/build/{buildName}/object/")
public class ObjectStepResource extends AbstractBuildContentResource {

	private final ConfigurationRepository configurationRepository = RepositoryLocator.INSTANCE
		.getConfigurationRepository();

	private final LongObjectNamesResolver longObjectNamesResolver = new LongObjectNamesResolver();

	private final AggregatedDocuDataReader aggregatedDataReader = new ScenarioDocuAggregationDao(
		configurationRepository.getDocumentationDataDirectory(), longObjectNamesResolver);

	private final ScenarioLoader scenarioLoader = new ScenarioLoader(aggregatedDataReader);
	private final StepIndexResolver stepIndexResolver = new StepIndexResolver();
	private final StepLoader stepLoader = new StepLoader(scenarioLoader, stepIndexResolver);

	/**
	 * Get references to all steps, that contain a specific object.
	 *
	 * @param branchName branch to search in
	 * @param buildName  build to searhc in
	 * @param objectType type of the object
	 * @param objectName name of the object
	 * @return a flat list of step reference objects
	 */
	@GetMapping("{type}/{objectName}/steps")
	public List<StepReference> getStepReferences(@PathVariable("branchName") final String branchName,
												 @PathVariable("buildName") final String buildName,
												 @PathVariable("type") final String objectType,
												 @PathVariable("objectName") final String objectName) {

		List<StepLoaderResult> stepLoaderResults = getRelatedSteps(branchName, buildName, objectType, objectName);
		return transformStepsToStepReferences(stepLoaderResults);
	}

	private List<StepLoaderResult> getRelatedSteps(String branchName, String buildName, String objectType, String objectName) {
		BuildIdentifier buildIdentifier = ScenarioDocuBuildsManager.INSTANCE.resolveBranchAndBuildAliases(branchName,
			buildName);

		ObjectIndex objectIndex = aggregatedDataReader.loadObjectIndex(buildIdentifier, objectType, objectName);
		List<StepLoaderResult> stepLoaderResults = new ObjectList<>();
		for (ObjectTreeNode<Object> useCaseTreeNode : objectIndex.getReferenceTree().getChildren()) {
			visitUseCase(buildIdentifier, stepLoaderResults, useCaseTreeNode);
		}
		return stepLoaderResults;
	}

	private void visitUseCase(BuildIdentifier buildIdentifier, List<StepLoaderResult> stepLoaderResults, ObjectTreeNode<Object> useCaseTreeNode) {
		if (useCaseTreeNode.getItem() instanceof ObjectReference) {
			ObjectReference useCaseItem = (ObjectReference) useCaseTreeNode.getItem();
			if (useCaseItem.getType().equals("usecase")) {
				for (ObjectTreeNode<Object> scenarioTreeNode : useCaseTreeNode.getChildren()) {
					visitScenario(buildIdentifier, stepLoaderResults, useCaseItem, scenarioTreeNode);
				}
			}
		}
	}

	private void visitScenario(BuildIdentifier buildIdentifier, List<StepLoaderResult> stepLoaderResults, ObjectReference useCaseItem, ObjectTreeNode<Object> scenarioTreeNode) {
		if (scenarioTreeNode.getItem() instanceof ObjectReference) {
			ObjectReference scenarioTreeNodeItem = (ObjectReference) scenarioTreeNode.getItem();
			if (scenarioTreeNodeItem.getType().equals("scenario")) {
				for (ObjectTreeNode<Object> stepTreeNode : scenarioTreeNode.getChildren()) {
					visitStep(buildIdentifier, stepLoaderResults, useCaseItem, scenarioTreeNodeItem, stepTreeNode);
				}
			}
		}
	}

	private void visitStep(BuildIdentifier buildIdentifier, List<StepLoaderResult> stepLoaderResults, ObjectReference useCaseItem, ObjectReference scenarioTreeNodeItem, ObjectTreeNode<Object> stepTreeNode) {
		if (stepTreeNode.getItem() instanceof ObjectReference) {
			ObjectReference stepItem = (ObjectReference) stepTreeNode.getItem();
			if (stepItem.getType().equals("step")) {
				String[] split = stepItem.getName().split(Pattern.quote("/"));
				StepIdentifier stepIdentifier = new StepIdentifier(buildIdentifier, useCaseItem.getName(),
					scenarioTreeNodeItem.getName(), split[0],
					Integer.parseInt(split[1]), Integer.parseInt(split[2]));
				StepLoaderResult stepLoaderResult = stepLoader.loadStep(stepIdentifier);
				stepLoaderResults.add(stepLoaderResult);
			}
		}
	}

	private List<StepReference> transformStepsToStepReferences(List<StepLoaderResult> stepLoaderResults) {
		ArrayList<StepReference> stepDetailUrls = new ArrayList<>();
		for (StepLoaderResult stepLoaderResult : stepLoaderResults) {
			String stepDetailUrl = generateStepDetailLink(stepLoaderResult);
			String screenshotUrl = generateScreenshotLink(stepLoaderResult);
			stepDetailUrls.add(new StepReference(stepDetailUrl, screenshotUrl));
		}
		return stepDetailUrls;
	}

	private String generateScreenshotLink(StepLoaderResult stepLoaderResult) {
		return String.format("rest/branch/%s/build/%s/usecase/%s/scenario/%s/image/%s",
			stepLoaderResult.getStepIdentifier().getBranchName(),
			stepLoaderResult.getStepIdentifier().getBuildName(),
			stepLoaderResult.getStepIdentifier().getUsecaseName(),
			stepLoaderResult.getStepIdentifier().getScenarioName(),
			stepLoaderResult.getScreenshotFileName());
	}

	private String generateStepDetailLink(StepLoaderResult stepLoaderResult) {
		return String.format("rest/branch/%s/build/%s/usecase/%s/scenario/%s/pageName/%s/pageOccurrence/%d/stepInPageOccurrence/%d",
			stepLoaderResult.getStepIdentifier().getBranchName(),
			stepLoaderResult.getStepIdentifier().getBuildName(),
			stepLoaderResult.getStepIdentifier().getUsecaseName(),
			stepLoaderResult.getStepIdentifier().getScenarioName(),
			stepLoaderResult.getStepIdentifier().getPageName(),
			stepLoaderResult.getStepIdentifier().getPageOccurrence(),
			stepLoaderResult.getStepIdentifier().getStepInPageOccurrence());
	}
}
