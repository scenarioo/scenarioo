package ngusd.rest.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Branch {
	
	private String name;
	private String description;
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
		builds.add(new Build("build1"));
		builds.add(new Build("build2"));
		builds.add(new Build("build3"));
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
	
}
