package ngusd.rest.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DummyDataProvider {
	private static ThreadLocal<DummyDataProvider> THREAD_LOCAL = new ThreadLocal<DummyDataProvider>() {
		@Override
		protected DummyDataProvider initialValue() {
			return new DummyDataProvider();
		}
		
	};
	private final Map<String, Branch> branches;
	
	private DummyDataProvider() {
		Branch trunk = new Branch("trunk", "main development");
		Build build1 = new Build("build1");
		build1.setDate(new Date());
		build1.setRevision("rev:42");
		build1.setState("successful");
		trunk.addBuild(build1);
		
		Build build2 = new Build("build2");
		build2.setDate(new Date());
		build2.setRevision("rev:56");
		build2.setState("failed");
		trunk.addBuild(build1);
		
		Branch maiBranch = new Branch("mai", "");
		Build build3 = new Build("build3");
		build3.setDate(new Date());
		build3.setRevision("rev:22");
		build3.setState("successful");
		maiBranch.addBuild(build3);
		
		Build build4 = new Build("build4");
		build4.setDate(new Date());
		build4.setRevision("rev:12");
		build4.setState("warning");
		maiBranch.addBuild(build4);
		
		branches = new HashMap<String, Branch>();
		branches.put(trunk.getName(), trunk);
		branches.put(maiBranch.getName(), maiBranch);
	}
	
	public static DummyDataProvider getInstance() {
		return THREAD_LOCAL.get();
	}
	
	public Map<String, Branch> getBranches() {
		return branches;
	}
	
	public Branch getBranch(final String branchName) {
		return this.branches.get(branchName);
		
	}
}
