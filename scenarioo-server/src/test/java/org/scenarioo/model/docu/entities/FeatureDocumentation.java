package org.scenarioo.model.docu.entities;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.scenarioo.api.util.xml.ScenarioDocuXMLFileUtil;

import java.io.File;
import java.util.ArrayList;

public class FeatureDocumentation {


	@Test
	public void featureDashboardView() {
		ImportFeature feature = new ImportFeature();

		feature.name = "Dashboard-View";
		feature.description = "Dashboard mit Subfeature und subsubFeature Cards, welche sortiert und mit swimlanes geordnet werden können";
		feature.folderName = "dashboard-view";
		writeToFile(feature);
		Assert.assertTrue(new File(baseFolder + "/" + feature.folderName + "/feature.xml").exists());
	}

	@Test
	public void featureFeatureView() {
		ImportFeature feature = new ImportFeature();

		feature.name = "Feature-View";
		feature.description = "Overview over the current subfeatures, compareable to todays usecase view";
		feature.folderName = "feature-view";
		writeToFile(feature);
		Assert.assertTrue(new File(baseFolder + "/" + feature.folderName + "/feature.xml").exists());
	}

	@Test
	public void featureScenarioView() {
		ImportFeature feature = new ImportFeature();

		feature.name = "Scenario-View";
		feature.description = "Overview over the current scenarios, comparable to todays scenario view";
		feature.folderName = "scenario-view";
		writeToFile(feature);
		Assert.assertTrue(new File(baseFolder + "/" + feature.folderName + "/feature.xml").exists());
	}

	@Test
	public void featureDocumentationView() {
		ImportFeature feature = new ImportFeature();

		feature.name = "Documentation-View";
		feature.description = "Shows the documentation file and the spec file of a feature and the subfeatures with their files";
		feature.folderName = "documentation-view";
		writeToFile(feature);
		Assert.assertTrue(new File(baseFolder + "/" + feature.folderName + "/feature.xml").exists());
	}

	@Test
	public void featureGUI() {
		ImportFeature feature = new ImportFeature();

		feature.name = "GUI-Elements";
		feature.description = "The 4 new GUI views";
		feature.folderName = "gui-elements";
		feature.featureNames = new ArrayList<String>();
		feature.featureNames.add("feature-view");
		feature.featureNames.add("scenarios-view");
		feature.featureNames.add("documentation-view");
		feature.featureNames.add("dashboard-view");
		feature.featureNames.add("tree-navigation");
		feature.status = Status.SUCCESS.getKeyword();
		writeToFile(feature);
		Assert.assertTrue(new File(baseFolder + "/" + feature.folderName + "/feature.xml").exists());
	}

	@Test
	public void featureImporter() {
		ImportFeature feature = new ImportFeature();

		feature.name = "Importer";
		feature.description = "Imports the data to save it on the server";
		feature.folderName = "importer";
		writeToFile(feature);
		Assert.assertTrue(new File(baseFolder + "/" + feature.folderName + "/feature.xml").exists());
	}

	@Test
	public void featureClient() {
		ImportFeature feature = new ImportFeature();

		feature.name = "Client";
		feature.description = "Contains all work todo on the client side";
		feature.folderName = "client";
		feature.featureNames = new ArrayList<String>();
		feature.featureNames.add("gui-elements");
		feature.featureNames.add("sketch-editor");
		feature.featureNames.add("client-api-connection");
		writeToFile(feature);
		Assert.assertTrue(new File(baseFolder + "/" + feature.folderName + "/feature.xml").exists());
	}

	@Test
	public void featureServer() {
		ImportFeature feature = new ImportFeature();

		feature.name = "Server";
		feature.description = "Contains all work todo on the server side";
		feature.folderName = "server";
		feature.featureNames = new ArrayList<String>();
		feature.featureNames.add("importer");
		feature.featureNames.add("comparison");
		feature.featureNames.add("full-text-search");
		feature.featureNames.add("sketch-editor");
		feature.featureNames.add("rest-api");
		feature.status = Status.SUCCESS.getKeyword();
		writeToFile(feature);
		Assert.assertTrue(new File(baseFolder + "/" + feature.folderName + "/feature.xml").exists());
	}

	@Test
	public void featureComparison() {
		ImportFeature feature = new ImportFeature();

		feature.name = "Comparison";
		feature.description = "Contains the work to do for the comparison with older builds";
		feature.folderName = "comparison";
		writeToFile(feature);
		Assert.assertTrue(new File(baseFolder + "/" + feature.folderName + "/feature.xml").exists());
	}

	@Test
	public void featureFullTextSearch() {
		ImportFeature feature = new ImportFeature();

		feature.name = "Full Text Search";
		feature.description = "Every page must be searchable with full text search";
		feature.folderName = "full-text-search";
		writeToFile(feature);
		Assert.assertTrue(new File(baseFolder + "/" + feature.folderName + "/feature.xml").exists());
	}

	@Test
	public void featureSketchEditor() {
		ImportFeature feature = new ImportFeature();

		feature.name = "Sketch Editor";
		feature.description = "Editor to annotate Screenshots with extra information like Textboxes, or markings";
		feature.folderName = "sketch-editor";

		writeToFile(feature);
		Assert.assertTrue(new File(baseFolder + "/" + feature.folderName + "/feature.xml").exists());
	}

	@Test
	public void featureTreeNavigation() {
		ImportFeature feature = new ImportFeature();

		feature.name = "Featrue Tree Navigation";
		feature.description = "navigation to other featrues in currently sellected build";
		feature.folderName = "tree-navigation";

		writeToFile(feature);
		Assert.assertTrue(new File(baseFolder + "/" + feature.folderName + "/feature.xml").exists());
	}


	@Test
	public void featureRestAPI() {
		ImportFeature feature = new ImportFeature();
		feature.name = "Rest API";
		feature.description = "All changes necessary to the Rest API";
		feature.folderName = "rest-api";
		writeToFile(feature);
		Assert.assertTrue(new File(baseFolder + "/" + feature.folderName + "/feature.xml").exists());
	}

	@Test
	public void featureClientAPIConnection() {
		ImportFeature feature = new ImportFeature();
		feature.name = "Client API Connection";
		feature.description = " all changes to the client side API";
		feature.folderName = "client-api-connection";
		writeToFile(feature);
		Assert.assertTrue(new File(baseFolder + "/" + feature.folderName + "/feature.xml").exists());
	}


	private String baseFolder = "";

	public void writeToFile(ImportFeature feature) {
		File folder = new File(baseFolder);
		File featureFolder = new File(folder.getAbsolutePath() + "/" + feature.folderName);
		featureFolder.mkdir();
		ScenarioDocuXMLFileUtil.marshal(feature, new File(featureFolder.getAbsolutePath() + "/feature.xml"));
	}

	@Before
	public void Before() {
		baseFolder = new File("").getAbsolutePath() + "/docs/features/";
		new File(baseFolder).mkdirs();
	}


}
