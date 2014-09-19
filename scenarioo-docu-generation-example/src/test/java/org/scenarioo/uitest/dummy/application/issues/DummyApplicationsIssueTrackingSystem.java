package org.scenarioo.uitest.dummy.application.issues;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * This is just a fake of your project's issue tracking system to get the requirements that are linked to your test
 * cases somehow. It could be helpful (if you wish to have this) to implement an integration of your workitems with your
 * Scenarioo documentation to also link your test cases with additional informations from the issue tracking system
 * (e.g. Feature and User Story descriptions, like in this example).
 */
public class DummyApplicationsIssueTrackingSystem {
	
	private static Map<Long, WorkItem> workItems = new LinkedHashMap<Long, WorkItem>();
	static {
		WorkItemBuilder builder = new WorkItemBuilder();
		builder.feature("Search Pages", "User want to search for pages")
				.epic("Search Content", "User wants to find search results with content he searched for.")
				.story("Search Content - List Results", "User want to see a list of all search results to browse them.")
				.story("Search Content - Open Page with Searched Title directly",
						"As a user I like to directly get directed to the article, in case there is one with the exact title I searched for.")
				.story("Search Content - Display No Results Message",
						"As a user I want to see special message with user guidance in case no result is found.")
				.epic("Ambiguities", "Give user guidance in case there are ambiguous search results")
				.story("List Ambiguities for Articles",
						"As a user I want to see different articles with almost same title, in case for an article there are ambiguities known for an article.");
		builder.feature("Switch Language", "As a user I want to be able to switch the language")
				.epic("Language Switch for Content Search",
						"Possibility to switch language for content that is searched for.")
				.story("Select Language on Search Page",
						"As a user I want to be able to select in what language to search")
				.story("Select Language for Search Results",
						"As a user I want to be able to select a different language to search for in case that I did not found what I want in current language, the system should directly propose other languages to search.");
		
	}
	
	/**
	 * Returns a work item by its Id.
	 * 
	 * this is the Place in your real tests where you would implement calling your work item tracking system webservice
	 * or Database to resolve a work item that you want to add to your Scenarioo documentation.
	 */
	public static WorkItem loadWorkItemById(final long id) {
		return workItems.get(id);
	}
	
	public static void addWorkItem(final WorkItem item) {
		workItems.put(item.getId(), item);
	}
	
	/**
	 * Self documentation of available work items in dummy data, just run it as a java application.
	 */
	public static void main(final String[] args) {
		for (WorkItem item : workItems.values()) {
			String indent = "  ";
			if (item.getType().equals("epic")) {
				indent += "   ";
			}
			if (item.getType().equals("userStory")) {
				indent += "      ";
			}
			System.out.println(indent + item.getType() + " #" + item.getId() + " - " + item.getTitle() + " - "
					+ item.getDescription() + "[parent=" + item.getParentId() + "]");
		}
	}
}
