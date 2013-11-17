package org.scenarioo.uitest.dummy.application.steps;

import static org.scenarioo.uitest.dummy.application.DummySimulationConfig.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.scenarioo.model.docu.entities.generic.ObjectDescription;
import org.scenarioo.model.docu.entities.generic.ObjectTreeNode;
import org.scenarioo.uitest.dummy.application.ApplicationsStateData;
import org.scenarioo.uitest.dummy.application.DummySimulationConfig;
import org.scenarioo.uitest.dummy.application.steps.calls.Action;
import org.scenarioo.uitest.dummy.application.steps.calls.Business;
import org.scenarioo.uitest.dummy.application.steps.calls.Service;


/**
 * Creates the example data to simulate a running Wikipedia application for some dummy UI test examples.
 * 
 * In a real application you would not simulate this.
 */
public class DummyApplicationStepDataFactory {
	
	private List<DummyApplicationStepData> steps = new ArrayList<DummyApplicationStepData>();
	
	private String screenshotPrefix = "screenshot_";
	
	private DummySimulationConfig config;
	
	private String startBrowserUrl;
	
	private int index = 0;
	
	private String browserUrl;
	
	private String pageName = null;
	
	private Map<String, String> elementTexts = new HashMap<String, String>();
	
	private ObjectTreeNode<ObjectDescription> callTree;
	
	private List<ObjectTreeNode<ObjectDescription>> callTreePathUnderConstruction;
	
	public static List<DummyApplicationStepData> createDummyStepData() {
		DummyApplicationStepDataFactory factory = new DummyApplicationStepDataFactory();
		return factory.createDummySteps();
	}
	
	public List<DummyApplicationStepData> createDummySteps() {
		
		// create DIRECT_SEARCH_CONFIG step data:
		startConfig(DIRECT_SEARCH_CONFIG).startUrl("http://www.wikipedia.org");
		title("Wikipedia Suche").pageName("startSearch.jsp").callTreeStart();
		createStep("startPage");
		createStep("startPageTypedFcBasel");
		title("FC Basel").pageName("contentPage.jsp").url("http://en.wikipedia.org/wiki/FC_Basel").callTreeFindPage();
		createStep("pageFcBasel");
		
		// create DEFAULT_CONFIG step data:
		startConfig(DEFAULT_CONFIG).startUrl("http://www.wikipedia.org");
		title("Wikipedia Suche").pageName("startSearch.jsp").callTreeStart();
		createStep("startPage");
		createStep("startPageTypedBestBandInTheWorld");
		title("Search results").pageName("searchResults.jsp")
				.url("http://en.wikipedia.org/wiki/Special:Search?search=yourSearchText&go=Go").callTreeFindPage();
		createStep("searchResultsMultiple");
		createStep("pageU2");
		
		// create AMBIGUOTIES_CONFIG step data:
		startConfig(AMBIGUOTIES_CONFIG).startUrl("http://www.wikipedia.org");
		title("Wikipedia Suche").pageName("startSearch.jsp").callTreeStart();
		createStep("startPage");
		createStep("startPageTyped42");
		title("42").pageName("contentPage.jsp").url("http://en.wikipedia.org/wiki/42").callTreeFindPage();
		createStep("page42WithAmbiguoties");
		title("42 (number)").pageName("contentPage.jsp").url("http://en.wikipedia.org/wiki/42_%28number%29")
				.callTreeFindPage();
		createStep("page42Number");
		title("Phrases from The Hitchhiker's Guide to the Galaxy")
				.pageName("contentPage.jsp")
				.url("http://en.wikipedia.org/wiki/Phrases_from_The_Hitchhiker%27s_Guide_to_the_Galaxy#Answer_to_the_Ultimate_Question_of_Life.2C_the_Universe.2C_and_Everything_.2842.29")
				.callTreeFindPage();
		createStep("page42AnswerToUltimateQuestion");
		
		// create SEARCH_NOT_FOUND_CONFIG step data:
		startConfig(SEARCH_NOT_FOUND_CONFIG).startUrl("http://www.wikipedia.org");
		title("Wikipedia Suche").pageName("startSearch.jsp").callTreeStart();
		createStep("startPage");
		createStep("startPageTypedScenarioo");
		title("Search results").pageName("searchResults.jsp").url("http://en.wikipedia.org/wiki/42").callTreeFindPage();
		createStep("searchResultsNone");
		
		return steps;
		
	}
	
	private void callTreeStart() {
		callTree();
		call(Action.START_INIT).call(Business.SESSION_INIT).call(Service.AUTHENTICATION_CHECK)
				.call(Business.MENU_LOAD).call(Service.MENU);
		call(Action.SEARCH_INIT);
	}
	
	private void callTreeFindPage() {
		callTree();
		call(Action.SEARCH_PROCESS).call(Business.SEARCH).call(Service.SEARCH);
		call(Action.PAGE_SHOW_CONTENT_INIT).call(Business.PAGE_GET).call(Service.CONTENT_LOAD);
	}
	
	private DummyApplicationStepDataFactory callTree() {
		callTreePathUnderConstruction = new LinkedList<ObjectTreeNode<ObjectDescription>>();
		ObjectDescription httpRequest = new ObjectDescription("httpCall", browserUrl);
		callTree = addCallTreeNode(httpRequest);
		return this;
	}
	
	private DummyApplicationStepDataFactory call(final Action action) {
		addCallTreeNode(action.getObject());
		return this;
	}
	
	private DummyApplicationStepDataFactory call(final Business ejb) {
		addCallTreeNode(ejb.getObject());
		return this;
	}
	
	private DummyApplicationStepDataFactory call(final Service service) {
		addCallTreeNode(service.getObject());
		return this;
	}
	
	private ObjectTreeNode<ObjectDescription> addCallTreeNode(final ObjectDescription object) {
		// Search parent node where to insert object of this type.
		int index = 0;
		ObjectTreeNode<ObjectDescription> parent = null;
		for (ObjectTreeNode<ObjectDescription> node : callTreePathUnderConstruction) {
			if (node.getItem().getType().equals(object.getType())) {
				callTreePathUnderConstruction = callTreePathUnderConstruction.subList(0, index);
				break;
			}
			else {
				parent = node;
				index++;
			}
		}
		
		// Insert object
		ObjectTreeNode<ObjectDescription> newNode = new ObjectTreeNode<ObjectDescription>(object);
		if (parent != null) {
			parent.addChild(newNode);
		}
		callTreePathUnderConstruction.add(newNode);
		return newNode;
	}
	
	private DummyApplicationStepDataFactory startConfig(final DummySimulationConfig config) {
		this.config = config;
		return this;
	}
	
	private DummyApplicationStepDataFactory startUrl(final String url) {
		index = 0;
		startBrowserUrl = url;
		browserUrl = url;
		return this;
	}
	
	private DummyApplicationStepDataFactory title(final String title) {
		elementTexts.put("pagetitle", title);
		return this;
	}
	
	private DummyApplicationStepDataFactory url(final String url) {
		this.browserUrl = url;
		return this;
	}
	
	private DummyApplicationStepDataFactory pageName(final String pageName) {
		this.pageName = pageName;
		return this;
	}
	
	private void createStep(final String stepName) {
		DummyApplicationStepData step = new DummyApplicationStepData();
		step.setBrowserUrl(browserUrl);
		step.setElementTexts(elementTexts);
		step.setIndex(index);
		step.setScreenshotFileName(screenshotPrefix + stepName + ".png");
		step.setSimulationConfig(config);
		step.setStartBrowserUrl(startBrowserUrl);
		step.setApplicationStateData(createApplicationsStateData());
		steps.add(step);
		index++;
	}
	
	private ApplicationsStateData createApplicationsStateData() {
		ApplicationsStateData stateData = new ApplicationsStateData();
		stateData.setCurrentSimulationConfiguration(config.getObjectDescription());
		stateData.setPageName(pageName);
		stateData.setCallTree(callTree);
		callTree = null; // only use it once.
		return stateData;
	}
	
}
