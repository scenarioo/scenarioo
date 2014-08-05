package org.scenarioo.rest.request;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

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
	
	public void setBranchName(final String branchName) {
		this.branchName = branchName;
	}
	
	public String getBuildName() {
		return buildName;
	}
	
	public void setBuildName(final String buildName) {
		this.buildName = buildName;
	}
	
	@Override
	public String toString() {
		return new ToStringBuilder(this).append(branchName).append(buildName).toString();
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(branchName).append(buildName).toHashCode();
	}
	
	@Override
	public boolean equals(final Object obj) {
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
		return new EqualsBuilder().append(branchName, other.getBranchName()).append(buildName, other.getBuildName())
				.isEquals();
	}
	
}
