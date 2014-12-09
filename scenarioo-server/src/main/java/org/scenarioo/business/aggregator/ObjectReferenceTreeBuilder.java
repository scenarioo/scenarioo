/* scenarioo-server
 * Copyright (C) 2014, scenarioo.org Development Team
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.scenarioo.business.aggregator;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.scenarioo.model.docu.entities.generic.ObjectReference;
import org.scenarioo.model.docu.entities.generic.ObjectTreeNode;

/**
 * Builder for collecting trees of object reference pathes and building according reference trees
 */
public class ObjectReferenceTreeBuilder {

	private final ObjectReference node;

	private final Map<ObjectReference, ObjectReferenceTreeBuilder> children = new LinkedHashMap<ObjectReference, ObjectReferenceTreeBuilder>();

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
				children.put(object, tree);
			}
			tree.addPathInternal(path);
		}
	}

	public ObjectTreeNode<ObjectReference> build() {
		ObjectTreeNode<ObjectReference> result = new ObjectTreeNode<ObjectReference>();
		result.setItem(node);
		if (node instanceof ObjectReferenceWithLabels) {
			result.getDetails().putAll(((ObjectReferenceWithLabels) node).getDetails());
		}
		for (ObjectReferenceTreeBuilder childTreeBuilder : children.values()) {
			result.addChild(childTreeBuilder.build());
		}
		return result;
	}

}
