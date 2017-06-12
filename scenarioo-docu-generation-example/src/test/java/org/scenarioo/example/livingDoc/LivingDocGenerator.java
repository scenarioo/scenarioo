package org.scenarioo.example.livingDoc;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.scenarioo.api.ScenarioDocuWriter;
import org.scenarioo.model.docu.entities.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple example generator to transform a file structure of markdown files and gherkin feature files
 * as well as other tests files into the new Scenarioo feature structure.
 */
public class LivingDocGenerator {

	File targetPath;

	List<DocLinkConfig> docLinkConfigs = new ArrayList<>();

	private ScenarioDocuWriter docuWriter;

	public LivingDocGenerator(File targetPath, String branchName, String buildName) {
		this.targetPath = targetPath;
		this.docuWriter = new ScenarioDocuWriter(targetPath, branchName, buildName);
	}

	/**
	 * Store XML with meta info for this branch.
	 */
	public void saveBranchDescription(Branch branch) {
		docuWriter.saveBranchDescription(branch);
	}

	/**
	 * Store XML with meta information for the whole build.
	 */
	public void saveBuildDescription(Build build) {
		docuWriter.saveBuildDescription(build);
	}

	/**
	 * Enable the egenrator to generate links for files with adding one (or several) link configurations.
	 */
	public void addDocLinkConfig(DocLinkConfig linkConfig) {
		docLinkConfigs.add(linkConfig);
	}

	/**
	 * Generate feature structure from docs directory with feature files.
	 * Structure is derived from recursive directory structure.
	 * Also will al the content of the source docs directory be copied to `docs` folder inside the generated docu build.
	 * @param docFilesSourceDir the source directory with feature files to generate feature structure from
	 */
	public void generateFeaturesFromDocs(File docFilesSourceDir) {
		try {

			// 1. copy all docs to docs folder in target dir
			FileUtils.copyDirectory(docFilesSourceDir, docuWriter.getDocsDirectory());

			// 2. recursively go through all documents in docs folder and generate features
			generateFeatures(docFilesSourceDir, "");

		} catch (IOException e) {
			throw new RuntimeException("IO Exception on writing feature structure", e);
		}
	}

	private List<Feature> generateFeatures(File directory, String basePath) {
		// Generate features for all sub files (except for README.md)
		List<Feature> features = new ArrayList<>();
		File[] files = directory.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				Feature feature = generateFeatureForDirectory(file, basePath + file.getName() + "/");
				features.add(feature);
			} else if (file.getName().equals("README.md")) {
				// ignore it, those files are used to document current feature directory only
			} else {
				Feature feature = generateFeatureForFile(file, basePath + file.getName());
				features.add(feature);
			}
		}
		return features;
	}

	private Feature generateFeatureForDirectory(File directory, String basePath) {

		// Create feature
		String name = directory.getName();
		Feature feature = new Feature(makeReadable(name), "description not yet parsed");
		feature.setId(name);

		// Attach README.md, if available
		File readmeFile = new File(directory, "README.md");
		if (readmeFile.exists()) {
			feature.setMarkdown(generateDocFile(readmeFile, basePath + "README.md"));
		}

		// Child features
		List<Feature> childFeatures = generateFeatures(directory, basePath);
		List<String> childFeatureNames = new ArrayList<>();
		for (Feature childFeature : childFeatures) {
			childFeatureNames.add(childFeature.getId());
		}
		feature.setFeatureNames(childFeatureNames);

		// save
		docuWriter.saveFeature(feature);
		return feature;
	}

	private Feature generateFeatureForFile(File file, String basePath) {
		String name = truncEnding(file.getName());
		Feature feature = new Feature(makeReadable(name), "description not yet parsed");
		feature.setId(name);
		defineFeatureDetailsFromFileContent(feature, file);
		if (file.getName().toLowerCase().endsWith(".md")) {
			feature.setMarkdown(generateDocFile(file, basePath));
		} else {
			feature.setSpecification(generateDocFile(file, basePath));
		}
		docuWriter.saveFeature(feature);
		return feature;
	}

	private DokuFile generateDocFile(File file, String relativePath) {
		DokuFile docFile = new DokuFile();
		docFile.name = file.getName();
		docFile.url = relativePath;
		docFile.type = FileType.getFromFileEnding(file);
		for (DocLinkConfig linkConfig : docLinkConfigs) {
			docFile.addLink(new Link(linkConfig.getName(), linkConfig.getBaseUrl() + "/" + relativePath));
		}
		return docFile;
	}

	private void defineFeatureDetailsFromFileContent(Feature feature, File file) {
		// TODO: work in progress ...
		// Parse the file content line by line:
		// if a line starts with "# Milestone:" or "Milestone:" --> read the rest as milestone
		// if a line starts with "Order:" --> read the rest as orderIndex
		// if possible: try to extract a useful short description as well (first paragraph)
	}

	private String truncEnding(String fileName) {
		return fileName.split("\\.")[0];
	}

	/**
	 * Make a feature name readable, by applying following rules:
	 * * replace `-` by space
	 * * capitalize start of words
	 * @param name the name to make readable
	 */
	private String makeReadable(String name) {
		String nameWithSpaces = name.replaceAll("-", " ");
		return WordUtils.capitalize(nameWithSpaces);
	}

	/**
	 * Wait for all asynch writing to be finished.
	 */
	public void flush() {
		docuWriter.flush();
	}

}
