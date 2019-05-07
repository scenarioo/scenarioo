package org.scenarioo.rest.diffViewer;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.scenarioo.model.diffViewer.ComparisonCalculationStatus;
import org.scenarioo.rest.integrationtest.AbstractIntegrationTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:test-application.properties")
public class ComparisonCalculationsResourceIntegrationTest extends AbstractIntegrationTest {

	@Autowired
	private TestRestTemplate testRestTemplate;

	@Test
	public void testListAllComparisonResults() {
		//act
		ResponseEntity<List> response =
			testRestTemplate
				.withBasicAuth("scenarioo", "only4test")
				.getForEntity("/rest/comparisons", List.class);

		//assert
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isNotNull();
		//we expect at least two comparisons
		assertThat(response.getBody().size()).isGreaterThan(1);
		assertThat(response.getBody().get(0)).isInstanceOf(Map.class);
		Map<String, Object> firstComparison = (Map<String, Object>) response.getBody().get(0);
		assertThat(firstComparison.get("name")).isEqualTo("testComparison");
		assertThat(firstComparison.get("status")).isEqualTo(ComparisonCalculationStatus.SUCCESS.name());
	}
}
