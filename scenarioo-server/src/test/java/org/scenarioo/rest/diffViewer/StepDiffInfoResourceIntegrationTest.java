package org.scenarioo.rest.diffViewer;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.scenarioo.model.diffViewer.StepDiffInfo;
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
public class StepDiffInfoResourceIntegrationTest extends AbstractIntegrationTest {

	@Autowired
	private TestRestTemplate testRestTemplate;

	@Test
	public void testGetStepDiffInfo() {
		//act
		ResponseEntity<StepDiffInfo> response =
			testRestTemplate
				.getForEntity("/rest/diffViewer/baseBranchName/testBranch/baseBuildName/testBuild/comparisonName/testComparison/useCaseName/testUseCase/scenarioName/testScenario/stepIndex/1/stepDiffInfo", StepDiffInfo.class);

		//assert
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isNotNull();
		StepDiffInfo result = response.getBody();
		assertThat(result.getIndex()).isEqualTo(1);
		assertThat(result.getChangeRate()).isEqualTo(0);
	}

	@Test
	public void testGetStepDiffInfos() {
		//act
		ResponseEntity<Map<Integer, StepDiffInfo>> response = testRestTemplate.exchange(
			"/rest/diffViewer/baseBranchName/testBranch/baseBuildName/testBuild/comparisonName/testComparison/useCaseName/testUseCase/scenarioName/testScenario/stepDiffInfos", GET,
			noRequestEntity(),
			new ParameterizedTypeReference<Map<Integer, StepDiffInfo>>() {
			});

		//assert
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody())
			.hasSize(2)
			.extracting(0)
			.extracting("comparisonScreenshotName")
			.containsOnly("000.png");
	}
}
