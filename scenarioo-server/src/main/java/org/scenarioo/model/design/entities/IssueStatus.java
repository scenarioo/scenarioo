package org.scenarioo.model.design.entities;

public enum IssueStatus {

	/**
	 * Status for new and not yet solved issues.
	 */
	OPEN("open"),

	/**
	 * Status for resolved issues.
	 */
	CLOSED("closed");

	IssueStatus(final String keyword) {
		this.keyword = keyword;
	}

	private String keyword;

	public String getKeyword() {
		return keyword;
	}

	public static String toKeywordNullSafe(final IssueStatus status) {
		if (status == null) {
			return null;
		}
		else {
			return status.getKeyword();
		}
	}

}