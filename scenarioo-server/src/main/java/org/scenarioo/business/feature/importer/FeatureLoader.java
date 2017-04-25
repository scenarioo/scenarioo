package org.scenarioo.business.feature.importer;

import org.scenarioo.api.util.xml.ScenarioDocuXMLFileUtil;
import org.scenarioo.model.docu.feature.model.ImportFeature;
import org.scenarioo.model.implementation.feature.model.Feature;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class FeatureLoader {

	private final String folder;

	public FeatureLoader(String folder){
		this.folder = folder;
	}

	public Feature loadTree(){
		Feature root = new Feature(new ImportFeature());
		root.folderName = folder;
		root.name = folder;

		List<File> list = Importer.getFileList(folder, fileFolder ->
			new Validator<>(ImportFeature.class, folder).isTypeFolder(fileFolder.getName()));

		List<Feature> features = new ArrayList<>();

		for (File file: list){
			System.out.println(file.getAbsolutePath());
			ImportFeature importFeature = ScenarioDocuXMLFileUtil.unmarshal(ImportFeature.class,
				new File(file.getAbsolutePath()+"/"+Validator.getTypeFiles().get(ImportFeature.class)));
			features.add(new Feature(importFeature));
		}

		HashSet<Feature> featureClear = new HashSet<>();

		for (Feature feature: features){
			for (String featureName : feature.featureNames){
				Feature clear = getFor(featureName, features);
				feature.features.add(clear);
				featureClear.add(clear);
			}
		}
		features.removeAll(featureClear);
		root.features.addAll(features);
		return root;
	}


	private Feature getFor(String featureName, List<Feature> features) {
		for (Feature feature:features){
			if (feature.folderName.equals(featureName))
				return feature;
		}
		return null;
	}
}
