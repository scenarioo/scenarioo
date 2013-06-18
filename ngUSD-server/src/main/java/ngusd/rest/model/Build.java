package ngusd.rest.model;

import java.util.Date;

public class Build {
	
	private String name;
	private String revision;
	private Date date;
	private String state;
	
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
	
}
