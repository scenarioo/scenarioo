package ngusd.rest.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Scenario {
	
	private String name;
	private String description;
	private String status = "";
	private int numberOfPages;
	private int numberOfSteps;
	private Details details = new Details();
	
	public Scenario() {
		this("", "", 0, 0);
	}
	
	public Scenario(final String name, final String description, final int numberOfPages, final int numberOfSteps) {
		super();
		this.name = name;
		this.description = description;
		this.numberOfPages = numberOfPages;
		this.numberOfSteps = numberOfSteps;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(final String name) {
		this.name = name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(final String description) {
		this.description = description;
	}
	
	public String getStatus() {
		return status;
	}
	
	public void setStatus(final String status) {
		this.status = status;
	}
	
	public int getNumberOfPages() {
		return numberOfPages;
	}
	
	public void setNumberOfPages(final int numberOfPages) {
		this.numberOfPages = numberOfPages;
	}
	
	public int getNumberOfSteps() {
		return numberOfSteps;
	}
	
	public void setNumberOfSteps(final int numberOfSteps) {
		this.numberOfSteps = numberOfSteps;
	}
	
	public Details getDetails() {
		return details;
	}
	
	public void setDetails(Details details) {
		this.details = details;
	}
	
}
