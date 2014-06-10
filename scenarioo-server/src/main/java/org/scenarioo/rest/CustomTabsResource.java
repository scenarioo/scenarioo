package org.scenarioo.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.scenarioo.model.docu.aggregates.objects.CustomTabObjectTree;
import org.scenarioo.model.docu.entities.generic.ObjectReference;
import org.scenarioo.model.docu.entities.generic.ObjectTreeNode;
import org.scenarioo.rest.base.AbstractBuildContentResource;

@Path("/rest/branches/{branchName}/builds/{buildName}/customTabObjects/{tabId}")
public class CustomTabsResource extends AbstractBuildContentResource {

	@GET
	@Produces({ "application/xml", "application/json" })
	public CustomTabObjectTree readObjectTreeForTab(
			@PathParam("branchName") final String branchName,
			@PathParam("buildName") final String buildName,
			@PathParam("tabId") final String tabId) {

		// String resolvedBuildName = ScenarioDocuBuildsManager.INSTANCE
		// .resolveAliasBuildName(branchName, buildName);

		List<ObjectTreeNode<ObjectReference>> mockedResult = new ArrayList<ObjectTreeNode<ObjectReference>>();
		mockedResult.add(createSampleMockedFeatureTree("Feature", 1, 2));
		mockedResult.add(createSampleMockedFeatureTree("Feature", 2, 2));
		mockedResult.add(createSampleMockedFeatureTree("Feature", 3, 2));

		return new CustomTabObjectTree(mockedResult);
	}

	private ObjectTreeNode<ObjectReference> createSampleMockedFeatureTree(
			final String type, final int index, final int children) {
		ObjectTreeNode<ObjectReference> featureNode = new ObjectTreeNode<ObjectReference>(
				new ObjectReference(type, type + " " + index));
		featureNode.getDetails().addDetail("description",
				"Bla bla, just a description to test");

		for (int i = 0; i < children; i++) {
			String childType = "Story";
			if (type.equals("Feature")) {
				childType = "Epic";
			}

			featureNode.addChild(createSampleMockedFeatureTree(childType,
					i + 1, children - 1));
		}
		return featureNode;
	}

}
