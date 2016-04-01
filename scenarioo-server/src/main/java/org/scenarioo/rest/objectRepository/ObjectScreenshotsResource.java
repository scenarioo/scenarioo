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

import org.codehaus.jackson.map.ObjectMapper;
import org.scenarioo.business.builds.ScenarioDocuBuildsManager;
import org.scenarioo.dao.aggregates.AggregatedDocuDataReader;
import org.scenarioo.dao.aggregates.ScenarioDocuAggregationDAO;
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

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Resource for getting the URLs for screenshots for a certain object
 */
@Path("/rest/branch/{branchName}/build/{buildName}/screens/{type}")
public class ObjectScreenshotsResource extends AbstractBuildContentResource {

    private final ConfigurationRepository configurationRepository = RepositoryLocator.INSTANCE
            .getConfigurationRepository();

    private final LongObjectNamesResolver longObjectNamesResolver = new LongObjectNamesResolver();

    private final AggregatedDocuDataReader aggregatedDataReader = new ScenarioDocuAggregationDAO(
            configurationRepository.getDocumentationDataDirectory(), longObjectNamesResolver);

    private final ScenarioLoader scenarioLoader = new ScenarioLoader(aggregatedDataReader);
    private final StepIndexResolver stepIndexResolver = new StepIndexResolver();
    private final StepLoader stepLoader = new StepLoader(scenarioLoader, stepIndexResolver);

    @GET
    @Produces({"application/json" })
    @Path("{name}/")
    public List<String> readStepDetails(@PathParam("branchName") final String branchName,
                                             @PathParam("buildName") final String buildName,
                                             @PathParam("type") final String objectType,
                                             @PathParam("name") final String objectName) throws IOException {

        BuildIdentifier buildIdentifier = ScenarioDocuBuildsManager.INSTANCE.resolveBranchAndBuildAliases(branchName,
                buildName);

        ObjectIndex objectIndex = aggregatedDataReader.loadObjectIndex(buildIdentifier, objectType, objectName);
        List<StepLoaderResult> stepLoaderResults = new ObjectList<StepLoaderResult>();
        for (ObjectTreeNode<Object> useCaseTreeNode : objectIndex.getReferenceTree().getChildren()) {
            visitUseCase(buildIdentifier, stepLoaderResults, useCaseTreeNode);
        }
        return asResponse(stepLoaderResults);
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

    private List<String> asResponse(List<StepLoaderResult> stepLoaderResults) throws IOException {
        ArrayList<String> screenshotUrls = new ArrayList<String>();
        for (StepLoaderResult stepLoaderResult : stepLoaderResults) {
            String screenshotUrl = String.format("rest/branch/%s/build/%s/usecase/%s/scenario/%s/image/%s",
                    stepLoaderResult.getStepIdentifier().getBranchName(),
                    stepLoaderResult.getStepIdentifier().getBuildName(),
                    stepLoaderResult.getStepIdentifier().getUsecaseName(),
                    stepLoaderResult.getStepIdentifier().getScenarioName(),
                    stepLoaderResult.getScreenshotFileName());
            screenshotUrls.add(screenshotUrl);
        }
        return screenshotUrls;
    }


}
