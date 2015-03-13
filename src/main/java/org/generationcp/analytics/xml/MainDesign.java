package org.generationcp.analytics.xml;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="Templates")
public class MainDesign implements Serializable{
	
	private ExpDesign design;

	public MainDesign(){
		
	}
	
	public MainDesign(ExpDesign design) {
		super();
		this.design = design;
	}

	@XmlElement(name="Template")
	public ExpDesign getDesign() {
		return design;
	}

	public void setDesign(ExpDesign design) {
		this.design = design;
	}
	
	
}
