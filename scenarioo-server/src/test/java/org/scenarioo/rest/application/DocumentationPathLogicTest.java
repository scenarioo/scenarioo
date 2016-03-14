package org.scenarioo.rest.application;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import org.junit.Before;
import org.junit.Test;

public class DocumentationPathLogicTest {

	private final ServletContext servletContext = mock(ServletContext.class);
	private final SystemEnvironment systemEnvironment = mock(SystemEnvironment.class);

	private DocumentationPathLogic logic;

	@Before
	public void setUp() {
		logic = new DocumentationPathLogic(systemEnvironment);
	}

	@Test
	public void getDocumentationPath_GivenServletConfigAndEnvVariable_ChosesServletContextFirst() {
		when(servletContext.getInitParameter("scenariooConfigurationDirectory")).thenReturn("tmp/test");
		when(systemEnvironment.getScenariooHome()).thenReturn("tmp/itShouldNotBeMe");

		String actual = logic.getDocumentationPath(new ServletContextEvent(servletContext));

		assertEquals("tmp/test", actual);
	}

	@Test
	public void getDocumentationPath_GivenBothServletContextParameters_ChosesNewOneBeforeOldOne() {
		when(servletContext.getInitParameter("scenariooConfigurationDirectory")).thenReturn("tmp/test");
		when(servletContext.getInitParameter("configurationDirectory")).thenReturn("tmp/itShouldNotBeMe");

		String actual = logic.getDocumentationPath(new ServletContextEvent(servletContext));

		assertEquals("tmp/test", actual);
	}

	@Test
	public void getDocumentationPath_WhenServletContextParamIsNotPresent_FallsBackToOldServletContextParam() {
		when(servletContext.getInitParameter("scenariooConfigurationDirectory")).thenReturn(null);
		when(servletContext.getInitParameter("configurationDirectory")).thenReturn("tmp/test");

		String actual = logic.getDocumentationPath(new ServletContextEvent(servletContext));

		assertEquals("tmp/test", actual);
	}

	@Test
	public void getDocumentationPath_WhenServletContextParamIsNotPresent_ChosesEnvironmentVariable() {
		when(servletContext.getInitParameter("scenariooConfigurationDirectory")).thenReturn(null);
		when(servletContext.getInitParameter("configurationDirectory")).thenReturn(null);
		when(systemEnvironment.getScenariooHome()).thenReturn("tmp/test");

		String actual = logic.getDocumentationPath(new ServletContextEvent(servletContext));

		assertEquals("tmp/test", actual);
	}

	@Test
	public void getDocumentationPath_WhenNoExternalConfigurationIsGiven_DefaultsToScenariooHomeFolder() {
		when(servletContext.getInitParameter("scenariooConfigurationDirectory")).thenReturn(null);
		when(servletContext.getInitParameter("configurationDirectory")).thenReturn(null);
		when(systemEnvironment.getScenariooHome()).thenReturn(null);
		when(systemEnvironment.getUserHome()).thenReturn("/home/someuser");

		String actual = logic.getDocumentationPath(new ServletContextEvent(servletContext));

		assertEquals("/home/someuser/.scenarioo", actual);
	}

}
