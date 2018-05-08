package org.scenarioo.rest.diffViewer;

import org.scenarioo.dao.diffViewer.DiffViewerDao;
import org.scenarioo.model.diffViewer.BuildDiffInfo;
import org.scenarioo.model.diffViewer.ComparisonCalculationStatus;
import org.scenarioo.rest.base.BuildIdentifier;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
/**
 * Resource that returns information about all available comparison calculations, also the failed ones or still processing ones.
 */
@Path("/rest/comparisons")
public class ComparisonCalculationsResource {

	private DiffViewerDao diffViewerDao = new DiffViewerDao();

	@GET
	@Produces({"application/xml", "application/json"})
	public List<BuildDiffInfo> listAllComparisonResults() {
		return diffViewerDao.loadAllBuildDiffInfos();
	}

}
