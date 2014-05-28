package org.scenarioo.model.docu.aggregates.objects;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.scenarioo.model.docu.entities.generic.ObjectReference;
import org.scenarioo.model.docu.entities.generic.ObjectTreeNode;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class CustomTabObjectTree {

	private List<ObjectTreeNode<ObjectReference>> tree;
	
	public CustomTabObjectTree() {	
	}
	
	public CustomTabObjectTree(List<ObjectTreeNode<ObjectReference>> tree) {
		super();
		this.tree = tree;
	}

	public List<ObjectTreeNode<ObjectReference>> getTree() {
		return tree;
	}

	public void setTree(List<ObjectTreeNode<ObjectReference>> tree) {
		this.tree = tree;
	}
			
}
