package org.scenarioo.model.docu.aggregates;


import org.scenarioo.api.util.xml.ScenarioDocuXMLFileUtil;
import org.scenarioo.model.docu.entities.Status;
import org.scenarioo.model.docu.feature.model.ImportFeature;

import java.io.File;
import java.util.ArrayList;

public class FeatureDocumentation {

	public static ImportFeature featureDashboardView(){
		ImportFeature feature = new ImportFeature();

		feature.name = "Dashboard-View";
		feature.description = "Dashboard mit Subfeature und subsubFeature Cards, welche sortiert und mit swimlanes geordnet werden können";
		feature.folderName = "dashboard-view";
		return feature;
	}
	public static ImportFeature featureFeatureView(){
		ImportFeature feature = new ImportFeature();

		feature.name = "Feature-View";
		feature.description = "Overview over the current subfeatures, compareable to todays usecase view";
		feature.folderName = "feature-view";
		return feature;
	}
	public static ImportFeature featureScenarioView(){
		ImportFeature feature = new ImportFeature();

		feature.name = "Scenario-View";
		feature.description = "Overview over the current scenarios, comparable to todays scenario view";
		feature.folderName = "scenario-view";
		return feature;
	}
	public static ImportFeature featureDocumentationView(){
		ImportFeature feature = new ImportFeature();

		feature.name = "Documentation-View";
		feature.description = "Shows the documentation file and the spec file of a feature and the subfeatures with their files";
		feature.folderName = "documentation-view";
		return feature;
	}
	public static ImportFeature featureGUI(){
		ImportFeature feature = new ImportFeature();

		feature.name = "GUI-Elements";
		feature.description = "The 4 new GUI views";
		feature.folderName = "gui-elements";
		feature.featureNames = new ArrayList<String>();
		feature.featureNames.add("feature-view");
		feature.featureNames.add("scenarios-view");
		feature.featureNames.add("documentation-view");
		feature.featureNames.add("dashboard-view");
		feature.status = Status.SUCCESS.getKeyword();
		return feature;
	}




	public static String baseFolder = "";
	public static void writeToFile(ImportFeature feature){
		File folder = new File(baseFolder);

		File featureFolder = new File(folder.getAbsolutePath() + "/" + feature.folderName);
		featureFolder.mkdir();
		ScenarioDocuXMLFileUtil.marshal(feature, new File(featureFolder.getAbsolutePath()+"/feature.xml"));


	}


	public static void main(String[] args){
		baseFolder = new File("").getAbsolutePath()+"/scenarioo-server/docs/features/";
		new File(baseFolder).mkdirs();

		writeToFile(featureDashboardView());
		writeToFile(featureDocumentationView());
		writeToFile(featureFeatureView());
		writeToFile(featureScenarioView());
		writeToFile(featureGUI());

	}


}
