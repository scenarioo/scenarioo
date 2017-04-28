/* scenarioo-api
 * Copyright (C) 2014, scenarioo.org Development Team
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * As a special exception, the copyright holders of this library give you
 * permission to link this library with independent modules, according
 * to the GNU General Public License with "Classpath" exception as provided
 * in the LICENSE file that accompanied this code.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.scenarioo.api;

import org.scenarioo.api.files.ObjectFromDirectory;
import org.scenarioo.api.files.ScenarioDocuFiles;
import org.scenarioo.api.util.xml.ScenarioDocuXMLFileUtil;
import org.scenarioo.model.docu.entities.*;

import java.io.File;
import java.util.List;

import static org.scenarioo.api.rules.CharacterChecker.checkIdentifier;

/**
 * Gives access to the geenrated scenario docu files in the filesystem.
 */
public class ScenarioDocuReader {

	private final ScenarioDocuFiles docuFiles;

	public ScenarioDocuReader(final File rootDirectory) {
		this.docuFiles = new ScenarioDocuFiles(rootDirectory);
	}

	public Branch loadBranch(final String branchName) {
		File file = docuFiles.getBranchFile(checkIdentifier(branchName));
		return ScenarioDocuXMLFileUtil.unmarshal(Branch.class, file);
	}

	public List<Branch> loadBranches() {
		List<File> branchFiles = docuFiles.getBranchFiles();
		return ScenarioDocuXMLFileUtil.unmarshalListOfFiles(Branch.class, branchFiles);
	}

	public Build loadBuild(final String branchName, final String buildName) {
		File file = docuFiles.getBuildFile(checkIdentifier(branchName), checkIdentifier(buildName));
		return ScenarioDocuXMLFileUtil.unmarshal(Build.class, file);
	}

	/**
	 * This method was changed in in Version 2.0 of the API to return a list of ObjectFromDirectory&lt;Build&gt; instead
	 * of a list of BuildLinks. This was done because BuildLinks belongs to the Server now and is not part of the API
	 * anymore.
	 */
	public List<ObjectFromDirectory<Build>> loadBuilds(final String branchName) {
		List<File> buildFiles = docuFiles.getBuildFiles(checkIdentifier(branchName));
		return ScenarioDocuXMLFileUtil.unmarshalListOfFilesWithDirNames(buildFiles, Build.class);
	}

	public List<ImportFeature> loadUsecases(final String branchName, final String buildName) {
		List<File> files = docuFiles.getUseCaseFiles(checkIdentifier(branchName), checkIdentifier(buildName));
		return ScenarioDocuXMLFileUtil.unmarshalListOfFiles(ImportFeature.class, files);
	}

	public ImportFeature loadUsecase(final String branchName, final String buildName, final String useCaseName) {
		File file = docuFiles.getUseCaseFile(checkIdentifier(branchName), checkIdentifier(buildName),
				checkIdentifier(useCaseName));
		return ScenarioDocuXMLFileUtil.unmarshal(ImportFeature.class, file);
	}

	public List<Scenario> loadScenarios(final String branchName, final String buildName, final String useCaseName) {
		List<File> files = docuFiles.getScenarioFiles(checkIdentifier(branchName), checkIdentifier(buildName),
				checkIdentifier(useCaseName));
		return ScenarioDocuXMLFileUtil.unmarshalListOfFiles(Scenario.class, files);
	}

	public Scenario loadScenario(final String branchName, final String buildName, final String useCaseName,
			final String scenarioName) {
		File file = docuFiles.getScenarioFile(checkIdentifier(branchName), checkIdentifier(buildName),
				checkIdentifier(useCaseName), checkIdentifier(scenarioName));
		return ScenarioDocuXMLFileUtil.unmarshal(Scenario.class, file);
	}

	public List<Step> loadSteps(final String branchName, final String buildName, final String useCaseName,
			final String scenarioName) {
		List<File> files = docuFiles.getStepFiles(checkIdentifier(branchName), checkIdentifier(buildName),
				checkIdentifier(useCaseName), checkIdentifier(scenarioName));
		return ScenarioDocuXMLFileUtil.unmarshalListOfFiles(Step.class, files);
	}

	public Step loadStep(final String branchName, final String buildName, final String useCaseName,
			final String scenarioName, final int stepIndex) {
		File file = docuFiles.getStepFile(checkIdentifier(branchName), checkIdentifier(buildName),
				checkIdentifier(useCaseName), checkIdentifier(scenarioName), stepIndex);
		return ScenarioDocuXMLFileUtil.unmarshal(Step.class, file);
	}

	/**
	 * Screenshot files are simply provided by path, the REST service will take care of streaming it.
	 */
	public File getScreenshotFile(final String branchName, final String buildName, final String useCaseName,
			final String scenarioName, final String imageName) {
		return new File(docuFiles.getScreenshotsDirectory(checkIdentifier(branchName), checkIdentifier(buildName),
				checkIdentifier(useCaseName), checkIdentifier(scenarioName)), imageName);
	}

}
