package org.scenarioo.rest.application;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import org.junit.Before;
import org.junit.Test;

public class DocumentationPathLogicTest {

	private final ServletContext servletContext = mock(ServletContext.class);
	private final SystemWrapper systemWrapper = mock(SystemWrapper.class);

	private DocumentationPathLogic logic;

	@Test
	public void getDocumentationPath_GivenServletConfigAndEnvVariable_ChosesServletContextFirst() {
		when(servletContext.getInitParameter("scenariooConfigurationDirectory")).thenReturn("tmp/test");
		when(systemWrapper.getEnv("SCENARIOO_HOME")).thenReturn("tmp/itShouldNotBeMe");

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
		when(systemWrapper.getEnv("SCENARIOO_HOME")).thenReturn("tmp/test");

		String actual = logic.getDocumentationPath(new ServletContextEvent(servletContext));

		assertEquals("tmp/test", actual);
	}

	@Test
	public void getDocumentationPath_WhenNoExternalConfigurationIsGiven_DefaultsToScenariooHomeFolder() {
		when(servletContext.getInitParameter("scenariooConfigurationDirectory")).thenReturn(null);
		when(servletContext.getInitParameter("configurationDirectory")).thenReturn(null);
		when(systemWrapper.getEnv("SCENARIOO_HOME")).thenReturn(null);
		when(systemWrapper.getUserHome()).thenReturn("/home/someuser");

		String actual = logic.getDocumentationPath(new ServletContextEvent(servletContext));

		assertEquals("/home/someuser/.scenarioo", actual);
	}

	@Before
	public void setUp() {
		logic = new DocumentationPathLogic(systemWrapper);
	}
}
