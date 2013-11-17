package ngusd.model.docu.aggregates.branches;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.scenarioo.model.docu.derived.BuildLink;
import org.scenarioo.model.docu.entities.Branch;

import lombok.Data;

/**
 * Represents a branch and all its belonging builds.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Data()
public class BranchBuilds {
	
	private Branch branch;
	
	@XmlElementWrapper(name = "builds")
	@XmlElement(name = "buildLink")
	private List<BuildLink> builds = new ArrayList<BuildLink>();
	
}
