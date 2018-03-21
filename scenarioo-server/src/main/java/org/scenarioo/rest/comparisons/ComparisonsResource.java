package org.scenarioo.rest.comparisons;

import org.scenarioo.model.configuration.ComparisonConfiguration;
import org.scenarioo.model.diffViewer.ComparisonCalculationStatus;
import org.scenarioo.model.diffViewer.ComparisonResult;
import org.scenarioo.rest.base.BuildIdentifier;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Path("/rest/comparisons")
public class ComparisonsResource {

	@GET
	@Produces({"application/xml", "application/json"})
	public List<ComparisonResult> listAllComparisonResults() {
		return Arrays.asList(
			createComparisonResult("feature-650-diff-list", "2017-11-29", "To last develop",
				ComparisonCalculationStatus.SUCCESS, 22.3542),
			createComparisonResult("feature-650-diff-list", "2017-11-30", "To last develop",
				ComparisonCalculationStatus.PROCESSING, 10.5));
	}

	private ComparisonResult createComparisonResult(String branchName, String buildName, String comparisonName,
													ComparisonCalculationStatus calculationStatus, double changeRate) {
		ComparisonResult comparisonResult = new ComparisonResult();
		comparisonResult.setBaseBuild(new BuildIdentifier(branchName, buildName));
		comparisonResult.setCompareBuild(new BuildIdentifier("develop", "2017-11-28"));
		ComparisonConfiguration comparisonConfiguration = new ComparisonConfiguration();
		comparisonConfiguration.setName(comparisonName);
		comparisonResult.setComparisonConfiguration(comparisonConfiguration);
		comparisonResult.setChangeRate(changeRate);
		comparisonResult.setBaseBuildDate(new Date());
		comparisonResult.setCalculationDate(new Date());
		comparisonResult.setStatus(calculationStatus);
		return comparisonResult;
	}

}
