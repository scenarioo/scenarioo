package org.scenarioo.rest.diffViewer;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.scenarioo.model.diffViewer.UseCaseDiffInfo;
import org.scenarioo.rest.integrationtest.AbstractIntegrationTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.GET;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:test-application.properties")
public class UseCaseDiffInfoResourceIntegrationTest extends AbstractIntegrationTest {

	@Autowired
	private TestRestTemplate testRestTemplate;

	@Test
	public void testGetUseCaseDiffInfo() {
		//act
		ResponseEntity<UseCaseDiffInfo> response =
			testRestTemplate
				.withBasicAuth("scenarioo", "only4test")
				.getForEntity("/rest/diffViewer/baseBranchName/testBranch/baseBuildName/testBuild/comparisonName/testComparison/useCaseName/testUseCase/useCaseDiffInfo", UseCaseDiffInfo.class);

		//assert
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isNotNull();
		UseCaseDiffInfo result = response.getBody();
		assertThat(result.getName()).isEqualTo("testUseCase");
		assertThat(result.getAdded()).isEqualTo(0);
		assertThat(result.getChanged()).isEqualTo(1);
		assertThat(result.getRemoved()).isEqualTo(0);
		assertThat(result.getChangeRate()).isBetween(33.3D, 33.4D);
	}

	@Test
	public void testGetUseCaseDiffInfos() {
		//act
		ResponseEntity<Map<String, UseCaseDiffInfo>> response = testRestTemplate.exchange(
			"/rest/diffViewer/baseBranchName/testBranch/baseBuildName/testBuild/comparisonName/testComparison/useCaseDiffInfos", GET,
			noRequestEntity(),
			new ParameterizedTypeReference<Map<String, UseCaseDiffInfo>>() {
			});

		//assert
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody())
			.hasSize(1)
			.extracting("testUseCase")
			.extracting("name")
			.containsOnly("testUseCase");
	}
}
