package org.scenarioo.dao.version;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class ApplicationVersionHolderTest {

	@Test
	public void initializeFromClassContext_loadsVersionProperlyFromVersionPropertiesFile() {
		ApplicationVersionHolder.INSTANCE.initializeFromClassContext();
		assertThat(ApplicationVersionHolder.INSTANCE.getApplicationVersion().getVersion()).isNotBlank();
		assertThat(ApplicationVersionHolder.INSTANCE.getApplicationVersion().getVersion()).isNotEqualToIgnoringCase("unknown");
		assertThat(ApplicationVersionHolder.INSTANCE.getApplicationVersion().getVersion()).describedAs("Version loaded from version.properties is not expected to contain unresolved property expression - Hint: use gradle to build to have proper resolved version.properties").doesNotContain("${");
	}

}
