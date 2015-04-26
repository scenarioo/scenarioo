/* scenarioo-server
 * Copyright (C) 2015, scenarioo.org Development Team
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
package org.scenarioo.dao.design.aggregates;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.scenarioo.api.util.xml.ScenarioDocuXMLFileUtil;
import org.scenarioo.business.builds.BuildLink;
import org.scenarioo.dao.design.aggregates.issues.IssueProposalsList;
import org.scenarioo.model.design.aggregates.IssueProposals;
import org.scenarioo.model.docu.aggregates.branches.BuildImportSummary;
import org.scenarioo.model.docu.aggregates.objects.CustomObjectTabTree;
import org.scenarioo.model.docu.aggregates.objects.LongObjectNamesResolver;
import org.scenarioo.model.docu.aggregates.objects.ObjectIndex;
import org.scenarioo.model.docu.aggregates.steps.StepLink;
import org.scenarioo.model.docu.aggregates.steps.StepNavigation;
import org.scenarioo.model.docu.entities.generic.ObjectDescription;
import org.scenarioo.model.docu.entities.generic.ObjectList;
import org.scenarioo.model.docu.entities.generic.ObjectReference;
import org.scenarioo.rest.base.BuildIdentifier;
import org.scenarioo.rest.base.ScenarioIdentifier;
import org.scenarioo.utils.ResourceUtils;
import org.scenarioo.utils.design.readers.DesignReader;

public class IssueAggregationDAO {

	private static final Logger LOGGER = Logger.getLogger(IssueAggregationDAO.class);

	private static final String VERSION_PROPERTY_KEY = "scenarioo.derived.file.format.version";

	final File rootDirectory;
	private final DesignAggregateFiles files;
	private final DesignReader designReader;

	private LongObjectNamesResolver longObjectNameResolver = null;
	DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");

	public IssueAggregationDAO(final File rootDirectory) {
		this.rootDirectory = rootDirectory;
		files = new DesignAggregateFiles(rootDirectory);
		designReader = new DesignReader(rootDirectory);
	}

	public IssueAggregationDAO(final File rootDirectory, final LongObjectNamesResolver longObjectNameResolver) {
		this(rootDirectory);
		this.longObjectNameResolver = longObjectNameResolver;
	}

	/**
	 * @see org.scenarioo.dao.design.aggregates.AggregatedDataReader#loadVersion(java.lang.String, java.lang.String)
	 */
	public String loadVersion(final BuildIdentifier buildIdentifier) {
		File versionFile = files.getVersionFile(buildIdentifier);
		if (versionFile.exists()) {
			Properties properties = new Properties();
			FileReader reader = null;
			try {
				reader = new FileReader(versionFile);
				properties.load(reader);
				return properties.getProperty(VERSION_PROPERTY_KEY);
			} catch (FileNotFoundException e) {
				throw new RuntimeException("file not found: " + versionFile.getAbsolutePath(), e);
			} catch (IOException e) {
				throw new RuntimeException("file not readable: " + versionFile.getAbsolutePath(), e);
			} finally {
				ResourceUtils.close(reader, versionFile.getAbsolutePath());
			}
		} else {
			return "";
		}
	}


	public List<IssueProposals> loadIssueProposalsList(final BuildIdentifier buildIdentifier) {
		File file = files.getIssuesAndProposalsFile(buildIdentifier);
		IssueProposalsList list = ScenarioDocuXMLFileUtil.unmarshal(IssueProposalsList.class, file);
		return list.getIssueProposals();
	}


	public IssueProposals loadIssueProposals(final BuildIdentifier buildIdentifier, final String issueName) {
		File file = files.getIssueProposalsFile(buildIdentifier, issueName);
		IssueProposals issueProposals = ScenarioDocuXMLFileUtil.unmarshal(IssueProposals.class, file);
		return issueProposals;
	}


	// public ProposalSteps loadProposalSteps(final ScenarioIdentifier proposalIdentifier) {
	// // TODO Auto-generated method stub
	// return null;
	// }


	public ObjectDescription loadObjectDescription(final BuildIdentifier buildIdentifier, final ObjectReference objectRef) {
		// TODO Auto-generated method stub
		return null;
	}


	public ObjectDescription loadObjectDescription(final File file) {
		// TODO Auto-generated method stub
		return null;
	}


	public ObjectIndex loadObjectIndex(final BuildIdentifier buildIdentifier, final String objectType, final String objectName) {
		// TODO Auto-generated method stub
		return null;
	}


	public ObjectList<ObjectDescription> loadObjectsList(final BuildIdentifier buildIdentifier, final String type) {
		// TODO Auto-generated method stub
		return null;
	}


	public CustomObjectTabTree loadCustomObjectTabTree(final BuildIdentifier buildIdentifier, final String tabId) {
		// TODO Auto-generated method stub
		return null;
	}


	public ObjectIndex loadObjectIndexIfExistant(final BuildIdentifier buildIdentifier, final String objectType, final String objectName) {
		// TODO Auto-generated method stub
		return null;
	}


	public List<BuildImportSummary> loadBuildImportSummaries() {
		// TODO Auto-generated method stub
		return null;
	}


	public LongObjectNamesResolver loadLongObjectNamesIndex(final BuildIdentifier buildIdentifier) {
		// TODO Auto-generated method stub
		return null;
	}


	public StepNavigation loadStepNavigation(final BuildIdentifier build, final StepLink step) {
		// TODO Auto-generated method stub
		return null;
	}


	public StepNavigation loadStepNavigation(final ScenarioIdentifier scenarioIdentifier, final int stepIndex) {
		// TODO Auto-generated method stub
		return null;
	}


	public List<BuildLink> loadBuildLinks(final String branchName) {
		// TODO Auto-generated method stub
		return null;
	}

}
