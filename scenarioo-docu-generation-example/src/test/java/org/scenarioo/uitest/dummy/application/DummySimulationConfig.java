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

import static org.scenarioo.uitest.dummy.application.DummySimulationConfigModuleValue.*;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.scenarioo.model.docu.entities.generic.ObjectDescription;
import org.scenarioo.model.docu.entities.generic.ObjectList;

/**
 * Some example dummy configurations for UI tests.
 *
 * In a real application you would probably have defined this through config files for the simulator to simulate your
 * backend systems.
 */
public enum DummySimulationConfig {

	DEFAULT_CONFIG(),
	DIRECT_SEARCH_CONFIG(SEARCH_RESULTS_DIRECT_PAGE),
	AMBIGUOTIES_CONFIG(SEARCH_RESULTS_DIRECT_PAGE_WITH_AMBIGUOTIES, PAGE_CONTENTS_WITH_AMBIGUOTIES),
	SEARCH_NOT_FOUND_CONFIG(SEARCH_RESULTS_NONE),
	SWITCH_LANGUAGE_CONFIG(),

	/**
	 * A special configuration for technical corner case: only one page with only one page variant (only use in one test
	 * case with one step!)
	 */
	TECHNICAL_ONE_PAGE_CONFIG,

	/**
	 * Special configuration to test what happens when there are no page names stored.
	 */
	TECHNICAL_NO_PAGE_NAMES_CONFIG,

	/**
	 * Special technical configuration to have a page with jpeg step images
	 */
	TECHNICAL_JPEG_STEP_IMAGES_CONFIG,

	/**
	 * Special technical configuration to have a page with parentheses and a space in the URL
	 */
	TECHNICAL_PARENTHESES_SPACE_STEP_CONFIG,

	/**
	 * Special technical configuration to have an encoded space in the URL
	 */
	TECHNICAL_ENCODED_SPACE_STEP_CONFIG,

	/**
	 * Special technical configuration to have encoded characters in the URL
	 */
	TECHNICAL_ENCODED_SPECIAL_CHARACTERS_STEP_CONFIG;

	private static final String TYPE = "configuration";

	private List<DummySimulationConfigModuleValue> overridenModules;

	DummySimulationConfig(final DummySimulationConfigModuleValue... overridenModules) {
		this.overridenModules = Arrays.asList(overridenModules);
	}

	public ObjectDescription getObjectDescription() {

		ObjectDescription configObject = new ObjectDescription();
		configObject.setName(name().toLowerCase());
		configObject.setType(TYPE);
		configObject.addDetail("overridenConfigModules", createModulesDescriptions(overridenModules));
		configObject.addDetail("defaultConfigModules", createModulesDescriptions(getDefaultModules()));
		return configObject;
	}

	private List<DummySimulationConfigModuleValue> getDefaultModules() {
		List<DummySimulationConfigModuleValue> defaultModules = DummySimulationConfigModuleValue
				.defaultValues();
		removeModules(defaultModules, overridenModules);
		return defaultModules;
	}

	private void removeModules(final List<DummySimulationConfigModuleValue> defaultModules,
			final List<DummySimulationConfigModuleValue> overridenModules2) {
		for (DummySimulationConfigModuleValue value : overridenModules) {
			removetModule(defaultModules, value.getModule());
		}
	}

	private void removetModule(final List<DummySimulationConfigModuleValue> defaultModules,
			final DummySimulationConfigModule module) {
		Iterator<DummySimulationConfigModuleValue> it = defaultModules.iterator();
		while (it.hasNext()) {
			DummySimulationConfigModule defaultModule = it.next().getModule();
			if (defaultModule.equals(module)) {
				defaultModules.remove(defaultModule);
				break;
			}
		}
	}

	private ObjectList<ObjectDescription> createModulesDescriptions(
			final List<DummySimulationConfigModuleValue> moduleValues) {
		ObjectList<ObjectDescription> values = new ObjectList<>();
		for (DummySimulationConfigModuleValue moduleValue : moduleValues) {
			values.add(moduleValue.getObjectDescription());
		}
		return values;
	}
}
