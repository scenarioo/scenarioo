package org.scenarioo.model.docu.aggregates;

import org.scenarioo.model.docu.entities.ImportFeature;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
public class Feature extends ImportFeature {
	public List<Feature> features = new ArrayList<>();

	public Feature(ImportFeature importFeature) {
		super(importFeature);
	}

	@Override
	public String toString() {
		String s = super.toString();

		for (Feature feature:features){
			s += "\n\t"+feature.toString();
		}
		return s;
	}
}
