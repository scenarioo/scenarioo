package ngusd.aggregator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.scenarioo.model.docu.entities.generic.ObjectReference;
import org.scenarioo.model.docu.entities.generic.ObjectTreeNode;


/**
 * Builder for collecting trees of object reference pathes and building according reference trees
 */
public class ObjectReferenceTreeBuilder {
	
	private ObjectReference node;
	
	private List<ObjectReferenceTreeBuilder> childrenInOrder = new ArrayList<ObjectReferenceTreeBuilder>();
	
	private Map<ObjectReference, ObjectReferenceTreeBuilder> children = new HashMap<ObjectReference, ObjectReferenceTreeBuilder>();
	
	ObjectReferenceTreeBuilder(final ObjectReference node) {
		this.node = node;
	}
	
	public void addPath(final List<ObjectReference> path) {
		if (!path.isEmpty()) {
			LinkedList<ObjectReference> copiedPath = new LinkedList<ObjectReference>(path);
			addPathInternal(copiedPath);
		}
	}
	
	private void addPathInternal(final List<ObjectReference> path) {
		if (!path.isEmpty()) {
			ObjectReference object = path.remove(0);
			ObjectReferenceTreeBuilder tree = children.get(object);
			if (tree == null) {
				tree = new ObjectReferenceTreeBuilder(object);
				childrenInOrder.add(tree);
				children.put(object, tree);
			}
			tree.addPathInternal(path);
		}
	}
	
	public ObjectTreeNode<ObjectReference> build() {
		ObjectTreeNode<ObjectReference> result = new ObjectTreeNode<ObjectReference>();
		result.setItem(node);
		for (ObjectReferenceTreeBuilder childTreeBuilder : childrenInOrder) {
			result.addChild(childTreeBuilder.build());
		}
		return result;
	}
}
