/* Copyright (c) 2014, scenarioo.org Development Team
 * All rights reserved.
 *
 * See https://github.com/scenarioo?tab=members
 * for a complete list of contributors to this project.
 *
 * Redistribution and use of the Scenarioo Examples in source and binary forms,
 * with or without modification, are permitted provided that the following
 * conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright notice, this
 *   list of conditions and the following disclaimer in the documentation and/or
 *   other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.scenarioo.uitest.dummy.application.steps;

import static org.scenarioo.api.util.IdentifierSanitizer.*;
import static org.scenarioo.uitest.dummy.application.DummySimulationConfig.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.scenarioo.model.docu.entities.generic.ObjectDescription;
import org.scenarioo.model.docu.entities.generic.ObjectTreeNode;
import org.scenarioo.model.docu.entities.screenAnnotations.ScreenRegion;
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

	private final List<DummyApplicationStepData> steps = new ArrayList<>();

	private final String screenshotPrefix = "screenshot_";

	private DummySimulationConfig config;

	private String startBrowserUrl;

	private int index = 0;

	private String browserUrl;

	private String pageName = null;

	private final Map<String, String> elementTexts = new HashMap<>();

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
		title("FC Basel").pageName("contentPage.jsp").url("http://en.wikipedia.org/wiki/FC_Basel")
				.callTreeFindPageOneResult();
		createStep("pageFcBasel");

		// create DEFAULT_CONFIG step data:
		startConfig(DEFAULT_CONFIG).startUrl("http://www.wikipedia.org");
		title("Wikipedia Suche").pageName("startSearch.jsp").callTreeStart();
		createStep("startPage");
		createStep("startPageTypedBestBandInTheWorld");
		title("Search results").pageName("searchResults.jsp")
				.url("http://en.wikipedia.org/wiki/Special:Search?search=yourSearchText&go=Go").callTreeFindPage();
		createStep("searchResultsMultiple");
		title("U2").pageName("contentPage.jsp").url("http://en.wikipedia.org/wiki/42_%28number%29").callTreeLoadPage();
		createStep("pageU2");

		// create AMBIGUOTIES_CONFIG step data:
		startConfig(AMBIGUOTIES_CONFIG).startUrl("http://www.wikipedia.org");
		title("Wikipedia Suche").pageName("startSearch.jsp").callTreeStart();
		createStep("startPage");
		createStep("startPageTyped42");
		title("42").pageName("contentPage.jsp").url("http://en.wikipedia.org/wiki/42").callTreeFindPageOneResult();
		createStep("page42WithAmbiguoties");
		title("42 (number)").pageName("contentPage.jsp").url("http://en.wikipedia.org/wiki/42_%28number%29")
				.callTreeLoadPage();
		createStep("page42Number");
		title("Phrases from The Hitchhiker's Guide to the Galaxy")
				.pageName("contentPage.jsp")
				.url("http://en.wikipedia.org/wiki/Phrases_from_The_Hitchhiker%27s_Guide_to_the_Galaxy#Answer_to_the_Ultimate_Question_of_Life.2C_the_Universe.2C_and_Everything_.2842.29")
				.callTreeLoadPage();
		createStep("page42AnswerToUltimateQuestion");

		// create SEARCH_NOT_FOUND_CONFIG step data:
		startConfig(SEARCH_NOT_FOUND_CONFIG).startUrl("http://www.wikipedia.org");
		title("Wikipedia Suche").pageName("startSearch.jsp").callTreeStart();
		createStep("startPage");
		createStep("startPageTypedScenarioo");
		title("Search results").pageName("searchResults.jsp").url("http://en.wikipedia.org/wiki/42").callTreeFindPage();
		createStep("searchResultsNone");

		startConfig(SWITCH_LANGUAGE_CONFIG).startUrl("http://www.wikipedia.org");
		title("Wikipedia Search").pageName("startSearch.jsp").callTreeStart();
		createStep("startPage");
		createStep("startPageSelectedGermanTypedAngularJS");
		title("AngularJS German").pageName("contentPage.jsp").url("http://de.wikipedia.org/wiki/AngularJS");
		createStep("pageAngularJSGerman");
		title("AngularJS Spanish").pageName("contentPage.jsp").url("http://es.wikipedia.org/wiki/AngularJS");
		createStep("pageAngularJSSpanish");

		// create TECHNICAL_NO_PAGE_NAMES_CONFIG:
		startConfig(TECHNICAL_NO_PAGE_NAMES_CONFIG).startUrl(
				"http://www.wikipedia.org/technical-multiple-pages-scenario");
		title("Technical Page 1").pageName(null).callTreeStart();
		createStep("specialDummyPage1");
		title("Technical Page 2");
		createStep("specialDummyPage2");
		title("Technical Page 3").pageName(null).callTreeFindPage();
		createStep("specialDummyPage3");

		// create TECHNICAL_ONE_PAGE_CONFIG step data:
		startConfig(TECHNICAL_ONE_PAGE_CONFIG).startUrl("http://www.wikipedia.org/technical-one-page-scenario");
		title("Wikipedia Suche").pageName("specialPageWithOnlyOneVariant.jsp").callTreeStart();
		createSteps(4, "specialDummyPage");

		// create TECHNICAL_JPEG_IMAGE_CONFIG
		startConfig(TECHNICAL_JPEG_STEP_IMAGES_CONFIG).startUrl("http://www.wikipedia.org/jpeg-image-page");
		title("Wikipedia Suche").pageName("jpegPage.jsp").callTreeStart();
		createStep("jpegImage");

		// create TECHNICAL_PARENTHESES_SPACE_STEP_CONFIG
		startConfig(TECHNICAL_PARENTHESES_SPACE_STEP_CONFIG).startUrl("http://www.wikipedia.org/url-(with-parentheses)-and space");
		title("Technical Page with Parentheses and Space").pageName("url-(with-parentheses)-and space.jsp").callTreeStart();
		createStep("urlWithParenthesesAndSpace");

		// create TECHNICAL_PARENTHESES_STEP_CONFIG
		startConfig(TECHNICAL_ENCODED_SPACE_STEP_CONFIG).startUrl("http://www.wikipedia.org/url-with%2520encoded%20space");
		title("Technical Page with Encoded Space").pageName("url-with%2520encoded%20space.jsp").callTreeStart();
		createStep("urlWithEncodedSpace");

		startConfig(TECHNICAL_ENCODED_SPECIAL_CHARACTERS_STEP_CONFIG).startUrl("http://www.wikipedia.org/url-with-sp€c!al#ch@racters");
		title("Technical Page with Encoded Characters").pageName("url-with-sp€c!al#ch@racters.jsp").callTreeStart();
		createStep("specialDummyPage");

		return steps;

	}

	/**
	 * Define some elements with screen regions, to place screen annotations on for demonstration purposes.
	 */
	private Map<String, ScreenRegion> createElementRegions(final String stepName) {
		Map<String, ScreenRegion> elementRegions = new HashMap<>();

		// Define some default element positions for most used elements
		elementRegions.put("searchField", new ScreenRegion(382, 462, 164, 26));
		elementRegions.put("searchButton", new ScreenRegion(758, 462, 55, 28));
		elementRegions.put("linkWithText=U2", new ScreenRegion(200, 400, 26, 20));
		elementRegions.put("linkWithText=42 (number)", new ScreenRegion(530, 168, 72, 20));
		elementRegions.put("linkWithText=42 - The answer to everything", new ScreenRegion(472, 244, 410, 20));
		elementRegions.put("linkWithText=Espanol", new ScreenRegion(30, 755, 50, 14));
		elementRegions.put("linkWithText=dummy-next-link-not-visible-on-screenshot",
				new ScreenRegion(1050, 844, 120, 28));

		// Later here: some of this elements might be placed different for some special steps dependent on the passed
		// stepName

		return elementRegions;
	}

	private void callTreeStart() {
		callTree();
		call(Action.START_INIT).call(Business.SESSION_INIT).call(Service.AUTHENTICATION_CHECK).call(Business.MENU_LOAD)
				.call(Service.MENU);
		call(Action.SEARCH_INIT);
	}

	private void callTreeFindPageOneResult() {
		callTree();
		call(Action.SEARCH_PROCESS).call(Business.SEARCH).call(Service.SEARCH);
		call(Action.PAGE_SHOW_CONTENT_INIT).call(Business.PAGE_GET).call(Service.CONTENT_LOAD);
	}

	private void callTreeFindPage() {
		callTree();
		call(Action.SEARCH_PROCESS).call(Business.SEARCH).call(Service.SEARCH)
				.call(Service.SEARCH_ADVANCED_FOR_MULTIPLE_RESULTS_WITH_LONG_NAME_1)
				.call(Service.SEARCH_ADVANCED_FOR_MULTIPLE_RESULTS_WITH_LONG_NAME_2);
		call(Action.SEARCH_RESULTS_LIST_INIT);
		call(Action.PAGE_SHOW_CONTENT_INIT).call(Business.PAGE_GET).call(Service.CONTENT_LOAD);
	}

	private void callTreeLoadPage() {
		callTree();
		call(Action.PAGE_SHOW_CONTENT_INIT).call(Business.PAGE_GET).call(Service.CONTENT_LOAD);
	}

	private DummyApplicationStepDataFactory callTree() {
		callTreePathUnderConstruction = new LinkedList<>();
		ObjectDescription httpRequest = new ObjectDescription("httpCall", sanitize(browserUrl));
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
			} else {
				parent = node;
				index++;
			}
		}

		// Insert object
		ObjectTreeNode<ObjectDescription> newNode = new ObjectTreeNode<>(object);
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

	/**
	 * Helper method for producing dummy application data for multiple steps with same screenshot and metadata
	 */
	private void createSteps(final int numberOfSteps, final String stepNameForScreenshot) {
		for (int i = 0; i < numberOfSteps; i++) {
			createStep(stepNameForScreenshot);
		}
	}

	private void createStep(final String stepName) {
		DummyApplicationStepData step = new DummyApplicationStepData();
		step.setBrowserUrl(browserUrl);
		step.setElementTexts(new HashMap<>(elementTexts));
		step.setElementRegions(createElementRegions(stepName));
		step.setIndex(index);
		step.setScreenshotFileName(screenshotPrefix + stepName);
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
