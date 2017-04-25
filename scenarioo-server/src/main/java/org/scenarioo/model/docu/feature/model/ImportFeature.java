package org.scenarioo.model.docu.feature.model;


import org.scenarioo.model.docu.feature.model.files.DokuFile;
import org.scenarioo.model.docu.feature.model.generic.Detailable;
import org.scenarioo.model.docu.feature.model.generic.Details;
import org.scenarioo.model.docu.feature.model.lable.Label;
import org.scenarioo.model.docu.feature.model.lable.Labelable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@XmlRootElement(name = "feature")
@XmlAccessorType(XmlAccessType.FIELD)
public class ImportFeature implements Detailable, Labelable {

	public Set<Label> labels = new HashSet<>();
	public Details details;

	public String name; // Display name
	public String folderName; // Unique Folder name

	public String description;
	public String milestone; // string to filter by
	public String type; // Display Type
	public String status;


	public List<String> scenarioNames = new ArrayList<>();
	public List<String> featureNames = new ArrayList<>(); //Subfeatures FolderName!
	public List<Link> links = new ArrayList<>();
	public DokuFile markdown; //Or List
	public DokuFile specification; //Or List

	public ImportFeature(){}

	public ImportFeature(ImportFeature other) {
		this.labels = other.labels;
		this.details = other.details;
		this.name = other.name;
		this.folderName = other.folderName;
		this.description = other.description;
		this.milestone = other.milestone;
		this.type = other.type;
		this.status = other.status;
		this.scenarioNames = other.scenarioNames;
		this.featureNames = other.featureNames;
		this.links = other.links;
		this.markdown = other.markdown;
		this.specification = other.specification;
	}

	@Override
	public String toString() {
		final String[] txt = {name};
		featureNames.forEach(featureName -> txt[0] += featureName);

		return txt[0];
	}
}
