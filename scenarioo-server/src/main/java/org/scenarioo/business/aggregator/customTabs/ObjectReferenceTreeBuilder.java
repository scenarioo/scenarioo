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

package org.scenarioo.business.aggregator.customTabs;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.scenarioo.model.docu.entities.generic.ObjectDescription;
import org.scenarioo.model.docu.entities.generic.ObjectReference;
import org.scenarioo.model.docu.entities.generic.ObjectTreeNode;

/**
 * Builder for building trees of tree nodes containing {@link ObjectReference}
 * objects.
 */
class ObjectReferenceTreeBuilder {

	private final ObjectTreeNode<ObjectReference> node;

	private final Map<ObjectReference, ObjectReferenceTreeBuilder> children = new LinkedHashMap<ObjectReference, ObjectReferenceTreeBuilder>();

	ObjectReferenceTreeBuilder(final ObjectTreeNode<ObjectReference> node) {
		this.node = node;
	}

	/**
	 * Find existing node form its path up to the last element from path.
	 * 
	 * @throws IllegalArgumentException
	 *             in case the path is not yet existing in current built tree.
	 */
	public ObjectReferenceTreeBuilder findNode(final List<ObjectReference> path) {
		ObjectReferenceTreeBuilder current = this;
		for (ObjectReference ref : path) {
			current = current.getChild(ref);
			if (current == null) {
				throw new IllegalArgumentException(
						"could not find element in tree builder " + ref);
			}
		}
		return current;
	}

	public ObjectReferenceTreeBuilder getChild(final ObjectReference ref) {
		return children.get(ref);
	}

	public boolean containsChild(final ObjectDescription object) {
		return children.containsKey(new ObjectReference(object.getType(),
				object.getName()));
	}

	public void addChild(final ObjectTreeNode<ObjectReference> objectTreeNode) {
		ObjectReferenceTreeBuilder builder = new ObjectReferenceTreeBuilder(objectTreeNode);
		children.put(objectTreeNode.getItem(), builder);
	}

	/**
	 * Only allowed to be called once per builder (otherwise childs are added
	 * more than once)
	 * 
	 * @return the tree that has been built.
	 */
	public ObjectTreeNode<ObjectReference> build() {
		for (ObjectReferenceTreeBuilder childBuilder : children.values()) {
			node.addChild(childBuilder.build());
		}
		return node;
	}

}
