package org.scenarioo.rest.diffViewer;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.scenarioo.model.diffViewer.ScenarioDiffInfo;
import org.scenarioo.model.diffViewer.StepDiffInfo;
import org.scenarioo.rest.integrationtest.IntegrationTestBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:test-application.properties")
public class StepDiffInfoResourceIntegrationTest extends IntegrationTestBase {

	@Autowired
	private TestRestTemplate testRestTemplate;

	@Test
	public void testGetStepDiffInfo() {
		//act
		ResponseEntity<StepDiffInfo> response =
			testRestTemplate
				.withBasicAuth("scenarioo", "only4test")
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
		ResponseEntity<Map> response =
			testRestTemplate
				.withBasicAuth("scenarioo", "only4test")
				.getForEntity("/rest/diffViewer/baseBranchName/testBranch/baseBuildName/testBuild/comparisonName/testComparison/useCaseName/testUseCase/scenarioName/testScenario/stepDiffInfos", Map.class);

		//assert
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isNotNull();
		//we expect at least two comparisons
		assertThat(response.getBody().size()).isEqualTo(2);
		assertThat(response.getBody().get("0")).isInstanceOf(Map.class);
		Map<String, Object> testStep = (Map<String, Object>) response.getBody().get("0");
		assertThat(testStep.get("comparisonScreenshotName")).isEqualTo("000.png");
	}
}
