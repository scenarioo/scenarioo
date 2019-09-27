package org.scenarioo.rest.step;

import org.junit.Test;
import org.scenarioo.rest.integrationtest.AbstractIntegrationTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.GET;

public class ScreenshotResourceTest extends AbstractIntegrationTest {

	// Chrome only shows Accept Headers when accessing an image with a direct link, so we assume that it is the same for when an image is shown on a webpage as well.
	private static final String ACCEPT_HEADER_CHROME = "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3";

	// Accept Header when loading an image that is shown in a webpage
	private static final String ACCEPT_HEADER_FIREFOX_IMAGE_IN_PAGE = "image/webp,*/*";
	private static final String ACCEPT_HEADER_IE11_EDGE_IMAGE_IN_PAGE = "image/png, image/svg+xml, image/jxr, image/*; q=0.8, */*; q=0.5";

	// Accept Header when loading an image with a direct link.
	private static final String ACCEPT_HEADER_IE11_DIRECT_LINK = "text/html, application/xhtml+xml, image/jxr, */*";
	private static final String ACCEPT_HEADER_EDGE_DIRECT_LINK = "text/html, application/xhtml+xml, application/xml; q=0.9, */*; q=0.8";
	private static final String ACCEPT_HEADER_FIREFOX_DIRECT_LINK = "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8";

	@Autowired
	private TestRestTemplate testRestTemplate;

	@Test
	public void getScreenshotPng_accept_header_similar_to_Firefox_should_return_content_type_png() {
		assertGetScreenshotPngReturnsContentTypePng(ACCEPT_HEADER_FIREFOX_IMAGE_IN_PAGE);
	}

	@Test
	public void getScreenshotPng_accept_header_similar_to_IE11_and_Edge_should_return_content_type_png() {
		assertGetScreenshotPngReturnsContentTypePng(ACCEPT_HEADER_IE11_EDGE_IMAGE_IN_PAGE);
	}

	@Test
	public void getScreenshotPng_accept_header_similar_to_Chrome_should_return_content_type_png() {
		assertGetScreenshotPngReturnsContentTypePng(ACCEPT_HEADER_CHROME);
	}

	@Test
	public void getScreenshotJpg_accept_header_similar_to_Firefox_should_return_content_type_jpeg() {
		assertGetScreenshotJpgReturnsContentTypeJpeg(ACCEPT_HEADER_FIREFOX_IMAGE_IN_PAGE);
	}

	@Test
	public void getScreenshotJpg_accept_header_similar_to_IE11_and_Edge_should_return_content_type_jpeg() {
		assertGetScreenshotJpgReturnsContentTypeJpeg(ACCEPT_HEADER_IE11_EDGE_IMAGE_IN_PAGE);
	}

	@Test
	public void getScreenshotJpg_accept_header_similar_to_Chrome_should_return_content_type_jpeg() {
		assertGetScreenshotJpgReturnsContentTypeJpeg(ACCEPT_HEADER_CHROME);
	}

	@Test
	public void getScreenshotStablePng_accept_header_similar_to_Firefox_should_return_content_type_png() {
		assertGetScreenshotStablePngReturnsContentTypePng(ACCEPT_HEADER_FIREFOX_DIRECT_LINK);
	}

	@Test
	public void getScreenshotStablePng_accept_header_similar_to_IE11_should_return_content_type_png() {
		assertGetScreenshotStablePngReturnsContentTypePng(ACCEPT_HEADER_IE11_DIRECT_LINK);
	}

	@Test
	public void getScreenshotStablePng_accept_header_similar_to_Edge_should_return_content_type_png() {
		assertGetScreenshotStablePngReturnsContentTypePng(ACCEPT_HEADER_EDGE_DIRECT_LINK);
	}

	@Test
	public void getScreenshotStablePng_accept_header_similar_to_Chrome_should_return_content_type_png() {
		assertGetScreenshotStablePngReturnsContentTypePng(ACCEPT_HEADER_CHROME);
	}

	@Test
	public void getScreenshotStableJpg_accept_header_similar_to_Firefox_should_return_content_type_jpeg() {
		assertGetScreenshotStableJpgReturnsContentTypeJpeg(ACCEPT_HEADER_FIREFOX_DIRECT_LINK);
	}

	@Test
	public void getScreenshotStableJpg_accept_header_similar_to_IE11_should_return_content_type_jpeg() {
		//act
		assertGetScreenshotStableJpgReturnsContentTypeJpeg(ACCEPT_HEADER_IE11_DIRECT_LINK);
	}

	@Test
	public void getScreenshotStableJpg_accept_header_similar_to_Edge_should_return_content_type_jpeg() {
		//act
		assertGetScreenshotStableJpgReturnsContentTypeJpeg(ACCEPT_HEADER_EDGE_DIRECT_LINK);
	}

	@Test
	public void getScreenshotStableJpg_accept_header_similar_to_Chrome_should_return_content_type_jpeg() {
		//act
		assertGetScreenshotStableJpgReturnsContentTypeJpeg(ACCEPT_HEADER_CHROME);
	}

	private void assertGetScreenshotPngReturnsContentTypePng(String acceptHeaderValue) {
		//act
		ResponseEntity<InputStreamResource> response = testRestTemplate
			.exchange(
				"/rest/branch/testBranch/build/testBuild/usecase/testUseCase/scenario/testScenario/image/000.png", GET,
				getEntityWithRequestHeaders(acceptHeaderValue),
				new ParameterizedTypeReference<InputStreamResource>() {
				});

		//assert
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getHeaders().getContentType()).isEqualTo(MediaType.IMAGE_PNG);
	}

	private void assertGetScreenshotJpgReturnsContentTypeJpeg(String acceptHeaderValue) {
		//act
		ResponseEntity<InputStreamResource> response = testRestTemplate
			.exchange(
				"/rest/branch/testBranch/build/testBuild/usecase/testUseCase/scenario/testScenario/image/001.jpg", GET,
				getEntityWithRequestHeaders(acceptHeaderValue),
				new ParameterizedTypeReference<InputStreamResource>() {
				});

		//assert
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getHeaders().getContentType()).isEqualTo(MediaType.IMAGE_JPEG);
	}

	private void assertGetScreenshotStablePngReturnsContentTypePng(String acceptHeaderValue) {
		//act
		ResponseEntity<InputStreamResource> response = testRestTemplate
			.exchange(
				"/rest/branch/testBranch/build/testBuild/usecase/testUseCase/scenario/testScenario/pageName/unknownPage/pageOccurrence/0/stepInPageOccurrence/0/image.png", GET,
				getEntityWithRequestHeaders(acceptHeaderValue),
				new ParameterizedTypeReference<InputStreamResource>() {
				});

		//assert
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getHeaders().getContentType()).isEqualTo(MediaType.IMAGE_PNG);
	}

	private void assertGetScreenshotStableJpgReturnsContentTypeJpeg(String acceptHeaderValue) {
		//act
		ResponseEntity<InputStreamResource> response = testRestTemplate
			.exchange(
				"/rest/branch/testBranch/build/testBuild/usecase/testUseCase/scenario/testScenario/pageName/unknownPage/pageOccurrence/0/stepInPageOccurrence/1/image.jpg", GET,
				getEntityWithRequestHeaders(acceptHeaderValue),
				new ParameterizedTypeReference<InputStreamResource>() {
				});

		//assert
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getHeaders().getContentType()).isEqualTo(MediaType.IMAGE_JPEG);
	}

	private HttpEntity<Object> getEntityWithRequestHeaders(String headerValue) {
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.ACCEPT, headerValue);
		return new HttpEntity<>(headers);
	}
}
