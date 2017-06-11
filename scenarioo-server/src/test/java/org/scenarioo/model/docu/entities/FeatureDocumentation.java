package org.scenarioo.model.docu.entities;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.scenarioo.api.util.xml.ScenarioDocuXMLFileUtil;

import java.io.File;
import java.util.ArrayList;

public class FeatureDocumentation {


	public static final String FEATURE_XML = "/feature.xml";
	public static final DokuFile md1 = new DokuFile("Markdown-Test","https://raw.githubusercontent.com/scenarioo/scenarioo/develop/README.md",FileType.MARKDOWN);
	public static final DokuFile spec1 = new DokuFile("Specification-Test", "https://raw.githubusercontent.com/scenarioo/scenarioo/develop/scenarioo-client/test/spec/scenario/scenario.controller.spec.js", FileType.JAVASCRIPT);

	@Test
	public void featureDashboardView() {
		Feature feature = new Feature();

		feature.setName("Dashboard-View");
		feature.setDescription("Dashboard-View shows all the subfeatures of the current feature with all their subfeatures (=subsubfeatures) for a better overview of the whole project.\n" +
			"\tThe feature cards can be sortet, to  follow the story order or to show the closest release.");
		feature.setId("dashboard-view");
		feature.setStatus(Status.FAILED.getKeyword());
		feature.setMilestone("R3");
		feature.setMarkdown(md1);
		feature.setSpecification(spec1);
		writeToFile(feature);
		Assert.assertTrue(new File(baseFolder + "/" + feature.getId() + FEATURE_XML).exists());
	}

	@Test
	public void featureFeatureView() {
		Feature feature = new Feature();

		feature.setName("Feature-View");
		feature.setDescription("this view is compareable to the current feature view and shows the subfeatures of the current feature. the table can be sorted very easy after various topics, like name, release date, number of subfeatures or scenarios, changes form the last build");
		feature.setId("feature-view");
		feature.setStatus(Status.FAILED.getKeyword());
		feature.setMilestone("R1");
		feature.setMarkdown(md1);
		feature.setSpecification(spec1);
		writeToFile(feature);
		Assert.assertTrue(new File(baseFolder + "/" + feature.getId() + FEATURE_XML).exists());
	}

	@Test
	public void featureScenarioView() {
		Feature feature = new Feature();

		feature.setName("Scenario-View");
		feature.setDescription("Overview over the current scenarios, comparable to todays scenario view");
		feature.setId("scenario-view");
		feature.setStatus(Status.FAILED.getKeyword());
		feature.setMilestone("R1");
		feature.setMarkdown(md1);
		feature.setSpecification(spec1);
		writeToFile(feature);
		Assert.assertTrue(new File(baseFolder + "/" + feature.getId() + FEATURE_XML).exists());
	}

	@Test
	public void featureDocumentationView() {
		Feature feature = new Feature();

		feature.setName("Documentation-View");
		feature.setDescription("Shows the documentation file and the specification file of a feature and the subfeatures with their files. This view helps to explain the exact functions of a feature.");
		feature.setId("documentation-view");
		feature.setStatus(Status.FAILED.getKeyword());
		feature.setMilestone("R1");
		feature.setMarkdown(md1);
		feature.setSpecification(spec1);
		writeToFile(feature);
		Assert.assertTrue(new File(baseFolder + "/" + feature.getId() + FEATURE_XML).exists());
	}

	@Test
	public void featureGUI() {
		Feature feature = new Feature();

		feature.setName("GUI-Elements");
		feature.setDescription("The 4 GUI views, of which two will be slightly changed from the old scenarioo and two will be totaly new");
		feature.setId("gui-elements");
		feature.setFeatureNames(new ArrayList<String>());
		feature.getFeatureNames().add("feature-view");
		feature.getFeatureNames().add("scenario-view");
		feature.getFeatureNames().add("documentation-view");
		feature.getFeatureNames().add("dashboard-view");
		feature.getFeatureNames().add("tree-navigation");
		feature.setStatus(Status.SUCCESS.getKeyword());
		feature.setMilestone("R1");
		feature.setMarkdown(new DokuFile("GUI-Documentation", "GUI-Elements.md", FileType.MARKDOWN));
		feature.setSpecification(spec1);
		feature.getMarkdown().links = new ArrayList<>();
		Link mdLink = new Link();
		mdLink.name = "Original";
		mdLink.url = "https://google.com";
		feature.getMarkdown().links.add(mdLink);
		writeToFile(feature);
		Assert.assertTrue(new File(baseFolder + "/" + feature.getId() + FEATURE_XML).exists());
	}

	@Test
	public void featureImporter() {
		Feature feature = new Feature();

		feature.setName("Importer");
		feature.setDescription("Imports the data and fits it to the new data structure on the server");
		feature.setId("importer");
		feature.setStatus(Status.FAILED.getKeyword());
		feature.setMilestone("R1");
		feature.setMarkdown(md1);
		feature.setSpecification(spec1);
		writeToFile(feature);
		Assert.assertTrue(new File(baseFolder + "/" + feature.getId() + FEATURE_XML).exists());
	}

	@Test
	public void featureClient() {
		Feature feature = new Feature();

		feature.setName("Client");
		feature.setDescription("Contains all work todo on the client side. Including all the Views, the api-connection and the Diff- and sketch functionality.");
		feature.setId("client");
		feature.setFeatureNames(new ArrayList<String>());
		feature.getFeatureNames().add("gui-elements");
		feature.getFeatureNames().add("sketch-editor");
		feature.getFeatureNames().add("client-api-connection");
		feature.setStatus(Status.FAILED.getKeyword());
		feature.setMilestone("R1");
		feature.setMarkdown(md1);
		feature.setSpecification(spec1);
		writeToFile(feature);
		Assert.assertTrue(new File(baseFolder + "/" + feature.getId() + FEATURE_XML).exists());
	}

	@Test
	public void featureServer() {
		Feature feature = new Feature();

		feature.setName("Server");
		feature.setDescription("Contains all work todo on the server side");
		feature.setId("server");
		feature.setFeatureNames(new ArrayList<String>());
		feature.getFeatureNames().add("importer");
		feature.getFeatureNames().add("comparison");
		feature.getFeatureNames().add("full-text-search");
		feature.getFeatureNames().add("sketch-editor");
		feature.getFeatureNames().add("rest-api");
		feature.setStatus(Status.FAILED.getKeyword());
		feature.setMilestone("R1");
		feature.setMarkdown(md1);
		feature.setSpecification(spec1);
		writeToFile(feature);
		Assert.assertTrue(new File(baseFolder + "/" + feature.getId() + FEATURE_XML).exists());
	}

	@Test
	public void featureComparison() {
		Feature feature = new Feature();

		feature.setName("Comparison");
		feature.setDescription("Contains the work to do for the comparison with older builds");
		feature.setId("comparison");
		feature.setStatus(Status.FAILED.getKeyword());
		feature.setMilestone("R2");
		feature.setMarkdown(md1);
		feature.setSpecification(spec1);
		writeToFile(feature);
		Assert.assertTrue(new File(baseFolder + "/" + feature.getId() + FEATURE_XML).exists());
	}

	@Test
	public void featureFullTextSearch() {
		Feature feature = new Feature();

		feature.setName("Full Text Search");
		feature.setDescription("Every page must be searchable with full text search");
		feature.setId("full-text-search");
		feature.setStatus(Status.FAILED.getKeyword());
		feature.setMilestone("R2");
		feature.setMarkdown(md1);
		feature.setSpecification(spec1);
		writeToFile(feature);
		Assert.assertTrue(new File(baseFolder + "/" + feature.getId() + FEATURE_XML).exists());
	}

	@Test
	public void featureSketchEditor() {
		Feature feature = new Feature();

		feature.setName("Sketch Editor");
		feature.setDescription("Editor to annotate Screenshots with extra information like Textboxes, or markings");
		feature.setId("sketch-editor");
		feature.setStatus(Status.FAILED.getKeyword());
		feature.setMilestone("R3");
		feature.setMarkdown(md1);
		feature.setSpecification(spec1);
		writeToFile(feature);
		Assert.assertTrue(new File(baseFolder + "/" + feature.getId() + FEATURE_XML).exists());
	}

	@Test
	public void featureTreeNavigation() {
		Feature feature = new Feature();

		feature.setName("Featrue Tree Navigation");
		feature.setDescription("navigation to other featrues in currently sellected build");
		feature.setId("tree-navigation");
		feature.setStatus(Status.FAILED.getKeyword());
		feature.setMilestone("R2");
		feature.setMarkdown(md1);
		feature.setSpecification(spec1);
		writeToFile(feature);
		Assert.assertTrue(new File(baseFolder + "/" + feature.getId() + FEATURE_XML).exists());
	}


	@Test
	public void featureRestAPI() {
		Feature feature = new Feature();
		feature.setName("Rest API");
		feature.setDescription("All changes necessary to the Rest API");
		feature.setId("rest-api");
		feature.setStatus(Status.FAILED.getKeyword());
		feature.setMilestone("R1");
		feature.setMarkdown(md1);
		feature.setSpecification(spec1);
		writeToFile(feature);
		Assert.assertTrue(new File(baseFolder + "/" + feature.getId() + FEATURE_XML).exists());
	}

	@Test
	public void featureClientAPIConnection() {
		Feature feature = new Feature();
		feature.setName("Client API Connection");
		feature.setDescription(" all changes to the client side API");
		feature.setId("client-api-connection");
		feature.setStatus(Status.FAILED.getKeyword());
		feature.setMilestone("R1");
		feature.setMarkdown(md1);
		feature.setSpecification(spec1);
		writeToFile(feature);
		Assert.assertTrue(new File(baseFolder + "/" + feature.getId() + FEATURE_XML).exists());
	}


	private String baseFolder = "";

	public void writeToFile(Feature feature) {
		File folder = new File(baseFolder);
		File featureFolder = new File(folder.getAbsolutePath() + "/" + feature.getId());
		featureFolder.mkdir();
		ScenarioDocuXMLFileUtil.marshal(feature, new File(featureFolder.getAbsolutePath() + FEATURE_XML));
	}

	@Before
	public void Before() {
		baseFolder = new File("").getAbsolutePath() + "/docs/features/";
		new File(baseFolder).mkdirs();
	}

}
