package ngusd.rest.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Branch {
	
	private String name;
	private String description;
	
	@XmlElementWrapper(name = "builds")
	@XmlElement(name = "build")
	private List<Build> builds = new ArrayList<Build>();
	
	public Branch() {
		this("", "");
	}
	
	public Branch(final String name) {
		this(name, "");
	}
	
	public Branch(final String name, final String description) {
		this.name = name;
		this.description = description;
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
	
	public List<Build> getBuilds() {
		return builds;
	}
	
	public void setBuilds(final List<Build> builds) {
		this.builds = builds;
	}
	
	public void addBuild(final Build build) {
		this.builds.add(build);
	}
	
}
