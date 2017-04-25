package org.scenarioo.model.docu.feature.model.generic;


import java.util.ArrayList;
import java.util.List;

public class ObjectTreeNode<ITEM_TYPE, CHILDREN_TYPE> implements Detailable {

	public Details details;
	public ITEM_TYPE item;

	public List<CHILDREN_TYPE> children = new ArrayList<>();

}
