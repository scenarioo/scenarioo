package org.scenarioo.rest.step.logic;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

public class LabelsQueryParamParser {
	
	public Set<String> parseLabels(final String labelsQueryParam) {
		if (StringUtils.isBlank(labelsQueryParam)) {
			return null;
		}
		
		String[] labels = labelsQueryParam.split(",");
		
		return new HashSet<String>(Arrays.asList(labels));
	}
	
}
