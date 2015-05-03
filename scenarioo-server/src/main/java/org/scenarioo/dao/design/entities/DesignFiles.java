/* scenarioo-server
 * Copyright (C) 2015, scenarioo.org Development Team
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * As a special exception, the copyright holders of this library give you
 * permission to link this library with independent modules, according
 * to the GNU General Public License with "Classpath" exception as provided
 * in the LICENSE file that accompanied this code.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.scenarioo.dao.design.entities;

import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.List;

import org.scenarioo.api.util.files.FilesUtil;

/**
 * Represents the file structure of the design domain.
 */
public class DesignFiles {

	private static final String DIRECTORY_NAME_PROPOSAL_SCREENSHOTS = "screenshots";

	private static final String DIRECTORY_NAME_PROPOSAL_STEPS = "steps";

	private static final String FILE_NAME_PROPOSAL = "proposal.xml";

	private static final String FILE_NAME_ISSUE = "issue.xml";

	private static final String FILE_NAME_BRANCH = "branch.xml";

	private static NumberFormat THREE_DIGIT_NUM_FORMAT = createNumberFormatWithMinimumIntegerDigits(3);

	private final File rootDirectory;

	public DesignFiles(final File rootDirectory) {
		this.rootDirectory = rootDirectory;
	}

	public void assertRootDirectoryExists() {
		if (!rootDirectory.exists()) {
			throw new IllegalArgumentException("Directory for design storage does not exist: "
					+ rootDirectory.getAbsolutePath());
		}
	}

	public File getRootDirectory() {
		return rootDirectory;
	}

	public File getBranchDirectory(final String branchName) {
		File branchDirectory = new File(rootDirectory, FilesUtil.encodeName(branchName));
		return branchDirectory;
	}

	public File getBranchFile(final String branchName) {
		return new File(getBranchDirectory(branchName), FILE_NAME_BRANCH);
	}

	public List<File> getBranchFiles() {
		return FilesUtil.getListOfFilesFromSubdirs(rootDirectory, FILE_NAME_BRANCH);
	}

	public File getIssueDirectory(final String branchName, final String issueName) {
		File branchDirectory = new File(getBranchDirectory(branchName), FilesUtil.encodeName(issueName));
		return branchDirectory;
	}

	public File getIssueFile(final String branchName, final String issueName) {
		return new File(getIssueDirectory(branchName, issueName), FILE_NAME_ISSUE);
	}

	public List<File> getIssueFiles(final String branchName) {
		return FilesUtil.getListOfFilesFromSubdirs(getBranchDirectory(branchName), FILE_NAME_ISSUE);
	}

	public File getProposalDirectory(final String branchName, final String issueName, final String proposalName) {
		File branchDirectory = new File(getIssueDirectory(branchName, issueName), FilesUtil.encodeName(proposalName));
		return branchDirectory;
	}

	public File getProposalFile(final String branchName, final String issueName, final String proposalName) {
		return new File(getProposalDirectory(branchName, issueName, proposalName), FILE_NAME_PROPOSAL);
	}

	public List<File> getProposalFiles(final String branchName, final String issueName) {
		return FilesUtil.getListOfFilesFromSubdirs(getIssueDirectory(branchName, issueName), FILE_NAME_PROPOSAL);
	}

	public File getStepsDirectory(final String branchName, final String issueName, final String proposalName) {
		File branchDirectory = new File(getProposalDirectory(branchName, issueName, proposalName),
				DIRECTORY_NAME_PROPOSAL_STEPS);
		return branchDirectory;
	}

	public File getStepFile(final String branchName, final String issueName, final String proposalName,
			final int stepIndex) {
		return new File(getStepsDirectory(branchName, issueName, proposalName),
				THREE_DIGIT_NUM_FORMAT.format(stepIndex) + ".xml");
	}

	public List<File> getStepFiles(final String branchName, final String issueName, final String proposalName) {
		return FilesUtil.getListOfFiles(getStepsDirectory(branchName, issueName, proposalName));
	}

	public File getScreenshotsDirectory(final String branchName, final String issueName,
			final String proposalName) {
		return new File(getProposalDirectory(branchName, issueName, proposalName), DIRECTORY_NAME_PROPOSAL_SCREENSHOTS);
	}

	/**
	 * @return A {@link File} object pointing to the PNG file of the step screenshot. The method does not care whether
	 *         the file actually exists.
	 */
	public File getScreenshotFile(final String branchName, final String issueName,
			final String proposalName, final int stepIndex) {
		return new File(getScreenshotsDirectory(branchName, issueName, proposalName),
				THREE_DIGIT_NUM_FORMAT.format(stepIndex) + ".png");
	}

	private static NumberFormat createNumberFormatWithMinimumIntegerDigits(
			final int minimumIntegerDigits) {
		final NumberFormat numberFormat = NumberFormat.getIntegerInstance();
		numberFormat.setMinimumIntegerDigits(minimumIntegerDigits);
		return numberFormat;
	}

	public boolean createIssueDirectory(final String branchName, final String issueName) {
		File issueDirectory = new File(getBranchDirectory(branchName), FilesUtil.encodeName(issueName));
		return issueDirectory.mkdir();
	}

	public File createIssueFile(final String branchName, final String issueName) {
		File issueFile = new File(getIssueDirectory(branchName, issueName), FILE_NAME_ISSUE);
		try {
			issueFile.createNewFile();
			return issueFile;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return issueFile;
	}

}
