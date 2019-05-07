package org.scenarioo.rest.diffViewer;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.scenarioo.model.diffViewer.BuildDiffInfo;
import org.scenarioo.model.diffViewer.ComparisonCalculationStatus;
import org.scenarioo.rest.base.BuildIdentifier;
import org.scenarioo.rest.integrationtest.IntegrationTestBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:test-application.properties")
public class ComparisonsResourceIntegrationTest extends IntegrationTestBase {
	@Autowired
	private TestRestTemplate testRestTemplate;

	@Test
	public void getCalculation_should_return_calculation() {
		//arrange
		HttpHeaders headers = new HttpHeaders();

		HttpEntity<Object> entity = new HttpEntity<>(headers);

		//act
		ResponseEntity<BuildDiffInfo> response =
			testRestTemplate
				.withBasicAuth("scenarioo", "only4test")
				.exchange("/rest/builds/testBranch/testBuild/comparisons/testComparison", HttpMethod.GET, entity, BuildDiffInfo.class);

		//assert
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isNotNull();
		assertThat(response.getBody().getStatus()).isEqualTo(ComparisonCalculationStatus.SUCCESS);
		assertThat(response.getBody().getName()).isEqualTo("testComparison");
	}

	@Test
	public void getLog_should_return_log_if_build_exists() {
		//arrange
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.TEXT_PLAIN));

		HttpEntity<Object> entity = new HttpEntity<>(headers);

		//act
		ResponseEntity<String> response =
			testRestTemplate
				.withBasicAuth("scenarioo", "only4test")
				.exchange("/rest/builds/testBranch/testBuild/comparisons/testComparison/log", HttpMethod.GET, entity, String.class);

		//assert
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).contains("=== START OF BUILD COMPARISON ===");
		assertThat(response.getBody()).contains("Comparison testComparison on target build testBranch/testBuild for comparing with build testBranch/testOldBuild");
		assertThat(response.getBody()).contains("=== END OF BUILD COMPARISON (success) ===");
	}

	@Test
	public void importAndCompare_should_return_buildDiffInfo() {
		//arrange
		BuildIdentifier buildIdentifier = new BuildIdentifier();
		buildIdentifier.setBranchName("testBranch");
		buildIdentifier.setBuildName("testOldBuild");

		HttpEntity<BuildIdentifier> entity = new HttpEntity<>(buildIdentifier, new HttpHeaders());

		//act
		ResponseEntity<BuildDiffInfo> response =
			testRestTemplate
				.withBasicAuth("scenarioo", "only4test")
				.exchange("/rest/builds/testBranch/testBuild/comparisons/testNewComparison/importAndCompare", HttpMethod.POST, entity, BuildDiffInfo.class);

		//assert
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isNotNull();
		assertThat(response.getBody().getStatus()).isEqualTo(ComparisonCalculationStatus.SUCCESS);
		assertThat(response.getBody().getName()).isEqualTo("testNewComparison");
		assertThat(response.getBody().getBaseBuild().getBuildName()).isEqualTo("testBuild");
		assertThat(response.getBody().getCompareBuild().getBuildName()).isEqualTo("testOldBuild");
	}

	@Test
	public void calculate_should_create_new_comparison() {
		//arrange
		BuildIdentifier buildIdentifier = new BuildIdentifier();
		buildIdentifier.setBranchName("testBranch");
		buildIdentifier.setBuildName("testOldBuild");

		HttpEntity<BuildIdentifier> entity = new HttpEntity<>(buildIdentifier, new HttpHeaders());

		//act
		ResponseEntity<Object> response =
			testRestTemplate
				.withBasicAuth("scenarioo", "only4test")
				.exchange("/rest/builds/testBranch/testBuild/comparisons/testNewAsyncComparison/calculate", HttpMethod.POST, entity, Object.class);

		//assert
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertComparisonWasCreated("testNewAsyncComparison");
	}

	@Test
	public void recalculate_should_queue_existing_comparison() {
		//act
		ResponseEntity<Object> response =
			testRestTemplate
				.withBasicAuth("scenarioo", "only4test")
				.exchange("/rest/builds/testBranch/testBuild/comparisons/testComparisonReversed/recalculate", HttpMethod.POST, null, Object.class);

		//assert
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertComparisonWasCreated("testComparisonReversed");
	}

	private void assertComparisonWasCreated(String comparisonName) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.TEXT_PLAIN));

		HttpEntity<Object> entity = new HttpEntity<>(headers);

		//act
		ResponseEntity<String> response =
			testRestTemplate
				.withBasicAuth("scenarioo", "only4test")
				.exchange("/rest/builds/testBranch/testBuild/comparisons/" + comparisonName + "/calculationStatus", HttpMethod.GET, entity, String.class);

		//assert
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isIn(ComparisonCalculationStatus.SUCCESS.name(), ComparisonCalculationStatus.PROCESSING.name(), ComparisonCalculationStatus.QUEUED_FOR_PROCESSING.name());
	}
}
