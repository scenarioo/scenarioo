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

package org.scenarioo.uitest.example.testcases;

import org.junit.Test;
import org.scenarioo.uitest.dummy.application.DummyApplicationSimulator;
import org.scenarioo.uitest.dummy.application.DummySimulationConfig;
import org.scenarioo.uitest.example.infrastructure.DocuDescription;
import org.scenarioo.uitest.example.infrastructure.Labels;
import org.scenarioo.uitest.example.infrastructure.UITest;
import org.scenarioo.uitest.example.issues.UserStories;

@DocuDescription(name = "Switch Language",
		description = "Search in a different language and switch language of current article.")
public class SwitchLanguageUITest extends UITest {
	
	@Test
	@DocuDescription(description = "User enters some text and finds multiple pages that contain this text.")
	@UserStories({ 122, 123 })
	@Labels({ "i18n", "exact match" })
	public void search_article_in_german_and_switch_to_spanish() {
		DummyApplicationSimulator.setConfiguration(DummySimulationConfig.SWITCH_LANGUAGE_CONFIG);
		toolkit.loadUrl("http://www.wikipedia.org");
		toolkit.selectLanguage("Deutsch");
		toolkit.enterText("searchField", "AngularJS");
		toolkit.clickButton("searchButton");
		toolkit.assertTextPresent("oft einfach als Angular bezeichnet");
		toolkit.clickLink("Espanol");
		toolkit.assertTextPresent("es un framework");
	}
	
}
