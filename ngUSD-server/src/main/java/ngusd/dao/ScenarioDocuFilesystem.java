package ngusd.dao;

import java.io.File;
import java.util.List;

import ngusd.docu.model.Branch;
import ngusd.docu.model.Build;
import ngusd.docu.model.Scenario;
import ngusd.docu.model.UseCase;

public class ScenarioDocuFilesystem {
	
	public static File DOCU_ROOT_DIR = new File("/home/ngUSD/webtestDocuContentExample");
	
	public File rootDir;
	
	public ScenarioDocuFilesystem() {
		// TODO read this from configuration file instead.
		this.rootDir = DOCU_ROOT_DIR;
	}
	
	public List<Branch> getBranches() {
		return XMLFileUtil.unmarshallListOfFilesFromSubdirs(rootDir, "branch.xml", Branch.class);
	}
	
	public Branch getBranch(final String branchName) {
		File file = filePath(branchName, "branch.xml");
		return XMLFileUtil.unmarshal(file, Branch.class);
	}
	
	public List<Build> getBuilds(final String branchName) {
		File dir = filePath(branchName);
		return XMLFileUtil.unmarshallListOfFilesFromSubdirs(dir, "build.xml", Build.class);
	}
	
	public Build getBuild(final String branchName, final String buildName) {
		File file = filePath(branchName, buildName, "build.xml");
		return XMLFileUtil.unmarshal(file, Build.class);
	}
	
	public List<UseCase> getUsecases(final String branchName, final String buildName) {
		File dir = filePath(branchName, buildName);
		return XMLFileUtil.unmarshallListOfFilesFromSubdirs(dir, "usecase.xml", UseCase.class);
	}
	
	public UseCase getUsecase(final String branchName, final String buildName, final String usecaseName) {
		File file = filePath(branchName, buildName, usecaseName, "usecase.xml");
		return XMLFileUtil.unmarshal(file, UseCase.class);
	}
	
	public List<Scenario> getScenarios(final String branchName, final String buildName, final String usecaseName) {
		File dir = filePath(branchName, buildName, usecaseName);
		return XMLFileUtil.unmarshallListOfFilesFromSubdirs(dir, "scenario.xml", Scenario.class);
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
	
}
