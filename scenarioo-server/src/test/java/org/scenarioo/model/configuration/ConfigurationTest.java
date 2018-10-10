package org.scenarioo.model.configuration;

import org.junit.Test;

import java.awt.*;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;



public class ConfigurationTest {

	public static final Color DEFAULT_DIFF_COLOR = new Color(255, 126, 0, 127);

	private Configuration configuration = new Configuration();

	@Test
	public void testDefaultDiffImageColor() {

		Color actual = configuration.getDiffImageAwtColor();

		assertThat(actual, is(DEFAULT_DIFF_COLOR));
	}

	@Test
	public void testSetAndGetDiffImageColor() {

		Color expected = new Color(237, 176, 77, 127);
		configuration.setDiffImageAwtColor(expected);

		Color actual = configuration.getDiffImageAwtColor();
		assertThat(actual, is(expected));
	}
}
