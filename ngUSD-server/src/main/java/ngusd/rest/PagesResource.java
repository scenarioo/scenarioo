package ngusd.rest;

import java.util.Arrays;
import java.util.Collection;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import ngusd.rest.model.Page;
import ngusd.rest.model.Step;

@Path("/rest/branches/{branchId}/builds/{buildId}/usecases/{usecaseId}/scenarios/{scenarioId}/pages/")
public class PagesResource {
	
	@GET
	@Produces({ "application/xml", "application/json" })
	public Collection<Page> listPages(@PathParam("branchId") final String branchId,
			@PathParam("buildId") final String buildId, @PathParam("usecaseId") final String usecaseId,
			@PathParam("scenarioId") final String scenarioId) {
		
		Page page1 = new Page("mainPage");
		Step step1InPage1 = new Step("listAllUseCases");
		Step step2InPage1 = new Step("listAllServiceCalls");
		Step step3InPage1 = new Step("performFulltextSearch");
		page1.addStep(step1InPage1);
		page1.addStep(step2InPage1);
		page1.addStep(step3InPage1);
		
		Page page2 = new Page("usecaseOverview");
		Step step1InPage2 = new Step("listAllScenarios");
		page2.addStep(step1InPage2);
		
		return Arrays.asList(page1, page2);
		
	}
	
}
