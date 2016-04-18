package org.scenarioo.dao.aggregates;

import java.io.File;

import org.scenarioo.api.util.xml.ScenarioDocuXMLFileUtil;
import org.scenarioo.business.lastSuccessfulScenarios.LastSuccessfulScenariosBuildUpdater;
import org.scenarioo.model.lastSuccessfulScenarios.LastSuccessfulScenariosIndex;
import org.scenarioo.utils.UrlEncoding;

public class LastSuccessfulScenariosIndexDao {
	
	/**
	 * Name of the index file used to track the last successful scenarios with their build dates.
	 */
	public static final String LAST_SUCCESSFUL_SCENARIOS_INDEX_FILENAME = "lastSuccessfulScenariosIndex.derived";
	
	private final File lastSuccessfulScenariosBuildIndexFile;
	
	public LastSuccessfulScenariosIndexDao(final File dataDirectory, final String branchName) {
		lastSuccessfulScenariosBuildIndexFile = new File(dataDirectory, UrlEncoding.encode(branchName) + "/"
				+ UrlEncoding.encode(LastSuccessfulScenariosBuildUpdater.LAST_SUCCESSFUL_SCENARIO_BUILD_NAME) + "/"
				+ UrlEncoding.encode(LAST_SUCCESSFUL_SCENARIOS_INDEX_FILENAME));
	}
	
	/**
	 * Reads the last successful scenarios index file if it exists or otherwise returns an empty index object.
	 */
	public static LastSuccessfulScenariosIndex loadLastSuccessfulScenariosIndex(final File dataDirectory,
			final String branchName) {
		LastSuccessfulScenariosIndexDao dao = new LastSuccessfulScenariosIndexDao(dataDirectory, branchName);
		return dao.loadLastSuccessfulScenariosIndex();
	}
	
	/**
	 * Reads the last successful scenarios index file if it exists or otherwise returns an empty index object.
	 */
	public LastSuccessfulScenariosIndex loadLastSuccessfulScenariosIndex() {
		if (lastSuccessfulScenariosBuildIndexFile.exists()) {
			return ScenarioDocuXMLFileUtil.unmarshal(LastSuccessfulScenariosIndex.class,
					lastSuccessfulScenariosBuildIndexFile);
		}
		return new LastSuccessfulScenariosIndex();
	}
	
	public void saveLastSuccessfulScenariosIndex(final LastSuccessfulScenariosIndex index) {
		ScenarioDocuXMLFileUtil.marshal(index, lastSuccessfulScenariosBuildIndexFile);
	}
	
}
