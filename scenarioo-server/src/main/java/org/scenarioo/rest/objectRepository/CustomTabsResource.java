package org.scenarioo.rest.objectRepository;

import org.scenarioo.business.builds.ScenarioDocuBuildsManager;
import org.scenarioo.model.docu.aggregates.objects.CustomObjectTabTree;
import org.scenarioo.rest.base.AbstractBuildContentResource;
import org.scenarioo.rest.base.BuildIdentifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/branches/{branchName}/builds/{buildName}/customTabObjects/{tabId}")
public class CustomTabsResource extends AbstractBuildContentResource {

	@GetMapping
	public CustomObjectTabTree readObjectTreeForTab(@PathVariable("branchName") final String branchName,
			@PathVariable("buildName") final String buildName, @PathVariable("tabId") final String tabId) {

		BuildIdentifier buildIdentifier = ScenarioDocuBuildsManager.INSTANCE.resolveBranchAndBuildAliases(branchName,
				buildName);

		return getDAO(buildIdentifier).loadCustomObjectTabTree(buildIdentifier, tabId);
	}

}
