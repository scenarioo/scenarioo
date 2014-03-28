package org.scenarioo.model.docu.aggregates.branches;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.scenarioo.model.docu.entities.Build;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class BuildImportSummary {

	private BuildIdentifier identifier;

	private Build buildDescription;

	private BuildImportStatus status = BuildImportStatus.UNPROCESSED;

	private String statusMessage;

	private Date importDate = new Date();
	
	private BuildStatistics buildStatistics = new BuildStatistics();
	
	public BuildImportSummary() {
	}

	public BuildImportSummary(final String branchName, final Build build) {
		identifier = new BuildIdentifier(branchName, build.getName());
		buildDescription = build;
	}

	public BuildIdentifier getIdentifier() {
		return identifier;
	}

	public void setIdentifier(BuildIdentifier identifier) {
		this.identifier = identifier;
	}

	public Build getBuildDescription() {
		return buildDescription;
	}

	public void setBuildDescription(Build buildDescription) {
		this.buildDescription = buildDescription;
	}

	public BuildImportStatus getStatus() {
		return status;
	}

	public void setStatus(BuildImportStatus status) {
		this.status = status;
	}

	public String getStatusMessage() {
		return statusMessage;
	}

	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}

	public Date getImportDate() {
		return importDate;
	}

	public void setImportDate(Date importDate) {
		this.importDate = importDate;
	}

	public void setBuildStatistics(BuildStatistics buildStatistics) {
		this.buildStatistics = buildStatistics;
	}
	
	public BuildStatistics getBuildStatistics() {
		return buildStatistics;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((identifier == null) ? 0 : identifier.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
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
		BuildImportSummary other = (BuildImportSummary) obj;
		if (identifier == null) {
			if (other.identifier != null) {
				return false;
			}
		} else if (!identifier.equals(other.identifier)) {
			return false;
		}
		return true;
	}
}
