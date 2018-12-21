package org.scenarioo.rest.diffViewer;

import org.apache.log4j.Logger;
import org.scenarioo.dao.diffViewer.DiffViewerDao;
import org.scenarioo.model.diffViewer.BuildDiffInfo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
/**
 * Resource that returns information about all available comparison calculations, also the failed ones or still processing ones.
 */
@RestController
@RequestMapping("/rest/comparisons")
public class ComparisonCalculationsResource {

	private static final Logger LOGGER = Logger.getLogger(ComparisonCalculationsResource.class);

	private DiffViewerDao diffViewerDao = new DiffViewerDao();

	@GetMapping
	public List<BuildDiffInfo> listAllComparisonResults() {
		try {
			return diffViewerDao.loadAllBuildDiffInfos();
		} catch (Throwable e) {
			LOGGER.error("Could not load comparison list (corrupted data?)", e);
			throw e;
		}
	}

}
