/* scenarioo-api
 * Copyright (C) 2014, scenarioo.org Development Team
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * As a special exception, the copyright holders of this library give you
 * permission to link this library with independent modules, according
 * to the GNU General Public License with "Classpath" exception as provided
 * in the LICENSE file that accompanied this code.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.scenarioo.model.docu.entities;

import org.scenarioo.api.rules.Preconditions;
import org.scenarioo.api.util.files.FilesUtil;
import org.scenarioo.model.docu.entities.generic.Details;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Description and other informations to store and display for one feature (usualy tested in same feature test class).
 *
 * It is important that each feature gets a unique 'name'.
 */
@XmlRootElement(name = "feature")
@XmlAccessorType(XmlAccessType.FIELD)
public class ImportFeature implements Serializable, Labelable, Detailable {



	private Details details = new Details();
	private Labels labels = new Labels();




	private List<ImportFeature> features = new ArrayList<>();
	private int orderIndex;
	private String name;
	private String description;
	private String status;
	private String id; // Unique Folder name
	private String milestone; // string to filter by
	private String type; // Display Type
	private List<String> featureNames = new ArrayList<String>();
	private List<Link> links = new ArrayList<Link>();
	private DokuFile markdown;
	private DokuFile specification;



	public ImportFeature() {
	}

	public ImportFeature(final String name, final String description) {
		this();
		this.setName(name);
		this.setDescription(description);
		this.setStatus("");
	}
	public ImportFeature(ImportFeature other) {
		this.labels = other.labels;
		this.details = other.details;
		this.setName(other.getName());
		this.setId(other.getId());
		this.setDescription(other.getDescription());
		this.setMilestone(other.getMilestone());
		this.setType(other.getType());
		this.setStatus(other.getStatus());
		this.setFeatureNames(other.getFeatureNames());
		this.setLinks(other.getLinks());
		this.setMarkdown(other.getMarkdown());
		this.setSpecification(other.getSpecification());
		this.setOrderIndex(other.getOrderIndex());
	}

	@Override
	public String toString() {
		final String[] txt = {getName()};
		for (String featureName: getFeatureNames()){
			txt[0] += featureName;
		}

		return txt[0];
	}

	public String getId() {
		if (id == null || id.equals("")){
			setId(FilesUtil.encodeName(getName()));
		}
		return id;
	}

	/**
	 * A unique name for this feature.
	 *
	 * Make sure to use descriptive names that stay stable as much as possible between multiple builds, such that you
	 * can compare features and its scenarios between different builds.
	 */
	public void setNameAndId(final String name) {
		this.setName(name);
	}

	public String getDescription() {
		return description;
	}

	/**
	 * (optional but recommended) More detailed description for current scenario (additionally to descriptive name).
	 */
	public void setDescription(final String description) {
		this.description = description;
	}

	public String getStatus() {
		return status;
	}

	/**
	 * Set status of current step.
	 *
	 * See also {@link #setStatus(String)} for setting additional application-specific states.
	 */
	public void setStatus(final Status status) {
		setStatus(Status.toKeywordNullSafe(status));
	}

	/**
	 * (optional) Status of the whole feature: did all tests for this feature succeed or not?<br/>
	 * Usual values are "success" or "failed".<br/>
	 * But you can use application specific additional values, like "not implemented", "unknown" etc. where it makes
	 * sense. Those additional values will be displayed in warning-style by the scenarioo webapplication.
	 *
	 * You do not have to set this value, if not set the value will be calculated from all contained scenarios, if no
	 * scenario is marked as "failed" the feature will be marked as "success" otherwise as "failed".
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

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getFeatureNames() {
		return featureNames;
	}

	public void setFeatureNames(List<String> featureNames) {
		this.featureNames = featureNames;
	}

	public List<ImportFeature> getFeatures() {
		return features;
	}

	public void setFeatures(List<ImportFeature> features) {
		this.features = features;
	}

	public int getOrderIndex() {
		return orderIndex;
	}

	public void setOrderIndex(int orderIndex) {
		this.orderIndex = orderIndex;
	}

	public String getMilestone() {
		return milestone;
	}

	public void setMilestone(String milestone) {
		this.milestone = milestone;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<Link> getLinks() {
		return links;
	}

	public void setLinks(List<Link> links) {
		this.links = links;
	}

	public DokuFile getMarkdown() {
		return markdown;
	}

	public void setMarkdown(DokuFile markdown) {
		this.markdown = markdown;
	}

	public DokuFile getSpecification() {
		return specification;
	}

	public void setSpecification(DokuFile specification) {
		this.specification = specification;
	}
}
