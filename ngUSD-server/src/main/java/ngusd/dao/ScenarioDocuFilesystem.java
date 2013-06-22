package ngusd.dao;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import ngusd.docu.model.Branch;
import ngusd.docu.model.Build;
import ngusd.docu.model.Page;
import ngusd.docu.model.Scenario;
import ngusd.docu.model.Step;
import ngusd.docu.model.StepDescription;
import ngusd.docu.model.UseCase;
import ngusd.rest.model.BuildLink;
import ngusd.rest.model.PageSteps;

public class ScenarioDocuFilesystem {
	
	public static File DOCU_ROOT_DIR = new File("/home/ngUSD/webtestDocuContentExample");
	
	public File rootDir;
	
	public ScenarioDocuFilesystem() {
		// TODO read this from configuration file instead.
		this.rootDir = DOCU_ROOT_DIR;
	}
	
	public List<Branch> getBranches() {
		return XMLFileUtil.unmarshalListOfFilesFromSubdirs(rootDir, "branch.xml", Branch.class);
	}
	
	public Branch getBranch(final String branchName) {
		File file = filePath(branchName, "branch.xml");
		return XMLFileUtil.unmarshal(file, Branch.class);
	}
	
	public List<BuildLink> getBuilds(final String branchName) {
		File dir = filePath(branchName);
		List<BuildLink> result = new ArrayList<BuildLink>();
		for (ObjectFromDirectory<Build> build : XMLFileUtil.unmarshalListOfFilesFromSubdirsWithDirNames(dir,
				"build.xml", Build.class)) {
			BuildLink link = new BuildLink(build.getObject(), build.getDirectoryName());
			result.add(link);
		}
		return result;
	}
	
	public Build getBuild(final String branchName, final String buildName) {
		File file = filePath(branchName, buildName, "build.xml");
		return XMLFileUtil.unmarshal(file, Build.class);
	}
	
	public List<UseCase> getUsecases(final String branchName, final String buildName) {
		File dir = filePath(branchName, buildName);
		return XMLFileUtil.unmarshalListOfFilesFromSubdirs(dir, "usecase.xml", UseCase.class);
	}
	
	public UseCase getUsecase(final String branchName, final String buildName, final String usecaseName) {
		File file = filePath(branchName, buildName, usecaseName, "usecase.xml");
		return XMLFileUtil.unmarshal(file, UseCase.class);
	}
	
	public List<Scenario> getScenarios(final String branchName, final String buildName, final String usecaseName) {
		File dir = filePath(branchName, buildName, usecaseName);
		return XMLFileUtil.unmarshalListOfFilesFromSubdirs(dir, "scenario.xml", Scenario.class);
	}
	
	public Scenario getScenario(final String branchName, final String buildName, final String usecaseName,
			final String scenarioName) {
		File file = filePath(branchName, buildName, usecaseName, scenarioName, "scenario.xml");
		return XMLFileUtil.unmarshal(file, Scenario.class);
	}
	
	private File filePath(final String... names) {
		File file = rootDir;
		for (String name : names) {
			file = new File(file, name);
		}
		return file;
	}
	
	public List<PageSteps> readPageSteps(final String branchName, final String buildName, final String usecaseName,
			final String scenarioName) {
		File dir = filePath(branchName, buildName, usecaseName, scenarioName, "steps");
		List<Step> steps = XMLFileUtil.unmarshalListOfFiles(dir, Step.class);
		List<PageSteps> result = new ArrayList<PageSteps>();
		Page page = null;
		PageSteps pageSteps = null;
		for (Step step : steps) {
			if (page == null || step.getPage() == null || !page.equals(step.getPage())) {
				page = step.getPage();
				pageSteps = new PageSteps();
				pageSteps.setPage(page);
				pageSteps.setSteps(new ArrayList<StepDescription>());
				result.add(pageSteps);
			}
			pageSteps.getSteps().add(step.getStep());
		}
		return result;
	}
}
