package ngusd.manager;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ngusd.dao.ConfigurationDAO;
import ngusd.dao.UserScenarioDocuAggregator;
import ngusd.dao.UserScenarioDocuFilesystem;
import ngusd.model.docu.aggregates.branches.BranchBuilds;
import ngusd.model.docu.aggregates.branches.BuildLink;

/**
 * Manages the user scenario docu file contents.
 * 
 * As soon as a new build or branch is added to one of the branch directories
 * the manager takes care of aggregating and precalculating all needed data for
 * this build in the form needed to access it efficiently.
 * 
 * Only after this information is calculated correctly the build will be
 * available for browsing.
 * 
 * TODO: ensure that the manager runs updateAll from time to time when it
 * detects changes in the documentation directory.
 */
public class UserScenarioDocuManager {
	
	public static UserScenarioDocuManager INSTANCE = new UserScenarioDocuManager();
	
	private UserScenarioDocuManager() {
	}
	
	private final UserScenarioDocuFilesystem filesystem = new UserScenarioDocuFilesystem();
	
	private List<BranchBuilds> branchBuildsList = new ArrayList<BranchBuilds>();
	
	/**
	 * Processes the content of configured documentation filesystem directory
	 * discovering newly added builds or branches to calculate all data for
	 * them. Also updates the branches and builds list.
	 * 
	 * This method should be called on server startup and whenever something on
	 * the filesystem changed.
	 */
	public void updateAll() {
		List<BranchBuilds> branchBuildsList = filesystem.loadBranchBuildsList();
		for (BranchBuilds branchBuilds : branchBuildsList) {
			for (BuildLink buildLink : branchBuilds.getBuilds()) {
				UserScenarioDocuAggregator aggregator = new UserScenarioDocuAggregator();
				if (!aggregator.containsAggregatedDataForBuild(branchBuilds.getBranch().getName(),
						buildLink.getLinkName())) {
					aggregator.calculateAggregatedDataForBuild(branchBuilds.getBranch().getName(),
							buildLink.getLinkName());
				}
			}
			sortBuilds(branchBuilds);
		}
		this.branchBuildsList = branchBuildsList;
	}
	
	/**
	 * Special sorting for builds: 1. default build 2. other tagged builds in
	 * order of their dates 3. other builds in order of their dates
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
	
	public List<BranchBuilds> getBranchBuildsList() {
		return branchBuildsList;
	}
	
}
