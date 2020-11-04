package org.scenarioo.uitest.example.infrastructure;


import org.apache.log4j.Logger;
import org.scenarioo.api.ScenarioDocuWriter;
import org.scenarioo.model.docu.entities.Build;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.scenarioo.uitest.example.config.ExampleUITestDocuGenerationConfig.SCENARIOO_DATA_DIRECTORY;

/**
 * Workaround for Gradle 5+ and JUnit 4:
 * Gradle 5+ does not execute tests with the same name multiple times, thus we cannot use the Rules mechanism.
 * Creating a TestSuite for each BuildRun did result in sufficiently "different" tests so that Gradle executed all of them.
 */
public class MultipleBuildsConfiguration {

	private static final Logger LOGGER = Logger.getLogger(MultipleBuildsConfiguration.class);

	private static BuildRun currentBuildRun;

	private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	public static void initBuildRun(BuildRun buildRun) {
		currentBuildRun = buildRun;
		LOGGER.info("Build " + currentBuildRun.getDate());
		write_build_description();
	}


	private static void write_build_description() {
		ScenarioDocuWriter docuWriter = new ScenarioDocuWriter(SCENARIOO_DATA_DIRECTORY, getCurrentBranchName(), getCurrentBuildName());
		Build build = new Build();
		build.setName(getCurrentBuildName());
		build.setDate(getDate());
		build.setRevision(getCurrentBuildRun().getRevision());
		build.setStatus(getCurrentBuildRun().getStatus());
		docuWriter.saveBuildDescription(build);
	}

	private static Date getDate() {
		try {
			return new SimpleDateFormat("yyyy-MM-dd").parse(getCurrentBuildRun().getDate());
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static BuildRun getCurrentBuildRun() {
		return currentBuildRun;
	}

	public static String getCurrentBranchName() {
		return currentBuildRun.getBranchName();
	}

	public static String getCurrentBuildName() {
		return currentBuildRun.getDate();
	}
}
