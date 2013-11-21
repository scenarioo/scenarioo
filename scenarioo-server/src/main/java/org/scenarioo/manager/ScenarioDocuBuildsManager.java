package org.scenarioo.manager;

import java.io.File;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.log4j.Logger;
import org.scenarioo.aggregator.ScenarioDocuAggregator;
import org.scenarioo.api.ScenarioDocuReader;
import org.scenarioo.dao.ConfigurationDAO;
import org.scenarioo.model.docu.aggregates.branches.BranchBuilds;
import org.scenarioo.model.docu.derived.BuildLink;
import org.scenarioo.model.docu.entities.Branch;

/**
 * Manages the list of branches and builds that are currently available in the documentation directory:
 * 
 * 1. Using {@link #updateAll()} all branches and builds are read and processed from the file system, this will
 * calculate any aggregated data using {@link ScenarioDocuAggregator}.
 * 
 * 2. The current (last processed) list of branches and builds is cached and can be accessed using
 * {@link #getBranchBuildsList()}.
 */
public class ScenarioDocuBuildsManager {
	
	public static ScenarioDocuBuildsManager INSTANCE = new ScenarioDocuBuildsManager();
	
	private static final Logger LOGGER = Logger.getLogger(ScenarioDocuBuildsManager.class);
	
	/**
	 * Is a singleton. Use {@link #INSTANCE}.
	 */
	private ScenarioDocuBuildsManager() {
	}
	
	private List<BranchBuilds> branchBuildsList = new ArrayList<BranchBuilds>();
	private int buildsProcessed = 0;
	private int buildsImportedOrUpdated = 0;
	
	/**
	 * Only available after updateAll() was successfull at least once.
	 */
	public List<BranchBuilds> getBranchBuildsList() {
		return branchBuildsList;
	}
	
	/**
	 * Processes the content of configured documentation filesystem directory discovering newly added builds or branches
	 * to calculate all data for them. Also updates the branches and builds list.
	 * 
	 * This method should be called on server startup and whenever something on the filesystem changed.
	 */
	public void updateAll() {
		File docuDirectory = ConfigurationDAO.getDocuDataDirectoryPath();
		if (docuDirectory == null) {
			LOGGER.error("No documentation directory is configured.");
			LOGGER.error("Please configure valid documentation directory in configuration UI");
		} else if (!docuDirectory.exists()) {
			LOGGER.error("No valid documentation directory is configured: " + docuDirectory.getAbsolutePath());
			LOGGER.error("Please configure valid documentation directory in configuration UI");
		}
		else {
			LOGGER.info("  Processing documentation content data in directory: " + docuDirectory.getAbsoluteFile());
			LOGGER.info("  Calculating aggregated data in derived XML files, this may take a while ...");
			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					doUpdateAll();
				}
			});
			t.start();
		}
	}
	
	/**
	 * Only allowed to be called once at a time (synchronized, because called asynchronously from {@link #updateAll()}).
	 */
	private synchronized void doUpdateAll() {
		try {
			LOGGER.info(" ============= START OF SCENARIOO DOCUMENTATION ASYNC DATA UPDATE =================");
			LOGGER.info("  This might take a while ...");
			
			doUpdateAllBranchesAndBuilds();
			LOGGER.info("  Recognized valid documentation build directories in file system: " + buildsProcessed);
			LOGGER.info("  Updated or imported builds: " + buildsImportedOrUpdated);
			LOGGER.info("  SUCCESS: Documentation content directory has been processed and updated successfully.");
			LOGGER.info(" ============= END OF SCENARIOO DOCUMENTATION ASYNC DATA UPDATE (success) ===========");
		} catch (Throwable e) {
			LOGGER.error("  FAILED: Updating documentation data failed.", e);
			LOGGER.info(" ============= END OF SCENARIOO DOCUMENTATION ASYNC DATA UPDATE (failed) ===========");
		}
	}
	
	private synchronized void doUpdateAllBranchesAndBuilds() {
		buildsProcessed = 0;
		buildsImportedOrUpdated = 0;
		List<BranchBuilds> branchBuildsList = loadBranchBuildsList();
		for (BranchBuilds branchBuilds : branchBuildsList) {
			LOGGER.info("Calculating aggregated data for branch : " + branchBuilds.getBranch().getName() + "...");
			for (BuildLink buildLink : branchBuilds.getBuilds()) {
				buildsProcessed++;
				ScenarioDocuAggregator aggregator = new ScenarioDocuAggregator();
				if (!aggregator.containsAggregatedDataForBuild(branchBuilds.getBranch().getName(),
						buildLink.getLinkName())) {
					aggregator.calculateAggregatedDataForBuild(branchBuilds.getBranch().getName(),
							buildLink.getLinkName());
					buildsImportedOrUpdated++;
				}
			}
			sortBuilds(branchBuilds);
		}
		this.branchBuildsList = branchBuildsList;
	}
	
	private List<BranchBuilds> loadBranchBuildsList() {
		final ScenarioDocuReader reader = new ScenarioDocuReader(ConfigurationDAO.getDocuDataDirectoryPath());
		List<BranchBuilds> result = new ArrayList<BranchBuilds>();
		List<Branch> branches = reader.loadBranches();
		for (Branch branch : branches) {
			BranchBuilds branchBuilds = new BranchBuilds();
			branchBuilds.setBranch(branch);
			branchBuilds.setBuilds(reader.loadBuilds(branch.getName()));
			result.add(branchBuilds);
		}
		return result;
	}
	
	/**
	 * Special sorting for builds: 1. default build 2. other tagged builds in order of their dates 3. other builds in
	 * order of their dates
	 */
	private void sortBuilds(final BranchBuilds branchBuilds) {
		Collections.sort(branchBuilds.getBuilds(), new Comparator<BuildLink>() {
			
			Collator collator = Collator.getInstance();
			
			@Override
			public int compare(final BuildLink bl1, final BuildLink bl2) {
				
				String defaultBuildName = ConfigurationDAO.getConfiguration().getDefaultBuildName();
				
				// Default build is always sorted to the top
				if (bl1.getLinkName().equals(defaultBuildName)) {
					return -1;
				}
				if (bl2.getLinkName().equals(defaultBuildName)) {
					return 1;
				}
				
				// Link (=tagged build) is always sorted to the top if other is
				// not a link
				boolean isLink1 = !bl1.getBuild().getName().equals(bl1.getLinkName());
				boolean isLink2 = !bl2.getBuild().getName().equals(bl2.getLinkName());
				if (isLink1 && !isLink2) {
					return -1;
				}
				if (!isLink1 && isLink2) {
					return 1;
				}
				
				// if date is missing simply sort by link name, but builds with
				// a date are sorted on top.
				if (bl1.getBuild().getDate() == null && bl2.getBuild().getDate() == null) {
					return collator.compare(bl1.getLinkName(), bl2.getLinkName());
				} else if (bl1.getBuild().getDate() == null) {
					return 1;
				} else if (bl2.getBuild().getDate() == null) {
					return -1;
				}
				
				// Links and non-links are both sorted by date (descending!)
				return bl2.getBuild().getDate().compareTo(bl1.getBuild().getDate());
			}
		});
	}
	
}
