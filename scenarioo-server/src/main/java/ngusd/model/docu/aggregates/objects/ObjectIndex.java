package ngusd.model.docu.aggregates.objects;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;
import ngusd.model.docu.entities.generic.ObjectDescription;
import ngusd.model.docu.entities.generic.ObjectReference;
import ngusd.model.docu.entities.generic.ObjectTreeNode;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Data()
public class ObjectIndex {
	
	private ObjectDescription object;
	
	private ObjectTreeNode<ObjectReference> referenceTree;
	
}
