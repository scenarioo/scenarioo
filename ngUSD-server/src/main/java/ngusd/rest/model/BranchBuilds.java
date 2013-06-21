package ngusd.rest.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import ngusd.docu.model.Branch;

/**
 * All builds for a specififc branch
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class BranchBuilds {
	
	private Branch branch;
	
	@XmlElementWrapper(name = "builds")
	@XmlElement(name = "buildLink")
	private List<BuildLink> builds = new ArrayList<BuildLink>();
	
	public BranchBuilds() {
	}
	
	public Branch getBranch() {
		return branch;
	}
	
	public void setBranch(final Branch branch) {
		this.branch = branch;
	}
	
	public List<BuildLink> getBuilds() {
		return builds;
	}
	
	public void setBuilds(final List<BuildLink> builds) {
		this.builds = builds;
	}
	
}
