package org.scenarioo.rest.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.servlet.ServletContext;
import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ScenariooDataPathLogicTest {

	private final ServletContext servletContext = mock(ServletContext.class);
	private final SystemEnvironment systemEnvironment = mock(SystemEnvironment.class);

	private ScenariooDataPathLogic logic;

	@BeforeEach
	void setUp() {
		logic = new ScenariooDataPathLogic(systemEnvironment);
	}

	@Test
	void getDocumentationPath_GivenServletConfigAndEnvVariable_ChosesServletContextFirst() {
		when(servletContext.getInitParameter("scenariooDataDirectory")).thenReturn("tmp/test");
		when(systemEnvironment.getScenariooDataDirectory()).thenReturn("tmp/itShouldNotBeMe");

		String actual = logic.getDataPath(servletContext);

		assertEquals("tmp/test", actual);
	}

	@Test
	void getDocumentationPath_WhenServletContextParamIsNotPresent_ChosesEnvironmentVariable() {
		when(servletContext.getInitParameter("scenariooDataDirectory")).thenReturn(null);
		when(systemEnvironment.getScenariooDataDirectory()).thenReturn("tmp/test");

		String actual = logic.getDataPath(servletContext);

		assertEquals("tmp/test", actual);
	}

	@Test
	void getDocumentationPath_WhenNoExternalConfigurationIsGiven_DefaultsToScenariooHomeFolder() {
		when(servletContext.getInitParameter("scenariooDataDirectory")).thenReturn(null);
		when(systemEnvironment.getScenariooDataDirectory()).thenReturn(null);
		when(systemEnvironment.getUserHome()).thenReturn("/home/someuser");

		String expectedPath = new File("/home/someuser/.scenarioo").getAbsolutePath();
		String actual = logic.getDataPath(servletContext);

		assertEquals(expectedPath, actual);
	}

}
