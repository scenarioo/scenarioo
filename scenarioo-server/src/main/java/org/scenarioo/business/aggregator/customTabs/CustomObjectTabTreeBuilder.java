package org.scenarioo.business.aggregator.customTabs;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.scenarioo.dao.aggregates.ScenarioDocuAggregationDao;
import org.scenarioo.model.configuration.CustomObjectDetailColumn;
import org.scenarioo.model.configuration.CustomObjectTab;
import org.scenarioo.model.docu.aggregates.objects.CustomObjectTabTree;
import org.scenarioo.model.docu.entities.generic.ObjectDescription;
import org.scenarioo.model.docu.entities.generic.ObjectReference;
import org.scenarioo.model.docu.entities.generic.ObjectTreeNode;
import org.scenarioo.rest.base.BuildIdentifier;

/**
 * This builder collects all found objects for some configured object types (as configured by the user in configuration
 * as a {@link CustomObjectTab}) and puts them into a tree structure to be displayed on this configured custom object
 * tab.
 */
public class CustomObjectTabTreeBuilder {
	
	private static final Logger LOGGER = Logger.getLogger(CustomObjectTabTreeBuilder.class);
	
	private final CustomObjectTab tabConfig;
	
	private final Set<String> objectTypesToAggregate;
	
	private final ScenarioDocuAggregationDao dao;
	
	private final BuildIdentifier buildIdentifier;
	
	private final ObjectReferenceTreeBuilder treeToBuild = new ObjectReferenceTreeBuilder(
			new ObjectTreeNode<ObjectReference>());
	
	public CustomObjectTabTreeBuilder(final CustomObjectTab tabConfig, final ScenarioDocuAggregationDao dao,
			final BuildIdentifier buildIdentifier) {
		this.tabConfig = tabConfig;
		this.dao = dao;
		this.buildIdentifier = buildIdentifier;
		objectTypesToAggregate = new HashSet<String>(tabConfig.getObjectTypesToDisplay());
	}
	
	/**
	 * Checks the passed object whether it is relevant for the current custom tab tree structure, if relevant adds it to
	 * the correct position.
	 */
	public void addRelevantObjectIntoTreeStructure(final List<ObjectReference> referencePath,
			final ObjectDescription object) {
		if (objectTypesToAggregate.contains(object.getType())) {
			List<ObjectReference> relevantReferencePath = getPathFilteredForRelevantTypes(referencePath);
			ObjectReferenceTreeBuilder builder = treeToBuild.findNode(relevantReferencePath);
			if (!builder.containsChild(object)) {
				builder.addChild(createObjectTreeNode(object));
			}
		}
	}
	
	private List<ObjectReference> getPathFilteredForRelevantTypes(final List<ObjectReference> referencePath) {
		List<ObjectReference> result = new LinkedList<ObjectReference>();
		for (ObjectReference ref : referencePath) {
			if (objectTypesToAggregate.contains(ref.getType())) {
				result.add(ref);
			}
		}
		return result;
	}
	
	private ObjectTreeNode<ObjectReference> createObjectTreeNode(final ObjectDescription object) {
		ObjectTreeNode<ObjectReference> treeNode = new ObjectTreeNode<ObjectReference>();
		treeNode.setItem(new ObjectReference(object.getType(), object.getName()));
		for (CustomObjectDetailColumn detailColumn : tabConfig.getCustomObjectDetailColumns()) {
			String propertyKey = detailColumn.getPropertyKey();
			treeNode.addDetail(propertyKey, object.getDetails().get(propertyKey));
		}
		return treeNode;
	}
	
	public void saveAggregatedTreeStructure() {
		LOGGER.info("    Writing Object-Tree for Custom Object Tab '" + tabConfig.getId() + "' ...");
		CustomObjectTabTree tree = build();
		dao.saveCustomObjectTabTree(buildIdentifier, tabConfig.getId(), tree);
		LOGGER.info("    Finished successfully writing Object-Tree for Custom Object Tab '" + tabConfig.getId() + "'.");
	}
	
	/**
	 * Build and get the built tree.
	 */
	private CustomObjectTabTree build() {
		List<ObjectTreeNode<ObjectReference>> childrenOfRootDummyNode = treeToBuild.build().getChildren();
		return new CustomObjectTabTree(childrenOfRootDummyNode);
	}
}
