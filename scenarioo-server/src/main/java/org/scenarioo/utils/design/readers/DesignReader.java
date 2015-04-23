package org.scenarioo.utils.design.readers;

import static org.scenarioo.api.rules.CharacterChecker.*;

import java.io.File;
import java.util.List;

import org.scenarioo.api.util.xml.ScenarioDocuXMLFileUtil;
import org.scenarioo.dao.design.entities.DesignFiles;
import org.scenarioo.model.design.entities.Issue;
import org.scenarioo.model.design.entities.Proposal;
import org.scenarioo.model.docu.entities.Branch;
import org.scenarioo.model.docu.entities.Step;

/**
 * Gives access to the design domain files in the filesystem.
 */
public class DesignReader {

	private final DesignFiles designFiles;

	public DesignReader(final File rootDirectory) {
		this.designFiles = new DesignFiles(rootDirectory);
	}

	public Branch loadBranch(final String branchName) {
		File file = designFiles.getBranchFile(checkIdentifier(branchName));
		return ScenarioDocuXMLFileUtil.unmarshal(Branch.class, file);
	}

	public List<Branch> loadBranches() {
		List<File> branchFiles = designFiles.getBranchFiles();
		return ScenarioDocuXMLFileUtil.unmarshalListOfFiles(Branch.class, branchFiles);
	}

	public List<Issue> loadIssues(final String branchName) {
		List<File> files = designFiles.getIssueFiles(checkIdentifier(branchName));
		return ScenarioDocuXMLFileUtil.unmarshalListOfFiles(Issue.class, files);
	}

	public Issue loadIssue(final String branchName, final String issueName) {
		File file = designFiles.getIssueFile(checkIdentifier(branchName), checkIdentifier(issueName));
		return ScenarioDocuXMLFileUtil.unmarshal(Issue.class, file);
	}

	public List<Proposal> loadProposals(final String branchName, final String issueName) {
		List<File> files = designFiles.getProposalFiles(checkIdentifier(branchName), checkIdentifier(issueName));
		return ScenarioDocuXMLFileUtil.unmarshalListOfFiles(Proposal.class, files);
	}

	public Proposal loadProposal(final String branchName, final String issueName,
			final String proposalName) {
		File file = designFiles.getProposalFile(checkIdentifier(branchName), checkIdentifier(issueName),
				checkIdentifier(proposalName));
		return ScenarioDocuXMLFileUtil.unmarshal(Proposal.class, file);
	}

	public List<Step> loadSteps(final String branchName, final String issueName, final String proposalName) {
		List<File> files = designFiles.getStepFiles(checkIdentifier(branchName), checkIdentifier(issueName),
				checkIdentifier(proposalName));
		return ScenarioDocuXMLFileUtil.unmarshalListOfFiles(Step.class, files);
	}

	public Step loadStep(final String branchName, final String issueName,
			final String proposalName, final int stepIndex) {
		File file = designFiles.getStepFile(checkIdentifier(branchName), checkIdentifier(issueName),
				checkIdentifier(proposalName), stepIndex);
		return ScenarioDocuXMLFileUtil.unmarshal(Step.class, file);
	}

	/**
	 * Screenshot files are simply provided by path, the REST service will take care of streaming it.
	 */
	public File getScreenshotFile(final String branchName, final String issueName, final String proposalName,
			final String imageName) {
		return new File(designFiles.getScreenshotsDirectory(checkIdentifier(branchName),
				checkIdentifier(issueName), checkIdentifier(proposalName)), imageName);
	}

}