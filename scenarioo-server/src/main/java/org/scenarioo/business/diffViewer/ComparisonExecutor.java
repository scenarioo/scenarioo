/* scenarioo-server
 * Copyright (C) 2014, scenarioo.org Development Team
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.scenarioo.business.diffViewer;

import org.apache.log4j.Logger;
import org.scenarioo.api.ScenarioDocuReader;
import org.scenarioo.api.files.ObjectFromDirectory;
import org.scenarioo.business.builds.ScenarioDocuBuildsManager;
import org.scenarioo.business.diffViewer.comparator.BuildComparator;
import org.scenarioo.business.diffViewer.comparator.ComparisonParameters;
import org.scenarioo.dao.diffViewer.DiffReader;
import org.scenarioo.dao.diffViewer.impl.DiffReaderXmlImpl;
import org.scenarioo.model.configuration.ComparisonConfiguration;
import org.scenarioo.model.diffViewer.BuildDiffInfo;
import org.scenarioo.model.diffViewer.ComparisonResult;
import org.scenarioo.model.docu.entities.Build;
import org.scenarioo.model.docu.entities.Status;
import org.scenarioo.repository.ConfigurationRepository;
import org.scenarioo.repository.RepositoryLocator;
import org.scenarioo.rest.base.BuildIdentifier;
import org.scenarioo.utils.ThreadLogAppender;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * Executes the comparisons for a base build. Each comparison is executed in a separate thread.
 */
public class ComparisonExecutor {

	private static Logger LOGGER = Logger.getLogger(ComparisonExecutor.class);

	private ConfigurationRepository configurationRepository = RepositoryLocator.INSTANCE
		.getConfigurationRepository();

	private ScenarioDocuBuildsManager docuBuildsManager;

	private DiffReader diffReader = new DiffReaderXmlImpl();

	private ScenarioDocuReader docuReader = new ScenarioDocuReader(
		configurationRepository.getDocumentationDataDirectory());

	private ExecutorService asyncComparisonExecutor;

	public ComparisonExecutor(ExecutorService executorService) {
		asyncComparisonExecutor = executorService;
		docuBuildsManager = ScenarioDocuBuildsManager.INSTANCE;
	}

	/**
	 * Submits all comparisons for the given build.
	 */
	public synchronized void doComparison(String baseBranchName, String baseBuildName) {
		docuBuildsManager = ScenarioDocuBuildsManager.INSTANCE;
		List<ComparisonConfiguration> comparisonConfigurationsForBaseBranch = getComparisonConfigurationsForBaseBranch(
				baseBranchName);
		for (ComparisonConfiguration comparisonConfiguration : comparisonConfigurationsForBaseBranch) {
			submitBuildForComparison(baseBranchName, baseBuildName, comparisonConfiguration);
		}
	}

	public ArrayList<Future<ComparisonResult>> doComparison(String baseBranchName, String baseBuildName, String comparisonBranchName, String comparisonBuildName, String comparisonName) {

		ArrayList<Future<ComparisonResult>> futureList = new ArrayList<Future<ComparisonResult>>();

		docuBuildsManager = ScenarioDocuBuildsManager.INSTANCE;

		ComparisonConfiguration comparisonConfiguration = new ComparisonConfiguration();
		comparisonConfiguration.setName(comparisonName);
		comparisonConfiguration.setBaseBranchName(baseBranchName);
		comparisonConfiguration.setComparisonBranchName(comparisonBranchName);
		comparisonConfiguration.setComparisonBuildName(comparisonBuildName);


		Future<ComparisonResult> result = submitBuildForComparison(baseBranchName, baseBuildName, comparisonConfiguration);
		futureList.add(result);
		return futureList;
	}

	/**
	 * Executes a comparison for the given build and comparison configuration in a separate thread.
	 */
	private synchronized Future<ComparisonResult> submitBuildForComparison(final String baseBranchName, final String baseBuildName,
																   final ComparisonConfiguration comparisonConfiguration) {

		LOGGER.info("Submitting build for Comparison. Base build [" + baseBranchName + "/"
			+ baseBuildName + "] and comparison build [" + comparisonConfiguration.getComparisonBranchName() + "/"
			+ comparisonConfiguration.getComparisonBuildName() + "]");

		return asyncComparisonExecutor.submit(new Callable<ComparisonResult>() {
			@Override
			public ComparisonResult call() {
				return runComparison(baseBranchName, baseBuildName, comparisonConfiguration);
			}
		});
	}

	private ComparisonResult runComparison(String baseBranchName, String baseBuildName,
										   ComparisonConfiguration comparisonConfiguration) {

		BuildDiffInfo buildDiffInfo = null;

		docuBuildsManager = ScenarioDocuBuildsManager.INSTANCE;
		ThreadLogAppender comparisonLog = null;

		try {

			comparisonLog = registerLogFile(baseBranchName, baseBuildName, comparisonConfiguration);
			long startTime = System.currentTimeMillis();

			LOGGER.info("============= START OF BUILD COMPARISON ================");
			LOGGER.info("Comparing base build: " + baseBranchName + "/"
				+ baseBuildName + " with defined comparison: " + comparisonConfiguration.getName());
			LOGGER.info("This might take a while ...");

			ComparisonConfiguration resolvedComparisonConfiguration = resolveComparisonConfiguration(
				comparisonConfiguration,
				baseBuildName);
			if (resolvedComparisonConfiguration == null) {
				LOGGER.warn("No comparison build found for base build: " + baseBranchName + "/"
					+ baseBuildName + " with defined comparison: " + comparisonConfiguration.getName());
			} else {
				ComparisonParameters cp = new ComparisonParameters(baseBranchName, baseBuildName, resolvedComparisonConfiguration,
					configurationRepository.getConfiguration().getDiffImageAwtColor());
				buildDiffInfo = new BuildComparator(cp).compareAndWrite();
			}

			LOGGER.info("SUCCESS on comparing base build: " + baseBranchName + "/"
				+ baseBuildName + " with defined comparison: " + comparisonConfiguration.getName());
			logDuration(startTime);
			LOGGER.info("============= END OF BUILD COMPARISON (success) ===========");
		} catch (Throwable e) {
			LOGGER.error("FAILURE on comparing build " + baseBranchName + "/"
				+ baseBuildName + " with defined comparison: " + comparisonConfiguration.getName(), e);
			LOGGER.info("============= END OF BUILD COMPARISON (failed) ===========");
		} finally {
			if (comparisonLog != null) {
				comparisonLog.unregisterAndFlush();
			}
		}


		ComparisonResult comparisonResult = new ComparisonResult();
		comparisonResult.setBaseBuild(new BuildIdentifier(baseBranchName, baseBuildName));

		String comparisonBranchName = comparisonConfiguration.getComparisonBranchName();
		String comparisonBuildName = comparisonConfiguration.getComparisonBuildName();
		BuildIdentifier resolvedBuildIdentifier = docuBuildsManager.resolveBranchAndBuildAliases(comparisonBranchName, comparisonBuildName);

		comparisonResult.setCompareBuild(resolvedBuildIdentifier);

		comparisonResult.setComparisonConfiguration(comparisonConfiguration);
		if (buildDiffInfo != null) {
			comparisonResult.setRmaePercentage(buildDiffInfo.getChangeRate());
		}

		return comparisonResult;
	}

	private void logDuration(long startTime) {
		long duration = (System.currentTimeMillis() - startTime) / 1000;
		long minutes = duration / 60;
		long seconds = duration % 60;
		LOGGER.info("Comparison finished in " + minutes + " min. " + seconds + " sec.");
	}

	/**
	 * Reads the reloaded xml configuration and returns all comparison configurations for the given base branch.
	 */
	protected synchronized List<ComparisonConfiguration> getComparisonConfigurationsForBaseBranch(
		String baseBranchName) {
		List<ComparisonConfiguration> comparisonConfigurationsForBaseBranch = new LinkedList<ComparisonConfiguration>();

		List<ComparisonConfiguration> comparisonConfigurations = configurationRepository.getConfiguration()
			.getComparisonConfigurations();
		String resolvedBaseBranchName = docuBuildsManager.resolveBranchAlias(baseBranchName);
		for (ComparisonConfiguration comparisonConfiguration : comparisonConfigurations) {
			String resolvedComparisonBranchName = docuBuildsManager
				.resolveBranchAlias(comparisonConfiguration.getBaseBranchName());
			if (resolvedBaseBranchName.equals(resolvedComparisonBranchName)) {
				comparisonConfigurationsForBaseBranch.add(comparisonConfiguration);
			}
		}
		return comparisonConfigurationsForBaseBranch;
	}

	/**
	 * Resolves branch and build aliases in a comparison configuration so that the effective branch and build name is in
	 * the comparison configuration.
	 * <p>
	 * If in a comparison configuration the alias for last successful or most recent is used then we need to make sure
	 * that on every execution time the correct previous build is used.
	 */
	protected ComparisonConfiguration resolveComparisonConfiguration(
		ComparisonConfiguration comparisonConfiguration,
		String baseBuildName) {

		String lastSuccessFulAlias = configurationRepository.getConfiguration().getAliasForLastSuccessfulBuild();
		String mostRecentAlias = configurationRepository.getConfiguration().getAliasForMostRecentBuild();

		BuildIdentifier comparisonBuildIdentifier;

		if (comparisonConfiguration.getComparisonBuildName().equals(lastSuccessFulAlias) &&
			comparisonConfiguration.getBaseBranchName().equals(comparisonConfiguration.getComparisonBranchName())) {
			comparisonBuildIdentifier = getPreviousBuildIdentifier(
				comparisonConfiguration, baseBuildName, true);
		} else if (comparisonConfiguration.getComparisonBuildName().equals(mostRecentAlias) &&
			comparisonConfiguration.getBaseBranchName().equals(comparisonConfiguration.getComparisonBranchName())) {
			comparisonBuildIdentifier = getPreviousBuildIdentifier(
				comparisonConfiguration, baseBuildName, false);
		} else {
			comparisonBuildIdentifier = docuBuildsManager.resolveBranchAndBuildAliases(
				comparisonConfiguration.getComparisonBranchName(),
				comparisonConfiguration.getComparisonBuildName());
		}

		if (comparisonBuildIdentifier == null || baseBuildName.equals(comparisonBuildIdentifier.getBuildName())) {
			return null;
		}

		return getResolvedComparisonConfiguration(comparisonConfiguration, comparisonBuildIdentifier);
	}

	private BuildIdentifier getPreviousBuildIdentifier(
		ComparisonConfiguration comparisonConfiguration, String baseBuildName,
		boolean needsSuccessfulBuild) {

		Build baseBuild = loadBaseBuild(comparisonConfiguration, baseBuildName);
		String resolvedComparisonBranchName = docuBuildsManager
			.resolveBranchAlias(comparisonConfiguration.getComparisonBranchName());
		Date baseBuildDate = baseBuild.getDate();
		Date comparisonBuildDate = new Date(0);
		Build comparisonBuild = null;

		for (ObjectFromDirectory<Build> objectBuild : docuReader.loadBuilds(resolvedComparisonBranchName)) {
			Build possibleComparisonBuild = objectBuild.getObject();
			if (possibleComparisonBuild.getDate().before(baseBuildDate)
				&& possibleComparisonBuild.getDate().after(comparisonBuildDate)) {
				if (needsSuccessfulBuild) {
					if (Status.SUCCESS.toString().equalsIgnoreCase(possibleComparisonBuild.getStatus())) {
						comparisonBuild = possibleComparisonBuild;
						comparisonBuildDate = possibleComparisonBuild.getDate();
					}
				} else {
					comparisonBuild = possibleComparisonBuild;
					comparisonBuildDate = possibleComparisonBuild.getDate();
				}
			}
		}
		if (comparisonBuild == null) {
			return null;
		}

		return new BuildIdentifier(resolvedComparisonBranchName, comparisonBuild.getName());
	}

	private Build loadBaseBuild(ComparisonConfiguration comparisonConfiguration, String baseBuildName) {

		BuildIdentifier resolvedBuildIdentifier = docuBuildsManager
			.resolveBranchAndBuildAliases(comparisonConfiguration.getBaseBranchName(), baseBuildName);
		Build baseBuild = docuReader.loadBuild(resolvedBuildIdentifier.getBranchName(),
			resolvedBuildIdentifier.getBuildName());

		if (baseBuild == null) {
			throw new RuntimeException("Unable to execute comparison for non existing build: "
				+ comparisonConfiguration.getBaseBranchName() + "/"
				+ baseBuildName);
		}
		return baseBuild;
	}

	private ComparisonConfiguration getResolvedComparisonConfiguration(
		ComparisonConfiguration comparisonConfiguration, BuildIdentifier comparisonBuildIdentifier) {
		String resolvedBaseBranchName = docuBuildsManager
			.resolveBranchAlias(comparisonConfiguration.getBaseBranchName());

		ComparisonConfiguration resolvedComparisonConfiguration = new ComparisonConfiguration();
		resolvedComparisonConfiguration.setBaseBranchName(resolvedBaseBranchName);
		resolvedComparisonConfiguration.setComparisonBranchName(comparisonBuildIdentifier.getBranchName());
		resolvedComparisonConfiguration.setComparisonBuildName(comparisonBuildIdentifier.getBuildName());
		resolvedComparisonConfiguration.setName(comparisonConfiguration.getName());
		return resolvedComparisonConfiguration;
	}

	private ThreadLogAppender registerLogFile(String baseBranchName, String baseBuildName,
											  ComparisonConfiguration comparisonConfiguration) {
		String comparisonName = comparisonConfiguration.getName();
		File comparisonLogFile = diffReader.getBuildComparisonLogFile(baseBranchName, baseBuildName,
			comparisonName);
		String comparisonIdentifier = baseBranchName + "/" + baseBuildName + "/" + comparisonName;

		return ThreadLogAppender.createAndRegisterForLogs(comparisonIdentifier,
			comparisonLogFile);
	}
}
