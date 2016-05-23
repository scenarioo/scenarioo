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

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.scenarioo.business.diffViewer.comparator.BuildComparator;
import org.scenarioo.model.configuration.ComparisonConfiguration;
import org.scenarioo.repository.ConfigurationRepository;
import org.scenarioo.repository.RepositoryLocator;

/**
 * Executes the comparisons for a base build. Each comparison is executed in a separate thread.
 */
public class ComparisonExecutor {

	private static final Logger LOGGER = Logger.getLogger(ComparisonExecutor.class);

	private static final long KEEP_ALIVE_TIME = 60L;
	private static final int MAXIMUM_POOL_SIZE = 1;
	private static final int CORE_POOL_SIZE = 1;

	private final ConfigurationRepository configurationRepository = RepositoryLocator.INSTANCE
			.getConfigurationRepository();

	private ExecutorService asyncComparisonExecutor = newAsyncComparisonExecutor();

	public synchronized void doComparison(final String baseBranchName, final String baseBuildName) {
		final List<ComparisonConfiguration> comparisonConfigurationsForBaseBranch = getComparisonConfigurationsForBaseBranch(baseBranchName);
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
				runComparison(baseBranchName, baseBuildName, comparisonConfiguration.getName());
			}
		});
	}

	private void runComparison(final String baseBranchName, final String baseBuildName, final String comparisonName) {
		BuildComparisonLogAppender buildComparisonLog = null;
		try {
			buildComparisonLog = BuildComparisonLogAppender.createAndRegisterForLogsOfBuild(baseBranchName,
					baseBuildName, comparisonName);

			LOGGER.info("============= START OF BUILD COMPARISON ================");
			LOGGER.info("Comparing base build: " + baseBuildName + "/"
					+ baseBuildName + " with defined comparison: " + comparisonName);
			LOGGER.info("This might take a while ...");

			new BuildComparator(baseBranchName, baseBuildName, comparisonName).compare();

			LOGGER.info("SUCCESS on comparing base build: " + baseBuildName + "/"
					+ baseBuildName + " with defined comparison: " + comparisonName);
			LOGGER.info("============= END OF BUILD COMPARISON (success) ===========");
		} catch (final Throwable e) {
			LOGGER.error("FAILURE on comparing build " + baseBuildName + "/"
					+ baseBuildName + " with defined comparison: " + comparisonName, e);
			LOGGER.info("============= END OF BUILD COMPARISON (failed) ===========");
		} finally {
			if (buildComparisonLog != null) {
				buildComparisonLog.unregisterAndFlush();
			}
		}
	}

	private synchronized List<ComparisonConfiguration> getComparisonConfigurationsForBaseBranch(final String baseBranchName) {
		final List<ComparisonConfiguration> comparisonConfigurationsForBaseBranch = new LinkedList<ComparisonConfiguration>();
		final List<ComparisonConfiguration> comparisonConfigurations = configurationRepository.getConfiguration()
				.getComparisonConfigurations();
		for (final ComparisonConfiguration comparisonConfiguration : comparisonConfigurations) {
			if (baseBranchName.equals(comparisonConfiguration.getBaseBranchName())) {
				comparisonConfigurationsForBaseBranch.add(comparisonConfiguration);
			}
		}
		return comparisonConfigurationsForBaseBranch;
	}

	/**
	 * Creates an executor that queues the passed tasks for execution by one single additional thread.
	 */
	private static ExecutorService newAsyncComparisonExecutor() {
		return new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE_TIME, TimeUnit.SECONDS,
				new LinkedBlockingQueue<Runnable>());
	}
}
