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
import org.scenarioo.api.exception.ResourceNotFoundException;
import org.scenarioo.api.files.ObjectFromDirectory;
import org.scenarioo.business.builds.AliasResolver;
import org.scenarioo.business.diffViewer.comparator.ComparisonParameters;
import org.scenarioo.business.diffViewer.comparator.UseCaseComparator;
import org.scenarioo.dao.diffViewer.DiffViewerDao;
import org.scenarioo.model.configuration.ComparisonConfiguration;
import org.scenarioo.model.diffViewer.BuildDiffInfo;
import org.scenarioo.model.diffViewer.ComparisonCalculationStatus;
import org.scenarioo.model.docu.entities.Build;
import org.scenarioo.model.docu.entities.Status;
import org.scenarioo.repository.ConfigurationRepository;
import org.scenarioo.repository.RepositoryLocator;
import org.scenarioo.rest.base.BuildIdentifier;
import org.scenarioo.utils.ThreadLogAppender;

import java.io.File;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Executes the comparisons for a base build. Each comparison is executed in a separate thread.
 */
public class ComparisonExecutor {

	private static Logger LOGGER = Logger.getLogger(ComparisonExecutor.class);

	private ConfigurationRepository configurationRepository = RepositoryLocator.INSTANCE
		.getConfigurationRepository();

	private DiffViewerDao diffViewerDao = new DiffViewerDao();

	private ScenarioDocuReader docuReader = new ScenarioDocuReader(
		configurationRepository.getDocumentationDataDirectory());

	private ExecutorService asyncComparisonExecutor;
	private AliasResolver aliasResolver;

	/**
	 * Create comparison executor to execute comparisons in its own executors (to not block imports of builds and run comparisons of imported builds in parallel)
	 */
	public ComparisonExecutor(AliasResolver aliasResolver) {
		this(newAsyncComparisonExecutor(), aliasResolver);
	}

	/**
	 * For unit testing only
	 */
	ComparisonExecutor(ExecutorService executorService, AliasResolver aliasResolver) {
		asyncComparisonExecutor = executorService;
		this.aliasResolver = aliasResolver;
	}

	/**
	 * Submits all comparisons for the given build.
	 * <p>
	 * Does not recalculate any comparisons that have already been calculated in the filesystem.
	 */
	public void scheduleAllConfiguredComparisonsForOneBuild(String baseBranchName, String baseBuildName) {
		List<ComparisonConfiguration> comparisonConfigurationsForBaseBranch = getComparisonConfigurationsForBaseBranch(
			baseBranchName);
		LOGGER.info("Scheduling all comparisons for current build to be calculated in background:");
		for (ComparisonConfiguration comparisonConfiguration : comparisonConfigurationsForBaseBranch) {
			if (!isComparisonAlreadyCalculated(baseBranchName, baseBuildName, comparisonConfiguration.getName())) {
				scheduleComparison(baseBranchName, baseBuildName, comparisonConfiguration);
			} else {
				LOGGER.info("Comparison for build that already exists is not automatically recalculated: \n"
					+ "     " + getComparisonConfigString(baseBranchName, baseBuildName, comparisonConfiguration));
			}
		}
	}

	private synchronized boolean isComparisonAlreadyCalculated(final String baseBranchName, final String baseBuildName, String comparisonName) {
		try {
			BuildDiffInfo diff = diffViewerDao.loadBuildDiffInfo(baseBranchName, baseBuildName, comparisonName);
			return diff != null && diff.getStatus() != null && diff.getStatus() != ComparisonCalculationStatus.QUEUED_FOR_PROCESSING;
		} catch (ResourceNotFoundException e) {
			return false;
		}
	}

	/**
	 * Schedule a comparison calculation for a single build to become calculated
	 * or even force to recalculate it (even if it already exists).
	 */
	public Future<BuildDiffInfo> scheduleComparison(String baseBranchName, String baseBuildName,
													String comparisonBranchName, String comparisonBuildName, String comparisonName) {

		ComparisonConfiguration comparisonConfiguration = new ComparisonConfiguration();
		comparisonConfiguration.setName(comparisonName);
		comparisonConfiguration.setBaseBranchName(baseBranchName);
		comparisonConfiguration.setComparisonBranchName(comparisonBranchName);
		comparisonConfiguration.setComparisonBuildName(comparisonBuildName);

		return scheduleComparison(baseBranchName, baseBuildName, comparisonConfiguration);
	}

	/**
	 * Executes a comparison for the given build and comparison configuration in a separate thread.
	 */
	private synchronized Future<BuildDiffInfo> scheduleComparison(final String baseBranchName, final String baseBuildName,
																  final ComparisonConfiguration comparisonConfiguration) {

		LOGGER.info("Scheduling Comparison of build for background calculation: \n"
			+ "     " + getComparisonConfigString(baseBranchName, baseBuildName, comparisonConfiguration));

		Future<BuildDiffInfo> futureComparison = asyncComparisonExecutor.submit(new Callable<BuildDiffInfo>() {
			@Override
			public BuildDiffInfo call() {
				return calculateComparison(baseBranchName, baseBuildName, comparisonConfiguration);
			}
		});

		ComparisonParameters comparisonParameters = new ComparisonParameters(baseBranchName, baseBuildName, comparisonConfiguration,
			configurationRepository.getConfiguration().getDiffImageAwtColor());

		storeComparisonAsQueuedForProcessing(comparisonParameters);

		return futureComparison;
	}

	private void storeComparisonAsQueuedForProcessing(ComparisonParameters comparisonParameters) {
		saveBuildDiffInfoWithStatus(comparisonParameters, ComparisonCalculationStatus.QUEUED_FOR_PROCESSING);
	}


	/**
	 * Calculate a comparison and catch all exceptions (also runtime exceptions)
	 * to not let the asynch task executor just silently crash on errors.
	 * <p>
	 * Must log errors and return without errors in case of any problems!
	 * @return the claculated diff result ot null (in case it failed or there was nothing to calculate)
	 */
	private BuildDiffInfo calculateComparison(String baseBranchName, String baseBuildName,
											  ComparisonConfiguration comparisonConfiguration) {

		// Init parameters
		BuildDiffInfo buildDiffInfo = null;
		ComparisonConfiguration resolvedComparisonConfiguration;
		ThreadLogAppender comparisonLog = null;
		ComparisonParameters comparisonParameters = new ComparisonParameters(baseBranchName, baseBuildName,
			comparisonConfiguration, configurationRepository.getConfiguration().getDiffImageAwtColor());
		long startTime = System.currentTimeMillis();

		// Precondition Check
		if (!isComparisonInStateQueued(baseBranchName, baseBuildName, comparisonConfiguration)) {
			// Nothing to calculate if this check fails.
			return null;
		}

		// Catch all exceptions in any case and log them to not let asynch execution silently fail!
		try {

			comparisonLog = startComparisonAndRecordLog(baseBranchName, baseBuildName, comparisonConfiguration);
			resolvedComparisonConfiguration = resolveComparisonConfiguration(comparisonConfiguration, baseBuildName);
			if (resolvedComparisonConfiguration == null) {
				// SKIPPED:
				storeAndLogComparisonSkipped(comparisonParameters);
			} else {
				// PROCESSING:
				comparisonParameters = new ComparisonParameters(baseBranchName,	baseBuildName,
					resolvedComparisonConfiguration, configurationRepository.getConfiguration().getDiffImageAwtColor());
				storeComparisonAsProcessing(comparisonParameters);
				buildDiffInfo = new UseCaseComparator(comparisonParameters).compare();

				// SUCCESS:
				storeAndLogComparisonSuccessful(comparisonParameters, buildDiffInfo, startTime);
			}

		} catch (Throwable e) {
			// FAILED:
			buildDiffInfo = storeAndLogComparisonFailed(comparisonParameters, e);
		} finally {
			flushComparisonLog(comparisonLog);
		}
		return buildDiffInfo;
	}

	/**
	 * Checks the precondition and logs if a comparison is not ready to be calulated.
	 */
	private synchronized boolean isComparisonInStateQueued(final String baseBranchName, final String baseBuildName, ComparisonConfiguration comparisonConfiguration) {
		try {
			BuildDiffInfo diff = diffViewerDao.loadBuildDiffInfo(baseBranchName, baseBuildName, comparisonConfiguration.getName());
			boolean isQueued = diff != null && diff.getStatus() == ComparisonCalculationStatus.QUEUED_FOR_PROCESSING;
			if (!isQueued) {
				// DO NOTHING: The comparison was already processed somehow inbetween (maybe same comparison was scheduled more than once)
				LOGGER.info("Scheduled Comparison has already been calculated inbetween, not calculating it again: "
					+ getComparisonConfigString(baseBranchName, baseBuildName, comparisonConfiguration));
			}
			return isQueued;
		} catch (ResourceNotFoundException e) {
			LOGGER.info("Scheduled Comparison has no comparison build file, somehow removed, will ignore it for calculation: "
				+ getComparisonConfigString(baseBranchName, baseBuildName, comparisonConfiguration));
			return false;
		} catch (Throwable e) {
			// All other exceptions - log error and return false as well
			LOGGER.error("Exception in ComparisonExecutor task in precondition check for comparison: "
				+ getComparisonConfigString(baseBranchName, baseBuildName, comparisonConfiguration), e);
			return false;
		}
	}

	private ThreadLogAppender startComparisonAndRecordLog(String baseBranchName, String baseBuildName,
														  ComparisonConfiguration comparisonConfiguration) {
		String comparisonName = comparisonConfiguration.getName();
		File comparisonLogFile = diffViewerDao.getBuildComparisonLogFile(baseBranchName, baseBuildName,
			comparisonName);
		String comparisonIdentifier = baseBranchName + "/" + baseBuildName + "/" + comparisonName;


		ThreadLogAppender logAppender = ThreadLogAppender.createAndRegisterForLogs(comparisonIdentifier,
			comparisonLogFile);

		LOGGER.info("=== START OF BUILD COMPARISON ===");
		LOGGER.info(getComparisonConfigString(baseBranchName, baseBuildName, comparisonConfiguration));

		return logAppender;
	}

	private BuildDiffInfo storeAndLogComparisonSkipped(ComparisonParameters comparisonParameters) {
		LOGGER.warn("No comparison build found for base build: " + comparisonParameters.getBaseBranchName()
			+ "/" + comparisonParameters.getBaseBuildName()
			+ " with defined comparison: " + comparisonParameters.getComparisonConfiguration().getName());
		BuildDiffInfo buildDiffInfo = saveBuildDiffInfoWithStatus(comparisonParameters, ComparisonCalculationStatus.SKIPPED);
		LOGGER.info("SKIPPED comparing base build: " + comparisonParameters.getBaseBranchName()
			+ "/" + comparisonParameters.getBaseBuildName()
			+ " with defined comparison: " + comparisonParameters.getComparisonConfiguration().getName());
		LOGGER.info("=== END OF BUILD COMPARISON (skipped) ===");
		return buildDiffInfo;
	}

	private void storeComparisonAsProcessing(ComparisonParameters comparisonParameters) {
		saveBuildDiffInfoWithStatus(comparisonParameters, ComparisonCalculationStatus.PROCESSING);
	}

	private void storeAndLogComparisonSuccessful(ComparisonParameters comparisonParameters,
												 BuildDiffInfo buildDiffInfo,
												 long startTime) {
		LOGGER.info("SUCCESS on comparing base build: " + comparisonParameters.getBaseBranchName() + "/"
			+ comparisonParameters.getBaseBuildName() + " with defined comparison: " + comparisonParameters.getComparisonConfiguration().getName());
		saveBuildDiffInfo(comparisonParameters, buildDiffInfo);
		logDuration(startTime);
		LOGGER.info("=== END OF BUILD COMPARISON (success) ===");
	}

	private BuildDiffInfo storeAndLogComparisonFailed(ComparisonParameters comparisonParameters, Throwable e) {
		BuildDiffInfo result = saveBuildDiffInfoWithStatus(comparisonParameters, ComparisonCalculationStatus.FAILED);
		LOGGER.error("FAILURE on comparing build " + comparisonParameters.getBaseBranchName() + "/"
			+ comparisonParameters.getBaseBuildName() + " with defined comparison: " + comparisonParameters.getComparisonConfiguration().getName(), e);
		LOGGER.info("=== END OF BUILD COMPARISON (failed) ===");
		return result;
	}

	private BuildDiffInfo saveBuildDiffInfoWithStatus(ComparisonParameters comparisonParameters,
													  ComparisonCalculationStatus comparisonCalculationStatus) {
		BuildDiffInfo buildDiffInfo = new BuildDiffInfo();
		buildDiffInfo.setStatus(comparisonCalculationStatus);
		return saveBuildDiffInfo(comparisonParameters, buildDiffInfo);
	}

	private synchronized BuildDiffInfo saveBuildDiffInfo(ComparisonParameters comparisonParameters, BuildDiffInfo buildDiffInfo) {

		if (comparisonParameters == null) {
			LOGGER.warn("Can't save BuildDiffInfo with status " + buildDiffInfo.getStatus() + " because" +
				" comparisonParameters is null");
			return null;
		}

		if (comparisonParameters.getComparisonConfiguration() == null) {
			LOGGER.warn("Can't save BuildDiffInfo for status " + buildDiffInfo.getStatus() + " because" +
				" comparisonConfiguration is null");
			return null;
		}

		buildDiffInfo.setName(comparisonParameters.getComparisonConfiguration().getName());
		buildDiffInfo.setBaseBuild(new BuildIdentifier(comparisonParameters.getBaseBranchName(), comparisonParameters.getBaseBuildName()));
		ComparisonConfiguration comparisonConfiguration = comparisonParameters.getComparisonConfiguration();
		buildDiffInfo.setCompareBuild(new BuildIdentifier(comparisonConfiguration.getComparisonBranchName(), comparisonConfiguration.getComparisonBuildName()));
		buildDiffInfo.setCalculationDate(new Date());

		comparisonParameters.getDiffWriter().saveBuildDiffInfo(buildDiffInfo);
		return buildDiffInfo;
	}

	private void flushComparisonLog(ThreadLogAppender comparisonLog) {
		if (comparisonLog != null) {
			comparisonLog.unregisterAndFlush();
		}
	}


	private String getComparisonConfigString(String baseBranchName, String baseBuildName, ComparisonConfiguration comparisonConfiguration) {
		return "Comparison " + comparisonConfiguration.getName() + " on target build " + baseBranchName + "/" + baseBuildName + " for comparing with build " + comparisonConfiguration.getComparisonBranchName() + "/" + comparisonConfiguration.getComparisonBuildName();
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
	synchronized List<ComparisonConfiguration> getComparisonConfigurationsForBaseBranch(
		String baseBranchName) {
		List<ComparisonConfiguration> comparisonConfigurationsForBaseBranch = new LinkedList<>();

		List<ComparisonConfiguration> comparisonConfigurations = configurationRepository.getConfiguration()
			.getComparisonConfigurations();
		String resolvedBaseBranchName = aliasResolver.resolveBranchAlias(baseBranchName);
		for (ComparisonConfiguration comparisonConfiguration : comparisonConfigurations) {
			String resolvedComparisonBranchName = aliasResolver.resolveBranchAlias(comparisonConfiguration.getBaseBranchName());
			if (resolvedBaseBranchName.equals(resolvedComparisonBranchName)
				|| resolvedBaseBranchName.matches(resolvedComparisonBranchName)) {
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
	ComparisonConfiguration resolveComparisonConfiguration(
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
			comparisonBuildIdentifier = this.aliasResolver.resolveBranchAndBuildAliases(
				comparisonConfiguration.getComparisonBranchName(),
				comparisonConfiguration.getComparisonBuildName());
		}

		if (comparisonBuildIdentifier == null || baseBuildName.equals(comparisonBuildIdentifier.getBuildName())) {
			return null;
		}

		return getResolvedComparisonConfiguration(comparisonConfiguration, comparisonBuildIdentifier);
	}

	private BuildIdentifier getPreviousBuildIdentifier(
		ComparisonConfiguration comparisonConfiguration, String baseBuildName, boolean needsSuccessfulBuild) {

		Build baseBuild = loadBaseBuild(comparisonConfiguration, baseBuildName);
		String resolvedComparisonBranchName = aliasResolver.resolveBranchAlias(comparisonConfiguration.getComparisonBranchName());
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

		BuildIdentifier resolvedBuildIdentifier = this.aliasResolver
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
		String resolvedBaseBranchName = aliasResolver.resolveBranchAlias(comparisonConfiguration.getBaseBranchName());

		ComparisonConfiguration resolvedComparisonConfiguration = new ComparisonConfiguration();
		resolvedComparisonConfiguration.setBaseBranchName(resolvedBaseBranchName);
		resolvedComparisonConfiguration.setComparisonBranchName(comparisonBuildIdentifier.getBranchName());
		resolvedComparisonConfiguration.setComparisonBuildName(comparisonBuildIdentifier.getBuildName());
		resolvedComparisonConfiguration.setName(comparisonConfiguration.getName());
		return resolvedComparisonConfiguration;
	}


	/**
	 * Creates an executor that queues the passed tasks for execution by one single additional thread.
	 */
	private static ExecutorService newAsyncComparisonExecutor() {
		return new ThreadPoolExecutor(1, 1, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
	}

}
