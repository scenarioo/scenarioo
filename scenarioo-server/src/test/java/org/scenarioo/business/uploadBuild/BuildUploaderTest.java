package org.scenarioo.business.uploadBuild;

import org.junit.Assert;
import org.junit.Test;

public class BuildUploaderTest {

	@Test
	public void apiKeyMustNotBeNull() {
		try {
			// new BuildUploader().uploadBuild(createValidMultipartMessage(), null);
		} catch (NullPointerException e) {
			Assert.assertEquals("GET-Parameter apiKey is missing", e.getMessage());
		}
	}


	@Test
	public void apiKeyMustBeEqualToTheOneDefinedInTheConfigFile() {
		// TODO
	}

	@Test
	public void mediaTypeOfInputPartMustBeApplicationOctetStream() {
		try {
			// TODO
		} catch (NullPointerException e) {
			Assert.assertEquals("GET-Parameter apiKey is missing", e.getMessage());
		}
	}
	//
	// private MultipartFormDataInput createMultipartMessageWithWrongPartType() {
	// MultipartFormDataInput input = new MultipartFormDataInputImpl(MediaType.MULTIPART_FORM_DATA_TYPE, null);
	// input.getParts().add(createInputPart("application/abcd"));
	// return input;
	// }
	//
	// private InputPart createInputPart(final String string) {
	// InputPart inputPart = new MultipartInputImpl().PartImpl(new BodyPart());
	//
	// return inputPart;
	// }
	//
	// private MultipartFormDataInput createValidMultipartMessage() {
	// MultipartFormDataInput input = new MultipartFormDataInputImpl(MediaType.MULTIPART_FORM_DATA_TYPE, null);
	//
	// return input;
	// }

}
