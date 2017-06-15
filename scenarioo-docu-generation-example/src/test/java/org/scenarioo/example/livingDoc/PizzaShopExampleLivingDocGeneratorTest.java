package org.scenarioo.example.livingDoc;

import org.junit.Test;
import org.scenarioo.model.docu.entities.Branch;
import org.scenarioo.model.docu.entities.Build;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Date;

public class PizzaShopExampleLivingDocGeneratorTest {

	@Test
	public void generatePizzaShopExampleData() throws URISyntaxException {

		// Config of generator:
		String branchName = "PizzaaaS - Living Doc Example";
		String buildName = "build-1";
		File targetDir = new File("build/scenarioDocuExample");
		File docFilesSourceDir = getResourceFile("example/pizza-shop-example");
		LivingDocGenerator generator = new LivingDocGenerator(targetDir, branchName, buildName);

		// Write Branch Meta Data (container for several builds)
		Branch branch = new Branch(branchName);
		branch.setDescription("Example living documentation of PizzaaaS - the next generation world wide 'PaaS' (Pizza as a Service) platform.");
		generator.saveBranchDescription(branch);

		// Write Build Meta Data (container for one generated version of the documentation)
		Build build = new Build(buildName);
		build.setRevision("0.0.1");
		build.setDate(new Date());
		generator.saveBuildDescription(build);

		// Configure links to generate for docs
		String gitRepoUrl = "https://github.com/scenarioo/scenarioo";
		String branchForDocReferences = "develop";
		String docFilesDirFullPath = "/scenarioo-docu-generation-example/src/test/resources/example/pizza-shop-example";
		String docFilesGitViewLinkBasePath = gitRepoUrl + "/blob/" + branchForDocReferences + docFilesDirFullPath;
		String docFilesGitEditLinkBasePath = gitRepoUrl + "/edit/" + branchForDocReferences + docFilesDirFullPath;
		generator.addDocLinkConfig(new DocLinkConfig("git", docFilesGitViewLinkBasePath));
		generator.addDocLinkConfig(new DocLinkConfig("edit", docFilesGitEditLinkBasePath));

		// Generate feature structure from docs directory with feature files
		// (structure is derived from recursive directory structure)
		generator.generateFeaturesFromDocs(docFilesSourceDir);

		// Flush all asynch writing
		generator.flush();

		System.out.println("Pizza Shop Living Doc Example generated.");

	}

	private static File getResourceFile(final String relativeResourcePath) throws URISyntaxException {
		URL url = PizzaShopExampleLivingDocGeneratorTest.class.getClassLoader().getResource(relativeResourcePath);
		return new File(url.toURI());
	}

}
