package org.scenarioo.business.builds;

import org.scenarioo.rest.base.BuildIdentifier;

/**
 * We can't inject the {@link ScenarioDocuBuildsManager} directly in the
 * {@link org.scenarioo.business.diffViewer.ComparisonExecutor} because at the point in time where the constructor
 * is executed the {@link ScenarioDocuBuildsManager} can be null. This is caused by a circular dependency of
 * {@link org.scenarioo.business.diffViewer.ComparisonExecutor}, {@link ScenarioDocuBuildsManager} and
 * {@link BuildImporter} which we should resolve sooner or later.
 *
 * As a workaround the {@link LazyAliasResolver} gets the {@link ScenarioDocuBuildsManager} instance each time
 * a method is called.
 */
public class LazyAliasResolver implements AliasResolver {

	@Override
	public BuildIdentifier resolveBranchAndBuildAliases(String branchName, String buildName) {
		return ScenarioDocuBuildsManager.INSTANCE.resolveBranchAndBuildAliases(branchName, buildName);
	}

	@Override
	public String resolveBranchAlias(String aliasOrRealBranchName) {
		return ScenarioDocuBuildsManager.INSTANCE.resolveBranchAlias(aliasOrRealBranchName);
	}

}
