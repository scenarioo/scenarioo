package ngusd.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import ngusd.rest.model.Scenario;
import ngusd.rest.model.UseCase;

@Path("/rest/scenarios")
public class ScenariosResource {
	
	@GET
	@Produces({ "application/xml", "application/json" })
	public List<UseCase> listUseCaseScenarios() {
		
		// Create a dummy model
		
		List<UseCase> cases = new ArrayList<UseCase>();
		
		UseCase case1 = new UseCase("Browsing Branches and Builds", "A User can navigate through Branches and Builds");
		case1.getProperties().put("testClassName", "BranchAndBuildNavigationViewWebTest");
		case1.getScenarios().add(
				createScenario("default_selection_is_trunk_latest_green",
						"The default selection is the latest green build on trunk"));
		case1.getScenarios().add(
				createScenario("selecting_build_on_different_branch", "User can select a branch and build through UI"));
		case1.getScenarios().add(
				createScenario("selecting_branch_and_build_through_url",
						"Branch and build can be selected through URL parameters"));
		cases.add(case1);
		
		UseCase case2 = new UseCase("Browsing Usecases", "Customer can browse all use cases and select a use case");
		case2.getProperties().put("testClassName", "UseCasesViewWebTest");
		case2.getScenarios().add(
				createScenario("table_of_usecases_displayed",
						"The table of all use cases is presented correctly and the user can filter it"));
		case2.getScenarios().add(
				createScenario("selecting_use_case",
						"A use case is selected without using the filter search"));
		case2.getScenarios().add(
				createScenario("selecting_use_case_with_filter_search",
						"A use case is selected by searching it through filter search."));
		
		cases.add(case2);
		
		return cases;
	}
	
	private Scenario createScenario(final String name, final String description) {
		Scenario scenario = new Scenario(name, description, 9, 42);
		scenario.setStatus("green");
		return scenario;
	}
}
