/* Copyright (c) 2014, scenarioo.org Development Team
 * All rights reserved.
 *
 * See https://github.com/scenarioo?tab=members
 * for a complete list of contributors to this project.
 *
 * Redistribution and use of the Scenarioo Examples in source and binary forms,
 * with or without modification, are permitted provided that the following
 * conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright notice, this
 *   list of conditions and the following disclaimer in the documentation and/or
 *   other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.scenarioo.uitest.example.infrastructure;

import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.scenarioo.model.docu.entities.Scenario;
import org.scenarioo.model.docu.entities.UseCase;
import org.scenarioo.uitest.dummy.toolkit.UITestToolkit;

/**
 * Example basis class for unit tests that contains UI test scenarios to record these scenarios using Scenarioo.
 */
public class UITest {
	
	/**
	 * Rule to write the use case information for each test class
	 */
	@ClassRule
	public static UseCaseDocuWritingRule useCaseWritingRule = new UseCaseDocuWritingRule();
	
	@Rule
	public ScenarioCustomisationRule scenarioCustomisationRule = new ScenarioCustomisationRule();
	
	/**
	 * Rule to write the scenario information for each test
	 */
	@Rule
	public ScenarioDocuWritingRule scenarioWritingRule = new ScenarioDocuWritingRule();

	
	/**
	 * This is the application dependent UI abstraction layer, to use for writing your UI tests against.
	 * 
	 * 
	 * It contains the functionality to write step descriptions and screenshots whenever the test does a click event.
	 */
	public UITestToolkitAbstraction toolkit;
	
	@Before
	public void setUp() {
		// The UITestToolkit is just a dummy toolkit implementation in this example you would replace this by your used
		// testing API (e.g. Selenium's WebDriver).
		toolkit = new UITestToolkitAbstraction(new UITestToolkit(), this);
	}
	
	@After
	public void tearDown() {
		// Wait until asynch writing of documentation data for current test scenario has finished.
		toolkit.getDocuWriter().flush();
	}
	
	/**
	 * Get use case for which this test generates documentation.
	 */
	public UseCase getUseCase() {
		return scenarioWritingRule.getUseCase();
	}
	
	/**
	 * Get the scenario for which this test generates documentation.
	 */
	public Scenario getScenario() {
		return scenarioWritingRule.getScenario();
	}
	
}
