package com.example.demo.tools;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * xml转换为Table结构.
 * 
 * 
 * @author lj
 */
public class Xml2TableUtils {

	/**
	 * 根据xmlDocement转换为数据库表名-值和表字段名-值.
	 * 
	 * @param doc
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List<Map<String, Object>> getAllTable(Document doc) {
		if (doc == null) {
			return null;
		}
		Element rootElement = doc.getRootElement();
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		for (Iterator iterator = rootElement.elementIterator(); iterator.hasNext();) {
			// 获取第一层子元素
			Element element = (Element) iterator.next();
			// 判断是否是表节点.1表示表节点，它的子节点为表字段节点；0表示不是表节点
			String isTable = element.attributeValue("isTable");
			if (isTable != null && "1".equals(isTable)) {
				List fields = element.elements();
				if (fields != null && fields.size() > 0) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("tableName", element.getName());
					map.put("fields", Xml2MapUtils.elements2Map(element));
					result.add(map);
				}
			} else {
				List<Element> elements = element.elements();
				for (Element tableElement : elements) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("tableName", tableElement.getName());
					map.put("fields", Xml2MapUtils.elements2Map(tableElement));
					result.add(map);
				}

			}
		}
		return result;
	}

	/**
	 * 根据xmlStr转换为数据库表名-值和表字段名-值.
	 * 
	 * @param xmlStr
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static List getAllTable(String xmlStr) {
		Document doc = Xml2MapUtils.xmlStr2XmlDocument(xmlStr);
		return getAllTable(doc);
	}

	public static void main(String[] args) throws Exception {
		SAXReader saxReader = new SAXReader();

		InputStreamReader strInStream = new InputStreamReader(new FileInputStream("D:\\test2.xml"), "GBK");
		Document document = saxReader.read(strInStream);

		List<Map<String, Object>> lists = getAllTable(document);
		for (Map<String, Object> map : lists) {
			System.out.println(map);
		}
	}
}