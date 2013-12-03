package org.scenarioo.model.docu.entities.generic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class ObjectTreeNode<T> implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private T item;
	
	private Details details = new Details();
	
	private List<ObjectTreeNode<?>> children = new ArrayList<ObjectTreeNode<?>>();
	
	public ObjectTreeNode() {
	}
	
	public ObjectTreeNode(final T item) {
		this.item = item;
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
	
}
