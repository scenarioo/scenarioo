package org.scenarioo.business.builds;

import org.scenarioo.rest.base.BuildIdentifier;

public interface AliasResolver {

	BuildIdentifier resolveBranchAndBuildAliases(String branchName, String buildName);

	String resolveBranchAlias(String aliasOrRealBranchName);

}
