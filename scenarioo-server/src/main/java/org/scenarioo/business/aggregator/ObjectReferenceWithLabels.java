package org.scenarioo.business.aggregator;

import org.scenarioo.model.docu.entities.Labels;
import org.scenarioo.model.docu.entities.generic.Details;
import org.scenarioo.model.docu.entities.generic.ObjectList;
import org.scenarioo.model.docu.entities.generic.ObjectReference;

public class ObjectReferenceWithLabels extends ObjectReference {
	
	// TODO [fallback, labels] question to the author by Rolf:
	// Why not directly wire the labels dependency here??
	// Or, if you want to allow for other details, the name
	// "ObjectReferenceWithLabels" is wrong and missleading.
	// I would even propose to add a details field to the class ObjectReference directly and in general, I think this
	// would not hurt at all, because somebody might want to link to an object but still put some additonal informations
	// (kind of annotations) on that link (like you are trying to do here).
	// It would be the same mechanism as in ObjectTreeNode, where there is already a details object for additional
	// informations about the object in the item, that does not belong to the object itself (only this one
	// occurence/node item).
	private Details details;
	
	public ObjectReferenceWithLabels() {
		super();
	}
	
	public ObjectReferenceWithLabels(final String type, final String name) {
		super(type, name);
	}
	
	public ObjectReferenceWithLabels(final String type, final String name, final Labels labels) {
		super(type, name);
		addLabelsToDetails(labels);
	}
	
	private void addLabelsToDetails(final Labels labels) {
		// TODO [fallback, labels] It would make more sense to use the labels object directly. But this does not work
		// yet. Maybe Labels has to be serializable or added explicitly to the JAXB context.
		// Message in a bottle from Rolf to the one that wrote this: yes, it needs to be serializable, probably it also
		// needs the annotations on the clas, like ObjectDescription, and last but not least, you might have to add it
		// to the constant ScenarioDocuXMLUtil.SUPPORTED_GENERIC_CLASSES
		ObjectList<String> labelList = new ObjectList<String>();
		
		for (String label : labels.getLabels()) {
			labelList.add(label);
		}
		
		if (details == null) {
			details = new Details();
		}
		details.addDetail("labels", labelList);
	}
	
	public Details getDetails() {
		return details;
	}
	
	public void setDetails(final Details details) {
		this.details = details;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((details == null) ? 0 : details.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		ObjectReferenceWithLabels other = (ObjectReferenceWithLabels) obj;
		if (details == null) {
			if (other.details != null) {
				return false;
			}
		} else if (!details.equals(other.details)) {
			return false;
		}
		return true;
	}
	
}
