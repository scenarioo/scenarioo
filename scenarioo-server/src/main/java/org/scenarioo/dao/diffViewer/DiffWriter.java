/* scenarioo-server
 * Copyright (C) 2014, scenarioo.org Development Team
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.scenarioo.dao.diffViewer;

import org.scenarioo.api.exception.ScenarioDocuSaveException;
import org.scenarioo.api.exception.ScenarioDocuTimeoutException;
import org.scenarioo.model.diffViewer.BuildDiffInfo;
import org.scenarioo.model.diffViewer.ScenarioDiffInfo;
import org.scenarioo.model.diffViewer.StepDiffInfo;
import org.scenarioo.model.diffViewer.FeatureDiffInfo;

public interface DiffWriter {

	void saveBuildDiffInfo(BuildDiffInfo buildDiffInfo);

	void saveFeatureDiffInfo(FeatureDiffInfo featureDiffInfo);

	void saveScenarioDiffInfo(ScenarioDiffInfo scenarioDiffInfo, String featureName);

	void saveStepDiffInfo(String featureName, String scenarioName, StepDiffInfo stepDiffInfo);

	/**
	 * Finish asynchronous writing of all saved files. This has to be called in the end, to ensure all data saved in
	 * this generator is written to the filesystem.
	 *
	 * Will block until writing has finished or timeout occurs.
	 *
	 * @throws ScenarioDocuSaveException
	 *             if any of the save commands throwed an exception during asynchronous execution.
	 * @throws ScenarioDocuTimeoutException
	 *             if waiting for the saving beeing finished exceeds the configured timeout
	 */
	void flush() throws ScenarioDocuSaveException, ScenarioDocuTimeoutException;
}
