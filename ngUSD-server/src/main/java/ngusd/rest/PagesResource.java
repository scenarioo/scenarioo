package ngusd.rest;

import java.util.Arrays;
import java.util.Collection;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import ngusd.docu.model.Details;
import ngusd.docu.model.Page;
import ngusd.docu.model.StepDescription;

@Path("/rest/branches/{branchId}/builds/{buildId}/usecases/{usecaseId}/scenarios/{scenarioId}/pages/")
public class PagesResource {
	
	@GET
	@Produces({ "application/xml", "application/json" })
	public Collection<Page> listPages(@PathParam("branchId") final String branchId,
			@PathParam("buildId") final String buildId, @PathParam("usecaseId") final String usecaseId,
			@PathParam("scenarioId") final String scenarioId) {
		
		Page page1 = new Page("mainPage");
		Details detailsPage1 = page1.getDetails();
		detailsPage1.addDetail("url", "www.google.ch");
		StepDescription step1InPage1 = new StepDescription();
		step1InPage1.getDetails().addDetail("action", "listAllUseCases");
		StepDescription step2InPage1 = new StepDescription();
		step2InPage1.getDetails().addDetail("action", "listAllServiceCalls");
		StepDescription step3InPage1 = new StepDescription();
		step3InPage1.getDetails().addDetail("action", "performFulltextSearch");
		// page1.addStep(step1InPage1);
		// page1.addStep(step2InPage1);
		// page1.addStep(step3InPage1);
		
		Page page2 = new Page("usecaseOverview");
		page2.getDetails().addDetail("url", "www.fork.ch");
		StepDescription step1InPage2 = new StepDescription();
		step1InPage2.getDetails().addDetail("action", "listAllScenarios");
		
		// page2.addStep(step1InPage2);
		
		return Arrays.asList(page1, page2);
		
	}
}
