package org.scenarioo.business.aggregator;

import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.scenarioo.model.docu.entities.Page;
import org.scenarioo.model.docu.entities.Step;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @see PageNameSanitizer
 */
class PageNameSanitizerTest {
	
	private static final String ILLEGAL = "/illegal\\";
	private static final String SANITIZED = "_illegal_";
	
	@Test
	void givenNullInput_sanitizingPageNames_doesNotThrowAnException() {
		PageNameSanitizer.sanitizePageName(null);
		PageNameSanitizer.sanitizePageNames(null);
	}
	
	@Test
	void givenStepWithNoPage_sanitizingPageNames_doesNotThrowAnException() {
		PageNameSanitizer.sanitizePageName(getStepWithoutPageName());
		PageNameSanitizer.sanitizePageNames(getListOfStepsWithoutPageName());
	}
	
	@Test
	void givenStepWithIllegalPageName_sanitizingPageNames_replacesIllegalCharacters() {
		Step step = getStepWithIllegalPageName();
		
		PageNameSanitizer.sanitizePageName(step);
		
		assertEquals(SANITIZED, step.getPage().getName());
	}
	
	@Test
	void givenStepListWithIllegalPageNames_sanitizingPageNames_replacesIllegalCharacters() {
		List<Step> steps = getStepsWithIllegalPageName();
		
		PageNameSanitizer.sanitizePageNames(steps);
		
		assertEquals(SANITIZED, steps.get(0).getPage().getName());
		assertEquals(SANITIZED, steps.get(1).getPage().getName());
	}
	
	private List<Step> getStepsWithIllegalPageName() {
		List<Step> list = new LinkedList<>();
		list.add(getStepWithIllegalPageName());
		list.add(getStepWithIllegalPageName());
		return list;
	}
	
	private Step getStepWithIllegalPageName() {
		Step step = new Step();
		step.setPage(getPageWithIllegalPageName());
		return step;
	}
	
	private Page getPageWithIllegalPageName() {
		Page page = new Page();
		page.setName(ILLEGAL);
		return page;
	}
	
	private List<Step> getListOfStepsWithoutPageName() {
		List<Step> list = new LinkedList<>();
		list.add(getStepWithoutPageName());
		list.add(getStepWithoutPageName());
		return list;
	}
	
	private Step getStepWithoutPageName() {
		Step step = new Step();
		step.setPage(null);
		return step;
	}
	
}
