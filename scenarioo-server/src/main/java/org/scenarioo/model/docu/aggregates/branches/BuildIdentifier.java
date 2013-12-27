package org.scenarioo.model.docu.aggregates.branches;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
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
	
}
