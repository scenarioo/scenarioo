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

import java.io.File;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;

import org.apache.log4j.Logger;
import org.scenarioo.api.ScenarioDocuReader;
import org.scenarioo.api.files.ObjectFromDirectory;
import org.scenarioo.business.builds.ScenarioDocuBuildsManager;
import org.scenarioo.business.diffViewer.comparator.BuildComparator;
import org.scenarioo.dao.diffViewer.DiffReader;
import org.scenarioo.dao.diffViewer.impl.DiffReaderXmlImpl;
import org.scenarioo.model.configuration.ComparisonConfiguration;
import org.scenarioo.model.docu.entities.Build;
import org.scenarioo.model.docu.entities.Status;
import org.scenarioo.repository.ConfigurationRepository;
import org.scenarioo.repository.RepositoryLocator;
import org.scenarioo.rest.base.BuildIdentifier;
import org.scenarioo.utils.ThreadLogAppender;

/**
 * Executes the comparisons for a base build. Each comparison is executed in a separate thread.
 */
public class ComparisonExecutor {

	private static final Logger LOGGER = Logger.getLogger(ComparisonExecutor.class);

	private final ConfigurationRepository configurationRepository = RepositoryLocator.INSTANCE
			.getConfigurationRepository();

	private DiffReader diffReader = new DiffReaderXmlImpl();

	private ScenarioDocuReader docuReader = new ScenarioDocuReader(
			configurationRepository.getDocumentationDataDirectory());

	private ExecutorService asyncComparisonExecutor;

	public ComparisonExecutor(final ExecutorService executorService) {
		asyncComparisonExecutor = executorService;
	}

	public synchronized void doComparison(final String baseBranchName, final String baseBuildName) {
		final List<ComparisonConfiguration> comparisonConfigurationsForBaseBranch = getComparisonConfigurationsForBaseBranch(
				baseBranchName);
		for (final ComparisonConfiguration comparisonConfiguration : comparisonConfigurationsForBaseBranch) {
			submitBuildForComparison(baseBranchName, baseBuildName, comparisonConfiguration);
		}

	}

	private synchronized void submitBuildForComparison(final String baseBranchName, final String baseBuildName,
			final ComparisonConfiguration comparisonConfiguration) {

		LOGGER.info("Submitting build for Comparison. Base build [" + baseBranchName + "/"
				+ baseBuildName + "] and comparison build [" + comparisonConfiguration.getComparisonBranchName() + "/"
				+ comparisonConfiguration.getComparisonBuildName() + "]");

		asyncComparisonExecutor.execute(new Runnable() {
			@Override
			public void run() {
				runComparison(baseBranchName, baseBuildName, comparisonConfiguration);
			}
		});
	}

	private void runComparison(final String baseBranchName, final String baseBuildName,
			final ComparisonConfiguration comparisonConfiguration) {
		ThreadLogAppender comparisonLog = null;
		try {
			final String comparisonName = comparisonConfiguration.getName();
			final File comparisonLogFile = diffReader.getBuildComparisonLogFile(baseBranchName, baseBuildName,
					comparisonName);
			final String comparisonIdentifier = baseBranchName + "/" + baseBuildName + "/" + comparisonName;

			comparisonLog = ThreadLogAppender.createAndRegisterForLogs(comparisonIdentifier,
					comparisonLogFile);

			LOGGER.info("============= START OF BUILD COMPARISON ================");
			LOGGER.info("Comparing base build: " + baseBranchName + "/"
					+ baseBuildName + " with defined comparison: " + comparisonConfiguration.getName());
			LOGGER.info("This might take a while ...");

			final ComparisonConfiguration resolvedComparisonConfiguration = resolveComparisonConfiguration(
					comparisonConfiguration,
					baseBuildName);
			if (resolvedComparisonConfiguration == null) {
				LOGGER.warn("No comparison build found for base build: " + baseBranchName + "/"
						+ baseBuildName + " with defined comparison: " + comparisonConfiguration.getName());
			} else {
				new BuildComparator(baseBranchName, baseBuildName, resolvedComparisonConfiguration).compare();
			}

			LOGGER.info("SUCCESS on comparing base build: " + baseBranchName + "/"
					+ baseBuildName + " with defined comparison: " + comparisonConfiguration.getName());
			LOGGER.info("============= END OF BUILD COMPARISON (success) ===========");
		} catch (final Throwable e) {
			LOGGER.error("FAILURE on comparing build " + baseBranchName + "/"
					+ baseBuildName + " with defined comparison: " + comparisonConfiguration.getName(), e);
			LOGGER.info("============= END OF BUILD COMPARISON (failed) ===========");
		} finally {
			if (comparisonLog != null) {
				comparisonLog.unregisterAndFlush();
			}
		}
	}

	private synchronized List<ComparisonConfiguration> getComparisonConfigurationsForBaseBranch(
			final String baseBranchName) {
		final List<ComparisonConfiguration> comparisonConfigurationsForBaseBranch = new LinkedList<ComparisonConfiguration>();

		configurationRepository.reloadConfiguration();
		final List<ComparisonConfiguration> comparisonConfigurations = configurationRepository.getConfiguration()
				.getComparisonConfigurations();
		final String resolvedBaseBranchName = ScenarioDocuBuildsManager.INSTANCE.resolveBranchAlias(baseBranchName);
		for (final ComparisonConfiguration comparisonConfiguration : comparisonConfigurations) {
			final String resolvedComparisonBranchName = ScenarioDocuBuildsManager.INSTANCE
					.resolveBranchAlias(comparisonConfiguration.getBaseBranchName());
			if (resolvedBaseBranchName.equals(resolvedComparisonBranchName)) {
				comparisonConfigurationsForBaseBranch.add(comparisonConfiguration);
			}
		}
		return comparisonConfigurationsForBaseBranch;
	}

	protected ComparisonConfiguration resolveComparisonConfiguration(
			final ComparisonConfiguration comparisonConfiguration,
			final String baseBuildName) {

		final String lastSuccessFulAlias = configurationRepository.getConfiguration().getAliasForLastSuccessfulBuild();
		final String mostRecentAlias = configurationRepository.getConfiguration().getAliasForMostRecentBuild();

		BuildIdentifier comparisonBuildIdentifier = null;

		if (comparisonConfiguration.getComparisonBuildName().equals(lastSuccessFulAlias) &&
				comparisonConfiguration.getBaseBranchName().equals(comparisonConfiguration.getComparisonBranchName())) {
			comparisonBuildIdentifier = getPreviousBuildIdentifier(
					comparisonConfiguration, baseBuildName, true);
		} else if (comparisonConfiguration.getComparisonBuildName().equals(mostRecentAlias) &&
				comparisonConfiguration.getBaseBranchName().equals(comparisonConfiguration.getComparisonBranchName())) {
			comparisonBuildIdentifier = getPreviousBuildIdentifier(
					comparisonConfiguration, baseBuildName, false);
		} else {
			comparisonBuildIdentifier = ScenarioDocuBuildsManager.INSTANCE.resolveBranchAndBuildAliases(
					comparisonConfiguration.getComparisonBranchName(),
					comparisonConfiguration.getComparisonBuildName());
		}

		if (comparisonBuildIdentifier == null || baseBuildName.equals(comparisonBuildIdentifier.getBuildName())) {
			return null;
		}

		return getResolvedComparisonConfiguration(comparisonConfiguration, comparisonBuildIdentifier);
	}

	private BuildIdentifier getPreviousBuildIdentifier(
			final ComparisonConfiguration comparisonConfiguration, final String baseBuildName,
			final boolean needsSuccessfulBuild) {

		final Build baseBuild = loadBaseBuild(comparisonConfiguration, baseBuildName);
		final String resolvedComparisonBranchName = ScenarioDocuBuildsManager.INSTANCE
				.resolveBranchAlias(comparisonConfiguration.getComparisonBranchName());
		final Date baseBuildDate = baseBuild.getDate();
		Date comparisonBuildDate = new Date(0);
		Build comparisonBuild = null;

		for (final ObjectFromDirectory<Build> objectBuild : docuReader.loadBuilds(resolvedComparisonBranchName)) {
			final Build possibleComparisonBuild = objectBuild.getObject();
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

	private Build loadBaseBuild(final ComparisonConfiguration comparisonConfiguration, final String baseBuildName) {

		final BuildIdentifier resolvedBuildIdentifier = ScenarioDocuBuildsManager.INSTANCE
				.resolveBranchAndBuildAliases(comparisonConfiguration.getBaseBranchName(), baseBuildName);
		final Build baseBuild = docuReader.loadBuild(resolvedBuildIdentifier.getBranchName(),
				resolvedBuildIdentifier.getBuildName());

		if (baseBuild == null) {
			throw new RuntimeException("Unable to execute comparison for non existing build: "
					+ comparisonConfiguration.getBaseBranchName() + "/"
					+ baseBuildName);
		}
		return baseBuild;
	}

	private ComparisonConfiguration getResolvedComparisonConfiguration(
			final ComparisonConfiguration comparisonConfiguration, final BuildIdentifier comparisonBuildIdentifier) {
		final ComparisonConfiguration resolvedComparisonConfiguration = new ComparisonConfiguration();
		resolvedComparisonConfiguration.setBaseBranchName(comparisonBuildIdentifier.getBranchName());
		resolvedComparisonConfiguration.setComparisonBranchName(comparisonBuildIdentifier.getBranchName());
		resolvedComparisonConfiguration.setComparisonBuildName(comparisonBuildIdentifier.getBuildName());
		resolvedComparisonConfiguration.setName(comparisonConfiguration.getName());
		return resolvedComparisonConfiguration;
	}
}
