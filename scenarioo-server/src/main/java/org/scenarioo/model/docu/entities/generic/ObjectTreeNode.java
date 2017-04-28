/* scenarioo-api
 * Copyright (C) 2014, scenarioo.org Development Team
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * As a special exception, the copyright holders of this library give you
 * permission to link this library with independent modules, according
 * to the GNU General Public License with "Classpath" exception as provided
 * in the LICENSE file that accompanied this code.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.scenarioo.model.docu.entities.generic;

import org.scenarioo.api.rules.Preconditions;
import org.scenarioo.model.docu.entities.Detailable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Representation of application specific tree structures of additional informations in the documentation. A tree node
 * may contain other generic objects as item, additional key value properties in the details of the node, and children
 * (nodes again).
 *
 * @param <T>
 *            the type of the node's content item (must be a type supported by Scenarioo, could be one of String,
 *            Integer, ObjectReference or ObjectDescription).
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ObjectTreeNode<T> implements Serializable, Detailable {

	private T item;

	private Details details = new Details();

	private final List<ObjectTreeNode<?>> children = new ArrayList<ObjectTreeNode<?>>();

	public ObjectTreeNode() {
	}

	public ObjectTreeNode(final T item) {
		this.item = item;
	}

	public T getItem() {
		return item;
	}

	public void setItem(final T item) {
		this.item = item;
	}

	@Override
	public Details getDetails() {
		return details;
	}

	/**
	 * Add application specific key value details directly to the current node (this is different than the details on
	 * the contained item, because the same item could be referenced in different nodes having different details).
	 */
	@Override
	public Details addDetail(final String key, final Object value) {
		return details.addDetail(key, value);
	}

	@Override
	public void setDetails(final Details details) {
		Preconditions.checkNotNull(details, "Details not allowed to set to null");
		this.details = details;
	}

	/**
	 * Get the children as unmodifiable list with the expected item type.
	 *
	 * @param <C>
	 *            the expected node item type
	 * @return the children as list of expected type
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <C> List<ObjectTreeNode<C>> getChildren() {
		return (List) children;
	}

	/**
	 * Add a child node. child nodes can be of different item type than the current node.
	 */
	public void addChild(final ObjectTreeNode<?> child) {
		children.add(child);
	}

	public void addChildren(final List<ObjectTreeNode<Object>> children) {
		if (children != null && !children.isEmpty()) {
			this.children.addAll(children);
		}
	}

}
