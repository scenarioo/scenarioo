package org.scenarioo.uitest.example.infrastructure;

import org.scenarioo.model.docu.entities.Status;

public enum BuildRun {

	JANUARY("2014-01-20", "1290FE2", Status.SUCCESS),
	FEBRUARY("2014-02-21", "1CAFE12" /* no status set to test status calulation based on scenarios */),
	MARCH("2014-03-19", "F398DA3" /* no status set to test status calulation based on scenarios */);

	private String date;
	private Status status;
	private String revision;

	/**
	 * Build run without status, to test whether status of build is correctly calculated from scenario
	 * statuses.
	 */
	private BuildRun(final String date, final String revision) {
		this(date, revision, null);
	}

	private BuildRun(final String date, final String revision, final Status status) {
		this.date = date;
		this.status = status;
		this.revision = revision;
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