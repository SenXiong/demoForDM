package com.example.demo.tools;

import java.io.Serializable;

public class XmlCompareBean implements Serializable {

	private static final long serialVersionUID = -233281284955984443L;

	private String xpath;
	private String preValue;
	private String nowValue;

	public String getXpath() {
		return xpath;
	}

	public void setXpath(String xpath) {
		this.xpath = xpath;
	}

	public String getPreValue() {
		return preValue;
	}

	public void setPreValue(String preValue) {
		this.preValue = preValue;
	}

	public String getNowValue() {
		return nowValue;
	}

	public void setNowValue(String nowValue) {
		this.nowValue = nowValue;
	}
}
