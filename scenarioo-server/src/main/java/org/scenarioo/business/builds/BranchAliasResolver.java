package org.scenarioo.business.builds;

import org.scenarioo.model.configuration.BranchAlias;
import org.scenarioo.model.configuration.Configuration;
import org.scenarioo.repository.ConfigurationRepository;
import org.scenarioo.repository.RepositoryLocator;

import java.util.List;

public class BranchAliasResolver {

	private final static ConfigurationRepository configurationRepository =
		RepositoryLocator.INSTANCE.getConfigurationRepository();

	public String resolveBranchAlias(final String aliasOrRealBranchName) {
		Configuration configuration = configurationRepository.getConfiguration();
		List<BranchAlias> branchAliases = configuration.getBranchAliases();
		for (BranchAlias branchAlias : branchAliases) {
			if (branchAlias.getName().equals(aliasOrRealBranchName)) {
				return branchAlias.getReferencedBranch();
			}
		}

		return aliasOrRealBranchName;
	}

}
