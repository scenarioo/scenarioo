package org.scenarioo.rest.builds;

import org.apache.commons.io.FileUtils;
import org.junit.BeforeClass;
import org.junit.Test;
import org.scenarioo.rest.integrationtest.AbstractIntegrationTest;
import org.scenarioo.utils.TestResourceFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.assertj.core.api.Assertions.assertThat;

public class BuildsResourceIntegrationTest extends AbstractIntegrationTest {

	private static final String UPLOADED_FOLDER_NAME = "pizza-delivery-feature-update-dependencies";

	@Autowired
	private TestRestTemplate testRestTemplate;

	@BeforeClass
	public static void cleanUpUploadedFiles() throws IOException {
		File scenariooConfigurationFolder = ScenariooDataPropertyInitializer.getScenariooConfigurationFolder();
		File uploadedBuildFolder = new File(scenariooConfigurationFolder, UPLOADED_FOLDER_NAME);
		if(uploadedBuildFolder.exists()) {
			FileUtils.deleteDirectory(uploadedBuildFolder);
		}
	}

	@Test
	public void should_reject_post_of_new_build_when_unauthorized() {
		ResponseEntity<String> response =
			testRestTemplate
				.postForEntity("/rest/builds", noRequestEntity(), String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
	}

	@Test
	public void should_allow_post_of_new_build_when_authorized() throws IOException {
		//arrange
		HttpEntity<?> request = createRequestToUploadSmallZipFile();

		//act
		ResponseEntity<String> response =
			testRestTemplate
				.withBasicAuth("scenarioo", "only4test")
				.exchange("/rest/builds", HttpMethod.POST, request, String.class);

		//assert
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).contains("Build successfully added to Scenarioo.");
	}

	private HttpEntity<?> createRequestToUploadSmallZipFile() throws IOException {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);

		File uploadFile = TestResourceFile.getResourceFile("org/scenarioo/rest/builds/exampleBuildForUploadTest.zip");

		// This nested HttpEntiy is important to create the correct Content-Disposition entry with metadata "name" and "filename"
		MultiValueMap<String, String> fileMap = new LinkedMultiValueMap<>();
		ContentDisposition contentDisposition = ContentDisposition
			.builder("form-data")
			.name("file")
			.filename(uploadFile.getName())
			.build();
		fileMap.add(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString());
		HttpEntity<byte[]> fileEntity = new HttpEntity<>(Files.readAllBytes(uploadFile.toPath()), fileMap);

		MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
		body.add("file", fileEntity);

		return new HttpEntity<>(body, headers);
	}
}
