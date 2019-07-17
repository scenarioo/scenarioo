package org.scenarioo.rest.builds;

import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;
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

	// waitForImportToFinish might run into problems and run endlessly. Thus we ensure that the test terminates in time.
	@Rule
	public Timeout timeout = Timeout.seconds(30);

	@Autowired
	private TestRestTemplate testRestTemplate;

	@AfterClass
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
	public void should_allow_post_of_new_build_when_authorized() throws IOException, InterruptedException {
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

		waitForImportToFinish();
	}

	// we have to wait for the import to finish, otherwise deleting the uploaded files fails and as a consequence, the test fails as well.
	private void waitForImportToFinish() throws InterruptedException {
		ResponseEntity<String> response;
		do {
			Thread.sleep(300);
			response =
				testRestTemplate
					.getForEntity("/rest/builds/importsAndComparisonCalculationsFinished", String.class);
		} while(Boolean.FALSE.toString().equalsIgnoreCase(response.getBody()));
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
