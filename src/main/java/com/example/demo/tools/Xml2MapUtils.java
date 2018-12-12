package com.example.demo.tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * XML工具类. 转换规则： 1.主要是Map与List的互相嵌套 2.同名称的节点会被装进List
 * 
 * @author lj
 */
public class Xml2MapUtils {

	protected static final Logger LOGGER = LoggerFactory.getLogger(Xml2MapUtils.class);

	/**
	 * 将Document对象递归解析为Map集合
	 * 
	 * @param doc
	 *            Document
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> parseXML2Map(Document doc) {
		Element root = doc.getRootElement();// 获取根节点
		String rootPath = "/" + root.getName();// 获取根节点xpath

		Map<String, Object> map = new HashMap<String, Object>();// 存储根节点下面XML的map

		List<Object> list = new ArrayList<Object>();// 存储根节点下面的重名子节点
		Iterator<Element> elems = root.elementIterator();// 根节点下面子节点列表
		while (elems.hasNext()) {
			Element elem = elems.next();// 取出根节点下面一个子节点
			String path = rootPath + "/" + elem.getName();// 获取子节点xpath
			parseSubNode(map, list, doc, elem, path);// 解析子节点
		}

		return map;
	}

	/**
	 * 递归解析子节点XML为map对象
	 * 
	 * @param map
	 *            存储XML节点
	 * @param list
	 * @param doc
	 * @param elem
	 * @param path
	 */
	@SuppressWarnings("unchecked")
	public static void parseSubNode(Map<String, Object> map, List<Object> list, Document doc, Element elem, String path) {
		Map<String, Object> mapNode = new HashMap<String, Object>();// 存储当前节点下面XML的map

		List<Object> listSub = new ArrayList<Object>();// 存储当前节点下面的重名子节点
		Iterator<Element> subNodes = elem.elementIterator();// 当前节点下面子节点列表

		int index = 0;// 判断子节点数目
		while (subNodes.hasNext()) {
			Map<String, Object> mapSub = new HashMap<String, Object>();
			Element subNode = subNodes.next();// 取出当前节点下面一个子节点
			String subPath = path + "/" + subNode.getName();// 获取当前节点下面子节点xpath
			parseSubNode(mapSub, listSub, doc, subNode, subPath);// 递归解析当前节点子节点

			for (String key : mapSub.keySet()) {
				mapNode.put(key, mapSub.get(key));// 将子节点map对象以键值存储到当前map
			}

			index++;
		}
		String nodeName = elem.getName();// 当前节点名称
		List<Node> nodeList = elem.getParent().selectNodes(nodeName);// 判断当前节点是否有同名兄弟节点
		if (nodeList.size() > 1) {
			list.add(mapNode);// 添加重名节点到列表里
		}

		if (index == 0) {// 为0表示没有子节点，为叶子节点
			map.put(nodeName, elem.getText());// 将叶子节点设置到Map对象里
		} else {
			map.put(nodeName, mapNode);// 将当前节点下设置到Map对象里
			if (list.size() > 0) {
				map.put(nodeName, list);// 将当前节点下重名子节点设置到Map对象里
			}
		}
	}

	/**
	 * xml字符串转换为Map.
	 * 
	 * @param xmlStr
	 * @return
	 */
	public static Map<String, Object> xmlStr2Map(String xmlStr) {
		Document document = xmlStr2XmlDocument(xmlStr);
		return dom2Map(document);

	}

	/**
	 * 
	 * XML document转换为Map.
	 * 
	 * @param doc
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static Map<String, Object> dom2Map(Document doc) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (doc == null) {
			return map;
		}
		// 获取根元素
		Element root = doc.getRootElement();
		for (Iterator iterator = root.elementIterator(); iterator.hasNext();) {
			// 获取第一层子元素
			Element element = (Element) iterator.next();
			// 获取第二层子元素
			List list = element.elements();
			if (list.size() > 0) {
				map.put(element.getName(), elements2Map(element));
			} else {
				map.put(element.getName(), element.getTextTrim());
			}
		}
		return map;
	}

	/**
	 * XML element转换为Map.
	 * 
	 * @param element
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Map<String, Object> elements2Map(Element element) {
		Map<String, Object> map = new HashMap<String, Object>();
		List list = element.elements();
		int size = list.size();
		if (size > 0) {
			for (int i = 0; i < size; i++) {
				Element e = (Element) list.get(i);
				List<Object> mapList = new ArrayList<Object>();
				if (e.elements().size() > 0) {
					// 迭代获取子元素
					Map<String, Object> m = elements2Map(e);
					if (map.get(e.getName()) != null) {
						Object obj = map.get(e.getName());
						if (obj instanceof ArrayList) {
							mapList = (List<Object>) obj;
							mapList.add(m);
						} else {
							mapList = new ArrayList<Object>();
							mapList.add(obj);
							mapList.add(m);
						}
						map.put(e.getName(), mapList);
					} else {
						map.put(e.getName(), m);
					}
				} else {
					if (map.get(e.getName()) != null) {
						Object obj = map.get(e.getName());
						if (obj instanceof ArrayList) {
							mapList = (List<Object>) obj;
							mapList.add(e.getTextTrim());
						} else {
							mapList = new ArrayList<Object>();
							mapList.add(obj);
							mapList.add(e.getTextTrim());
						}
						map.put(e.getName(), mapList);
					} else {
						map.put(e.getName(), e.getTextTrim());
					}
				}
			}
		} else {
			map.put(element.getName(), element.getTextTrim());
		}
		return map;
	}

	/**
	 * xml字符串转为 XmlDocument.
	 * 
	 * @param xmlStr
	 * @return
	 */
	public static Document xmlStr2XmlDocument(String xmlStr) {
		Document document = null;
		try {
			document = DocumentHelper.parseText(xmlStr);
		} catch (DocumentException e) {
			LOGGER.error("xml字符串转为 XmlDocument", e);
		}
		return document;
	}

	/**
	 * xmlDocument 转为 xml字符串.
	 * 
	 * @param document
	 * @return
	 */
	public static String xmlDocument2XmlStr(Document document) {
		return document.asXML();
	}
}
