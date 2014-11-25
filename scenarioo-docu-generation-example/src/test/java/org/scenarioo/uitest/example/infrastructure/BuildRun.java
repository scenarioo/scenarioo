package org.scenarioo.uitest.example.infrastructure;

import org.scenarioo.model.docu.entities.Status;

public enum BuildRun {
	
	JANUARY("2014-01-20", Status.SUCCESS, "1290FE2"),
	FEBRUARY("2014-02-21", Status.FAILED, "1CAFE12"),
	MARCH("2014-03-19", Status.SUCCESS, "F398DA3");
	
	private String date;
	private Status status;
	private String revision;

	private BuildRun(String date, Status status, String revision) {
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