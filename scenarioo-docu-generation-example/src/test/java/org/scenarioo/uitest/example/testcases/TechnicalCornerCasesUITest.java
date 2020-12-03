package org.scenarioo.uitest.example.testcases;

import org.junit.Test;
import org.scenarioo.model.docu.entities.screenAnnotations.ScreenAnnotationStyle;
import org.scenarioo.model.docu.entities.screenAnnotations.ScreenRegion;
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

	@Test
	@DocuDescription(
			description = "Dummy scenario with three steps on one page and all special variants of screen annotations and many labels on scenario level (with all different kind of breaking characters)")
	@Labels({ "short", "annotations", "label 1", "label-2", "label_3", "label4", "label 5", "label 6" })
	public void dummy_scenario_with_screen_annotations_of_all_types_on_one_page() {
		DummyApplicationSimulator.setConfiguration(DummySimulationConfig.TECHNICAL_ONE_PAGE_CONFIG);
		toolkit.loadUrl("http://www.wikipedia.org/technical-one-page-scenario");

		// Just produce all kind of possible screen annotations for testing
		toolkit.addScreenAnnotationOnSameStep("Life Is Beautiful", ScreenAnnotationStyle.DEFAULT, region(0, 0, 1, 1));
		toolkit.addScreenAnnotationOnSameStep("The Intouchables", ScreenAnnotationStyle.HIGHLIGHT, region(0, 1, 1, 1));
		toolkit.addScreenAnnotationOnSameStep("The Lord of The Rings", ScreenAnnotationStyle.INFO, region(0, 2, 1, 1));
		toolkit.addScreenAnnotationOnSameStep("The Matrix", ScreenAnnotationStyle.WARN, region(0, 3, 1, 1));
		toolkit.addScreenAnnotationOnSameStep("Titanic", ScreenAnnotationStyle.ERROR, region(0, 4, 1, 1));

		toolkit.addScreenAnnotationOnSameStep("High Fidelity", ScreenAnnotationStyle.EXPECTED, region(1, 0, 1, 1));
		toolkit.addScreenAnnotationOnSameStep("Good Will Hunting", ScreenAnnotationStyle.CLICK, region(1, 1, 1, 1));
		toolkit.addScreenAnnotationOnSameStep("Star Wars", ScreenAnnotationStyle.KEYBOARD, region(1, 2, 1, 1));
		toolkit.addScreenAnnotationOnSameStep("One Flew Over the Cuckoo's Nest", ScreenAnnotationStyle.NAVIGATE_TO_URL, region(1, 3, 1, 1));

		toolkit.addScreenAnnotationOnSameStep(
				"just a default annotation with very long information text that will wrap on multiple lines and still overflow the width of the box and therefore be cut off",
				null, region(0, 5, 2, 2));

		// link to next, produces the new steps (one before clicking, one after clicking)
		toolkit.clickLink("dummy-next-link-not-visible-on-screenshot");
	}

	@Test
	@DocuDescription(
		description = "Dummy scenario with one step that has a jpeg image.")
	@Labels({ "jpeg" })
	public void dummy_scenario_with_one_step_that_has_a_jpeg_image() {
		DummyApplicationSimulator.setConfiguration(DummySimulationConfig.TECHNICAL_JPEG_STEP_IMAGES_CONFIG);
		toolkit.loadUrl("http://www.wikipedia.org/jpeg-image-page");
	}

	@Test
	@DocuDescription(
		description = "Dummy scenario with one step with parentheses and a space in URL.")
	@Labels({ "encoding" })
	public void dummy_scenario_with_one_step_with_parentheses_and_space_in_url() {
		DummyApplicationSimulator.setConfiguration(DummySimulationConfig.TECHNICAL_PARENTHESES_SPACE_STEP_CONFIG);
		toolkit.loadUrl("http://www.wikipedia.org/url-(with-parentheses)-and space");
	}

	@Test
	@DocuDescription(
		description = "Dummy scenario with one step with an encoded space in URL.")
	@Labels({ "encoding" })
	public void dummy_scenario_with_one_step_with_an_encoded_space_in_url() {
		DummyApplicationSimulator.setConfiguration(DummySimulationConfig.TECHNICAL_ENCODED_SPACE_STEP_CONFIG);
		toolkit.loadUrl("http://www.wikipedia.org/url-with%2520encoded%20space");
	}

	@Test
	@DocuDescription(
		name = "T€stC@ase!d #9000",
		description = "Dummy scenario with one step with special characters in URL and scenario name")
	@Labels({ "encoding" })
	public void dummy_scenario_with_one_step_with_special_characters() {
		DummyApplicationSimulator.setConfiguration(DummySimulationConfig.TECHNICAL_ENCODED_SPECIAL_CHARACTERS_STEP_CONFIG);
		toolkit.loadUrl("http://www.wikipedia.org/url-with-sp€c!al#ch@racters");
	}



	private ScreenRegion region(final int colIndex, final int rowIndex, final int spanCols, final int spanRows) {
		int colWidth = 250;
		int rowHeight = 32;
		int margin = 64;
		return new ScreenRegion(
				colIndex * (colWidth + margin) + margin + 300,
				rowIndex * (rowHeight + margin) + margin + 300,
				spanCols * (colWidth + margin) - margin,
				spanRows * rowHeight);
	}

}
