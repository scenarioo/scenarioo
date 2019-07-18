package org.scenarioo.rest.diffViewer;

import org.assertj.core.api.ObjectAssert;
import org.junit.jupiter.api.Test;
import org.scenarioo.model.diffViewer.BuildDiffInfo;
import org.scenarioo.model.diffViewer.ComparisonCalculationStatus;
import org.scenarioo.rest.integrationtest.AbstractIntegrationTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.GET;


class ComparisonCalculationsResourceIntegrationTest extends AbstractIntegrationTest {

	@Autowired
	private TestRestTemplate testRestTemplate;

	@Test
	void testListAllComparisonResults() {
		//act

		ResponseEntity<List<BuildDiffInfo>> response = testRestTemplate.exchange(
			"/rest/comparisons", GET,
			noRequestEntity(),
			new ParameterizedTypeReference<List<BuildDiffInfo>>() {
			});

		//assert
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		ObjectAssert<BuildDiffInfo> firstBuildDiffInfo = assertThat(response.getBody())
			.hasAtLeastOneElementOfType(BuildDiffInfo.class)
			.first();
		firstBuildDiffInfo
			.extracting(BuildDiffInfo::getName)
			.contains("testComparison");
		firstBuildDiffInfo
			.extracting(BuildDiffInfo::getStatus)
			.contains(ComparisonCalculationStatus.SUCCESS);
	}
}
