package org.scenarioo.model.configuration;

import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.assertj.core.api.Assertions.assertThat;

class ConfigurationTest {

	private static final Color DEFAULT_DIFF_COLOR = new Color(255, 126, 0, 127);

	private Configuration configuration = new Configuration();

	@Test
	void testDefaultDiffImageColor() {

		Color actual = configuration.getDiffImageAwtColor();

		assertThat(actual).isEqualTo(DEFAULT_DIFF_COLOR);
	}

	@Test
	void testSetAndGetDiffImageColor() {

		Color expected = new Color(237, 176, 77, 127);
		configuration.setDiffImageAwtColor(expected);

		Color actual = configuration.getDiffImageAwtColor();
		assertThat(actual).isEqualTo(expected);
	}
}
