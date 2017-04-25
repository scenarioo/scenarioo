package org.scenarioo.business.feature.importer;


import org.scenarioo.api.util.xml.ScenarioDocuXMLFileUtil;
import org.scenarioo.model.docu.feature.model.ImportFeature;
import org.scenarioo.model.docu.feature.model.Link;
import org.scenarioo.model.docu.feature.model.files.DokuFile;
import org.scenarioo.model.docu.feature.model.files.FileType;
import org.scenarioo.model.docu.feature.model.generic.Details;
import org.scenarioo.model.docu.feature.model.lable.Label;
import org.scenarioo.model.implementation.feature.model.Feature;
import org.scenarioo.repository.ConfigurationRepository;
import org.scenarioo.repository.RepositoryLocator;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Importer<T> {

	private Class<T> type;
	private String currentFolder;

	public Importer(Class<T> tClass, String currentFolderPath){
		currentFolder = currentFolderPath;
		type = tClass;
	}

	public T load(){
		return ScenarioDocuXMLFileUtil.unmarshal(type, new File(currentFolder +"/"+Validator.getTypeFiles().get(type)));
	}


	interface FileFilter{
		boolean accept(File file);
	}

	public static List<File> getFileList(String folder, FileFilter filter){
		ArrayList<File> list = new ArrayList<>();
		File[] files = new File(folder).listFiles();
		if (files != null){
			for(File f:files){
				if (filter.accept(f)){
					list.add(f);
				}
			}
		}
		return list;
	}



	public static void main(String[] args) throws Exception {

		long mili = System.currentTimeMillis();

		RepositoryLocator.INSTANCE.initializeConfigurationRepository("", "");
		ConfigurationRepository repo = RepositoryLocator.INSTANCE.getConfigurationRepository();

		FeatureLoader loader = new FeatureLoader("D:\\testdata\\branchname\\buildname\\");
		System.out.println(loader.loadTree());

		/*
		Importer importer = new Importer();
		List<File> branchList = importer.getFileList(new File("D:\\testdata\\").getAbsolutePath(), File::isDirectory);


		List<File> buildList = importer.getFileList(branchList.get(0).getAbsolutePath(), File::isDirectory);

		List<File> featureList = importer.getFileList(buildList.get(0).getAbsolutePath(), File::isDirectory);

		List<ImportFeature> importFeatures = new ArrayList<>();
		List<Feature> features =  new ArrayList<>();
		for (File file: featureList){
			ImportFeature importFeature = ScenarioDocuXMLFileUtil.unmarshal(ImportFeature.class, new File(file.getAbsolutePath() + "\\feature.xml"));
			importFeatures.add(importFeature);
			features.add(new Feature(importFeature));
		}

		//List<ImportFeature> importFeatures = ScenarioDocuXMLFileUtil.unmarshalListOfFiles(ImportFeature.class, featureList);
		for (Feature feature: features){
			for (String featureName : feature.featureNames){
				feature.features.add(getFor(featureName, features));
			}
		}

		for (Feature feature : features) {
			System.out.print(feature + ":: ( ");
			for (Feature sub : feature.features) {
				System.out.print(sub);
			}
			System.out.println(" )");
		}

		ImportFeature f1 = new ImportFeature();
		f1.name = "Find Page";
		f1.folderName = "find-page";
		f1.description = "User wants to search for a page and read it.";
		f1.milestone = "1 - first";
		f1.type = "useCase";
		f1.status = "SUCCESS";

		f1.scenarioNames = new ArrayList<>();
		f1.scenarioNames.add("find_multiple_results");
		f1.scenarioNames.add("find_no_results");
		f1.scenarioNames.add("find_page_title_ambiguous_directly");
		f1.scenarioNames.add("find_page_title_unique_directly");




		f1.details = new Details();
		f1.details.put("Webtest Class", "org.scenarioo.uitest.example.testcases.FindPageUITest");


		f1.labels = new HashSet<Label>();
		f1.labels.add(new Label() {{
			name = "normal-case";
		}});

		f1.featureNames = new ArrayList<>();
		f1.featureNames.add("add-page");
		f1.featureNames.add("load-page");


		f1.links = new ArrayList<Link>();
		f1.links.add(new Link(){{name = "Orinal DokuFile"; url = "https://google.com";}});

		f1.markdown = new DokuFile(){{
				name = "Doku for Find Page";
				url = "http://link.to/markdown.md";
				type = FileType.MARKDOWN;

				links = new ArrayList<Link>(){{
					add(new Link(){{name = "Orinal DokuFile"; url = "https://google.com";}});
				}};
			}};

		f1.specification = new DokuFile(){{
				name = "Doku for Find Page";
				url = "http://link.to/markdown.md";
				type = FileType.JAVA;

				links = new ArrayList<Link>(){{
					add(new Link(){{name = "Orinal DokuFile"; url = "https://google.com";}});
				}};
			}};


		ImportFeature f2 = new ImportFeature();
			f2.name = "Add Page";
		f2.folderName = "add-page";
		f2.description = "User wants to add a page.";
		f2.milestone = "1 - first";
		f2.type = "useCase";
		f2.status = "SUCCESS";

		f2.scenarioNames = new ArrayList<String>() {{
				add("find_multiple_results");
				add("find_no_results");
				add("find_page_title_ambiguous_directly");
				add("find_page_title_unique_directly");
			}};



		f2.details = new Details() {{
				put("Webtest Class", "org.scenarioo.uitest.example.testcases.FindPageUITest");
			}};

		f2.labels = new HashSet<Label>() {{
				add(new Label() {{
					name = "normal-case";
				}});
			}};

		f2.featureNames = new ArrayList<String>(){{
			}};

		f2.links = new ArrayList<Link>(){{
				add(new Link(){{name = "Orinal DokuFile"; url = "https://google.com";}});
			}};

		f2.markdown = new DokuFile(){{
				name = "Doku for Find Page";
				url = "http://link.to/markdown.md";
				type = FileType.MARKDOWN;

				links = new ArrayList<Link>(){{
					add(new Link(){{name = "Orinal DokuFile"; url = "https://google.com";}});
				}};
			}};

		f2.specification = new DokuFile(){{
				name = "Doku for Find Page";
				url = "http://link.to/markdown.md";
				type = FileType.JAVA;

				links = new ArrayList<Link>(){{
					add(new Link(){{name = "Orinal DokuFile"; url = "https://google.com";}});
				}};
			}};


		ImportFeature f3 = new ImportFeature();
		f3.name = "Load Page";
		f3.folderName = "load-page";
		f3.description = "User wants to load a page.";
		f3.milestone = "1 - first";
		f3.type = "useCase";
		f3.status = "SUCCESS";

		f3.scenarioNames = new ArrayList<String>() {{
				add("find_multiple_results");
				add("find_no_results");
				add("find_page_title_ambiguous_directly");
				add("find_page_title_unique_directly");
			}};



		f3.details = new Details() {{
				put("Webtest Class", "org.scenarioo.uitest.example.testcases.FindPageUITest");
			}};

		f3.labels = new HashSet<Label>() {{
				add(new Label() {{
					name = "normal-case";
				}});
			}};

		f3.featureNames = new ArrayList<String>(){{
			}};

		f3.links = new ArrayList<Link>(){{
				add(new Link(){{name = "Orinal DokuFile"; url = "https://google.com";}});
			}};

		f3.markdown = new DokuFile(){{
				name = "Doku for Find Page";
				url = "http://link.to/markdown.md";
				type = FileType.MARKDOWN;

				links = new ArrayList<Link>(){{
					add(new Link(){{name = "Orinal DokuFile"; url = "https://google.com";}});
				}};
			}};

		f3.specification = new DokuFile(){{
				name = "Doku for Find Page";
				url = "http://link.to/markdown.md";
				type = FileType.JAVA;

				links = new ArrayList<Link>(){{
					add(new Link(){{name = "Orinal DokuFile"; url = "https://google.com";}});
				}};
			}};


		//ScenarioDocuXMLFileUtil.marshal(f1, new DokuFile("D:\\testdata\\f1\\feature.xml"));
		//ScenarioDocuXMLFileUtil.marshal(f2, new DokuFile("D:\\testdata\\f2\\feature.xml"));
		//ScenarioDocuXMLFileUtil.marshal(f3, new DokuFile("D:\\testdata\\f3\\feature.xml"));



		System.err.println("Time: "+(System.currentTimeMillis()-mili));

		//ScenarioDocuXMLFileUtil.unmarshalListOfFiles(Branch.class, )

		*/
	}

	private static Feature getFor(String featureName, List<Feature> features) {
		for (Feature feature:features){
			if (feature.folderName.equals(featureName))
				return feature;
		}
		return null;
	}
}
