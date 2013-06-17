package org.ngusd.rest.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Branch {
	private String name;
	private String description;

	public Branch() {
	}

	public Branch(String name) {
		this(name, "");
	}

	public Branch(String name, String description) {
		super();
		this.name = name;
		this.setDescription(description);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
