package org.scenarioo.model.docu.entities;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.scenarioo.api.util.xml.ScenarioDocuXMLFileUtil;

import java.io.File;
import java.util.ArrayList;

public class FeatureDocumentation {


	public static final String FEATURE_XML = "/usecase.xml";
	public static final DokuFile md1 = new DokuFile("Markdown-Test","https://raw.githubusercontent.com/scenarioo/scenarioo/develop/README.md",FileType.MARKDOWN);
	public static final DokuFile spec1 = new DokuFile("Specification-Test", "https://raw.githubusercontent.com/scenarioo/scenarioo/develop/scenarioo-client/test/spec/scenario/scenario.controller.spec.js", FileType.JAVASCRIPT);

	@Test
	public void featureDashboardView() {
		ImportFeature feature = new ImportFeature();

		feature.name = "Dashboard-View";
		feature.description = "Dashboard-View shows all the subfeatures of the current feature with all their subfeatures (=subsubfeatures) for a better overview of the whole project.\n" +
			"\tThe feature cards can be sortet, to  follow the story order or to show the closest release.";
		feature.id = "dashboard-view";
		feature.status = Status.FAILED.getKeyword();
		feature.milestone = "R3";
		feature.markdown=md1;
		feature.specification=spec1;
		writeToFile(feature);
		Assert.assertTrue(new File(baseFolder + "/" + feature.id + FEATURE_XML).exists());
	}

	@Test
	public void featureFeatureView() {
		ImportFeature feature = new ImportFeature();

		feature.name = "Feature-View";
		feature.description = "this view is compareable to the current useCase view and shows the subfeatures of the current feature. the table can be sorted very easy after various topics, like name, release date, number of subfeatures or scenarios, changes form the last build";
		feature.id = "feature-view";
		feature.status = Status.FAILED.getKeyword();
		feature.milestone = "R1";
		feature.markdown=md1;
		feature.specification=spec1;
		writeToFile(feature);
		Assert.assertTrue(new File(baseFolder + "/" + feature.id + FEATURE_XML).exists());
	}

	@Test
	public void featureScenarioView() {
		ImportFeature feature = new ImportFeature();

		feature.name = "Scenario-View";
		feature.description = "Overview over the current scenarios, comparable to todays scenario view";
		feature.id = "scenario-view";
		feature.status = Status.FAILED.getKeyword();
		feature.milestone = "R1";
		feature.markdown=md1;
		feature.specification=spec1;
		writeToFile(feature);
		Assert.assertTrue(new File(baseFolder + "/" + feature.id + FEATURE_XML).exists());
	}

	@Test
	public void featureDocumentationView() {
		ImportFeature feature = new ImportFeature();

		feature.name = "Documentation-View";
		feature.description = "Shows the documentation file and the specification file of a feature and the subfeatures with their files. This view helps to explain the exact functions of a feature.";
		feature.id = "documentation-view";
		feature.status = Status.FAILED.getKeyword();
		feature.milestone = "R1";
		feature.markdown=md1;
		feature.specification=spec1;
		writeToFile(feature);
		Assert.assertTrue(new File(baseFolder + "/" + feature.id + FEATURE_XML).exists());
	}

	@Test
	public void featureGUI() {
		ImportFeature feature = new ImportFeature();

		feature.name = "GUI-Elements";
		feature.description = "The 4 GUI views, of which two will be slightly changed from the old scenarioo and two will be totaly new";
		feature.id = "gui-elements";
		feature.featureNames = new ArrayList<String>();
		feature.featureNames.add("feature-view");
		feature.featureNames.add("scenario-view");
		feature.featureNames.add("documentation-view");
		feature.featureNames.add("dashboard-view");
		feature.featureNames.add("tree-navigation");
		feature.status = Status.SUCCESS.getKeyword();
		feature.milestone = "R1";
		feature.markdown=new DokuFile("GUI-Documentation", "GUI-Elements.md", FileType.MARKDOWN);
		feature.specification=spec1;
		feature.markdown.links = new ArrayList<>();
		Link mdLink = new Link();
		mdLink.name = "Original";
		mdLink.url = "https://google.com";
		feature.markdown.links.add(mdLink);
		writeToFile(feature);
		Assert.assertTrue(new File(baseFolder + "/" + feature.id + FEATURE_XML).exists());
	}

	@Test
	public void featureImporter() {
		ImportFeature feature = new ImportFeature();

		feature.name = "Importer";
		feature.description = "Imports the data and fits it to the new data structure on the server";
		feature.id = "importer";
		feature.status = Status.FAILED.getKeyword();
		feature.milestone = "R1";
		feature.markdown=md1;
		feature.specification=spec1;
		writeToFile(feature);
		Assert.assertTrue(new File(baseFolder + "/" + feature.id + FEATURE_XML).exists());
	}

	@Test
	public void featureClient() {
		ImportFeature feature = new ImportFeature();

		feature.name = "Client";
		feature.description = "Contains all work todo on the client side. Including all the Views, the api-connection and the Diff- and sketch functionality.";
		feature.id = "client";
		feature.featureNames = new ArrayList<String>();
		feature.featureNames.add("gui-elements");
		feature.featureNames.add("sketch-editor");
		feature.featureNames.add("client-api-connection");
		feature.status = Status.FAILED.getKeyword();
		feature.milestone = "R1";
		feature.markdown=md1;
		feature.specification=spec1;
		writeToFile(feature);
		Assert.assertTrue(new File(baseFolder + "/" + feature.id + FEATURE_XML).exists());
	}

	@Test
	public void featureServer() {
		ImportFeature feature = new ImportFeature();

		feature.name = "Server";
		feature.description = "Contains all work todo on the server side";
		feature.id = "server";
		feature.featureNames = new ArrayList<String>();
		feature.featureNames.add("importer");
		feature.featureNames.add("comparison");
		feature.featureNames.add("full-text-search");
		feature.featureNames.add("sketch-editor");
		feature.featureNames.add("rest-api");
		feature.status = Status.FAILED.getKeyword();
		feature.milestone = "R1";
		feature.markdown=md1;
		feature.specification=spec1;
		writeToFile(feature);
		Assert.assertTrue(new File(baseFolder + "/" + feature.id + FEATURE_XML).exists());
	}

	@Test
	public void featureComparison() {
		ImportFeature feature = new ImportFeature();

		feature.name = "Comparison";
		feature.description = "Contains the work to do for the comparison with older builds";
		feature.id = "comparison";
		feature.status = Status.FAILED.getKeyword();
		feature.milestone = "R2";
		feature.markdown=md1;
		feature.specification=spec1;
		writeToFile(feature);
		Assert.assertTrue(new File(baseFolder + "/" + feature.id + FEATURE_XML).exists());
	}

	@Test
	public void featureFullTextSearch() {
		ImportFeature feature = new ImportFeature();

		feature.name = "Full Text Search";
		feature.description = "Every page must be searchable with full text search";
		feature.id = "full-text-search";
		feature.status = Status.FAILED.getKeyword();
		feature.milestone = "R2";
		feature.markdown=md1;
		feature.specification=spec1;
		writeToFile(feature);
		Assert.assertTrue(new File(baseFolder + "/" + feature.id + FEATURE_XML).exists());
	}

	@Test
	public void featureSketchEditor() {
		ImportFeature feature = new ImportFeature();

		feature.name = "Sketch Editor";
		feature.description = "Editor to annotate Screenshots with extra information like Textboxes, or markings";
		feature.id = "sketch-editor";
		feature.status = Status.FAILED.getKeyword();
		feature.milestone = "R3";
		feature.markdown=md1;
		feature.specification=spec1;
		writeToFile(feature);
		Assert.assertTrue(new File(baseFolder + "/" + feature.id + FEATURE_XML).exists());
	}

	@Test
	public void featureTreeNavigation() {
		ImportFeature feature = new ImportFeature();

		feature.name = "Featrue Tree Navigation";
		feature.description = "navigation to other featrues in currently sellected build";
		feature.id = "tree-navigation";
		feature.status = Status.FAILED.getKeyword();
		feature.milestone = "R2";
		feature.markdown=md1;
		feature.specification=spec1;
		writeToFile(feature);
		Assert.assertTrue(new File(baseFolder + "/" + feature.id + FEATURE_XML).exists());
	}


	@Test
	public void featureRestAPI() {
		ImportFeature feature = new ImportFeature();
		feature.name = "Rest API";
		feature.description = "All changes necessary to the Rest API";
		feature.id = "rest-api";
		feature.status = Status.FAILED.getKeyword();
		feature.milestone = "R1";
		feature.markdown=md1;
		feature.specification=spec1;
		writeToFile(feature);
		Assert.assertTrue(new File(baseFolder + "/" + feature.id + FEATURE_XML).exists());
	}

	@Test
	public void featureClientAPIConnection() {
		ImportFeature feature = new ImportFeature();
		feature.name = "Client API Connection";
		feature.description = " all changes to the client side API";
		feature.id = "client-api-connection";
		feature.status = Status.FAILED.getKeyword();
		feature.milestone = "R1";
		feature.markdown=md1;
		feature.specification=spec1;
		writeToFile(feature);
		Assert.assertTrue(new File(baseFolder + "/" + feature.id + FEATURE_XML).exists());
	}


	private String baseFolder = "";

	public void writeToFile(ImportFeature feature) {
		File folder = new File(baseFolder);
		File featureFolder = new File(folder.getAbsolutePath() + "/" + feature.id);
		featureFolder.mkdir();
		ScenarioDocuXMLFileUtil.marshal(feature, new File(featureFolder.getAbsolutePath() + FEATURE_XML));
	}

	@Before
	public void Before() {
		baseFolder = new File("").getAbsolutePath() + "/docs/features/";
		new File(baseFolder).mkdirs();
	}

}
