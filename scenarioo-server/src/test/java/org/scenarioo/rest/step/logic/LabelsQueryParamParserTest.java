package org.scenarioo.rest.step.logic;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LabelsQueryParamParserTest {
	
	private final LabelsQueryParamParser labelsQueryParamParser = new LabelsQueryParamParser();
	private String queryParams;
	private Set<String> parsedLabelsString;
	
	@Test
	void nullInputResultsInNullOutput() {
		queryParams = null;
		
		whenParsingQueryString();
		
		assertNull(parsedLabelsString);
	}
	
	@Test
	void blankInputResultsInNullOutput() {
		queryParams = "\t    \n";
		
		whenParsingQueryString();
		
		assertNull(parsedLabelsString);
	}
	
	@Test
	void singleLabelResultsInSetWithOneEntry() {
		queryParams = "main scenario";
		
		whenParsingQueryString();
		
		assertSetContainsExactly("main scenario");
	}
	
	@Test
	void twoEqualLabelsResultInSetWithOneEntry() {
		queryParams = "main scenario,main scenario";
		
		whenParsingQueryString();
		
		assertSetContainsExactly("main scenario");
	}
	
	@Test
	void twoDistinctLabelsResultInSetWithTwoEntries() {
		queryParams = "main scenario,special scenario";
		
		whenParsingQueryString();
		
		assertSetContainsExactly("main scenario", "special scenario");
	}
	
	private void whenParsingQueryString() {
		parsedLabelsString = labelsQueryParamParser.parseLabels(queryParams);
	}
	
	private void assertSetContainsExactly(final String... string) {
		Set<String> expectedSet = new HashSet<>(Arrays.asList(string));
		
		assertEquals(expectedSet, parsedLabelsString);
	}
	
}
