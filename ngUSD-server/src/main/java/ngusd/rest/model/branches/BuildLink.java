package ngusd.rest.model.branches;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import ngusd.docu.model.Build;

/**
 * Represents a build that might also be linked in file system under a different
 * name (used for tagging builds). Usually the linkName should be the same as
 * the real build name, but could be different in case a build is linked under a
 * different name.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class BuildLink {
	
	private String linkName;
	
	private Build build;
	
	public BuildLink() {
	}
	
	/**
	 * Create build that is linked in file system under given linkName (might be
	 * the same as name but does not have to be).
	 */
	public BuildLink(final Build build, final String linkName) {
		this.build = build;
		this.linkName = linkName;
	}
	
	public String getLinkName() {
		return linkName;
	}
	
	public void setLinkName(final String linkName) {
		this.linkName = linkName;
	}
	
	public Build getBuild() {
		return build;
	}
	
	public void setBuild(final Build build) {
		this.build = build;
	}
	
}
