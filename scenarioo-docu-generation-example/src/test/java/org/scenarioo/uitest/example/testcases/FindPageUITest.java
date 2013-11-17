package org.scenarioo.uitest.example.testcases;

import org.junit.Test;
import org.scenarioo.uitest.dummy.application.DummyApplicationSimulator;
import org.scenarioo.uitest.dummy.application.DummySimulationConfig;
import org.scenarioo.uitest.example.infrastructure.DocuDescription;
import org.scenarioo.uitest.example.infrastructure.ScenarioDocuWritingRule;
import org.scenarioo.uitest.example.infrastructure.UITest;
import org.scenarioo.uitest.example.infrastructure.UITestToolkitAbstraction;
import org.scenarioo.uitest.example.infrastructure.UseCaseDocuWritingRule;


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
public class FindPageUITest extends UITest {
	
	@Test
	@DocuDescription(description = "User enters some text and finds multiple pages that contain this text.")
	public void find_page_with_text_on_page_from_multiple_results() {
		DummyApplicationSimulator.setConfiguration(DummySimulationConfig.DEFAULT_CONFIG);
		toolkit.loadUrl("http://www.wikipedia.org");
		toolkit.enterText("searchField", "best band in the world");
		toolkit.clickButton("searchButton");
		toolkit.assertElementPresent("searchResultList");
		toolkit.assertTextPresent("U2");
		toolkit.clickLink("U2");
	}
	
	@Test
	@DocuDescription(description = "User enters exact page title and finds it directly.")
	public void find_page_with_title_direct() {
		DummyApplicationSimulator.setConfiguration(DummySimulationConfig.DIRECT_SEARCH_CONFIG);
		toolkit.loadUrl("http://www.wikipedia.org");
		toolkit.enterText("searchField", "FC Basel");
		toolkit.clickButton("searchButton");
	}
	
	@Test
	@DocuDescription(
			description = "User enters page title that is ambiguous but matches directly a page, on the page he sees the list of other meanings, and can navigate to the page he meant.")
	public void find_page_with_title_ambiguous_navigate_to_other_meaning() {
		DummyApplicationSimulator.setConfiguration(DummySimulationConfig.AMBIGUOTIES_CONFIG);
		toolkit.loadUrl("http://www.wikipedia.org");
		toolkit.enterText("searchField", "42");
		toolkit.clickButton("searchButton");
		toolkit.clickLink("????");
	}
	
	@Test
	public void find_page_no_result() {
		DummyApplicationSimulator.setConfiguration(DummySimulationConfig.SEARCH_NOT_FOUND_CONFIG);
		toolkit.loadUrl("http://www.wikipedia.org");
		toolkit.enterText("searchField", "Scenarioo");
		toolkit.clickButton("searchButton");
		toolkit.assertElementNotPresent("searchResultList");
		toolkit.assertTextPresent("There where no results");
		toolkit.assertTextPresent("Did you mean: scenario");
	}
	
}
