package ngusd.docu.model;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Build {
	
	private String name;
	private String revision;
	private Date date;
	private String state;
	private Details details = new Details();
	
	public Build() {
	}
	
	public Build(final String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(final String name) {
		this.name = name;
	}
	
	public String getRevision() {
		return revision;
	}
	
	public void setRevision(final String revision) {
		this.revision = revision;
	}
	
	public Date getDate() {
		return date;
	}
	
	public void setDate(final Date date) {
		this.date = date;
	}
	
	public String getState() {
		return state;
	}
	
	public void setState(final String state) {
		this.state = state;
	}
	
	public Details getDetails() {
		return details;
	}
	
	public void setDetails(Details details) {
		this.details = details;
	}
	
}
