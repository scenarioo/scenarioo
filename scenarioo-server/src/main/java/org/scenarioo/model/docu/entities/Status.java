package org.scenarioo.model.docu.entities;

/**
 * Those are the default status values that scenarioo knows and supports out of the box to set for all status fields in
 * scenarioo.
 *
 * But for all statuses there is also a more generic setter method where you can use any other application-specific
 * status simply as a string. All such unknown states will be interpreted and displayed as a warning status.
 */
public enum Status {

	/**
	 * Status for successfully succeeded and tested without errors.
	 */
	SUCCESS("success"),

	/**
	 * Status for failed testing the corresponding step, scenario, feature or build.
	 */
	FAILED("failed");

	Status(final String keyword) {
		this.keyword = keyword;
	}

	private String keyword;

	public String getKeyword() {
		return keyword;
	}

	public static String toKeywordNullSafe(final Status status) {
		if (status == null) {
			return null;
		}
		else {
			return status.getKeyword();
		}
	}
}
