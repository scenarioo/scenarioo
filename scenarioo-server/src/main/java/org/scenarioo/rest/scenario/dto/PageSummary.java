package org.scenarioo.rest.scenario.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * All the page information needed on the overview page of a scenario.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class PageSummary {
	
	private String name;
	private int pageOccurrence;
	
	public String getName() {
		return name;
	}
	
	public void setName(final String name) {
		this.name = name;
	}
	
	public int getPageOccurrence() {
		return pageOccurrence;
	}
	
	public void setPageOccurrence(final int pageOccurrence) {
		this.pageOccurrence = pageOccurrence;
	}
	
}
