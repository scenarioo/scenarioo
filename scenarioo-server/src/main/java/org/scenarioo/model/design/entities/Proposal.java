package org.scenarioo.model.design.entities;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.scenarioo.api.rules.Preconditions;
import org.scenarioo.model.docu.entities.Detailable;
import org.scenarioo.model.docu.entities.Labelable;
import org.scenarioo.model.docu.entities.Labels;
import org.scenarioo.model.docu.entities.Status;
import org.scenarioo.model.docu.entities.generic.Details;

/**
 * Information to store and display for one design proposal.
 *
 * It is important that each proposal gets a unique 'name' inside the issue it belongs to.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Proposal implements Serializable, Labelable, Detailable {

	private String name;
	private String description;
	private String status = "";
	private final String author = "";

	private Details details = new Details();
	private Labels labels = new Labels();

	public Proposal() {
		this("", "");
	}

	public Proposal(final String name, final String description) {
		super();
		this.name = name;
		this.description = description;
	}

	public String getName() {
		return name;
	}

	/**
	 * A unique name for this proposal inside the {@link Issue} it belongs to.
	 *
	 * Make sure to use descriptive names that stay stable as much as possible.
	 */
	public void setName(final String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	/**
	 * (optional but recommended) More detailed description for the current proposal (in addition to the descriptive
	 * name).
	 */
	public void setDescription(final String description) {
		this.description = description;
	}

	public String getStatus() {
		return status;
	}

	/**
	 * Set status of this proposal.
	 *
	 * See also {@link #setStatus(String)} for setting additional application-specific states.
	 */
	public void setStatus(final Status status) {
		setStatus(Status.toKeywordNullSafe(status));
	}

	/**
	 * Status of the proposal (draft, published). <br/>
	 * Usual values are "draft" or "published".<br/>
	 * But you can use workflow-specific additional values, like "proposed-to-commitee", "archived" etc. where it makes
	 * sense.
	 */
	public void setStatus(final String status) {
		this.status = status;
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

}
