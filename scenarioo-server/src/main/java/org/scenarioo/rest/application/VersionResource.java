package org.scenarioo.rest.application;

import org.scenarioo.dao.version.ApplicationVersion;
import org.scenarioo.dao.version.ApplicationVersionHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/version")
public class VersionResource {

	@GetMapping
	public ApplicationVersion getVersionInformation() {
		return ApplicationVersionHolder.INSTANCE.getApplicationVersion();
	}

}
