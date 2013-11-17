package org.scenarioo.uitest.dummy.application.steps.calls;

import org.scenarioo.model.docu.entities.generic.ObjectDescription;
import org.scenarioo.model.docu.entities.generic.ObjectReference;

/**
 * Just some example UI Action classes of your application (might be Struts actions for examples) to use in dummy
 * example documentation data.
 */
public enum Action {
	
	START_INIT("example.action.StartInitAction", "Initializer for wiki, creates user session and initializes the menu"),
	SEARCH_INIT("example.action.SearchInitAction", "Initializer to init the search page"),
	SEARCH_PROCESS("example.action.SearchProcessAction", "Process user input on search page and start searching"),
	PAGE_SHOW_CONTENT_INIT("example.action.ShowPageInitAction", "Initialize page content to display"),
	PAGE_EDIT_INIT("example.action.EditPageInitAction", "Initialize edit page to edit the page content"),
	PAGE_EDIT_PROCESS("example.action.EditPageProcessAction",
			"Process the input of the user on edit page and store the updated page"),
	AMBIGUOUS_LIST_INIT("example.action.AmbiguousListInitAction",
			"Initilize the list of ambiguities to display to user");
	
	private String name;
	
	private String description;
	
	private Action(final String name, final String description) {
		this.name = name;
		this.description = description;
	}
	
	public String getName() {
		return name;
	}
	
	public String getType() {
		return "uiAction";
	}
	
	public ObjectReference getReference() {
		return new ObjectReference(getType(), getName());
	}
	
	public ObjectDescription getObject() {
		ObjectDescription result = new ObjectDescription(getType(), getName());
		result.addDetail("description", description);
		return result;
	}
	
}
