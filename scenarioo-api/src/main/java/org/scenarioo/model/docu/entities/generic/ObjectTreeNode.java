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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ObjectTreeNode<T> implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private T item;
	
	private final Details details = new Details();
	
	private final List<ObjectTreeNode<?>> children = new ArrayList<ObjectTreeNode<?>>();
	
	public ObjectTreeNode() {
	}
	
	public ObjectTreeNode(final T item) {
		this.item = item;
	}
	
	public T getItem() {
		return item;
	}
	
	public void setItem(T item) {
		this.item = item;
	}
	
	public Details getDetails() {
		return details;
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
		return Collections.unmodifiableList((List) children);
	}
	
	/**
	 * Add a child node. child nodes can be of different item type than the current node.
	 */
	public void addChild(final ObjectTreeNode<?> child) {
		children.add(child);
	}
	
	/**
	 * Add a detail directly to the current node (this is different than the details on the contained item, because the
	 * same item could be referenced in different nodes having different details).
	 */
	public void addDetail(final String key, final Object value) {
		details.addDetail(key, value);
	}
	
	public void addChildren(final List<ObjectTreeNode<Object>> children) {
		if (children != null && !children.isEmpty()) {
			this.children.addAll(children);
		}
	}
	
}
