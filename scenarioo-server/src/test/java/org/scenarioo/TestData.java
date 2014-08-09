package org.scenarioo;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.scenarioo.model.docu.aggregates.objects.ObjectIndex;
import org.scenarioo.model.docu.aggregates.scenarios.PageSteps;
import org.scenarioo.model.docu.aggregates.scenarios.ScenarioPageSteps;
import org.scenarioo.model.docu.entities.Page;
import org.scenarioo.model.docu.entities.StepDescription;
import org.scenarioo.model.docu.entities.generic.ObjectDescription;
import org.scenarioo.model.docu.entities.generic.ObjectReference;
import org.scenarioo.model.docu.entities.generic.ObjectTreeNode;
import org.scenarioo.rest.request.BuildIdentifier;
import org.scenarioo.rest.request.ScenarioIdentifier;
import org.scenarioo.rest.request.StepIdentifier;

public class TestData {
	
	public static final String TYPE_PAGE = "page";
	public static final String TYPE_USECASE = "usecase";
	public static final String TYPE_SCENARIO = "scenario";
	
	// branch and build
	public static final String BRANCH_NAME_VALID = "branch";
	public static final String BUILD_NAME_VALID = "build";
	public static final BuildIdentifier BUILD_IDENTIFIER_VALID = new BuildIdentifier(BRANCH_NAME_VALID,
			BUILD_NAME_VALID);
	
	// usecase and scenario
	public static final String USECASE_NAME_VALID = "Find the answer";
	public static final String SCENARIO_NAME_VALID = "Actually find it";
	public static final String SCENARIO_NAME_VALID_2 = "search for ever and ever";
	public static final String SCENARIO_NAME_INEXISTENT = "scenarioDoesNotExist";
	public static final ScenarioIdentifier SCENARIO_IDENTIFIER_VALID = new ScenarioIdentifier(BUILD_IDENTIFIER_VALID,
			USECASE_NAME_VALID, SCENARIO_NAME_VALID);
	public static final ScenarioIdentifier SCENARIO_IDENTIFIER_VALID_2 = new ScenarioIdentifier(BUILD_IDENTIFIER_VALID,
			USECASE_NAME_VALID, SCENARIO_NAME_VALID_2);
	public static final ScenarioIdentifier SCENARIO_IDENTIFIER_INEXISTENT_SCENARIO = new ScenarioIdentifier(
			BUILD_IDENTIFIER_VALID, USECASE_NAME_VALID, SCENARIO_NAME_INEXISTENT);
	
	// page / pageOccurrence / stepInPageOccurrence
	public static final String PAGE_NAME_VALID_1 = "pageName1";
	public static final String PAGE_NAME_VALID_2 = "pageName2";
	public static final String PAGE_NAME_NON_EXISTENT = "pageName3";
	public static final StepIdentifier STEP_IDENTIFIER_VALID = new StepIdentifier(SCENARIO_IDENTIFIER_VALID,
			PAGE_NAME_VALID_1, 0, 0);
	public static final StepIdentifier STEP_IDENTIFIER_INEXISTENT_SCENARIO = new StepIdentifier(
			SCENARIO_IDENTIFIER_INEXISTENT_SCENARIO, PAGE_NAME_VALID_1, 0, 0);
	
	// scenario data
	public static final ScenarioPageSteps SCENARIO = createScenarioPagesAndSteps();
	public static final ScenarioPageSteps SCENARIO_FALLBACK_IN_SAME_USECASE = createFallbackScenarioPagesAndSteps();
	
	// objects
	public static final ObjectIndex OBJECT_INDEX_FOR_PAGE_1 = createObjectIndexForPage1();
	
	private static ScenarioPageSteps createScenarioPagesAndSteps() {
		ScenarioPageSteps scenarioPageSteps = new ScenarioPageSteps();
		scenarioPageSteps.setPagesAndSteps(createPageSteps());
		return scenarioPageSteps;
	}
	
	private static ScenarioPageSteps createFallbackScenarioPagesAndSteps() {
		ScenarioPageSteps scenarioPageSteps = createScenarioPagesAndSteps();
		scenarioPageSteps.getPagesAndSteps().add(createPageSteps(PAGE_NAME_VALID_2, 2, 5));
		return scenarioPageSteps;
	}
	
	private static List<PageSteps> createPageSteps() {
		List<PageSteps> pageSteps = new LinkedList<PageSteps>();
		pageSteps.add(createPageSteps(PAGE_NAME_VALID_1, 1, 0));
		pageSteps.add(createPageSteps(PAGE_NAME_VALID_2, 1, 1));
		pageSteps.add(createPageSteps(PAGE_NAME_VALID_1, 3, 2));
		return pageSteps;
	}
	
	private static PageSteps createPageSteps(final String pageName, final int stepCount, final int startIndex) {
		PageSteps pageSteps = new PageSteps();
		pageSteps.setPage(createPage(pageName));
		pageSteps.setSteps(createSteps(stepCount, startIndex));
		return pageSteps;
	}
	
	private static Page createPage(final String pageName) {
		Page page = new Page();
		page.setName(pageName);
		return page;
	}
	
	private static List<StepDescription> createSteps(final int stepCount, final int startIndex) {
		List<StepDescription> stepDescriptions = new ArrayList<StepDescription>(stepCount);
		for (int index = startIndex; index < startIndex + stepCount; index++) {
			stepDescriptions.add(createStepDescription(index));
		}
		return stepDescriptions;
	}
	
	private static StepDescription createStepDescription(final int index) {
		StepDescription stepDescription = new StepDescription();
		stepDescription.setIndex(index);
		return stepDescription;
	}
	
	private static ObjectIndex createObjectIndexForPage1() {
		ObjectIndex objectIndex = new ObjectIndex();
		objectIndex.setObject(createObjectPage1());
		objectIndex.setReferenceTree(createReferenceTreePage1());
		return objectIndex;
	}
	
	private static ObjectDescription createObjectPage1() {
		ObjectDescription object = new ObjectDescription();
		object.setName(PAGE_NAME_VALID_1);
		object.setType(TYPE_PAGE);
		return object;
	}
	
	private static ObjectTreeNode<ObjectReference> createReferenceTreePage1() {
		ObjectTreeNode<ObjectReference> node = new ObjectTreeNode<ObjectReference>();
		node.setItem(createObjectReferencePage1());
		node.addChild(createUseCaseNode());
		return node;
	}
	
	private static ObjectReference createObjectReferencePage1() {
		ObjectReference objectReference = new ObjectReference();
		objectReference.setName(PAGE_NAME_VALID_1);
		objectReference.setType(TYPE_PAGE);
		return objectReference;
	}
	
	private static ObjectTreeNode<Object> createUseCaseNode() {
		ObjectTreeNode<Object> node = new ObjectTreeNode<Object>();
		node.setItem(createUseCaseObjectReference());
		node.addChild(createScenarioNodeWithDifferentScenario());
		return node;
	}
	
	private static ObjectReference createUseCaseObjectReference() {
		ObjectReference objectReference = new ObjectReference();
		objectReference.setType(TYPE_USECASE);
		objectReference.setName(USECASE_NAME_VALID);
		return objectReference;
	}
	
	private static ObjectTreeNode<Object> createScenarioNodeWithDifferentScenario() {
		ObjectTreeNode<Object> node = new ObjectTreeNode<Object>();
		node.setItem(createScenarioObjectReferenceWithDifferentScenario());
		return node;
	}
	
	private static Object createScenarioObjectReferenceWithDifferentScenario() {
		ObjectReference scenario = new ObjectReference();
		scenario.setType(TYPE_SCENARIO);
		scenario.setName(SCENARIO_NAME_VALID_2);
		return scenario;
	}
	
}
