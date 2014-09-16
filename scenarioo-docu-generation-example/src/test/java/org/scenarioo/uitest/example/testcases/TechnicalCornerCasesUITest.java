package org.scenarioo.uitest.example.testcases;

import org.junit.Test;
import org.scenarioo.uitest.dummy.application.DummyApplicationSimulator;
import org.scenarioo.uitest.dummy.application.DummySimulationConfig;
import org.scenarioo.uitest.example.infrastructure.DocuDescription;
import org.scenarioo.uitest.example.infrastructure.Labels;
import org.scenarioo.uitest.example.infrastructure.UITest;

/**
 * Just some additional technical dummy test scenarios to test what happens in the webapplication, when there are
 * scenarios with unusal data (like no steps, no pages, only one step, or pages with only one variant etc.)
 * 
 * If you look for a good Use-Case-Test-Example better look at {@link FindPageUITest}.
 */
@DocuDescription(
		name = "Technical Corner Cases",
		description = "Just some meaningless dummy scenarios for testing some corner cases in the Scenarioo web application, like what happens when there are no pages or a page has only one variant in all scenarios etc.")
@Labels("corner-case")
public class TechnicalCornerCasesUITest extends UITest {
	
	@Test
	@DocuDescription(description = "Dummy scenario with no steps at all.")
	public void dummy_scenario_with_no_steps() {
		// dummy empty test scenario
	}
	
	@Test
	@DocuDescription(
			description = "Dummy scenario with one step and no other page variants of this same step in other scenarios.")
	@Labels({ "short" })
	public void dummy_scenario_with_one_step_and_one_page_with_no_other_variants() {
		DummyApplicationSimulator.setConfiguration(DummySimulationConfig.TECHNICAL_ONE_PAGE_CONFIG);
		toolkit.loadUrl("http://www.wikipedia.org/technical-one-page-scenario");
	}
	
	@Test
	@DocuDescription(
			description = "Dummy scenario with no page names set for all pages, which should be presented in Scenarioo as if the steps are all for different (unknown) pages.")
	@Labels({ "rare" })
	public void dummy_scenario_with_no_page_names_set() {
		DummyApplicationSimulator.setConfiguration(DummySimulationConfig.TECHNICAL_NO_PAGE_NAMES_CONFIG);
		toolkit.loadUrl("http://www.wikipedia.org/technical-multiple-pages-scenario");
		toolkit.clickButton("go to page 2");
		toolkit.clickButton("go to page 3");
	}
	
}
