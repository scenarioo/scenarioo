package ngusd.model.docu.entities;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;

@Data
public class ScenarioCalculatedData {
	
	private int numberOfPages;
	private int numberOfSteps;
	
	/**
	 * This map enlists all occurences of a page, but only the first step and
	 * not any directly following.
	 */
	private Map<String, PageVariants> mapPageToOccurences = new HashMap<String, PageVariants>();
	/** This map enlists all steps of a page */
	private Map<String, PageVariants> mapPageToSteps = new HashMap<String, PageVariants>();
}
