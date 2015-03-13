package org.generationcp.analytics.xml;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class ExpDesignParameter implements Serializable {
	private String name;
	private String value;		
	private List<ListItem> listItem; //would only be created in xml if not null
	
	public ExpDesignParameter(){
		super();
	}
	
	public ExpDesignParameter(String name, String value) {
		super();
		this.name = name;
		this.value = value;
	}
	@XmlAttribute
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@XmlAttribute
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}

	@XmlElement(name="ListItem")
	public List<ListItem> getListItem() {
		return listItem;
	}

	public void setListItem(List<ListItem> listItem) {
		this.listItem = listItem;
	}
	
	
}
