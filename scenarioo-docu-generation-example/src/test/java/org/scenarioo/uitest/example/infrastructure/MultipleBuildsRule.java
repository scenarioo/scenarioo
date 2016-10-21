package org.scenarioo.uitest.example.infrastructure;


import static org.scenarioo.uitest.example.config.ExampleUITestDocuGenerationConfig.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.scenarioo.api.ScenarioDocuWriter;
import org.scenarioo.model.docu.entities.Build;

public class MultipleBuildsRule implements TestRule {

	private static final Logger LOGGER = Logger.getLogger(MultipleBuildsRule.class);

	private static BuildRun currentBuildRun;
	
	private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	
	@Override
	public Statement apply(final Statement base, final Description description) {
		return new Statement() {
			@Override
			public void evaluate() throws Throwable {
				try {
					for(BuildRun run : BuildRun.values()) {
						currentBuildRun = run;
						LOGGER.info("Build " + currentBuildRun.getDate());
						write_build_description();
						base.evaluate();
					}
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
			
			public void write_build_description() {
				ScenarioDocuWriter docuWriter = new ScenarioDocuWriter(DOCU_BUILD_DIRECTORY, getCurrentBranchName(), getCurrentBuildName());
				Build build = new Build();
				build.setName(getCurrentBuildName());
				build.setDate(getDate());
				build.setRevision(getCurrentBuildRun().getRevision());
				build.setStatus(getCurrentBuildRun().getStatus());
				docuWriter.saveBuildDescription(build);
			}

			private Date getDate()  {
				try {
					return dateFormat.parse(getCurrentBuildRun().getDate());
				} catch (ParseException e) {
					e.printStackTrace();
					return null;
				}
			}
		};
	}
	
	public static BuildRun getCurrentBuildRun() {
		return currentBuildRun;
	}
	
	public static String getCurrentBranchName() {
		return currentBuildRun.getBranchName();
	}
	
	public static String getCurrentBuildName() {
		return currentBuildRun.getDate();
	}

	
}
