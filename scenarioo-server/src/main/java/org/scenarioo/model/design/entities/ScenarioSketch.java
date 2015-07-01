package org.scenarioo.model.design.entities;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.scenarioo.api.rules.Preconditions;
import org.scenarioo.model.docu.entities.Detailable;
import org.scenarioo.model.docu.entities.Labelable;
import org.scenarioo.model.docu.entities.Labels;
import org.scenarioo.model.docu.entities.Status;
import org.scenarioo.model.docu.entities.generic.Details;

/**
 * Information to store and display for one scenario sketch.
 *
 * It is important that each scenario sketch gets a unique 'name' inside the issue it belongs to.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ScenarioSketch implements Serializable, Labelable, Detailable {

	private String scenarioSketchName;
	private String scenarioSketchId;
	private String description;
	private String issueId;
	private String scenarioSketchStatus = "";
	private String author = "";
	private String contextInDocu;
	private Date dateCreated;
	private Date dateModified;

	@XmlTransient
	private String branchName;

	private Details details = new Details();
	private Labels labels = new Labels();

	public ScenarioSketch() {
		this("", "");
	}

	public ScenarioSketch(final String name, final String description) {
		super();
		this.scenarioSketchName = name;
		this.description = description;
		this.scenarioSketchStatus = "Draft";
	}

	public void update(final ScenarioSketch update) {
		this.scenarioSketchName = update.getScenarioSketchName() != null ? update.getScenarioSketchName()
				: this.scenarioSketchName;
		this.issueId = update.getIssueId() != null ? update.getIssueId() : this.issueId;
		this.scenarioSketchId = update.getScenarioSketchId() != null ? update.getScenarioSketchId()
				: this.scenarioSketchId;
		this.description = update.getDescription() != null ? update.getDescription() : this.description;
		this.author = update.getAuthor() != null ? update.getAuthor() : this.author;
		this.contextInDocu = update.getContextInDocu() != null ? update.getContextInDocu() : this.contextInDocu;
		this.dateCreated = update.getDateCreated() != null ? update.getDateCreated() : this.dateCreated;
		this.dateModified = update.getDateModified() != null ? update.getDateModified() : this.dateModified;

		this.details = update.getDetails() != null ? update.getDetails() : this.details;
		this.labels = update.getLabels() != null ? update.getLabels() : this.labels;
	}

	public String getScenarioSketchName() {
		return scenarioSketchName;
	}

	/**
	 * A unique name for this scenario sketch inside the {@link Issue} it belongs to.
	 *
	 * Make sure to use descriptive names that stay stable as much as possible.
	 */
	public void setScenarioSketchName(final String name) {
		this.scenarioSketchName = name;
	}

	public String getScenarioSketchId() {
		return scenarioSketchId;
	}

	public void setScenarioSketchId(final String scenarioSketchId) {
		this.scenarioSketchId = scenarioSketchId;
	}

	public String getDescription() {
		return description;
	}

	/**
	 * (optional but recommended) More detailed description for the current scenario sketch (in addition to the
	 * descriptive
	 * name).
	 */
	public void setDescription(final String description) {
		this.description = description;
	}

	public String getScenarioSketchStatus() {
		return scenarioSketchStatus;
	}

	/**
	 * Set status of this scenario sketch.
	 *
	 * See also {@link #setStatus(String)} for setting additional application-specific states.
	 */
	public void setStatus(final Status status) {
		// setProposalStatus(Status.toKeywordNullSafe(status));
	}

	/**
	 * Status of the scenario sketch (draft, published). <br/>
	 * Usual values are "draft" or "published".<br/>
	 * But you can use workflow-specific additional values, like "proposed-to-commitee", "archived" etc. where it makes
	 * sense.
	 */
	public void setScenarioSketchStatus(final String scenarioSketchStatus) {
		this.scenarioSketchStatus = scenarioSketchStatus;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(final String author) {
		this.author = author;
	}

	public String getContextInDocu() {
		return contextInDocu;
	}

	public void setContextInDocu(final String contextInDocu) {
		this.contextInDocu = contextInDocu;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(final Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public Date getDateModified() {
		return dateModified;
	}

	public void setDateModified(final Date dateModified) {
		this.dateModified = dateModified;
	}

	@Override
	public Details getDetails() {
		return details;
	}

	@Override
	public Details addDetail(final String key, final Object value) {
		return details.addDetail(key, value);
	}

	@Override
	public void setDetails(final Details details) {
		Preconditions.checkNotNull(details, "Details not allowed to set to null");
		this.details = details;
	}

	@Override
	public Labels getLabels() {
		return labels;
	}

	@Override
	public Labels addLabel(final String label) {
		return labels.addLabel(label);
	}

	@Override
	public void setLabels(final Labels labels) {
		Preconditions.checkNotNull(labels, "Labels not allowed to set to null");
		this.labels = labels;
	}

	private String getBranchName() {
		return branchName;
	}

	private void setBranchName(final String branchName) {
		this.branchName = branchName;
	}

	public String getIssueId() {
		return issueId;
	}

	public void setIssueId(final String issueId) {
		this.issueId = issueId;
	}

}
