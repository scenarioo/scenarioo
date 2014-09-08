package org.scenarioo.rest.step.logic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.scenarioo.model.docu.aggregates.objects.ObjectIndex;
import org.scenarioo.model.docu.aggregates.scenarios.PageSteps;
import org.scenarioo.model.docu.aggregates.scenarios.ScenarioPageSteps;
import org.scenarioo.model.docu.entities.Page;
import org.scenarioo.model.docu.entities.StepDescription;
import org.scenarioo.model.docu.entities.generic.ObjectDescription;
import org.scenarioo.model.docu.entities.generic.ObjectList;
import org.scenarioo.model.docu.entities.generic.ObjectReference;
import org.scenarioo.model.docu.entities.generic.ObjectTreeNode;
import org.scenarioo.rest.base.BuildIdentifier;
import org.scenarioo.rest.base.ScenarioIdentifier;
import org.scenarioo.rest.base.StepIdentifier;

public class StepTestData {
	
	public static final String LABEL_USECASE = "security";
	public static final String LABEL_SCENARIO_1 = "80 percent path";
	public static final String LABEL_SCENARIO_2 = "mobile device";
	public static final String LABEL_STEP = "validation step";
	
	public static final Set<String> LABELS_FOR_STEP_IDENTIFIER = createLabelsForStepIdentifier();
	
	public static final String TYPE_PAGE = "page";
	public static final String TYPE_USECASE = "usecase";
	public static final String TYPE_SCENARIO = "scenario";
	public static final String TYPE_STEP = "step";
	
	// branch and build
	public static final String BRANCH_NAME_VALID = "bugfix-branch";
	public static final String BUILD_NAME_VALID = "build-2014-08-12";
	public static final BuildIdentifier BUILD_IDENTIFIER_VALID = new BuildIdentifier(BRANCH_NAME_VALID,
			BUILD_NAME_VALID);
	
	// usecase and scenario
	public static final String USECASE_NAME_VALID = "Find the answer";
	public static final String USECASE_NAME_VALID_WITH_MATCHING_LABELS = "Login";
	public static final String USECASE_NAME_INEXISTENT = "Some inexistent usecase";
	public static final String SCENARIO_NAME_VALID = "Actually find it";
	public static final String SCENARIO_NAME_VALID_2 = "search for ever and ever";
	public static final String SCENARIO_NAME_VALID_WITH_MATCHING_LABELS = "Scenario with matching labels";
	public static final String SCENARIO_NAME_INEXISTENT = "scenarioDoesNotExist";
	public static final ScenarioIdentifier SCENARIO_IDENTIFIER_VALID = new ScenarioIdentifier(BUILD_IDENTIFIER_VALID,
			USECASE_NAME_VALID, SCENARIO_NAME_VALID);
	public static final ScenarioIdentifier SCENARIO_IDENTIFIER_VALID_2 = new ScenarioIdentifier(BUILD_IDENTIFIER_VALID,
			USECASE_NAME_VALID, SCENARIO_NAME_VALID_2);
	public static final ScenarioIdentifier SCENARIO_IDENTIFIER_INEXISTENT_SCENARIO = new ScenarioIdentifier(
			BUILD_IDENTIFIER_VALID, USECASE_NAME_VALID, SCENARIO_NAME_INEXISTENT);
	public static final ScenarioIdentifier SCENARIO_IDENTIFIER_INEXISTENT_USECASE = new ScenarioIdentifier(
			BUILD_IDENTIFIER_VALID, USECASE_NAME_INEXISTENT, SCENARIO_NAME_VALID);
	
	// page / pageOccurrence / stepInPageOccurrence
	public static final String PAGE_NAME_VALID_1 = "pageName1";
	public static final String PAGE_NAME_VALID_2 = "pageName2";
	public static final String PAGE_NAME_NON_EXISTENT = "pageName3";
	public static final StepIdentifier STEP_IDENTIFIER_VALID = new StepIdentifier(SCENARIO_IDENTIFIER_VALID,
			PAGE_NAME_VALID_1, 0, 0);
	public static final StepIdentifier STEP_IDENTIFIER_INEXISTENT_SCENARIO = new StepIdentifier(
			SCENARIO_IDENTIFIER_INEXISTENT_SCENARIO, PAGE_NAME_VALID_1, 0, 0);
	public static final StepIdentifier STEP_IDENTIFIER_INEXISTENT_SCENARIO_WITH_LABELS = new StepIdentifier(
			SCENARIO_IDENTIFIER_INEXISTENT_SCENARIO, PAGE_NAME_VALID_1, 0, 1, LABELS_FOR_STEP_IDENTIFIER);
	
	public static final StepIdentifier STEP_IDENTIFIER_INEXISTENT_USECASE = new StepIdentifier(
			SCENARIO_IDENTIFIER_INEXISTENT_USECASE, PAGE_NAME_VALID_1, 0, 0);
	public static final StepIdentifier STEP_IDENTIFIER_INEXISTENT_USECASE_WITH_LABELS = new StepIdentifier(
			SCENARIO_IDENTIFIER_INEXISTENT_USECASE, PAGE_NAME_VALID_1, 23, 443, LABELS_FOR_STEP_IDENTIFIER);
	
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
	
	private static Set<String> createLabelsForStepIdentifier() {
		Set<String> labels = new HashSet<String>();
		labels.add(LABEL_USECASE);
		labels.add(LABEL_SCENARIO_1);
		labels.add(LABEL_SCENARIO_2);
		labels.add(LABEL_STEP);
		return labels;
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
		stepDescription.getLabels().addLabel("step-label-" + index);
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
		node.setItem(objectReference(PAGE_NAME_VALID_1, TYPE_PAGE));
		node.addChild(createUseCaseNode());
		node.addChild(createUseCaseNodeWithMatchingLabel());
		return node;
	}
	
	private static ObjectTreeNode<Object> createUseCaseNode() {
		ObjectTreeNode<Object> node = new ObjectTreeNode<Object>();
		node.setItem(objectReference(TYPE_USECASE, USECASE_NAME_VALID));
		node.addChild(createScenarioNodeWithDifferentScenario());
		node.addChild(createScenarioNodeWithMatchingLabels());
		return node;
	}
	
	private static ObjectTreeNode<?> createUseCaseNodeWithMatchingLabel() {
		ObjectTreeNode<Object> node = new ObjectTreeNode<Object>();
		node.setItem(objectReference(TYPE_USECASE, USECASE_NAME_VALID_WITH_MATCHING_LABELS));
		node.addChild(createScenarioNodeWithMatchingLabels());
		node.getDetails().addDetail("labels", createLabelsForObjectIndex(LABEL_USECASE));
		return node;
	}
	
	private static ObjectTreeNode<Object> createScenarioNodeWithDifferentScenario() {
		ObjectTreeNode<Object> node = new ObjectTreeNode<Object>();
		node.setItem(objectReference(TYPE_SCENARIO, SCENARIO_NAME_VALID_2));
		node.addChild(createStepNode());
		return node;
	}
	
	private static ObjectTreeNode<Object> createScenarioNodeWithMatchingLabels() {
		ObjectTreeNode<Object> node = new ObjectTreeNode<Object>();
		node.setItem(objectReference(TYPE_SCENARIO, SCENARIO_NAME_VALID_WITH_MATCHING_LABELS));
		node.addChild(createStepNode());
		node.addChild(createStepNodeWithLabels());
		node.getDetails().addDetail("labels", createLabelsForObjectIndex(LABEL_SCENARIO_1, LABEL_SCENARIO_2));
		return node;
	}
	
	private static ObjectList<String> createLabelsForObjectIndex(final String... labels) {
		ObjectList<String> objectList = new ObjectList<String>();
		objectList.addAll(Arrays.asList(labels));
		return objectList;
	}
	
	private static ObjectTreeNode<Object> createStepNode() {
		ObjectTreeNode<Object> node = new ObjectTreeNode<Object>();
		node.setItem(objectReference(TYPE_STEP, createStepId(STEP_IDENTIFIER_VALID)));
		node.addChild(createPageLeafNode());
		return node;
	}
	
	private static ObjectTreeNode<Object> createStepNodeWithLabels() {
		ObjectTreeNode<Object> node = new ObjectTreeNode<Object>();
		node.setItem(objectReference(TYPE_STEP, createStepId(STEP_IDENTIFIER_INEXISTENT_SCENARIO_WITH_LABELS)));
		node.addChild(createPageLeafNode());
		node.getDetails().addDetail("labels", createLabelsForObjectIndex(LABEL_STEP));
		return node;
	}
	
	private static String createStepId(final StepIdentifier stepIdentifier) {
		return stepIdentifier.getPageName() + "/" + stepIdentifier.getPageOccurrence() + "/"
				+ stepIdentifier.getStepInPageOccurrence();
	}
	
	private static ObjectTreeNode<ObjectReference> createPageLeafNode() {
		ObjectTreeNode<ObjectReference> node = new ObjectTreeNode<ObjectReference>();
		node.setItem(objectReference(PAGE_NAME_VALID_1, TYPE_PAGE));
		return node;
	}
	
	private static ObjectReference objectReference(final String type, final String name) {
		ObjectReference scenario = new ObjectReference();
		scenario.setType(type);
		scenario.setName(name);
		return scenario;
	}
	
}
