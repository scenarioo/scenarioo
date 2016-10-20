package org.scenarioo.uitest.example.infrastructure;

import org.scenarioo.model.docu.entities.Status;
import static org.scenarioo.uitest.example.config.ExampleUITestDocuGenerationConfig.*;

public enum BuildRun {

	JANUARY(EXAMPLE_BRANCH_NAME, "2014-01-20", "1290FE2", Status.SUCCESS),
	FEBRUARY(EXAMPLE_BRANCH_NAME, "2014-02-21", "1CAFE12" /* no status set to test status calculation based on scenarios */),
	MARCH(EXAMPLE_BRANCH_NAME, "2014-03-19", "F398DA3" /* no status set to test status calculation based on scenarios */),
	APRIL(WIKIPEADIA_EXAMPLE_DEVELOP, "2014-04-19", "F398DA5" /* no status set to test status calculation based on scenarios */),
	MAY(WIKIPEADIA_EXAMPLE_DEVELOP, "2014-05-19", "F398DA4" /* no status set to test status calculation based on scenarios */);

	private String branchName;
	private String date;
	private Status status;
	private String revision;

	/**
	 * Build run without status, to test whether status of build is correctly calculated from scenario
	 * statuses.
	 */
	BuildRun(final String branchName, final String date, final String revision) {
		this(branchName,date, revision, null);
	}

	BuildRun(final String branchName, final String date, final String revision, final Status status) {
		this.branchName = branchName;
		this.date = date;
		this.status = status;
		this.revision = revision;
	}

	public String getBranchName() {
		return branchName;
	}

	public String getDate() {
		return date;
	}

	public Status getStatus() {
		return status;
	}

	public String getRevision() {
		return revision;
	}

}
