package org.scenarioo.rest.diffViewer;

import org.scenarioo.model.configuration.ComparisonConfiguration;
import org.scenarioo.model.diffViewer.BuildDiffInfo;
import org.scenarioo.model.diffViewer.ComparisonCalculationStatus;
import org.scenarioo.model.diffViewer.ComparisonCalculation;
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
public class AllComparisonsResource {

	@GET
	@Produces({"application/xml", "application/json"})
	public List<BuildDiffInfo> listAllComparisonResults() {
		return Arrays.asList(
			createComparisonResult("feature-650-diff-list", "2017-11-29", "To last develop",
				ComparisonCalculationStatus.SUCCESS, 22.3542),
			createComparisonResult("feature-650-diff-list", "2017-11-30", "To last develop",
				ComparisonCalculationStatus.PROCESSING, 10.5));
	}

	private BuildDiffInfo createComparisonResult(String branchName, String buildName, String comparisonName,
													ComparisonCalculationStatus calculationStatus, double changeRate) {

		comparisonResult.setBaseBuild(new BuildIdentifier(branchName, buildName));
		comparisonResult.setCompareBuild(new BuildIdentifier("develop", "2017-11-28"));
		comparisonResult.setComparisonName(comparisonName);
		comparisonResult.setComparisonConfiguration(comparisonConfiguration);

		comparisonResult.setStatus(calculationStatus);
		comparisonResult.setChangeRate(changeRate);

		comparisonResult.setBaseBuildDate(new Date());
		comparisonResult.setCalculationDate(new Date());

		return comparisonResult;
	}

}
