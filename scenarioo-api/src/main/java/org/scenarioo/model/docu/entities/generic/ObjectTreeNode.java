package org.scenarioo.model.docu.entities.generic;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class ObjectTreeNode<T> {
	
	private T item;
	
	private Details details = new Details();
	
	private List<ObjectTreeNode<?>> children = new ArrayList<ObjectTreeNode<?>>();
	
	public ObjectTreeNode() {
	}
	
	public ObjectTreeNode(final T item) {
		this.item = item;
	}
	
	public void addChild(final ObjectTreeNode<?> child) {
		children.add(child);
	}
	
	public void addDetail(final String key, final Object value) {
		details.addDetail(key, value);
	}
	
}
