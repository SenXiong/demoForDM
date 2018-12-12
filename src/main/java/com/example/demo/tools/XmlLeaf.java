package com.example.demo.tools;

import java.io.Serializable;

public class XmlLeaf implements Serializable {
	private static final long serialVersionUID = -1737889994837363339L;
	private String xpath;
	private String value;

	public XmlLeaf(String xpath, String value) {
		this.xpath = xpath;
		this.value = value;
	}

	public String getXpath() {
		return xpath;
	}

	public void setXpath(String xpath) {
		this.xpath = xpath;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
