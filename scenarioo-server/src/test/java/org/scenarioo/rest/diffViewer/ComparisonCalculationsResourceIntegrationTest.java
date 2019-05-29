package org.scenarioo.rest.diffViewer;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.scenarioo.model.diffViewer.BuildDiffInfo;
import org.scenarioo.model.diffViewer.ComparisonCalculationStatus;
import org.scenarioo.rest.integrationtest.AbstractIntegrationTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.GET;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:test-application.properties")
public class ComparisonCalculationsResourceIntegrationTest extends AbstractIntegrationTest {

	@Autowired
	private TestRestTemplate testRestTemplate;

	@Test
	public void testListAllComparisonResults() {
		//act

		ResponseEntity<List<BuildDiffInfo>> response = testRestTemplate.exchange(
			"/rest/comparisons", GET,
			noRequestEntity(),
			new ParameterizedTypeReference<List<BuildDiffInfo>>() {
			});

		//assert
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isNotNull();
		//we expect at least two comparisons
		assertThat(response.getBody().size()).isGreaterThan(1);
		BuildDiffInfo resultBuildDiffInfo = response.getBody().get(0);
		assertThat(resultBuildDiffInfo.getName()).isEqualTo("testComparison");
		assertThat(resultBuildDiffInfo.getStatus()).isEqualTo(ComparisonCalculationStatus.SUCCESS);
	}
}
