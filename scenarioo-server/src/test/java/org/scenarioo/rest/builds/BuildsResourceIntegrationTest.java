package org.scenarioo.rest.builds;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:test-application.properties")
class BuildsResourceIntegrationTest {

	@Autowired
	private TestRestTemplate testRestTemplate;

	@Test
	void should_reject_post_of_new_build_when_unauthorized() {
		ResponseEntity<String> response =
			testRestTemplate
				.postForEntity("/rest/builds", null, String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
	}

	@Test
	void should_allow_post_of_new_build_when_authorized() {
		ResponseEntity<String> response =
			testRestTemplate
				.withBasicAuth("scenarioo", "only4test")
				.postForEntity("/rest/builds", null, String.class);

		// Not really a positive test (yet), but it shows that authorization was successful
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNSUPPORTED_MEDIA_TYPE);
	}


}
