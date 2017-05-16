package org.scenarioo.model.configuration;

import org.junit.Test;

import java.awt.Color;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;



public class ConfigurationTest {

	public static final Color DEFAULT_DIFF_COLOR = new Color(237, 176, 77, 127);

	private Configuration configuration = new Configuration();

	@Test
	public void testDefaultDiffImageColor() throws Exception {

		Color actual = configuration.getDiffImageColor();

		assertThat(actual, is(DEFAULT_DIFF_COLOR));
	}

	@Test
	public void testSetAndGetDiffImageColor() throws Exception {

		Color expected = new Color(237, 176, 77, 127);
		configuration.setDiffImageColor(expected);

		Color actual = configuration.getDiffImageColor();
		assertThat(actual, is(expected));
	}
}
