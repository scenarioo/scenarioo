package org.scenarioo.uitest.example.infrastructure;

import org.apache.log4j.Logger;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

public class ScenarioCustomisationRule implements TestRule {

	private final static Logger LOGGER = Logger.getLogger(ScenarioCustomisationRule.class);
	
	@Override
	public Statement apply(final Statement base, final Description description) {
		return new Statement() {
			@Override
			public void evaluate() throws Throwable {
				String methodName = description.getMethodName();
				String className = description.getTestClass().getSimpleName();
				
				if(BuildRunConfiguration.isScenarioFailing(className, methodName)) {
					LOGGER.info("Failing scenario " + className + "." + "methodName");
					throw new RuntimeException("Something went terribly wrong. This scenario failed.");
				}
				
				base.evaluate();
			}
		};
	}

}
