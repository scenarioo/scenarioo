package ngusd.api;

import java.io.File;
import java.io.FileOutputStream;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.HashMap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import ngusd.model.docu.entities.Branch;
import ngusd.model.docu.entities.Build;
import ngusd.model.docu.entities.Scenario;
import ngusd.model.docu.entities.Step;
import ngusd.model.docu.entities.UseCase;

/**
 * Generator to produce documentation files for a specific build.
 */
public class ScenarioDocuGenerator {
	
	private static NumberFormat THREE_DIGIT_NUM_FORMAT = createNumberFormatWithMinimumIntegerDigits(3);
	
	private File destinationRootDirectory;
	
	private String branchName;
	
	private String buildName;
	
	/**
	 * Initialize with directory inside which to generate the documentation contents.
	 * 
	 * @param destinationDirectory
	 *            the directory where the content should be generated.
	 * @param branchName
	 *            name of the branch we are generating content for
	 * @param buildName
	 *            name of the build (concrete identifier like revision and date) for which we are generating content.
	 */
	public ScenarioDocuGenerator(final File destinationRootDirectory, final String branchName, final String buildName) {
		this.destinationRootDirectory = destinationRootDirectory;
		this.branchName = branchName;
		this.buildName = buildName;
	}
	
	/**
	 * Save the branch description to appropriate directory
	 * 
	 * @param branch
	 *            the branch description to write.
	 */
	public void saveBranchDescription(final Branch branch) {
		createBuildDirectoryIfNotYetExists();
		File destBranchFile = new File(getBranchDirectory(), "branch.xml");
		marshal(branch, destBranchFile, Branch.class);
	}
	
	/**
	 * Save the build description to appropriate directory
	 * 
	 * @param build
	 *            the build description to write
	 */
	public void saveBuildDescription(final Build build) {
		createBuildDirectoryIfNotYetExists();
		File destBuildFile = new File(getBuildDirectory(), "build.xml");
		marshal(build, destBuildFile, Build.class);
	}
	
	/**
	 * Save the use case description to appropriate directory and file
	 * 
	 * @param useCase
	 *            the use case description to write
	 */
	public void saveUseCase(final UseCase useCase) {
		File destCaseDir = getUseCaseDirectory(useCase.getName());
		createDirectoryIfNotYetExists(destCaseDir);
		File destCaseFile = new File(destCaseDir, "usecase.xml");
		marshal(useCase, destCaseFile, UseCase.class);
	}
	
	public void saveScenario(final UseCase useCase, final Scenario scenario) {
		saveScenario(useCase.getName(), scenario);
	}
	
	public void saveScenario(final String useCaseName, final Scenario scenario) {
		File destScenarioDir = getScenarioDirectory(useCaseName, scenario.getName());
		createDirectoryIfNotYetExists(destScenarioDir);
		File destScenarioFile = new File(destScenarioDir, "scenario.xml");
		marshal(scenario, destScenarioFile, Scenario.class);
	}
	
	public void saveScenarioStep(final UseCase useCase, final Scenario scenario, final Step step) {
		saveScenarioStep(useCase.getName(), scenario.getName(), step);
	}
	
	public void saveScenarioStep(final String useCaseName, final String scenarioName, final Step step) {
		File destStepsDir = getScenarioStepsDirectory(useCaseName, scenarioName);
		createDirectoryIfNotYetExists(destStepsDir);
		File destStepFile = new File(destStepsDir,
				THREE_DIGIT_NUM_FORMAT.format(step.getStep().getIndex()) + ".xml");
		marshal(step, destStepFile, Step.class);
	}
	
	private File getBuildDirectory() {
		File buildDirectory = new File(getBranchDirectory(), buildName);
		return buildDirectory;
	}
	
	private File getBranchDirectory() {
		File branchDirectory = new File(destinationRootDirectory, branchName);
		return branchDirectory;
	}
	
	private File getUseCaseDirectory(final String useCaseName) {
		File branchDirectory = new File(getBuildDirectory(), useCaseName);
		return branchDirectory;
	}
	
	private File getScenarioDirectory(final String useCaseName, final String scenarioName) {
		File branchDirectory = new File(getUseCaseDirectory(useCaseName), scenarioName);
		return branchDirectory;
	}
	
	private File getScenarioStepsDirectory(final String useCaseName, final String scenarioName) {
		File branchDirectory = new File(getScenarioDirectory(useCaseName, scenarioName), "steps");
		return branchDirectory;
	}
	
	/**
	 * Get the driectory to store screenshots for a given scenario inside. Webtests have to store their screenshots for
	 * a scenario into this directory.
	 */
	public File getScenarioScreenshotsDirectory(final String useCaseName, final String scenarioName) {
		return new File(getScenarioDirectory(useCaseName, scenarioName), "screenshots");
	}
	
	private void createBuildDirectoryIfNotYetExists() {
		createDirectoryIfNotYetExists(getBuildDirectory());
	}
	
	private void createDirectoryIfNotYetExists(final File directory) {
		if (!destinationRootDirectory.exists()) {
			throw new IllegalArgumentException("Directory for docu content generation does not exist: "
					+ destinationRootDirectory.getAbsolutePath());
		}
		if (!directory.exists()) {
			directory.mkdirs();
		}
	}
	
	private static void marshal(final Object object,
			final File destFile, Class<?>... classesToBind) {
		JAXBContext contextObj;
		try {
			classesToBind = appendClass(classesToBind, HashMap.class);
			contextObj = JAXBContext.newInstance(classesToBind);
			
			Marshaller marshallerObj = contextObj.createMarshaller();
			marshallerObj.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			marshallerObj.marshal(object, new FileOutputStream(destFile));
		} catch (Exception e) {
			throw new RuntimeException("Could not marshall Object of type " + object.getClass().getName()
					+ " into file: " + destFile.getAbsolutePath(), e);
		}
		
	}
	
	private static Class<?>[] appendClass(Class<?>[] classesToBind, final Class<?> additionalClass) {
		classesToBind = Arrays.copyOf(classesToBind, classesToBind.length + 1);
		classesToBind[classesToBind.length - 1] = additionalClass;
		return classesToBind;
	}
	
	private static NumberFormat createNumberFormatWithMinimumIntegerDigits(
			final int minimumIntegerDigits) {
		final NumberFormat numberFormat = NumberFormat.getIntegerInstance();
		numberFormat.setMinimumIntegerDigits(minimumIntegerDigits);
		return numberFormat;
	}
	
}
