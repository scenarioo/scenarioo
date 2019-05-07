package org.scenarioo.rest.builds;

import org.junit.*;
import org.junit.runner.RunWith;
import org.scenarioo.rest.integrationtest.IntegrationTestBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.LinkedHashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:test-application.properties")
public class BranchBuildsResourceIntegrationTest extends IntegrationTestBase {

	@Autowired
	private TestRestTemplate testRestTemplate;

	@Test
	public void listBranchesAndBuilds_should_return_defined_branches_and_builds() {
		//act
		ResponseEntity<List> response =
			testRestTemplate
				.withBasicAuth("scenarioo", "only4test")
				.getForEntity("/rest/branches", List.class);

		//assert
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).hasSize(1);
		LinkedHashMap actual = (LinkedHashMap) response.getBody().get(0);
		assertThat(actual.get("branch")).isInstanceOf(LinkedHashMap.class);
		assertThat(((LinkedHashMap<String,String>)actual.get("branch")).get("name")).isEqualTo("testBranch");
		assertThat(actual.get("builds")).isInstanceOf(List.class);
		assertThat((List)actual.get("builds")).hasSize(4);
		assertThat(((List) actual.get("builds")).get(0)).isInstanceOf(LinkedHashMap.class);
		LinkedHashMap<String,String> testBuild = (LinkedHashMap<String, String>) ((List) actual.get("builds")).get(0);
		assertThat(((LinkedHashMap<String, String>) ((List) actual.get("builds")).get(0)).get("linkName")).isEqualTo("testBuild");
	}

}
