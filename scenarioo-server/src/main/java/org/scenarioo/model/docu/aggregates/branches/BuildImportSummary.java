package org.scenarioo.model.docu.aggregates.branches;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.scenarioo.model.docu.entities.Build;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Data
@EqualsAndHashCode(of = { "identifier" })
public class BuildImportSummary {
	
	private BuildIdentifier identifier;
	
	private Build buildDescription;
	
	private BuildImportStatus status = BuildImportStatus.UNPROCESSED;
	
	private String statusMessage;
	
	private Date importDate = new Date();
	
	public BuildImportSummary() {
	}
	
	public BuildImportSummary(final String branchName, final Build build) {
		identifier = new BuildIdentifier(branchName, build.getName());
		buildDescription = build;
	}
}
