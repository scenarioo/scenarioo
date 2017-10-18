package org.scenarioo.rest.application;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import org.junit.Before;
import org.junit.Test;

import java.io.File;

public class ScenariooDataPathLogicTest {

	private final ServletContext servletContext = mock(ServletContext.class);
	private final SystemEnvironment systemEnvironment = mock(SystemEnvironment.class);

	private ScenariooDataPathLogic logic;

	@Before
	public void setUp() {
		logic = new ScenariooDataPathLogic(systemEnvironment);
	}

	@Test
	public void getDocumentationPath_GivenServletConfigAndEnvVariable_ChosesServletContextFirst() {
		when(servletContext.getInitParameter("scenariooDataDirectory")).thenReturn("tmp/test");
		when(systemEnvironment.getScenariooDataDirectory()).thenReturn("tmp/itShouldNotBeMe");

		String actual = logic.getDataPath(new ServletContextEvent(servletContext));

		assertEquals("tmp/test", actual);
	}

	@Test
	public void getDocumentationPath_WhenServletContextParamIsNotPresent_ChosesEnvironmentVariable() {
		when(servletContext.getInitParameter("scenariooDataDirectory")).thenReturn(null);
		when(systemEnvironment.getScenariooDataDirectory()).thenReturn("tmp/test");

		String actual = logic.getDataPath(new ServletContextEvent(servletContext));

		assertEquals("tmp/test", actual);
	}

	@Test
	public void getDocumentationPath_WhenNoExternalConfigurationIsGiven_DefaultsToScenariooHomeFolder() {
		when(servletContext.getInitParameter("scenariooDataDirectory")).thenReturn(null);
		when(systemEnvironment.getScenariooDataDirectory()).thenReturn(null);
		when(systemEnvironment.getUserHome()).thenReturn("/home/someuser");

		String expectedPath = new File("/home/someuser/.scenarioo").getAbsolutePath();
		String actual = logic.getDataPath(new ServletContextEvent(servletContext));

		assertEquals(expectedPath, actual);
	}

}
