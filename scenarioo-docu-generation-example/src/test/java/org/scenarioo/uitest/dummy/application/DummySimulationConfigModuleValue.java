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

package org.scenarioo.uitest.dummy.application;

import static org.scenarioo.uitest.dummy.application.DummySimulationConfigModule.*;

import java.util.Arrays;
import java.util.List;

import org.scenarioo.model.docu.entities.generic.ObjectDescription;

/**
 * Just some example dummy simulation config modules configurations with descriptions to display in the scenarioo
 * documentation.
 * 
 * In a real system you would write this configurations together with the descriptions to display in the documentation
 * into your backend simulator configuration files.
 */
public enum DummySimulationConfigModuleValue {
	
	USER_RIGHTS_DEFAULT(USER_RIGHTS, "no user rights"),
	USER_RIGHTS_AUTHOR(USER_RIGHTS, "simple user with authorisation to edit pages"),
	SEARCH_RESULTS_DEFAULT(SEARCH_RESULTS, "a default list of pages"),
	SEARCH_RESULTS_DIRECT_PAGE(SEARCH_RESULTS, "one direct page"),
	SEARCH_RESULTS_DIRECT_PAGE_WITH_AMBIGUOTIES(SEARCH_RESULTS, "one direct page with ambiguoties"),
	SEARCH_RESULTS_NONE(SEARCH_RESULTS, "no pages found"),
	PAGE_CONTENTS_DEFAULT(PAGE_CONTENTS, "some default test page contents"),
	PAGE_CONTENTS_WITH_AMBIGUOTIES(PAGE_CONTENTS, "some special pages with ambiguoties linked for ambiguoties testing"),
	MENU_CONTENT_DEFAULT(MENU_CONTENT, "usual menu for unauthorised user"),
	MENU_CONTENT_AUTHORISED(MENU_CONTENT, "menu for authorised simple user");
	
	private static final DummySimulationConfigModuleValue[] DEFAULT_VALUES = new DummySimulationConfigModuleValue[] {
			USER_RIGHTS_DEFAULT, SEARCH_RESULTS_DEFAULT, PAGE_CONTENTS_DEFAULT, MENU_CONTENT_DEFAULT };
	
	private String description;
	
	private DummySimulationConfigModule module;
	
	private DummySimulationConfigModuleValue(final DummySimulationConfigModule module, final String description) {
		this.module = module;
		this.description = description;
	}
	
	public String getDescription() {
		return description;
	}
	
	public DummySimulationConfigModule getModule() {
		return module;
	}
	
	public static List<DummySimulationConfigModuleValue> defaultValues() {
		return Arrays.asList(DEFAULT_VALUES);
	}
	
	public ObjectDescription getObjectDescription() {
		ObjectDescription result = new ObjectDescription("configModuleValue", name().toLowerCase());
		result.addDetail("module", getModule().getReference());
		result.addDetail("description", description);
		return result;
	}
	
}
