package org.scenarioo.uitest.dummy.application;

import org.scenarioo.model.docu.entities.generic.ObjectReference;

/**
 * Just some example structure of your dummy simulation configurations into simulation config modules.
 */
public enum DummySimulationConfigModule {
	
	USER_RIGHTS,
	SEARCH_RESULTS,
	PAGE_CONTENTS,
	MENU_CONTENT;
	
	public ObjectReference getReference() {
		return new ObjectReference("configModule", name().toLowerCase());
	}
}
