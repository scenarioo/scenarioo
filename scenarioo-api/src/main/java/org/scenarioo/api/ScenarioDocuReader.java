package org.scenarioo.api;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.scenarioo.api.files.ScenarioDocuFiles;
import org.scenarioo.api.util.files.ObjectFromDirectory;
import org.scenarioo.api.util.files.XMLFileUtil;
import org.scenarioo.model.docu.derived.BuildLink;
import org.scenarioo.model.docu.entities.Branch;
import org.scenarioo.model.docu.entities.Build;
import org.scenarioo.model.docu.entities.Scenario;
import org.scenarioo.model.docu.entities.Step;
import org.scenarioo.model.docu.entities.UseCase;


/**
 * Gives access to the geenrated scenario docu files in the filesystem.
 */
public class ScenarioDocuReader {
	
	private ScenarioDocuFiles docuFiles;
	
	public ScenarioDocuReader(final File rootDirectory) {
		this.docuFiles = new ScenarioDocuFiles(rootDirectory);
	}
	
	public Branch loadBranch(final String branchName) {
		File file = docuFiles.getBranchFile(branchName);
		return XMLFileUtil.unmarshal(file, Branch.class);
	}
	
	public List<Branch> loadBranches() {
		List<File> branchFiles = docuFiles.getBranchFiles();
		return XMLFileUtil.unmarshalListOfFiles(branchFiles, Branch.class);
	}
	
	public Build loadBuild(final String branchName, final String buildName) {
		File file = docuFiles.getBuildFile(branchName, buildName);
		return XMLFileUtil.unmarshal(file, Build.class);
	}
	
	public List<BuildLink> loadBuilds(final String branchName) {
		List<File> buildFiles = docuFiles.getBuildFiles(branchName);
		List<BuildLink> result = new ArrayList<BuildLink>();
		for (ObjectFromDirectory<Build> build : XMLFileUtil.unmarshalListOfFilesWithDirNames(buildFiles, Build.class)) {
			BuildLink link = new BuildLink(build.getObject(), build.getDirectoryName());
			result.add(link);
		}
		return result;
	}
	
	public List<UseCase> loadUsecases(final String branchName, final String buildName) {
		List<File> files = docuFiles.getUseCaseFiles(branchName, buildName);
		return XMLFileUtil.unmarshalListOfFiles(files, UseCase.class);
	}
	
	public UseCase loadUsecase(final String branchName, final String buildName, final String useCaseName) {
		File file = docuFiles.getUseCaseFile(branchName, buildName, useCaseName);
		return XMLFileUtil.unmarshal(file, UseCase.class);
	}
	
	public List<Scenario> loadScenarios(final String branchName, final String buildName, final String useCaseName) {
		List<File> files = docuFiles.getScenarioFiles(branchName, buildName, useCaseName);
		return XMLFileUtil.unmarshalListOfFiles(files, Scenario.class);
	}
	
	public Scenario loadScenario(final String branchName, final String buildName, final String useCaseName,
			final String scenarioName) {
		File file = docuFiles.getScenarioFile(branchName, buildName, useCaseName, scenarioName);
		return XMLFileUtil.unmarshal(file, Scenario.class);
	}
	
	public List<Step> loadSteps(final String branchName, final String buildName, final String useCaseName,
			final String scenarioName) {
		List<File> files = docuFiles.getStepFiles(branchName, buildName, useCaseName, scenarioName);
		return XMLFileUtil.unmarshalListOfFiles(files, Step.class);
	}
	
	public Step loadStep(final String branchName, final String buildName, final String useCaseName,
			final String scenarioName, final int stepIndex) {
		File file = docuFiles.getStepFile(branchName, buildName, useCaseName, scenarioName, stepIndex);
		return XMLFileUtil.unmarshal(file, Step.class);
	}
	
	/**
	 * Screenshot files are simply provided by path, the REST service will take care of streaming it.
	 */
	public File getScreenshotFile(final String branchName, final String buildName, final String useCaseName,
			final String scenarioName, final String imageName) {
		return new File(docuFiles.getScreenshotsDirectory(branchName, buildName, useCaseName, scenarioName),
				imageName);
	}
}
