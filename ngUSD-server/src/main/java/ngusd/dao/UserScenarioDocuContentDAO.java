package ngusd.dao;

import java.io.File;
import java.util.List;

import ngusd.aggregator.UserScenarioDocuAggregator;
import ngusd.dao.filesystem.UserScenarioDocuFilesystem;
import ngusd.dao.filesystem.XMLFileUtil;
import ngusd.model.docu.aggregates.scenarios.ScenarioPageSteps;
import ngusd.model.docu.aggregates.usecases.PageVariantsCounter;
import ngusd.model.docu.aggregates.usecases.UseCaseScenarios;
import ngusd.model.docu.aggregates.usecases.UseCaseScenariosList;

/**
 * DAO for accessing user scenario docu content from filesystem, that is either
 * generated or already precalculated.
 * 
 * The DAO should in general only access data by reading one file and should not
 * have to calculate additional data or read a lot of different files or even
 * strip unwanted data.
 * 
 * Data that is not available directly from a file should be precalculated in
 * {@link UserScenarioDocuAggregator} to make it easily available for DAO.
 */
public class UserScenarioDocuContentDAO {
	
	private final UserScenarioDocuFilesystem filesystem = new UserScenarioDocuFilesystem();
	
	public List<UseCaseScenarios> loadUseCaseScenariosList(final String branchName, final String buildName) {
		File file = filesystem.filePath(branchName, buildName, UserScenarioDocuAggregator.FILENAME_USECASES_XML);
		UseCaseScenariosList list = XMLFileUtil.unmarshal(file, UseCaseScenariosList.class);
		return list.getUseCaseScenarios();
	}
	
	public UseCaseScenarios loadUseCaseScenarios(final String branchName, final String buildName,
			final String usecaseName) {
		File scenariosFile = filesystem.filePath(branchName, buildName, usecaseName,
				UserScenarioDocuAggregator.FILENAME_SCENARIOS_XML);
		return XMLFileUtil.unmarshal(scenariosFile, UseCaseScenarios.class);
	}
	
	public ScenarioPageSteps loadScenarioPageSteps(final String branchName, final String buildName,
			final String usecaseName,
			final String scenarioName) {
		
		File file = filesystem.filePath(branchName, buildName, usecaseName,
				scenarioName, UserScenarioDocuAggregator.FILENAME_SCENARIO_PAGE_STEPS_XML);
		return XMLFileUtil.unmarshal(file, ScenarioPageSteps.class);
	}
	
	public PageVariantsCounter loadPageVariantsCounter(final String branchName, final String buildName) {
		
		File file = filesystem.filePath(branchName, buildName, UserScenarioDocuAggregator.FILENAME_PAGE_VARIANT_COUNTERS_XML);
		return XMLFileUtil.unmarshal(file, PageVariantsCounter.class);
	}
	
}
