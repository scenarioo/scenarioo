package org.scenarioo.dao.design.aggregates.issues;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.scenarioo.model.design.aggregates.IssueScenarioSketches;

/**
 * List of all issues with their scenario sketches.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class IssueScenarioSketchesList {

		private String version;
		@XmlElementWrapper(name = "list")
		@XmlElement(name = "issueScenarios")
	private List<IssueScenarioSketches> issueScenarioSketches;

		public String getVersion() {
			return version;
		}

		public void setVersion(final String version) {
			this.version = version;
		}

	public List<IssueScenarioSketches> getIssueScenarioSketches() {
		return issueScenarioSketches;
		}

	public void setIssueScenarioSketches(final List<IssueScenarioSketches> issueScenarioSketches) {
		this.issueScenarioSketches = issueScenarioSketches;
		}

}
