package scenarioo.uitest.dummy.application;

import static scenarioo.uitest.dummy.application.DummySimulationConfigModuleValue.*;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import ngusd.model.docu.entities.generic.ObjectDescription;
import ngusd.model.docu.entities.generic.ObjectList;

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
	SEARCH_NOT_FOUND_CONFIG(SEARCH_RESULTS_NONE);
	
	private static final String TYPE = "configuration";
	
	private List<DummySimulationConfigModuleValue> overridenModules;
	
	private DummySimulationConfig(final DummySimulationConfigModuleValue... overridenModules) {
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
		ObjectList<ObjectDescription> values = new ObjectList<ObjectDescription>();
		for (DummySimulationConfigModuleValue moduleValue : moduleValues) {
			values.add(moduleValue.getObjectDescription());
		}
		return values;
	}
}
