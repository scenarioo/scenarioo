package org.scenarioo.dao.search;

import org.junit.Test;
import org.scenarioo.dao.search.dao.SearchDao;
import org.scenarioo.dao.search.dao.StepSearchDao;
import org.scenarioo.model.docu.aggregates.steps.StepLink;
import org.scenarioo.model.docu.entities.*;
import org.scenarioo.model.docu.entities.generic.ObjectReference;
import org.scenarioo.model.docu.entities.generic.ObjectTreeNode;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class SearchTreeTest {

	@Test
	public void emptyTree() {
		SearchTree searchTree = SearchTree.empty();

		ObjectTreeNode<ObjectReference> objectTree = searchTree.buildObjectTree();

		assertEquals(0, objectTree.getChildren().size());
	}

	@Test
	public void treeWithASingleStep() {
		SearchTree searchTree = givenSearchTreeWithSingleStep();

		ObjectTreeNode<ObjectReference> objectTree = searchTree.buildObjectTree();

		thenHasNodes(objectTree, "Use Case 1", "Scenario 1", "Page 1", "Page 1/3/2");
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

		List<SearchDao> searchResults = new ArrayList<SearchDao>();
		searchResults.add(new StepSearchDao(step, stepLink, scenario, usecase));

		return new SearchTree(searchResults, "");
	}

	private void thenHasNodes(ObjectTreeNode<ObjectReference> objectTree, String useCase, String scenario, String page, String step) {
		assertEquals(1, objectTree.getChildren().size());

		ObjectTreeNode<Object> useCaseNode = objectTree.getChildren().get(0);
		assertEquals(useCase, ((ObjectReference) useCaseNode.getItem()).getName());
		assertEquals(1, useCaseNode.getChildren().size());

		ObjectTreeNode<Object> scenarioNode = useCaseNode.getChildren().get(0);
		assertEquals(scenario, ((ObjectReference) scenarioNode.getItem()).getName());
		assertEquals(1, scenarioNode.getChildren().size());

		ObjectTreeNode<Object> pageNode = scenarioNode.getChildren().get(0);
		assertEquals(page, ((ObjectReference) pageNode.getItem()).getName());
		assertEquals(1, pageNode.getChildren().size());

		ObjectTreeNode<Object> stepNode = pageNode.getChildren().get(0);
		assertEquals(step, ((ObjectReference) stepNode.getItem()).getName());
	}

}
