package org.scenarioo.dao.search;

import org.junit.Test;
import org.scenarioo.dao.search.model.SearchableObject;
import org.scenarioo.dao.search.model.SearchResults;
import org.scenarioo.dao.search.model.SearchableStep;
import org.scenarioo.model.docu.aggregates.steps.StepLink;
import org.scenarioo.model.docu.entities.*;
import org.scenarioo.model.docu.entities.generic.ObjectReference;
import org.scenarioo.model.docu.entities.generic.ObjectTreeNode;
import org.scenarioo.rest.base.BuildIdentifier;
import org.scenarioo.rest.search.SearchRequest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class SearchTreeTest {

	@Test
	public void emptyTree() {
		SearchTree searchTree = SearchTree.empty();

		ObjectTreeNode<ObjectReference> objectTree = searchTree.getResults();

		assertEquals(0, objectTree.getChildren().size());
	}

	@Test
	public void treeWithASingleStep() {
		SearchTree searchTree = givenSearchTreeWithSingleStep();

		ObjectTreeNode<ObjectReference> objectTree = searchTree.getResults();

		thenHasNodes(objectTree, "Use Case 1", "Scenario 1", "Page 1/3/2");
	}

	private SearchTree givenSearchTreeWithSingleStep() {
		Page page = new Page();
		page.setName("Page 1");
		Step step = new Step();
		StepDescription stepDescription = new StepDescription();
		stepDescription.setTitle("Step 1");
		step.setStepDescription(stepDescription);
		step.setPage(page);

		StepLink stepLink = new StepLink();
		stepLink.setIndex(1);
		stepLink.setStepInPageOccurrence(2);
		stepLink.setPageOccurrence(3);

		Scenario scenario = new Scenario();
		scenario.setName("Scenario 1");

		UseCase usecase = new UseCase();
		usecase.setName("Use Case 1");

		List<SearchableObject> searchResults = new ArrayList<SearchableObject>();
		searchResults.add(new SearchableStep(step, stepLink, scenario, usecase));

		return new SearchTree(new SearchResults(searchResults, 4, 2), new SearchRequest(new BuildIdentifier(), "", false));
	}

	private void thenHasNodes(ObjectTreeNode<ObjectReference> objectTree, String useCase, String scenario, String step) {
		assertEquals(1, objectTree.getChildren().size());

		ObjectTreeNode<Object> useCaseNode = objectTree.getChildren().get(0);
		assertEquals(useCase, ((ObjectReference) useCaseNode.getItem()).getName());
		assertEquals(1, useCaseNode.getChildren().size());

		ObjectTreeNode<Object> scenarioNode = useCaseNode.getChildren().get(0);
		assertEquals(scenario, ((ObjectReference) scenarioNode.getItem()).getName());
		assertEquals(1, scenarioNode.getChildren().size());

		ObjectTreeNode<Object> stepNode = scenarioNode.getChildren().get(0);
		assertEquals(step, ((ObjectReference) stepNode.getItem()).getName());
	}

}
