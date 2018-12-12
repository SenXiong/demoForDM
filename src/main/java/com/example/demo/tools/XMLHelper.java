package com.example.demo.tools;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang.StringUtils;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.DOMException;

/**
 * xml转换、解析工具类.
 * 
 * 
 * 
 */
public class XMLHelper {

	protected static final Logger LOGGER = LoggerFactory.getLogger(XMLHelper.class);
	/**
	 * 排除的字符串起止标示
	 */
	private static final String[][] EXCLUDE_STR = { { "<?", "?>" }, { "<![CDATA[", "]]>" }, { "<!--", "-->" } };
	/**
	 * 
	 * @param xmlData
	 * @return
	 */
	public static Document parseDocument(String xmlData) {
		Document doc = null;
		if ("".equalsIgnoreCase(xmlData) || "null".equalsIgnoreCase(xmlData)) {
			return null;
		}

		try {
			/* System.out.println(xmlData); */
			doc = DocumentHelper.parseText(xmlData);
			// System.out.println(doc.asXML());
		} catch (DocumentException e) {
			LOGGER.error("文本转化为document错误", e);
		}
		return doc;
	}

	/**
	 * 根据返回Map刷新Xml文档的xpath节点
	 *
	 */
	@SuppressWarnings("rawtypes")
	public static void fillXmlDataFromMap(Document docsrc, String xPath, Map resultMap) {
		if (resultMap == null) {
			return;
		}

		// createXPath(docsrc, xPath);
		// Node nd = docsrc.selectSingleNode(xPath);
		// Element e = (Element) nd;

		Iterator it = resultMap.keySet().iterator();
		while (it.hasNext()) {
			Object obj = it.next();
			String str = "";
			if (resultMap.get(obj) != null) {
				str = String.valueOf(resultMap.get(obj));
			}
			String truePath = xPath + "/" + obj.toString().toLowerCase();
			createXPath(docsrc, truePath);
			Element e = createXPath(docsrc, truePath);
			e.setText(str);
			// e.addElement(obj.toString().toLowerCase());

			// e.addAttribute(obj.toString().toLowerCase(), str);
		}

	}
	
	public static Element getElement(Document doc, String xPath, Map<String, String> xmlMap) {
		org.dom4j.XPath xpath = doc.createXPath(xPath);
		xpath.setNamespaceURIs(xmlMap);
		return (Element) xpath.selectSingleNode(doc);
	}

	public static String getNodeValueByXpath(Document doc, String xpath) {
		String result = null;
		try {
			Element element = (Element) doc.selectSingleNode(xpath);
			result = element.getText();
			return result;
		} catch (Exception e) {
			return result;
		}
	}

	/**
	 * 刷新doc中对应节点
	 * 
	 * @param docsrc
	 * @param parentPath
	 * @param nodeXML
	 */
	public static void refreshXmlDataFromNode(Document docsrc, String parentPath, String nodeXML) {
		refreshXmlDataFromNode(docsrc, parentPath, nodeXML, false);
	}

	/**
	 * 刷新Xml对应节点
	 * 
	 * @param docsrc
	 * @param parentPath
	 * @param nodeXML
	 * @param directReplace
	 */
	public static void refreshXmlDataFromNode(Document docsrc, String parentPath, String nodeXML, boolean directReplace) {
		if ("".equalsIgnoreCase(nodeXML) || "null".equalsIgnoreCase(nodeXML)) {
			return;
		}
		// 加载节点XML
		Document doc = null;
		try {
			doc = DocumentHelper.parseText(nodeXML);
			// System.out.println(doc.asXML());
		} catch (DocumentException e) {
			LOGGER.error("", e);
		}
		Node nd = null;

		nd = doc.getRootElement();
		refreshXmlDataFromNode(docsrc, parentPath, nd, true);
	}

	/**
	 * TODO:请添加注释(修改好了把TODO删除)lqh add.
	 * 
	 * @param docsrc
	 * @param parentPath
	 * @param node
	 */
	public static void refreshXmlDataFromNode(Document docsrc, String parentPath, Node node) {
		refreshXmlDataFromNode(docsrc, parentPath, node, false);
	}

	/**
	 * 替换对应节点方法
	 * 
	 * @param docsrc
	 *            需刷新XML
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void refreshXmlDataFromNode(Document docsrc, String xPath, Node node, boolean directReplace) {
		refreshXmlDataFromNode(docsrc, xPath, node, false,true);

	}
	public static void refreshXmlDataFromNode(Document docsrc, String xPath, Node node, boolean directReplace, boolean clearDestIfSecNull) {
		Node ndsrc = null;
		if (node == null) {
			ndsrc = docsrc.selectSingleNode(xPath);
			if(clearDestIfSecNull){
				if (ndsrc != null) {
					ndsrc.detach();
				}
			}
			createXPath2(docsrc, xPath);
			return;
		}
		// 加载节点XML
		createXPath(docsrc, xPath);
		ndsrc = docsrc.selectSingleNode(xPath);
		String nodeName = xPath.substring(xPath.lastIndexOf("/") + 1);
		if (nodeName.indexOf("[") > 0) {
			nodeName = nodeName.substring(0, nodeName.indexOf("["));
		} else if (nodeName.indexOf("@") >= 0) {

		}
		node.setName(nodeName);
		if (directReplace) {
			// 直接替代XML
			List ele = ndsrc.getParent().content();
			ele.set(ele.indexOf(ndsrc), node);
		} else {
			refreshNode(docsrc, ndsrc, node);
		}
	}

	/**
	 * 刷新当前Xml节点
	 *
	 */
	public static void refreshNode(Document doc, Node ndsrc, Node node) {
		Element e = (Element) node;
		// add by lwy 2008.04.29采用刷新方式，而不是替代方式
		Element esrc = (Element) ndsrc;
		/**
		 * 更新属性
		 */

		for (int index = 0; index < e.attributeCount(); index++) {
			Attribute attr = e.attribute(index);
			esrc.addAttribute(attr.getName(), attr.getValue());
		}

		/**
		 * 更新节点值
		 */

		if (e.elements().size() > 0) {// 如果有子节点
			Element enew = null;
			List<Element> esrcList = esrc.elements();
			for (Element esrcc : esrcList) {
				esrc.remove(esrcc);
			}
			if (e.getName().equalsIgnoreCase(esrc.getName())) {
				enew = esrc;
			} else {
				enew = esrc.addElement(e.getName());
			}
			for (int i = 0; i < e.elements().size(); i++) {
				Element nodec = (Element) e.elements().get(i);// 取出需刷新进去的子节点
				Element enewc = enew.addElement(nodec.getName());// 生成源节点的子节点路径
				refreshNode(doc, enewc, nodec);// 递归调用进行替换
			}
		} else {
			if ((e.elements() == null || esrc.elements().size() == 0) && esrc.getName().equalsIgnoreCase(e.getName())) {// 如果添加的节点没有子节点,并且在原节点里存在，则首先清除节点
				//if (!e.getText().isEmpty() && !e.getText().equalsIgnoreCase("")) {
					esrc.setText(e.getText());
				//}
			}
			// Element enew = esrc.addElement(e.getName());// 建立节点
		}

	}

	public static void refreshDoc(Document doc, String xPath, Node node) {
		Node ndsrc = doc.selectSingleNode(xPath);
		ndsrc = node;
	}

	/**
	 * 根据xPath，创建节点.
	 * 
	 * @param docsrc
	 * @param xPath，形如：//data/persons/person[@submitpsn='1']/prp[@seq_no= 'ss']/test
	 */
	public static Element createXPath(Document docsrc, String xPath) {
 
		String[] paths = StringUtils.split(xPath, "/");
		Element parentE = null;
		for (String path2 : paths) {
			Node nd = null;
			if(parentE == null){
				nd = docsrc.selectSingleNode("/" + path2);
			}else{
				nd = parentE.selectSingleNode(path2);
			}
			if (nd == null) {
				if (path2.indexOf("[@") != -1) {
					String nodeName = path2.substring(0, path2.indexOf("["));
					String attrName = path2.substring(path2.indexOf("@") + 1,path2.indexOf("="));
					String attrValue = path2.substring(path2.indexOf("=") + 2,path2.indexOf("]") - 1);
					if(parentE == null){
						parentE = docsrc.addElement(nodeName);
					}else{
						parentE = parentE.addElement(nodeName);
					}
					parentE.addAttribute(attrName, attrValue);
					if (parentE != null) {
						path2 = path2.substring(path2.indexOf("]") + 1);
						if (path2 != null && path2.length() != 0) {
							while (path2 != null && path2.length() != 0) {
								// String nodeName2 =path2.substring(path2.indexOf("["));
								String attrName2 = path2.substring(path2.indexOf("@") + 1,path2.indexOf("="));
								String attrValue2 = path2.substring(path2.indexOf("=") + 2,	path2.indexOf("]") - 1);
								parentE.addAttribute(attrName2, attrValue2);
								path2 = path2.substring(path2.indexOf("]") + 1);
							}
						}
					}
				} else if (path2.indexOf("@") != -1) {// 忽略属性
					continue;
				} else {
					if(parentE == null){
						parentE = docsrc.addElement(path2);
					}else{
						parentE = parentE.addElement(path2);
					}
				}
			} else {
				parentE = (Element) nd;
			}
		}
		return parentE;
	}
	/**

	    * 根据xPath，创建节点.

	    *

	     * @param docsrc

	    * @param xPath，形如：//data/persons/person[@submitpsn='1']/prp[@seq_no= 'ss']/test

	    */

	 public static Element createXPath2(Document docsrc, String xPath) {

	 

	       String[] paths = StringUtils.split(xPath, "/");

	       Element parentE = null;

	       for (String path2 : paths) {

	           Node nd = null;

	           if(parentE == null){

	              nd = docsrc.selectSingleNode("/" + path2);

	           }else{

	              //主要的差别在这里，选择的是相对路径，而不是从根路径下来的绝对路径

	              nd = parentE.selectSingleNode(path2);

	           }

	           if (nd == null) {

	              if (path2.indexOf("[@") != -1) {

	                  String nodeName = path2.substring(0, path2.indexOf("["));

	                  String attrName = path2.substring(path2.indexOf("@") + 1,path2.indexOf("="));

	                  String attrValue = path2.substring(path2.indexOf("=") + 2,path2.indexOf("]") - 1);

	                  if(parentE == null){

	                     parentE = docsrc.addElement(nodeName);

	                  }else{

	                     parentE = parentE.addElement(nodeName);

	                  }

	                  parentE.addAttribute(attrName, attrValue);

	                  if (parentE != null) {

	                     path2 = path2.substring(path2.indexOf("]") + 1);

	                     if (path2 != null && path2.length() != 0) {

	                         while (path2 != null && path2.length() != 0) {

	                            // String nodeName2 =path2.substring(path2.indexOf("["));

	                            String attrName2 = path2.substring(path2.indexOf("@") + 1,path2.indexOf("="));

	                            String attrValue2 = path2.substring(path2.indexOf("=") + 2,    path2.indexOf("]") - 1);

	                            parentE.addAttribute(attrName2, attrValue2);

	                            path2 = path2.substring(path2.indexOf("]") + 1);

	                         }

	                     }

	                  }

	              } else if (path2.indexOf("@") != -1) {// 忽略属性

	                  continue;

	              } else {

	                  if(parentE == null){

	                     parentE = docsrc.addElement(path2);

	                  }else{

	                     parentE = parentE.addElement(path2);

	                  }

	              }

	           } else {

	              parentE = (Element) nd;

	           }

	       }

	       return parentE;

	    }

	 
	/**
	 * 从XML字符串中获取XML节点值.
	 * 
	 * @param xml
	 * @param nodePath
	 * @return
	 */
	public static String getNodeValueFromXML(String xml, String nodePath) {
		// 加载原XML
		Document doc = null;
		try {
			doc = DocumentHelper.parseText(xml);
		} catch (DocumentException e) {
			LOGGER.error("", e);
		}

		Node node = doc.selectSingleNode(nodePath);
		if (node == null) {
			return "";
		} else {
			return node.getText();
		}
	}

	/**
	 * 把一个表记录分解到xml节点中.
	 * 
	 * @param list
	 * @param parentNode
	 * @param subnode
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static String parseTableToXML(List list, String parentNode, String subnode) {
		if (list == null || list.size() == 0) {
			return "";
		}
		StringBuilder xml = new StringBuilder();

		if ("".equalsIgnoreCase(subnode)) {

			Map map = (Map) list.get(0);
			xml.append("<" + parentNode + " ");

			Iterator it = map.keySet().iterator();
			String key = null;
			String value = null;
			while (it.hasNext()) {
				key = it.next().toString().toLowerCase();
				value = IrisStringUtils.filterNull(map.get(key));
				xml.append(pingXML(key, value));
			}
			xml.append("/>");
		} else {
			xml.append("<" + parentNode + ">");

			for (int i = 0; i < list.size(); i++) {
				xml.append("<" + subnode + " ");
				Map map = (Map) list.get(i);

				Iterator it = map.keySet().iterator();
				String key = null;
				String value = null;
				while (it.hasNext()) {
					key = it.next().toString().toLowerCase();
					value = IrisStringUtils.filterNull(map.get(key));
					xml.append(pingXML(key, value));
				}
				xml.append("/>");
			}

			xml.append("</" + parentNode + ">");
		}
		return xml.toString();
	}

	/**
	 * 将xml拆分到数据库表中.
	 *
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List extractXMLToTable(String xml, String sql, String parentPath, String subPath, List fields) {
		List sqls = new ArrayList();

		Node nodes = getNodeFromXML(xml, parentPath);

		if (nodes != null) {
			for (Object nodeObj : nodes.selectNodes(subPath)) {
				List<Object> paramList = new ArrayList<Object>();

				Node node = (Node) nodeObj;
				if (node == null) {
					continue;
				}
				paramList.clear();

				for (int index = 0; index < fields.size(); index++) {
					Map map = (Map) fields.get(index);
					String type = String.valueOf(map.get("type"));
					String name = String.valueOf(map.get("name"));

					String value = node.valueOf(String.valueOf(name));

					if ("date".equalsIgnoreCase(type)) {
						SimpleDateFormat us = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
						Date date = null;
						try {
							if (value != null && !"".equals(value.trim())) {
								date = us.parse(value);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
						paramList.add(date);
					} else if ("long".equalsIgnoreCase(type)) {
						paramList.add(Long.parseLong(value));
					} else if ("decimal".equalsIgnoreCase(type)) {
						if ("".equalsIgnoreCase(value.trim())) {
							value = "0";
						} else {
							value = value.trim();
						}
						paramList.add(new BigDecimal(value));
					} else {
						paramList.add(value);
					}

				}

				// super.getJt().update(sql,paramList.toArray());
				HashMap sqlMap = new HashMap();
				sqlMap.put("sql", sql);
				sqlMap.put("params", paramList);
				sqls.add(sqlMap);
				// sqls.add(paramList);
			}
		}
		return sqls;

	}

	/**
	 * 从XML字符串中获取XML节点.
	 * 
	 * @param xml
	 * @param nodePath
	 * @return
	 */
	public static Node getNodeFromXML(String xml, String nodePath) {
		// 加载原XML
		Document doc = null;
		Node node = null;
		try {
			doc = DocumentHelper.parseText(xml);
			node = doc.selectSingleNode(nodePath);
		} catch (DocumentException e) {
			LOGGER.error("", e);
		}
		return node;
	}
	/**
	 * 从XML字符串中获取XML节点.
	 *
	 */
	public static Node getNodeFromXML(Document doc, String nodePath) {
		// 加载原XML
		Node node = null;
		node = doc.selectSingleNode(nodePath);
		return node;
	}

	/**
	 * 从XML字符串中获取XML节点值.
	 * 
	 * @param xml
	 * @param nodePath
	 * @return
	 */
	/** wk del 2014-6-25
	public static String getNodeValueFromXML(String xml, String nodePath) {
		// 加载原XML
		Document doc = null;
		try {
			doc = DocumentHelper.parseText(xml);
		} catch (DocumentException e) {
			LOGGER.error("", e);
		}

		Node node = doc.selectSingleNode(nodePath);
		if (node == null) {
			return "";
		} else {
			return node.getText();
		}
	}
	*/

	//wk modify 2014-6-25 from 省厅
	public static String getNodeValueFromXML(Document doc, String nodePath) {
		if(null == doc){
			return "";
		}
		
		Node node = doc.selectSingleNode(nodePath);
		if (node == null) {
			return "";
		} else {
			return node.getText();
		}
	}
	
	/**
	 * 从XML字符串中获取XML节点值.
	 * 
	 * @param xml
	 * @param nodePath
	 * @return
	 */
	public static String getWappedNodeValueFromXML(String xml, String nodePath) {
		// 加载原XML
		Document doc = null;
		try {
			doc = DocumentHelper.parseText(xml);
		} catch (DocumentException e) {
			LOGGER.error("", e);
		}

		if (nodePath.indexOf("file_code") == -1) {
			Node node = doc.selectSingleNode(nodePath);
			if (node == null) {
				return "";
			} else {
				return node.getText();
			}
		} else {
			Node node = doc.selectSingleNode(nodePath);
			if (node == null) {
				return "";
			} else {
				if (doc.selectSingleNode(nodePath.replace("file_code", "file_name")) != null) {
					return doc.selectSingleNode(nodePath.replace("file_code", "file_name")).getText() + "[tear]"
							+ doc.selectSingleNode(nodePath).getText();
				} else {
					return doc.selectSingleNode(nodePath.replace("file_code", "entity_file_name")).getText() + "[tear]"
							+ doc.selectSingleNode(nodePath).getText();
				}
			}
		}
	}

	/**
	 * 拼接xml属性.
	 * 
	 * @param key
	 *            节点属性名
	 * @param obj
	 *            属性值
	 * @return
	 */
	public static String pingXML(String key, Object obj) {
		String value = "";
		if (obj != null) {
			value = obj.toString();
		}

		value = Encoder.encodeHTML(value);
		return key + "=\"" + value + "\" ";
	}

	/**
	 * nodeSort(xmlDoc,"//data/persons","//data/persons/person","@sequence_no");.
	 * 
	 * @param xmlDoc
	 * @param parentXPath
	 * @param nodesXPath
	 * @param sortAttr
	 */
	@SuppressWarnings("rawtypes")
	public static void nodeSort(Document xmlDoc, String parentXPath, String nodesXPath, String sortAttr) {
		List nodes = xmlDoc.selectNodes(nodesXPath);
		Element p = (Element) xmlDoc.selectSingleNode(parentXPath);
		if (nodes.size() > 1) {
			Node[] result = nodeSortByAttritue(nodes, sortAttr);
			for (int i = 0; i < nodes.size(); i++) {
				((Node) nodes.get(i)).detach();
			}
			for (Node element : result) {
				p.add(element);
			}
		}
	}

	/**
	 * 调用nodeSortByAttritue(node,"@sequence_no").
	 * 
	 * @param nodes
	 * @param sortAttr
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static Node[] nodeSortByAttritue(List nodes, String sortAttr) {
		Node[] result = new Node[nodes.size()];
		/*
		 * for(int i=0;i<nodes.size();i++) { Node temp = (Node)nodes.get(i); String str = temp.valueOf(sortAttr);
		 * if(str!=null&&!"".equals(str)) { int currentSeqNo = Integer.parseInt(str); result[currentSeqNo-1] = temp; } }
		 */
		for (int i = 0; i < nodes.size(); i++) {
			result[i] = (Node) nodes.get(i);
		}

		Node temp;
		for (int i = 0; i < result.length; i++) {
			for (int j = result.length - 1; j > i; j--) {

				// result[j].valueOf(sortAttr).compareTo(result[j-1].valueOf(sortAttr))<0
				try {
					if (Integer.valueOf(result[j].valueOf(sortAttr)) < Integer.valueOf(result[j - 1].valueOf(sortAttr))) {
						// SortUtil.swap(data,j,j-1);
						temp = result[j];
						result[j] = result[j - 1];
						result[j - 1] = temp;
					}
					// 不能转化为数据进行比较的时候采用字符串比较
				} catch (Exception e) {
					if (result[j].valueOf(sortAttr).compareTo(result[j - 1].valueOf(sortAttr)) < 0) {
						// SortUtil.swap(data,j,j-1);
						temp = result[j];
						result[j] = result[j - 1];
						result[j - 1] = temp;
					}
				}

			}
		}

		return result;
	}

	/**
	 * document with w3c form to dom4j.
	 * 
	 * @param doc
	 * @return
	 */
	public static Document parseDoc(org.w3c.dom.Document doc) {
		if (doc == null) {
			return null;
		}
		org.dom4j.io.DOMReader xmlReader = new org.dom4j.io.DOMReader();
		return xmlReader.read(doc);
	}

	/**
	 * document with dom4j form to w3c.
	 * 
	 * @param doc
	 * @return
	 */
	public static org.w3c.dom.Document parseDoc(Document doc) throws Exception {
		if (doc == null) {
			return null;
		}
		java.io.StringReader reader = new java.io.StringReader(doc.asXML());
		org.xml.sax.InputSource source = new org.xml.sax.InputSource(reader);
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
				.newInstance();
		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
		return documentBuilder.parse(source);
	}

	/**
	 * transform xml-string to document in form of w3c.
	 * 
	 * @param xmlstr
	 * @return
	 */
	public static org.w3c.dom.Document parseW3cDoc(String xmlstr) {
		try {
			return parseDoc(parseDocument(xmlstr));
		} catch (Exception e) {
			LOGGER.error("error occuring in transforming from xml string to w3c document", e);
			return null;
		}
	}

	public static org.w3c.dom.Node selectSingleNode(String express, Object source) {// 查找节点，并返回第一个符合条件节点
		org.w3c.dom.Node result = null;
		XPathFactory xpathFactory = XPathFactory.newInstance();
		XPath xpath = xpathFactory.newXPath();
		try {
			result = (org.w3c.dom.Node) xpath.evaluate(express, source, XPathConstants.NODE);
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * 负责对xml的并集合并更新 ，目前不涉及属性的更新.
	 * 
	 * @author lineshow 2011-8-30
	 * @param destDoc
	 *            目标XML DOM
	 * @param srcDoc
	 *            源XML DOM
	 */
	public static void mergeXml(Document destDoc, Document srcDoc) {
		if (destDoc == null) {
			destDoc = srcDoc;
			return;
		}
		Element srcRootElement = srcDoc.getRootElement();
		Element destRootElement = destDoc.getRootElement();
		String srcRootName = srcRootElement.getQName().getName();
		String destRootName = destRootElement.getQName().getName();
		if (!srcRootName.equals(destRootName)) {
			LOGGER.error("根路径不同，退出合并。");
			return;
		}
		mergeCoreFunc(destDoc, srcRootElement, "/");
	}

	/**
	 * 负责对xml的并集合并更新 ，目前不涉及属性的更新.（只适合评议人信息修改）
	 * 
	 * @author lineshow 2011-8-30
	 * @param destDoc
	 *            目标XML DOM
	 * @param srcDoc
	 *            源XML DOM
	 */
	public static void mergeXmlPsn(Document destDoc, Document srcDoc) {
		if (destDoc == null) {
			destDoc = srcDoc;
			return;
		}
		Element srcRootElement = srcDoc.getRootElement();
		Element destRootElement = destDoc.getRootElement();
		String srcRootName = srcRootElement.getQName().getName();
		String destRootName = destRootElement.getQName().getName();
		if (!srcRootName.equals(destRootName)) {
			LOGGER.error("根路径不同，退出合并。");
			return;
		}
		mergeCoreFuncPsn(destDoc, srcRootElement, "/");
	}

	/**
	 * TODO:请添加注释(修改好了把TODO删除)lqh add.
	 * 
	 * @param destDoc
	 * @param srcElement
	 * @param path
	 */
	@SuppressWarnings("unchecked")
	private static void mergeCoreFunc(Document destDoc, Element srcElement, String path) {
		String currentNodePath = getElementPath(srcElement, path);
		Node node = destDoc.selectSingleNode(currentNodePath);
		if (node == null) {
			Element destE = (Element) destDoc.selectSingleNode(path);
			if (destE != null) {
				if (destE.elements().isEmpty()) {
					destE.clearContent();
				}
			} else {
				createXPath2(destDoc, "/" + path);
				destE = (Element) destDoc.selectSingleNode(path);
			}
			// destE.addElement(srcElement.getQName().getName());
			createXPath2(destDoc, "/" + currentNodePath);
			node = destDoc.selectSingleNode(currentNodePath);
		}
		if (srcElement.elements().isEmpty()) {
			node.setText(srcElement.getText());
			return;
		}
		Iterator<Element> elementIt = srcElement.elementIterator();
		while (elementIt.hasNext()) {
			Element cellElement = elementIt.next();
			mergeCoreFunc(destDoc, cellElement, currentNodePath);
		}
	}

	/**
	 * TODO:请添加注释(修改好了把TODO删除)lqh add.
	 * 
	 * @param destDoc
	 * @param srcElement
	 * @param path
	 */
	@SuppressWarnings("unchecked")
	private static void mergeCoreFuncPsn(Document destDoc, Element srcElement, String path) {
		String currentNodePath = path + "/" + srcElement.getQName().getName();
		String attrPath = "";
		if (!"//data".equals(currentNodePath)) {
			Iterator<Attribute> attributes = srcElement.attributeIterator();
			while (attributes.hasNext()) {
				Attribute attr = attributes.next();
				attrPath = "[@" + attr.getName() + "=\"" + attr.getText() + "\"]";
			}
		}
		currentNodePath = currentNodePath + attrPath;
		Node node = destDoc.selectSingleNode(currentNodePath);
		if (node == null) {
			createXPath(destDoc, currentNodePath);
			Element destE = (Element) destDoc.selectSingleNode(path);

			if (destE.elements().isEmpty()) {
				destE.clearContent();
			}
			node = destDoc.selectSingleNode(currentNodePath);
		}
		if (srcElement.elements().isEmpty()) {
			node.setText(srcElement.getText());
			return;
		}
		Iterator<Element> elementIt = srcElement.elementIterator();
		while (elementIt.hasNext()) {
			Element cellElement = elementIt.next();
			mergeCoreFuncPsn(destDoc, cellElement, currentNodePath);
		}
	}

	/**
	 * 测试入口.
	 * 
	 * @param args
	 * @throws UnsupportedEncodingException
	 * @throws DocumentException
	 */
	@SuppressWarnings("unused")
	public static void main(String[] args) throws Exception {
		/*
		 * String xmlstr =
		 * "<?xml version=\"1.0\" encoding=\"UTF-8\"?><data><budget><org_1_name>123123123</org_1_name><org_1_code>4312313</org_1_code></budget></data>"
		 * ; Document doc = XMLHelper.parseDocument(xmlstr); org.w3c.dom.Document w3cdom = XMLHelper.parseDoc(doc); Node
		 * node = doc.selectSingleNode("/data/aa"); String value = node.getText();
		 */
		String sourcefile = "D:/file2.xml";
		String targetfile = "D:/file3.xml";
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = null;
		try {
			db = dbf.newDocumentBuilder();
		} catch (ParserConfigurationException pce) {
			LOGGER.error("", pce);// 出现异常时，输出异常信息
		}
		Document docMain = null;
		Document docVice = null;
		// 获取两个XML文件的Document
		try {
			docMain = XMLHelper.parseDoc(db.parse(sourcefile));
			docVice = XMLHelper.parseDoc(db.parse(targetfile));
		} catch (DOMException e) {
			LOGGER.error("", e);
		} catch (Exception ioe) {
			LOGGER.error("", ioe);
		}

		// Map<String, Object> map = XMLHelper.compareXml(docMain, docVice);
		// rstMap.put("add", addList);
		// rstMap.put("update", updList);
		// rstMap.put("delete", delList);
		// rstMap.put("addKeys", addKeys);
		// rstMap.put("updKeys", updKeys);
		// rstMap.put("delKeys", delKeys);
		// List<String> keys = (List<String>) map.get("addKeys");
		// for (String key : keys) {
		// System.out.println(key);
		// }
		Map<String, Object> map = XMLHelper.compareXmlDiff(docMain, docVice, "#FF0000", "#FFFFF", true, true, null);
		System.out.println(map.get("oldXmlData"));
		System.out.println(map.get("oldXmlData"));

		String test = "<data></data>";
		Document doc = XMLHelper.parseDocument(test);
		createXPath(doc, "data/organizations/organization[@attr1='1'][@attr2='2']/value");
		System.out.println(doc.asXML());
	}

	/**
	 * 替换xml节点的值.
	 * 
	 * @param xmlData
	 * @param xmlPath
	 * @param value
	 * @return
	 */
	public static String refreshNodeValue(String xmlData, String xmlPath, Object value) {
		if (IrisStringUtils.isNullOrBlank(xmlData)) {
			return xmlData;
		}
		Document doc = null;
		try {
			doc = DocumentHelper.parseText(xmlData);
		} catch (Exception e) {
			LOGGER.error("doc转换错误", e);
		}
		if (doc == null) {
			return xmlData;
		}
		return  refreshNodeValue(doc,xmlPath,value);
	}
	public static String refreshNodeValue(Document doc, String xmlPath, Object value) {
	
		String strValue = null;
		if (value == null) {// 去除value为null时toString方法报错情况
			strValue = "";
		} else {
			strValue = value.toString();
		}
		Element e = (Element) doc.selectSingleNode(xmlPath);
		if (e == null) {// 如果不存在节点就造出来
			createXPath2(doc, xmlPath);
			e = (Element) doc.selectSingleNode(xmlPath);
		}
		e.setText(strValue);
		return doc.asXML();
	}

	/**
	 * 比较两个XML之间的差异
	 * 
	 * @param srcDoc
	 *            新XML
	 * @param destDoc
	 *            旧XML
	 * @return
	 */
	public static Map<String, Object> compareXml(Document srcDoc, Document destDoc) {

		Map<String, Object> rstMap = new HashMap<String, Object>();
		List<XmlCompareBean> addList = new ArrayList<XmlCompareBean>();
		List<XmlCompareBean> updList = new ArrayList<XmlCompareBean>();
		List<XmlCompareBean> delList = new ArrayList<XmlCompareBean>();

		if (srcDoc == null || destDoc == null) {
			rstMap.put("add", addList);
			rstMap.put("update", updList);
			rstMap.put("delete", delList);
			return rstMap;
		}
		List<XmlLeaf> elemListSrc = new ArrayList<XmlLeaf>();
		List<XmlLeaf> elemListDest = new ArrayList<XmlLeaf>();

		Element srcDocRoot = srcDoc.getRootElement();
		Element destDocRoot = destDoc.getRootElement();
		getElementList(elemListSrc, srcDocRoot);
		getElementList(elemListDest, destDocRoot);

		Map<String, String> mapSrc = getListString(elemListSrc);
		Map<String, String> mapDest = getListString(elemListDest);

		Set<String> keys = new HashSet<String>();
		keys.addAll(mapSrc.keySet());
		keys.addAll(mapDest.keySet());

		List<String> addKeys = new ArrayList<String>();
		List<String> updKeys = new ArrayList<String>();
		List<String> delKeys = new ArrayList<String>();
		for (String key : keys) {
			if (mapSrc.get(key) != null && mapDest.get(key) == null) {
				XmlCompareBean xcb = new XmlCompareBean();
				xcb.setNowValue(mapSrc.get(key));
				xcb.setPreValue("");
				xcb.setXpath(key);
				addKeys.add(key);
				addList.add(xcb);
			} else if (mapSrc.get(key) == null && mapDest.get(key) != null) {
				XmlCompareBean xcb = new XmlCompareBean();
				xcb.setPreValue(mapDest.get(key));
				xcb.setNowValue("");
				xcb.setXpath(key);
				delKeys.add(key);
				delList.add(xcb);
			} else {
				if (mapDest.get(key) != null && !mapDest.get(key).equals(mapSrc.get(key))) {
					if (IrisStringUtils.isNullOrBlank(mapDest.get(key))
							&& !IrisStringUtils.isNullOrBlank(mapSrc.get(key))) {
						XmlCompareBean xcb = new XmlCompareBean();
						xcb.setNowValue(mapSrc.get(key));
						xcb.setPreValue("");
						xcb.setXpath(key);
						addKeys.add(key);
						addList.add(xcb);
					} else if (!IrisStringUtils.isNullOrBlank(mapDest.get(key))
							&& IrisStringUtils.isNullOrBlank(mapSrc.get(key))) {
						XmlCompareBean xcb = new XmlCompareBean();
						xcb.setPreValue(mapDest.get(key));
						xcb.setNowValue("");
						xcb.setXpath(key);
						delKeys.add(key);
						delList.add(xcb);
					} else {
						XmlCompareBean xcb = new XmlCompareBean();
						xcb.setNowValue(mapSrc.get(key));
						xcb.setPreValue(mapDest.get(key));
						xcb.setXpath(key);
						updKeys.add(key);
						updList.add(xcb);
					}

				}
			}
		}
		rstMap.put("add", addList);
		rstMap.put("update", updList);
		rstMap.put("delete", delList);
		rstMap.put("addKeys", addKeys);
		rstMap.put("updKeys", updKeys);
		rstMap.put("delKeys", delKeys);

		boolean same = false;
		if (addList.size() == 0 && updList.size() == 0 && delList.size() == 0) {
			same = true;
		} else {
			same = false;
		}
		rstMap.put("same", same);
		return rstMap;
	}

	private static String wrapChangedValue(Map<String, String> mapSrc, Map<String, String> mapDest, String key, int type) {

		if (type == 1) {
			if (key.endsWith("file_code")) {
				if (mapDest.get(key.replace("file_code", "file_name")) != null) {
					return mapDest.get(key.replace("file_code", "file_name")).toString() + "[tear]" + mapDest.get(key);
				} else {
					return mapDest.get(key.replace("file_code", "entity_file_name")).toString() + "[tear]"
							+ mapDest.get(key);
				}
			} else {
				return mapDest.get(key);
			}
		} else {
			if (key.endsWith("file_code")) {
				if (mapSrc.get(key.replace("file_code", "file_name")) != null) {
					return mapSrc.get(key.replace("file_code", "file_name")).toString() + "[tear]" + mapSrc.get(key);
				} else {
					return mapSrc.get(key.replace("file_code", "entity_file_name")).toString() + "[tear]"
							+ mapSrc.get(key);
				}
			} else {
				return mapSrc.get(key);
			}
		}
	}

	/**
	 * 比较两个XML之间的差异
	 * 
	 * @param srcDoc
	 *            新XML
	 * @param destDoc
	 *            旧XML
	 * @return
	 */
	public static Map<String, Object> compareXmlNew(Document srcDoc, Document destDoc) {

		Map<String, Object> rstMap = new HashMap<String, Object>();
		List<XmlCompareBean> addList = new ArrayList<XmlCompareBean>();
		List<XmlCompareBean> updList = new ArrayList<XmlCompareBean>();
		List<XmlCompareBean> delList = new ArrayList<XmlCompareBean>();

		if (srcDoc == null || destDoc == null) {
			rstMap.put("add", addList);
			rstMap.put("update", updList);
			rstMap.put("delete", delList);
			return rstMap;
		}
		List<XmlLeaf> elemListSrc = new ArrayList<XmlLeaf>();
		List<XmlLeaf> elemListDest = new ArrayList<XmlLeaf>();

		Element srcDocRoot = srcDoc.getRootElement();
		Element destDocRoot = destDoc.getRootElement();
		getElementList(elemListSrc, srcDocRoot);
		getElementList(elemListDest, destDocRoot);

		Map<String, String> mapSrc = getListString(elemListSrc);
		Map<String, String> mapDest = getListString(elemListDest);

		Set<String> keys = new HashSet<String>();
		keys.addAll(mapSrc.keySet());
		keys.addAll(mapDest.keySet());

		List<String> addKeys = new ArrayList<String>();
		List<String> updKeys = new ArrayList<String>();
		List<String> delKeys = new ArrayList<String>();
		for (String key : keys) {
			if (mapSrc.get(key) != null && mapDest.get(key) == null) {
				XmlCompareBean xcb = new XmlCompareBean();
				xcb.setNowValue(wrapChangedValue(mapSrc, mapDest, key, 0));
				xcb.setPreValue("");
				xcb.setXpath(key);
				addKeys.add(key);
				addList.add(xcb);
			} else if (mapSrc.get(key) == null && mapDest.get(key) != null) {
				XmlCompareBean xcb = new XmlCompareBean();
				xcb.setPreValue(wrapChangedValue(mapSrc, mapDest, key, 1));
				xcb.setNowValue("");
				xcb.setXpath(key);
				delKeys.add(key);
				delList.add(xcb);
			} else {
				/*
				 * if (mapDest.get(key) != null && !mapDest.get(key).equals(mapSrc.get(key))) { XmlCompareBean xcb = new
				 * XmlCompareBean(); xcb.setNowValue(wrapChangedValue(mapSrc, mapDest, key, 0));
				 * xcb.setPreValue(wrapChangedValue(mapSrc, mapDest, key, 1)); xcb.setXpath(key); updKeys.add(key);
				 * updList.add(xcb); }
				 */

				if (mapDest.get(key) != null && !mapDest.get(key).equals(mapSrc.get(key))) {
					if (IrisStringUtils.isNullOrBlank(mapDest.get(key))
							&& !IrisStringUtils.isNullOrBlank(mapSrc.get(key))) {
						XmlCompareBean xcb = new XmlCompareBean();
						xcb.setNowValue(wrapChangedValue(mapSrc, mapDest, key, 0));
						xcb.setPreValue("");
						xcb.setXpath(key);
						addKeys.add(key);
						addList.add(xcb);
					} else if (!IrisStringUtils.isNullOrBlank(mapDest.get(key))
							&& IrisStringUtils.isNullOrBlank(mapSrc.get(key))) {
						XmlCompareBean xcb = new XmlCompareBean();
						xcb.setPreValue(wrapChangedValue(mapSrc, mapDest, key, 1));
						xcb.setNowValue("");
						xcb.setXpath(key);
						delKeys.add(key);
						delList.add(xcb);
					} else {
						XmlCompareBean xcb = new XmlCompareBean();
						xcb.setNowValue(wrapChangedValue(mapSrc, mapDest, key, 0));
						xcb.setPreValue(wrapChangedValue(mapSrc, mapDest, key, 1));
						xcb.setXpath(key);
						updKeys.add(key);
						updList.add(xcb);
					}

				}
			}
		}
		rstMap.put("add", addList);
		rstMap.put("update", updList);
		rstMap.put("delete", delList);
		rstMap.put("addKeys", addKeys);
		rstMap.put("updKeys", updKeys);
		rstMap.put("delKeys", delKeys);

		boolean same = false;
		if (addList.size() == 0 && updList.size() == 0 && delList.size() == 0) {
			same = true;
		} else {
			same = false;
		}
		rstMap.put("same", same);
		return rstMap;
	}

	public static Map<String, String> getListString(List<XmlLeaf> elemList) {
		Map<String, String> map = new HashMap<String, String>();
		for (Iterator<XmlLeaf> it = elemList.iterator(); it.hasNext();) {
			XmlLeaf leaf = it.next();
			map.put(leaf.getXpath(), leaf.getValue());
			// System.out.println(leaf.getXpath());
		}
		return map;
	}

	/**
	 * 递归遍历方法
	 * 
	 * @param element
	 */
	@SuppressWarnings("unchecked")
	private static void getElementList(List<XmlLeaf> elemList, Element element) {
		List<Element> elements = element.elements();
		if (elements.size() == 0) {
			// 没有子元素
			String xpath = getElementPath(element, "");
			String value = element.getText();
			elemList.add(new XmlLeaf(xpath, value));
		} else {
			// 有子元素
			for (Iterator<Element> it = elements.iterator(); it.hasNext();) {
				Element elem = it.next();

				// 递归遍历
				getElementList(elemList, elem);
			}
		}
	}

	/**
	 * 递归遍历方法
	 * 
	 * @param element
	 */
	@SuppressWarnings("unchecked")
	private static void xmlsccElementList(Element element) {
		xmlsccElementList(element, null);
	}

	/**
	 * 递归遍历方法
	 * 
	 * @param element
	 */
	@SuppressWarnings("unchecked")
	private static void xmlsccElementList(Element element, Set<String> excludes) {
		List<Element> elements = element.elements();
		if (elements.size() == 0) {
			// 没有子元素
			// String xpath = getElementPath(element, "");
			// String value = element.getText();
			// elemList.add(new XmlLeaf(xpath, value));
			if (excludes != null && excludes.contains(element.getName())) {
				element.setText(element.getText());
			} else {
				element.setText(element.getText().replaceAll(">", "&gt;").replaceAll("<", "&lt;"));
			}
		} else {
			// 有子元素
			for (Iterator<Element> it = elements.iterator(); it.hasNext();) {
				Element elem = it.next();
				// 递归遍历
				xmlsccElementList(elem, excludes);
			}
		}
	}

	@SuppressWarnings("unchecked")
	public static String getElementPath(Element element, String xpath) {
		xpath = "/" + element.getName();
		if (element.getParent() != null) {
			for (Iterator<Attribute> j = element.attributeIterator(); j.hasNext();) {
				Attribute attribute = j.next();
				xpath = xpath + "[@" + attribute.getName().trim() + "='" + attribute.getValue().trim() + "']";
			}
			if (xpath.indexOf("[@submit_org='1']") > -1 && xpath.indexOf("[@sequence_no='1']") > -1){
				xpath = xpath.replace("[@sequence_no='1']", "");
			}
			xpath = getElementPath(element.getParent(), xpath) + xpath;
		}
		return xpath;
	}

	/**
	 * 比较两个XML之间的差异，并标注颜色
	 * 
	 * @param newDoc
	 *            新XML
	 * @param oldDoc
	 *            旧XML
	 * @param fontColor
	 *            CSS格式字体颜色
	 * @param backgroundColor
	 *            CSS格式背景颜色
	 * @param isCompareAdd
	 *            是否比较新增加的节点
	 * @param isCompareDelete
	 *            是否比较删除了的节点
	 * @param highlightDiffNodeXPaths
	 *            值：节点xpath以,分隔。 对比时不管是不是只有部分文字发生变化，都将文本全部加亮。应对金额不一致时全部加亮文本的需求
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> compareXmlDiff(Document newDoc, Document oldDoc, String fontColor,
			String backgroundColor, boolean isCompareAdd, boolean isCompareDelete, String highlightDiffNodeXPaths) {
		// 设置默认颜色
		if (fontColor == null || fontColor.trim().equals("")) {
			fontColor = "#FFFFFF";
		}
		if (backgroundColor == null || backgroundColor.trim().equals("")) {
			backgroundColor = "#FF0000";
		}

		// 获取所有需要只对差异部分加亮处理的节点xpath
		Map<String, String> highlightDiffXPaths = new HashMap<String, String>();
		if (highlightDiffNodeXPaths != null) {
			for (String xpathStr : highlightDiffNodeXPaths.split(",")) {
				getSubNodeWithXPath(newDoc, highlightDiffXPaths, xpathStr);
			}
		}

		Map<String, Object> docMap = new HashMap<String, Object>();
		Map<String, Object> rstMap = compareXml(newDoc, oldDoc);// 两个XML之间的差异信息
		String oldXmlData = oldDoc.asXML();
		String newXmlData = newDoc.asXML();

		/**
		 * 比较两个XML之间的差异
		 */
		/*
		 * 取得两个XML之间的差异信息
		 */
		List<XmlCompareBean> xmlCompareList = new ArrayList<XmlCompareBean>();
		List<XmlCompareBean> updateList = (List<XmlCompareBean>) rstMap.get("update");// 新XML中更新的节点
		List<XmlCompareBean> addList = (List<XmlCompareBean>) rstMap.get("add");// 新XML中增加的节点
		List<XmlCompareBean> deleteList = (List<XmlCompareBean>) rstMap.get("delete");// 新XML中删除的节点
		if (updateList != null && updateList.size() > 0) {
			xmlCompareList.addAll(updateList);
		}
		if (isCompareAdd && addList != null && addList.size() > 0) {
			xmlCompareList.addAll(addList);
		}
		if (isCompareDelete && deleteList != null && deleteList.size() > 0) {
			xmlCompareList.addAll(deleteList);
		}

		for (XmlCompareBean compare : xmlCompareList) {
			String oldStr = compare.getPreValue();// 节点原值
			String newStr = compare.getNowValue();// 节点新值
			String xpath = compare.getXpath();// XML xpath
			// 去掉xpath第一个斜杠
			// if (xpath.startsWith("/")) {
			// xpath = xpath.substring(1);
			// }

			oldStr = getNodeValueFromXML(XMLHelper.parseDocument(oldXmlData), xpath);
			newStr = getNodeValueFromXML(XMLHelper.parseDocument(newXmlData), xpath);
			// 将节点值中包含<br/>或<br>的内容替换成\n
			if (oldStr.indexOf("<br/>") != -1) {
				oldStr = convertLine(oldStr, "<br/>");
			}
			if (oldStr.indexOf("<br>") != -1) {
				oldStr = convertLine(oldStr, "<br>");
			}
			if (newStr.indexOf("<br/>") != -1) {
				newStr = convertLine(newStr, "<br/>");
			}
			if (newStr.indexOf("<br>") != -1) {
				newStr = convertLine(newStr, "<br>");
			}

			// 换行转义
			Pattern p = Pattern.compile("\\r|\n");
			Matcher m = p.matcher(oldStr);
			oldStr = m.replaceAll("&lt;br&gt;");
			m = p.matcher(newStr);
			newStr = m.replaceAll("&lt;br&gt;");

			boolean isHighlightDiff = true;
			if (!oldStr.equals("") && !newStr.equals("")) {
				if (oldStr.equals(newStr)) {// 经过转义处理后2个字符串一样则跳过当前循环
					continue;
				}
				// 不需要对差异部分进行加亮处理的节点
				if (highlightDiffXPaths.containsKey(xpath)) {
					isHighlightDiff = false;
				}
				if (!isHighlightDiff) {// 对全部文本差异部分进行加亮处理
					// 获取文本差异信息
					List<TextDiff> diffs = TextComparer.compare(oldStr, newStr);
					List<TextDiff> diffsDeleted = new ArrayList<TextDiff>();
					List<TextDiff> diffsInserted = new ArrayList<TextDiff>();

					for (TextDiff diff : diffs) {
						if (diff.getDiffType() == TextDiff.TYPE_DELETED) {// 获取节点原值中删除了的字符串信息
							diffsDeleted.add(diff);
						} else if (diff.getDiffType() == TextDiff.TYPE_INSERTED) {// 获取节点新值中增加了的字符串信息
							diffsInserted.add(diff);
						}
					}

					// 将节点原值中删除了的字符串进行文本加亮处理
					if (diffsDeleted.size() > 0) {
						oldStr = getTextDiffAfterHighlight(oldStr, fontColor, backgroundColor, diffsDeleted);
						oldXmlData = refreshNodeValue(oldXmlData, xpath, oldStr);
					} else {
						if (diffs.size() == 0) {
							oldXmlData = refreshNodeValue(oldXmlData, xpath,
									highlightStr(oldStr, fontColor, backgroundColor));
						}
					}
					// 将节点新值中增加了的字符串进行文本加亮处理
					if (diffsInserted.size() > 0) {
						newStr = getTextDiffAfterHighlight(newStr, fontColor, backgroundColor, diffsInserted);
						newXmlData = refreshNodeValue(newXmlData, xpath, newStr);
					} else {
						if (diffs.size() == 0) {
							newXmlData = refreshNodeValue(newXmlData, xpath,
									highlightStr(newStr, fontColor, backgroundColor));
						}
					}
				} else {// 对整个文本进行加亮处理
					oldXmlData = refreshNodeValue(oldXmlData, xpath, highlightStr(oldStr, fontColor, backgroundColor));
					newXmlData = refreshNodeValue(newXmlData, xpath, highlightStr(newStr, fontColor, backgroundColor));
				}
			} else {
				if (newStr.equals("") && !oldStr.equals("")) {
					oldXmlData = refreshNodeValue(oldXmlData, xpath, highlightStr(oldStr, fontColor, backgroundColor));
				} else if (oldStr.equals("") && !newStr.equals("")) {
					newXmlData = refreshNodeValue(newXmlData, xpath, highlightStr(newStr, fontColor, backgroundColor));
				}
			}
		}

		docMap.put("oldXmlData", oldXmlData);
		docMap.put("newXmlData", newXmlData);
		return docMap;
	}

	/**
	 * 比较两个XML之间的差异，并标注颜色
	 * 
	 * @param newDoc
	 *            新XML
	 * @param oldDoc
	 *            旧XML
	 * @param fontColor
	 *            CSS格式字体颜色
	 * @param backgroundColor
	 *            CSS格式背景颜色
	 * @param isCompareAdd
	 *            是否比较新增加的节点
	 * @param isCompareDelete
	 *            是否比较删除了的节点
	 * @param highlightDiffNodeXPaths
	 *            值：节点xpath以,分隔。 对比时不管是不是只有部分文字发生变化，都将文本全部加亮。应对金额不一致时全部加亮文本的需求
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> compareXmlDiffNew(Document newDoc, Document oldDoc, String fontColor,
			String backgroundColor, boolean isCompareAdd, boolean isCompareDelete, String highlightDiffNodeXPaths,
			Set<String> excludes) {

		xmlsccElementList(newDoc.getRootElement(), excludes);
		xmlsccElementList(oldDoc.getRootElement(), excludes);
		// 设置默认颜色
		if (fontColor == null || fontColor.trim().equals("")) {
			fontColor = "#FFFFFF";
		}
		if (backgroundColor == null || backgroundColor.trim().equals("")) {
			backgroundColor = "#33cccc";
		}

		// 获取所有需要只对差异部分加亮处理的节点xpath
		Map<String, String> highlightDiffXPaths = new HashMap<String, String>();
		if (highlightDiffNodeXPaths != null) {
			for (String xpathStr : highlightDiffNodeXPaths.split(",")) {
				getSubNodeWithXPath(newDoc, highlightDiffXPaths, xpathStr);
			}
		}

		Map<String, Object> docMap = new HashMap<String, Object>();
		Map<String, Object> rstMap = compareXmlNew(newDoc, oldDoc);// 两个XML之间的差异信息
		docMap.put("rstMap", rstMap);
		String oldXmlData = oldDoc.asXML();
		String newXmlData = newDoc.asXML();

		/**
		 * 比较两个XML之间的差异
		 */
		/*
		 * 取得两个XML之间的差异信息
		 */
		List<XmlCompareBean> xmlCompareList = new ArrayList<XmlCompareBean>();
		List<XmlCompareBean> updateList = (List<XmlCompareBean>) rstMap.get("update");// 新XML中更新的节点
		List<XmlCompareBean> addList = (List<XmlCompareBean>) rstMap.get("add");// 新XML中增加的节点
		List<XmlCompareBean> deleteList = (List<XmlCompareBean>) rstMap.get("delete");// 新XML中删除的节点
		List<String> addKeys = (List<String>) rstMap.get("addKeys");
		List<String> updKeys = (List<String>) rstMap.get("updKeys");
		List<String> delKeys = (List<String>) rstMap.get("delKeys");
		if (updateList != null && updateList.size() > 0) {
			xmlCompareList.addAll(updateList);
		}
		if (isCompareAdd && addList != null && addList.size() > 0) {
			xmlCompareList.addAll(addList);
		}
		if (isCompareDelete && deleteList != null && deleteList.size() > 0) {
			xmlCompareList.addAll(deleteList);
		}

		for (XmlCompareBean compare : xmlCompareList) {
			String oldStr = compare.getPreValue();// 节点原值
			String newStr = compare.getNowValue();// 节点新值
			String xpath = compare.getXpath();// XML xpath
			// 去掉xpath第一个斜杠
			// if (xpath.startsWith("/")) {
			// xpath = xpath.substring(1);
			// }
			String name = xpath.substring(xpath.lastIndexOf('/') + 1);
			if (excludes != null && excludes.contains(name)) {
				continue;
			}
			oldStr = getWappedNodeValueFromXML(oldXmlData, xpath);
			newStr = getWappedNodeValueFromXML(newXmlData, xpath);
			// 将节点值中包含<br/>或<br>的内容替换成\n
			if (oldStr.indexOf("<br/>") != -1) {
				oldStr = convertLine(oldStr, "<br/>");
			}
			if (oldStr.indexOf("<br>") != -1) {
				oldStr = convertLine(oldStr, "<br>");
			}
			if (newStr.indexOf("<br/>") != -1) {
				newStr = convertLine(newStr, "<br/>");
			}
			if (newStr.indexOf("<br>") != -1) {
				newStr = convertLine(newStr, "<br>");
			}

			// 换行转义
			if (xpath.indexOf("xmldata") == -1) {
				Pattern p = Pattern.compile("\\r|\n");
				Matcher m = p.matcher(oldStr);
				oldStr = m.replaceAll("&lt;br&gt;");
				m = p.matcher(newStr);
				newStr = m.replaceAll("&lt;br&gt;");
			}
			boolean isHighlightDiff = false;
			if (!oldStr.equals("") && !newStr.equals("")) {
				if (oldStr.equals(newStr)) {// 经过转义处理后2个字符串一样则跳过当前循环
					continue;
				}
				newXmlData = refreshNodeValue(newXmlData, xpath,
						wrapUpdateStr(oldStr, newStr, xpath, fontColor, backgroundColor));
			} else {
				// 删除
				if (newStr.equals("") && !oldStr.equals("")) {
					if (delKeys.contains(xpath)) {
						newXmlData = refreshNodeValue(newXmlData, xpath,
								wrapUpdateStr(oldStr, "", xpath, fontColor, "#ff0000"));
					} else {
						// newXmlData = refreshNodeValue(newXmlData, xpath,
						// wrapUpdateStr(oldStr, newStr, xpath, fontColor, backgroundColor));
						newXmlData = refreshNodeValue(newXmlData, xpath,
								wrapUpdateStr(oldStr, "", xpath, fontColor, "#ff0000"));
					}
				}
				// 新增
				else if (oldStr.equals("") && !newStr.equals("")) {
					if (addKeys.contains(xpath)) {
						newXmlData = refreshNodeValue(newXmlData, xpath,
								wrapUpdateStr("", newStr, xpath, fontColor, "#339966"));
					} else {
						// newXmlData = refreshNodeValue(newXmlData, xpath,
						// wrapUpdateStr(oldStr, newStr, xpath, fontColor, backgroundColor));
						newXmlData = refreshNodeValue(newXmlData, xpath,
								wrapUpdateStr("", newStr, xpath, fontColor, "#339966"));
					}

				}
			}
		}

		docMap.put("oldXmlData", oldXmlData);
		docMap.put("newXmlData", newXmlData);
		return docMap;
	}

	/**
	 * 获取加亮之后的文本
	 * 
	 * @param str
	 *            原文本
	 * @param fontColor
	 *            CSS格式字体颜色
	 * @param backgroundColor
	 *            CSS格式背景颜色
	 * @param diffs
	 *            文本之间的差异
	 * @return
	 */
	private static String getTextDiffAfterHighlight(String str, String fontColor, String backgroundColor,
			List<TextDiff> diffs) {
		StringBuffer buffer = new StringBuffer();

		for (int i = 0; i < diffs.size(); i++) {
			TextDiff diffCurr = diffs.get(i);
			// 取得文本之间相同的字符串
			if (i == 0) {
				buffer.append(convertLine(str.substring(0, diffCurr.getDiffStartIndex())));
			} else {
				TextDiff diffPrev = diffs.get(i - 1);
				buffer.append(convertLine(str.substring(diffPrev.getDiffStartIndex() + diffPrev.getDiffLength(),
						diffCurr.getDiffStartIndex())));
			}
			// 取得文本之间差异的字符串
			buffer.append(highlightStr(str.substring(diffCurr.getDiffStartIndex(), diffCurr.getDiffStartIndex()
					+ diffCurr.getDiffLength()), fontColor, backgroundColor));
			// 取得文本之间相同的字符串
			if (i + 1 == diffs.size()) {
				buffer.append(convertLine(str.substring(diffCurr.getDiffStartIndex() + diffCurr.getDiffLength(),
						str.length())));
			}
		}

		return buffer.toString();
	}

	/**
	 * 加亮文本
	 * 
	 * @param str
	 * @param fontColor
	 *            CSS格式字体颜色
	 * @param backgroundColor
	 *            CSS格式背景颜色
	 * @return
	 */
	private static String highlightStr(String str, String fontColor, String backgroundColor) {
		/*
		 * 将&lt;br&gt;去掉，以\n替换
		 */
		String[] strArr = str.split("&lt;br&gt;");
		String newStr = "";
		for (String s : strArr) {
			if (strArr.length == 1) {
				newStr = ("<span style=\"color: " + fontColor + "; background-color: " + backgroundColor + "\">" + s + "</span>");
			} else {
				newStr += "\n"
						+ ("<span style=\"color: " + fontColor + "; background-color: " + backgroundColor + "\">" + s + "</span>");
			}
		}
		if (!newStr.equals("") && strArr.length > 1) {
			newStr = newStr.substring(1);
		}
		return newStr;
		// return ("<span style=\"color: " + fontColor + "; background-color: "
		// + backgroundColor + "\">" + str + "</span>");
	}

	private static String highlightStr(String str, String fontColor, String backgroundColor, boolean textDecrator) {
		/*
		 * 将&lt;br&gt;去掉，以\n替换
		 */
		String[] strArr = str.split("&lt;br&gt;");
		String newStr = "";
		for (String s : strArr) {
			if (strArr.length == 1) {
				newStr = ("<span style=\"color: " + fontColor + "; background-color: " + backgroundColor
						+ "   ;text-decoration: line-through;\">" + s + "</span> ");
			} else {
				newStr += "\n"
						+ ("<span style=\"color: " + fontColor + "; background-color: " + backgroundColor
								+ "  ;text-decoration: line-through;\"> " + s + "</span>");
			}
		}
		if (!newStr.equals("") && strArr.length > 1) {
			newStr = newStr.substring(1);
		}
		return newStr;
		// return ("<span style=\"color: " + fontColor + "; background-color: "
		// + backgroundColor + "\">" + str + "</span>");
	}

	private static String wrapUpdateStr(String oldStr, String newStr, String xpath, String fontColor,
			String backgroundColor) {
		/*
		 * 将&lt;br&gt;去掉，以\n替换
		 */
		// String[] strArr = str.split("&lt;br&gt;");
		newStr = newStr.replaceAll("&lt;br&gt;", "\n");
		oldStr = oldStr.replaceAll("&lt;br&gt;", "\n");
		// 类似于file_code 之类的 复合值 以 file_code[tear]file_name形式组合 故在此需要将其分割
		if (newStr.indexOf("[tear]") > 0) {
			newStr = newStr.substring(newStr.indexOf("[tear]") + 6);
		}
		if (oldStr.indexOf("[tear]") > 0) {
			oldStr = oldStr.substring(0, oldStr.indexOf("[tear]"));
		}
		xpath = xpath.replaceAll("\"", "'");
		if (!"".equals(newStr) && !"".equals(oldStr)) {
			newStr = ("<span id=\"compare_" + xpath + "\"  title='原值：" + oldStr + "' style=\"color: " + fontColor
					+ "; background-color: " + backgroundColor + ";\">" + newStr + "</span> ");
		}
		if ("".equals(newStr) && !"".equals(oldStr)) {
			newStr = ("<span id=\"compare_" + xpath + "\"  title='该记录已在新版本中删除' style=\"color: " + fontColor
					+ "; background-color: " + backgroundColor + ";text-decoration: line-through;\">" + oldStr + "</span> ");
		}
		if (!"".equals(newStr) && "".equals(oldStr)) {
			newStr = ("<span id=\"compare_" + xpath + "\" title='该记录在新版本中增加' style=\"color: " + fontColor
					+ "; background-color: " + backgroundColor + ";\">" + newStr + "</span> ");
		}

		return newStr;
		// return ("<span style=\"color: " + fontColor + "; background-color: "
		// + backgroundColor + "\">" + str + "</span>");
	}

	/**
	 * XML节点值换行处理
	 * 
	 * @param str
	 * @return
	 */
	public static String convertLine(String str) {
		/*
		 * 将&lt;br&gt;去掉，以\n替换
		 */
		String[] strArr = str.split("&lt;br&gt;");
		String newStr = "";
		for (String s : strArr) {
			if (strArr.length == 1) {
				newStr = s;
			} else {
				newStr += "\n" + s;
			}
		}
		if (!newStr.equals("") && strArr.length > 1) {
			newStr = newStr.substring(1);
		}
		return newStr;
	}

	/**
	 * XML节点值换行处理
	 * 
	 * @param str
	 * @return
	 */
	public static String convertLine(String str, String splitStr) {
		/*
		 * 将&lt;br&gt;去掉，以\n替换
		 */
		String[] strArr = str.split(splitStr);
		String newStr = "";
		for (String s : strArr) {
			if (strArr.length == 1) {
				newStr = s;
			} else {
				newStr += "\n" + s;
			}
		}
		if (!newStr.equals("") && strArr.length > 1) {
			newStr = newStr.substring(1);
		}
		return newStr;
	}

	/**
	 * 根据xpath获取所有子节点xpath，如果当前节点为叶子节点则返回当前节点xpath
	 * 
	 * @param doc
	 * @param xpaths
	 *            所有子节点xpath
	 * @param xpath
	 */
	@SuppressWarnings("unchecked")
	public static void getSubNodeWithXPath(Document doc, Map<String, String> xpaths, String xpath) {
		List<Element> elements = doc.selectNodes(xpath);// 找到当前xpath所有节点
		for (Iterator<Element> it = elements.iterator(); it.hasNext();) {
			Element element = it.next();
			List<Element> elementsSub = element.elements();
			// 如果当前节点没有子节点则确定为叶子节点，否则递归调用直到找到叶子节点
			if (elementsSub == null || elementsSub.size() == 0) {
				String key = getElementPath(element, "");
				String value = element.getText();
				xpaths.put(key, value);
			} else {
				for (Iterator<Element> itSub = elementsSub.iterator(); itSub.hasNext();) {
					Element elementSub = itSub.next();
					getSubNodeWithXPath(doc, xpaths, xpath + "/" + elementSub.getName());
				}
			}
		}
	}

	/**
	 * 将节点名全转成小写
	 * 
	 * @param data
	 * @return
	 */
	public static String nodeToLowerCase(String data) {
		StringBuilder sb = new StringBuilder();

		int len = data.length();
		int excludeIndex = -1;// 每次排除的字符串之后下一个字符的下标

		for (int i = 0; i < len; i++) {
			char c = data.charAt(i);
			sb.append(c);
			if (i < excludeIndex) {
				continue;
			}
			excludeIndex = -1;

			if (c == '<') {

				for (String[] str : EXCLUDE_STR) {
					if (i + str[0].length() + str[1].length() > len) {
						continue;
					}

					boolean isBeginMatch = true;// 是否匹配excludeStr[x][0]
					for (int j = 0; j < str[0].length(); j++) {
						if (data.charAt(i + j) != str[0].charAt(j)) {
							isBeginMatch = false;
							break;
						}
					}

					if (isBeginMatch) {
						int m = i + str[0].length();

						for (; m < len; m++) {

							boolean isEndMatch = true;// 是否匹配excludeStr[x][1]
							for (int n = 0; n < str[1].length(); n++) {
								if (data.charAt(m + n) != str[1].charAt(n)) {
									isEndMatch = false;
									break;
								}
							}

							if (isEndMatch) {
								excludeIndex = m + str[1].length();
								break;
							}
						}
						if (excludeIndex != -1) {
							break;
						}
					}
				}

				if (excludeIndex != -1) {
					continue;
				}

				for (i++; i < len; i++) {
					sb.append(Character.toLowerCase(data.charAt(i)));

					if (data.charAt(i) == '>' || data.charAt(i) == ' ') {
						break;
					}
				}
			}
		}
		return sb.toString();
	}
}