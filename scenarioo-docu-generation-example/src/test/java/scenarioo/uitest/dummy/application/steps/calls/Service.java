package scenarioo.uitest.dummy.application.steps.calls;

import ngusd.model.docu.entities.generic.ObjectDescription;
import ngusd.model.docu.entities.generic.ObjectReference;

/**
 * Just some example actions to use in dummy example documentation data.
 */
public enum Service {
	
	MENU("MenuService.loadMenu", "MENUSYS:LoadMenuService.load", "Service to load current wiki menu."),
	SEARCH("SearchService.search", "TIBCO:FastSearchService.getSearchResults", "PowerGridSearch.search",
			"Search for pages using the power grid"),
	AMBIGUITIES("SearchService.loadAmbiguitiesList", "TIBCO:AmbiguitiesService.getAmbiguitiesList",
			"ContentDB.loadAmbiguities",
			"Service to load a list of ambiguities to display to the user for a ambiguous search."),
	CONTENT_LOAD("ContentService.loadPage", "TIBCO:PageContentService.getPage", "ContentDB.loadPage",
			"Service to load the whole page content of a page"),
	AUTHENTICATION_CHECK("AuthenticationService.checkUserAuthenticated", "LDAP:AuthenticationService",
			"Service to check via LDAP the authentication for a user for given operation."),
	CONTENT_SAVE("ContentService.savePage", "TIBCO:PageContentService.updatePage", "ContentDB.savePage",
			"Service to save the updated page content as edited by a user");
	
	private String name;
	private String eaiName;
	private String realName;
	private String description;
	
	private Service(final String name, final String realName, final String description) {
		this.name = name;
		this.realName = realName;
		this.eaiName = null;
		this.description = description;
	}
	
	private Service(final String name, final String eaiName, final String realName, final String description) {
		this.name = name;
		this.realName = realName;
		this.eaiName = eaiName;
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
		result.addDetail("eaiName", eaiName);
		result.addDetail("realName", realName);
		return result;
	}
	
}
