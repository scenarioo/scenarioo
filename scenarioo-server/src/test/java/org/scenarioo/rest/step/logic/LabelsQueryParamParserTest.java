package org.scenarioo.rest.step.logic;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.scenarioo.rest.step.logic.LabelsQueryParamParser;

public class LabelsQueryParamParserTest {
	
	private final LabelsQueryParamParser labelsQueryParamParser = new LabelsQueryParamParser();
	String queryParams;
	Set<String> parsedLabelsString;
	
	@Test
	public void nullInputResultsInNullOutput() {
		queryParams = null;
		
		whenParsingQueryString();
		
		assertNull(parsedLabelsString);
	}
	
	@Test
	public void blankInputResultsInNullOutput() {
		queryParams = "\t    \n";
		
		whenParsingQueryString();
		
		assertNull(parsedLabelsString);
	}
	
	@Test
	public void singleLabelResultsInSetWithOneEntry() {
		queryParams = "main scenario";
		
		whenParsingQueryString();
		
		assertSetContainsExactly("main scenario");
	}
	
	@Test
	public void twoEqualLabelsResultInSetWithOneEntry() {
		queryParams = "main scenario,main scenario";
		
		whenParsingQueryString();
		
		assertSetContainsExactly("main scenario");
	}
	
	@Test
	public void twoDistinctLabelsResultInSetWithTwoEntries() {
		queryParams = "main scenario,special scenario";
		
		whenParsingQueryString();
		
		assertSetContainsExactly("main scenario", "special scenario");
	}
	
	private void whenParsingQueryString() {
		parsedLabelsString = labelsQueryParamParser.parseLabels(queryParams);
	}
	
	private void assertSetContainsExactly(final String... string) {
		Set<String> expectedSet = new HashSet<String>(Arrays.asList(string));
		
		assertEquals(expectedSet, parsedLabelsString);
	}
	
}
