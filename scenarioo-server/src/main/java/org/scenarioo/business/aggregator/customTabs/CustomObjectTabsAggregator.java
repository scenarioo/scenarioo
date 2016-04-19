package org.scenarioo.business.aggregator.customTabs;

import java.util.LinkedList;
import java.util.List;

import org.scenarioo.dao.aggregates.ScenarioDocuAggregationDao;
import org.scenarioo.model.configuration.CustomObjectTab;
import org.scenarioo.model.docu.entities.generic.ObjectDescription;
import org.scenarioo.model.docu.entities.generic.ObjectReference;
import org.scenarioo.rest.base.BuildIdentifier;

/**
 * Manages to aggregate all objects for configured custom object tab trees.
 */
public class CustomObjectTabsAggregator {

	List<CustomObjectTabTreeBuilder> customObjectTabTreeBuilders = new LinkedList<CustomObjectTabTreeBuilder>();

	public CustomObjectTabsAggregator(
			final List<CustomObjectTab> configuredTabs,
			final ScenarioDocuAggregationDao dao,
			final BuildIdentifier buildIdentifier) {
		for (CustomObjectTab tab : configuredTabs) {
			customObjectTabTreeBuilders.add(new CustomObjectTabTreeBuilder(tab,
					dao, buildIdentifier));
		}
	}

	public void aggregateRelevantObjectIntoCustomObjectTabTrees(
			final List<ObjectReference> referencePath,
			final ObjectDescription object) {
		for (CustomObjectTabTreeBuilder treeBuilder : customObjectTabTreeBuilders) {
			treeBuilder.addRelevantObjectIntoTreeStructure(referencePath,
					object);
		}
	}

	public void saveAggregatedTreeStructures() {
		for (CustomObjectTabTreeBuilder treeBuilder : customObjectTabTreeBuilders) {
			treeBuilder.saveAggregatedTreeStructure();
		}
	}

}
