package org.scenarioo.model.docu.aggregates.branches;

public class BuildIdentifier {

	private String branchName;
	private String buildName;

	public BuildIdentifier() {
	}

	public BuildIdentifier(final String branchName, final String buildName) {
		super();
		this.branchName = branchName;
		this.buildName = buildName;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public String getBuildName() {
		return buildName;
	}

	public void setBuildName(String buildName) {
		this.buildName = buildName;
	}
	
	@Override
	public String toString() {
		return "[branch: " + branchName + ", build: " + buildName + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((branchName == null) ? 0 : branchName.hashCode());
		result = prime * result
				+ ((buildName == null) ? 0 : buildName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		BuildIdentifier other = (BuildIdentifier) obj;
		if (branchName == null) {
			if (other.branchName != null) {
				return false;
			}
		} else if (!branchName.equals(other.branchName)) {
			return false;
		}
		if (buildName == null) {
			if (other.buildName != null) {
				return false;
			}
		} else if (!buildName.equals(other.buildName)) {
			return false;
		}
		return true;
	}

}
