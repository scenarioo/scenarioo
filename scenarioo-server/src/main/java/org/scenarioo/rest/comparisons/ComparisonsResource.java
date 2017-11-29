package org.scenarioo.rest.comparisons;

import org.scenarioo.model.configuration.ComparisonConfiguration;
import org.scenarioo.model.diffViewer.ComparisonCalculationStatus;
import org.scenarioo.model.diffViewer.ComparisonResult;
import org.scenarioo.rest.base.BuildIdentifier;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Path("/rest/comparisons")
public class ComparisonsResource {

	@GET
	@Produces({"application/xml", "application/json"})
	public List<ComparisonResult> listAllComparisonResults() {
		ComparisonResult comparisonResult = new ComparisonResult();
		comparisonResult.setBaseBuild(new BuildIdentifier("feature-650-diff-list", "2017-11-29"));
		comparisonResult.setCompareBuild(new BuildIdentifier("develop", "2017-11-28"));
		ComparisonConfiguration comparisonConfiguration = new ComparisonConfiguration();
		comparisonConfiguration.setName("To last develop");
		comparisonResult.setComparisonConfiguration(comparisonConfiguration);
		comparisonResult.setChangeRate(22.3542);
		comparisonResult.setBaseBuildDate(new Date());
		comparisonResult.setCalculationDate(new Date());
		comparisonResult.setStatus(ComparisonCalculationStatus.SUCCESS);
		return Collections.singletonList(comparisonResult);
	}

}
