package org.scenarioo.uitest.dummy.application.issues;

/**
 * Just some dummy work items of your application, that you could somehow extract from your task item management tool to
 * put this info into the details of your belonging test cases (scenarios) in your Scenarioo documentation.
 */
public class WorkItem {
	
	private String type;
	private Long id;
	private Long parentId = null;
	private String title;
	private String description;
	
	public WorkItem(final String type, final Long id, final String title, final String description) {
		super();
		this.type = type;
		this.id = id;
		this.title = title;
		this.description = description;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(final String type) {
		this.type = type;
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(final Long id) {
		this.id = id;
	}
	
	public Long getParentId() {
		return parentId;
	}
	
	public void setParentId(final Long parentId) {
		this.parentId = parentId;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(final String title) {
		this.title = title;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(final String description) {
		this.description = description;
	}
	
}
