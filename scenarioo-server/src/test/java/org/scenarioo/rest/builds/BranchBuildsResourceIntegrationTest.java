package org.scenarioo.rest.builds;

import org.junit.BeforeClass;
import org.junit.Test;
import org.scenarioo.business.builds.BuildLink;
import org.scenarioo.model.docu.aggregates.branches.BranchBuilds;
import org.scenarioo.rest.integrationtest.AbstractIntegrationTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.GET;

public class BranchBuildsResourceIntegrationTest extends AbstractIntegrationTest {

	@Autowired
	private TestRestTemplate testRestTemplate;

	@BeforeClass
	public static void ensureNoUnexpectedBuildsArePresent() throws IOException {
		BuildsResourceIntegrationTest.cleanUpUploadedFiles();
	}

	@Test
	public void listBranchesAndBuilds_should_return_defined_branches_and_builds() {
		//act
		ResponseEntity<List<BranchBuilds>> response = testRestTemplate.exchange(
			"/rest/branches", GET,
			noRequestEntity(),
			new ParameterizedTypeReference<List<BranchBuilds>>() {
			});

		//assert
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		List<BranchBuilds> actual = response.getBody();
		assertThat(actual).hasSize(1);

		BranchBuilds actualBranchBuilds = actual.get(0);
		assertThat(actualBranchBuilds.getBranch().getName()).isEqualTo("testBranch");
		assertThat(actualBranchBuilds.getBuilds())
			.hasSize(4)
			.extracting(BuildLink::getLinkName)
			.contains("testBuild");
	}
}
