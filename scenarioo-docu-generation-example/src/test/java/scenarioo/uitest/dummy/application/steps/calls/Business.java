package scenarioo.uitest.dummy.application.steps.calls;

import ngusd.model.docu.entities.generic.ObjectDescription;
import ngusd.model.docu.entities.generic.ObjectReference;

/**
 * Just some example Business logic operations of your application under test (might be EJB session beans for example)
 * to use in dummy example documentation data.
 */
public enum Business {
	
	MENU_LOAD("MenuSessionBean.load", "Load current menu page index"),
	SESSION_INIT("UserSessionBean.loadUserSessionData", "Load session data for current user"),
	SEARCH("SearchSessionBean.searchForText", "Process search for a text"),
	PAGE_GET("PageSessionBean.loadPageContent", "Load the page content to present to the user"),
	PAGE_SAVE("PageSessionBean.savePageContent", "Save the updated page content");
	
	private String name;
	private String description;
	
	private Business(final String name, final String description) {
		this.name = name;
		this.description = description;
	}
	
	public String getName() {
		return name;
	}
	
	public String getType() {
		return "businessOperation";
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
