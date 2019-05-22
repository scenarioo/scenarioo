package org.scenarioo.uitest.example.issues;

import java.util.List;
import java.util.Stack;

import org.scenarioo.model.docu.entities.generic.Details;
import org.scenarioo.model.docu.entities.generic.ObjectDescription;
import org.scenarioo.model.docu.entities.generic.ObjectList;
import org.scenarioo.model.docu.entities.generic.ObjectTreeNode;
import org.scenarioo.uitest.dummy.application.issues.DummyApplicationsIssueTrackingSystem;
import org.scenarioo.uitest.dummy.application.issues.WorkItem;

/**
 * A helper that uses the {@link DummyApplicationsIssueTrackingSystem} to get information about work items (in our
 * examples those are user stories) that are attached to the test scenarios (by annotation) to fill this requirements
 * information into the the {@link Details} of the scenarios in the documentation.
 */
public class IssuesTrackingAccessHelper {

	/**
	 * This is an example how you could fill in WorkItems from your Issues Tracking tool into Scenarioo generic
	 * application specific data (like {@link ObjectList} and {@link ObjectTreeNode} containing
	 * {@link ObjectDescription}s).
	 *
	 * @param workItemIds
	 *            the IDs of the work items (user stories in our example) that are attached to the test scenarios.
	 * @return the data structure to fill into the documentation as a list of feature trees (features with epics as
	 *         childs, with stories as leaf nodes, but only containing those epics and features that are parents of the
	 *         passed user story ids)
	 */
	public static ObjectList<ObjectTreeNode<ObjectDescription>> loadFeatureTreesForWorkItemIds(
			final long... workItemIds) {
		ObjectList<ObjectTreeNode<ObjectDescription>> featureTrees = new ObjectList<ObjectTreeNode<ObjectDescription>>();
		for (long id : workItemIds) {
			Stack<WorkItem> items = loadWorkItemWithParents(id);
			addItemsToTrees(featureTrees, items);
		}
		return featureTrees;
	}

	/**
	 * Load a item with an id and all its parents as a stack of work items. Top level of the stack is the top level
	 * parent, last item on the stack is the item (user story) with passed Id.
	 */
	public static Stack<WorkItem> loadWorkItemWithParents(final long id) {
		Stack<WorkItem> items = new Stack<WorkItem>();
		WorkItem item = DummyApplicationsIssueTrackingSystem.loadWorkItemById(id);
		items.push(item);
		while (item.getParentId() != null) {
			item = DummyApplicationsIssueTrackingSystem.loadWorkItemById(item.getParentId());
			items.push(item);
		}
		return items;
	}

	/**
	 * Adds the passed work items as a new branch of {@link ObjectDescription}s for this work items into the the target
	 * tree nodes.
	 */
	public static void addItemsToTrees(final List<ObjectTreeNode<ObjectDescription>> targetTreeNodes,
			final Stack<WorkItem> itemsToAdd) {

		// find or add top level item from stack
		WorkItem topItem = itemsToAdd.pop();
		ObjectTreeNode<ObjectDescription> node = findOrAddTreeNodeForWorkItem(targetTreeNodes, topItem);

		// recursively add following items to children
		if (!itemsToAdd.isEmpty()) {
			List<ObjectTreeNode<ObjectDescription>> children = node.getChildren();
			addItemsToTrees(children, itemsToAdd);
		}
	}

	private static ObjectTreeNode<ObjectDescription> findOrAddTreeNodeForWorkItem(
			final List<ObjectTreeNode<ObjectDescription>> targetTreeNodes,
			final WorkItem item) {

		// Search existing item (no need to add again, if already contained)
		String itemType = item.getType();
		String uniqueItemName = createUniqueItemName(item);
		for (ObjectTreeNode<ObjectDescription> node : targetTreeNodes) {
			if (node.getItem().getType().equals(itemType) && node.getItem().getName().equals(uniqueItemName)) {
				// it was found:
				return node;
			}
		}

		// Node for work item was not found --> create and add it:
		ObjectTreeNode<ObjectDescription> node = new ObjectTreeNode<ObjectDescription>();
		node.setItem(createObjectDescriptionForWorkItem(item));
		targetTreeNodes.add(node);
		return node;

	}

	private static String createUniqueItemName(final WorkItem item) {
		// use Id combined with title to get a unique but still human readable name for each work item
		return item.getId() + " - " + item.getTitle();
	}

	private static ObjectDescription createObjectDescriptionForWorkItem(final WorkItem item) {
		ObjectDescription objectDesc = new ObjectDescription();
		objectDesc.setType(item.getType());
		objectDesc.setName(createUniqueItemName(item));
		objectDesc.addDetail("description", item.getDescription());
		return objectDesc;
	}

}
