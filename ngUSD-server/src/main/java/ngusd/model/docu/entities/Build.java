package ngusd.model.docu.entities;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class Build {
	
	private String name;
	private String revision;
	private Date date;
	private String state;
	private Details details = new Details();
	
	public Build() {
	}
	
	public Build(final String name) {
		this();
	}
}
