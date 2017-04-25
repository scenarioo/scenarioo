package org.scenarioo.model.implementation.feature.model;


import org.scenarioo.model.docu.feature.model.ImportFeature;

import java.util.ArrayList;
import java.util.List;

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
