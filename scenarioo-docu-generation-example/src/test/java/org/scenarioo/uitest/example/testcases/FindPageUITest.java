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

package org.scenarioo.uitest.example.testcases;

import org.junit.Test;
import org.scenarioo.uitest.dummy.application.DummyApplicationSimulator;
import org.scenarioo.uitest.dummy.application.DummySimulationConfig;
import org.scenarioo.uitest.example.infrastructure.DocuDescription;
import org.scenarioo.uitest.example.infrastructure.Labels;
import org.scenarioo.uitest.example.infrastructure.ScenarioDocuWritingRule;
import org.scenarioo.uitest.example.infrastructure.UITest;
import org.scenarioo.uitest.example.infrastructure.UITestToolkitAbstraction;
import org.scenarioo.uitest.example.infrastructure.UseCaseDocuWritingRule;
import org.scenarioo.uitest.example.issues.UserStories;

/**
 * An example of how to use the Scenarioo API to generate User Scenario Documentation from your UI tests.
 * 
 * The structure of the tests assumes that one test class corresponds to the use cases and one test case corresponds to
 * a scenario of this use cases, which is a good practice to follow for structuring your UI tests and documenting your
 * software by user scenarios.
 * 
 * The example also demonstrates how to hide the writing of the documentation inside your testing infrastructure, see
 * the base infrastructure testing classes on how the documentation writing is done: {@link UITest},
 * {@link UseCaseDocuWritingRule}, {@link ScenarioDocuWritingRule} and {@link UITestToolkitAbstraction}.
 * 
 * Hint for real implementation of your UI tests: In a real application we would recommend to use some additional
 * patterns to structure your probably more complex webtests: Instead of directly programming your tests against the
 * abstracted toolkit, we recommend to use the "PageObject" pattern and to introduce page components such that you can
 * assemble your page objects simply from some reusable page components (see also Composite pattern). We did not
 * implement these patterns for this simple example.
 */
@DocuDescription(name = "Find Page", description = "User wants to search for a page and read it.")
@Labels("normal-case")
public class FindPageUITest extends UITest {

	@Test
	@DocuDescription(description = "User enters some text and finds multiple pages that contain this text.")
	@UserStories({ 115 })
	@Labels({ "happy", "multiple results" })
	public void find_multiple_results() {
		DummyApplicationSimulator.setConfiguration(DummySimulationConfig.DEFAULT_CONFIG);
		toolkit.loadUrl("http://www.wikipedia.org");
		toolkit.enterText("searchField", "best band in the world");
		toolkit.clickButton("searchButton");
		toolkit.assertElementPresent("searchResultList");
		toolkit.assertTextPresent("U2");
		toolkit.clickLink("U2");
	}
	
	@Test
	@DocuDescription(description = "User enters exact unique page title and is navigated to the page directly.")
	@UserStories({ 116 })
	@Labels({ "exact match" })
	public void find_page_title_unique_directly() {
		DummyApplicationSimulator.setConfiguration(DummySimulationConfig.DIRECT_SEARCH_CONFIG);
		toolkit.loadUrl("http://www.wikipedia.org");
		toolkit.enterText("searchField", "FC Basel");
		toolkit.clickButton("searchButton");
		toolkit.assertTextPresent("Swiss football club");
	}
	
	@Test
	@DocuDescription(
			description = "User enters exact page title that has ambiguities, he is navigated to the most relevant page directly and sees the ambiguities on top of the page.")
	@UserStories({ 116, 119 })
	@Labels({ "exact match", "ambiguity" })
	public void find_page_title_ambiguous_directly() {
		DummyApplicationSimulator.setConfiguration(DummySimulationConfig.AMBIGUOTIES_CONFIG);
		toolkit.loadUrl("http://www.wikipedia.org");
		toolkit.enterText("searchField", "42");
		toolkit.clickButton("searchButton");
		toolkit.clickLink("42 (number)");
		toolkit.clickLink("42 - The answer to everything");
	}
	
	@Test
	@DocuDescription(description = "User enters text that is not found in pages content.")
	@UserStories({ 117 })
	@Labels({ "no results" })
	public void find_no_results() {
		DummyApplicationSimulator.setConfiguration(DummySimulationConfig.SEARCH_NOT_FOUND_CONFIG);
		toolkit.loadUrl("http://www.wikipedia.org");
		toolkit.enterText("searchField", "Scenarioo");
		toolkit.clickButton("searchButton");
		toolkit.assertElementNotPresent("searchResultList");
		toolkit.assertTextPresent("There where no results");
		toolkit.assertTextPresent("Did you mean: scenario");
	}
	
}
