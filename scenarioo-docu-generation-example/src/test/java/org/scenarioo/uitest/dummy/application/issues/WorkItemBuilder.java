package org.scenarioo.uitest.dummy.application.issues;

import java.util.Stack;

/**
 * A builder to quickly build dummy hierarchies of WorkItems.
 */
public class WorkItemBuilder {

	private final Stack<WorkItem> parents = new Stack<WorkItem>();

	private long nextId = 113;

	public WorkItemBuilder story(final String title, final String description) {
		return item("userStory", title, description);
	}

	public WorkItemBuilder epic(final String title, final String description) {
		return item("epic", title, description);
	}

	public WorkItemBuilder feature(final String title, final String description) {
		return item("docufeature", title, description);
	}

	private WorkItemBuilder item(final String type, final String title, final String description) {
		WorkItem item = new WorkItem(type, nextId++, title, description);
		setParent(item);
		DummyApplicationsIssueTrackingSystem.addWorkItem(item);
		return this;
	}

	private void setParent(final WorkItem item) {
		String parentType = getParentType(item.getType());
		while (parents.size() > 0 && !parents.peek().getType().equals(parentType)) {
			parents.pop();
		}
		if (parents.size() > 0) {
			item.setParentId(parents.peek().getId());
		}
		parents.push(item);
	}

	private String getParentType(final String type) {
		if (type.equals("userStory")) {
			return "epic";
		}
		else if (type.equals("epic")) {
			return "docufeature";
		} else if (type.equals("docufeature")) {
			return "";
		}
		else {
			throw new IllegalArgumentException("Unexpected work item type not supported: " + type);
		}
	}

}
