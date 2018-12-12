package com.example.demo.tools;

import java.io.File;
import java.io.StringReader;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 * XML文件合并工具类.
 * 
 * @author lj
 */
public class XmlMergeUtils {

	protected static final Logger LOGGER = LoggerFactory.getLogger(XmlMergeUtils.class);

	/**
	 * XML文件的合并处理.
	 * 
	 * @param sourceXML
	 *            XML字符串，待合并处理的xml文件，合并后将更新此文件
	 * @param subXML
	 *            XML字符串，被合并的xml文件
	 * @return 合并成功返回Document
	 * @throws Exception
	 */
	public static Document mergeDocument(String sourceXML, String subXML) throws Exception {
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
			sourceXML = new String(sourceXML.getBytes(),"UTF-8");   
            StringReader sr = new StringReader(sourceXML);   
            InputSource is = new InputSource(sr);    
			docMain = db.parse(is);

			subXML = new String(subXML.getBytes(),"UTF-8");   
            StringReader sr2 = new StringReader(subXML);   
            InputSource is2 = new InputSource(sr2);    
			docVice = db.parse(is2);
		} catch (DOMException e) {
			LOGGER.error("", e);
		} catch (Exception ioe) {
			LOGGER.error("", ioe);
		}
		// 获取两个文件的根节点
		Element rootMain = docMain.getDocumentElement();
		Element rootVice = docVice.getDocumentElement();
		// 下面添加被合并文件根节点下的每个节点
		NodeList messageItems = rootVice.getChildNodes();
		int itemNumber = messageItems.getLength();
		// 如果去掉根节点下的第一个节点，那么i从3开始，否则i从1开始
		for (int i = 1; i < itemNumber; i = i + 2) {
			// 调用dupliate()，依次复制被合并XML文档中根节点下的节点
			Element messageItem = (Element) messageItems.item(i);
			dupliate(docMain, rootMain, messageItem);
		}
		return docMain;
	}

	/**
	 * XML文件的合并处理.
	 * 
	 * @param mainFileName
	 *            待合并处理的xml文件，合并后将更新此文件
	 * @param subFilename
	 *            被合并的xml文件
	 * @return 合并成功返回true，否则返回false
	 * @throws Exception
	 */
	public static boolean isMerging(String mainFileName, String subFilename) throws Exception {
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
			docMain = db.parse(mainFileName);
			docVice = db.parse(subFilename);
		} catch (DOMException e) {
			LOGGER.error("", e);
		} catch (Exception ioe) {
			LOGGER.error("", ioe);
		}
		// 获取两个文件的根节点
		Element rootMain = docMain.getDocumentElement();
		Element rootVice = docVice.getDocumentElement();
		// 下面添加被合并文件根节点下的每个节点
		NodeList messageItems = rootVice.getChildNodes();
		int itemNumber = messageItems.getLength();
		// 如果去掉根节点下的第一个节点，那么i从3开始，否则i从1开始
		for (int i = 1; i < itemNumber; i = i + 2) {
			// 调用dupliate()，依次复制被合并XML文档中根节点下的节点
			Element messageItem = (Element) messageItems.item(i);
			dupliate(docMain, rootMain, messageItem);
		}
		// 调用 writeTo()，将合并得到的Document写入目标XML文档
		boolean isWritten = writeTo(docMain, mainFileName);
		return isWritten;
	}

	/**
	 * 复制.
	 * 
	 * @param docDup
	 * @param father
	 * @param son
	 * @return
	 * @throws Exception
	 */
	private static boolean dupliate(Document docDup, Element father, Element son) throws Exception {
		boolean isdone = false;
		Element parentElement = null;

		DuplicateChildElementObject childElementObject = isChildElement(father, son);
		if (!childElementObject.isNeedDuplicate()) {
			// 节点相同不用合并
			isdone = true;
			parentElement = childElementObject.getElement();
		} else if (childElementObject.getElement() != null) {
			parentElement = childElementObject.getElement();
		} else {
			parentElement = father;
		}

		String sonName = son.getNodeName();
		Element subITEM = null;
		if (!isdone) {
			subITEM = docDup.createElement(sonName);
			// 复制节点的属性
			if (son.hasAttributes()) {
				NamedNodeMap attributes = son.getAttributes();
				for (int i = 0; i < attributes.getLength(); i++) {
					String attributeName = attributes.item(i).getNodeName();
					String attributeValue = attributes.item(i).getNodeValue();
					subITEM.setAttribute(attributeName, attributeValue);
				}
			}
			parentElement.appendChild(subITEM);
		} else {
			subITEM = parentElement;
		}

		// 复制子结点
		NodeList subMessageItems = son.getChildNodes();
		int subItemNumber = subMessageItems.getLength();
		if (subItemNumber < 2) {
			// 如果没有子节点,则返回
			
			//判断节点需不需要复制
			if(!isdone){
				//将节点值复制过去
				subITEM.setTextContent(son.getTextContent());
				subITEM.setNodeValue(son.getNodeValue());	
			}
			
		} else {
			for (int j = 1; j < subItemNumber; j = j + 2) {
				// 如果有子节点,则递归调用本方法
				Element subMessageItem = (Element) subMessageItems.item(j);
				isdone = dupliate(docDup, subITEM, subMessageItem);
			}
		}

		return isdone;
	}
	/**
	 * TODO:请添加注释(修改好了把TODO删除)lqh add.
	 * 
	 * @param doc
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	private static boolean writeTo(Document doc, String fileName) throws Exception {
		boolean isOver = false;
		DOMSource doms = new DOMSource(doc);
		File f = new File(fileName);
		StreamResult sr = new StreamResult(f);
		try {
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer t = tf.newTransformer();
			Properties properties = t.getOutputProperties();
			properties.setProperty(OutputKeys.ENCODING, "UTF-8");
			t.setOutputProperties(properties);
			t.transform(doms, sr);
			isOver = true;
		} catch (TransformerConfigurationException tce) {
			tce.printStackTrace();
		} catch (TransformerException te) {
			te.printStackTrace();
		}
		return isOver;
	}

	/**
	 * TODO:请添加注释(修改好了把TODO删除)lqh add.
	 * 
	 * @param father
	 * @param son
	 * @return
	 */
	private static DuplicateChildElementObject isChildElement(Element father, Element son) {

		DuplicateChildElementObject childElementObject = new DuplicateChildElementObject();

		NodeList messageItems = father.getChildNodes();
		int itemNumber = messageItems.getLength();
		// 首先遍历所有节点，查找是否有完全相同的节点，防止同一节点已定义多次
		for (int i = 1; i < itemNumber; i = i + 2) {
			Element messageItem = (Element) messageItems.item(i);
			if (!messageItem.getNodeName().equals(son.getNodeName())) {
				continue;
			}
			if (messageItem.isEqualNode(son)) {// 同时判断子节点是否一致
				childElementObject.setNeedDuplicate(false);
				childElementObject.setElement(messageItem);
				return childElementObject;
			}
		}
		for (int i = 1; i < itemNumber; i = i + 2) {
			Element messageItem = (Element) messageItems.item(i);
			// 判断节点是否处于同一级别
			if (!messageItem.getNodeName().equals(son.getNodeName())) {
				continue;
			}
			if (isEqualNode(messageItem, son)) {// 仅判断当前节点是否一致
				if (hasEqualAttributes(messageItem, son)) {// 当前节点完全相同不需要合并
					childElementObject.setNeedDuplicate(false);
					childElementObject.setElement(messageItem);
					return childElementObject;
				} /*else {// 当前节点的属性不相同，需要合并
				20160115 bug by cs 比较xml节点一致性问题，同节点下多条重名元素比较时，被比较元素只会跟重名元素的第一个元素比较，比较结果作为最后是否合并的结果，这个不对，需要与同节点下重名元素全部匹配比较后才能得到最终是否合并的结果。
					childElementObject.setNeedDuplicate(true);
					childElementObject.setElement(father);
					return childElementObject;
				}*/
			}
		}
		// 目标文档该节点不存在，需要合并到目标文档中
		childElementObject.setNeedDuplicate(true);
		childElementObject.setElement(father);
		return childElementObject;
	}

	/**
	 * 判断两个节点是否相同，未判断节点的属性.
	 * 
	 * @param arg0
	 * @param arg
	 * @return
	 */
	private static boolean isEqualNode(Node arg0, Node arg) {
		if (arg == arg0) {
			return true;
		}
		if (arg.getNodeType() != arg0.getNodeType()) {
			return false;
		}
		if (arg0.getNodeName() == null) {
			if (arg.getNodeName() != null) {
				return false;
			}
		} else if (!arg0.getNodeName().equals(arg.getNodeName())) {
			return false;
		}
		if (arg0.getLocalName() == null) {
			if (arg.getLocalName() != null) {
				return false;
			}
		} else if (!arg0.getLocalName().equals(arg.getLocalName())) {
			return false;
		}
		if (arg0.getNamespaceURI() == null) {
			if (arg.getNamespaceURI() != null) {
				return false;
			}
		} else if (!arg0.getNamespaceURI().equals(arg.getNamespaceURI())) {
			return false;
		}
		if (arg0.getPrefix() == null) {
			if (arg.getPrefix() != null) {
				return false;
			}
		} else if (!arg0.getPrefix().equals(arg.getPrefix())) {
			return false;
		}
		if (arg0.getNodeValue() == null) {
			if (arg.getNodeValue() != null) {
				return false;
			}
		} else if (!arg0.getNodeValue().equals(arg.getNodeValue())) {
			return false;
		}
		return true;
	}

	/**
	 * 判断节点的属性是否相同.
	 * 
	 * @param arg0
	 * @param arg
	 * @return
	 */
	private static boolean hasEqualAttributes(Node arg0, Node arg) {

		NamedNodeMap map1 = arg0.getAttributes();
		NamedNodeMap map2 = arg.getAttributes();
		int len = map1.getLength();
		if (len != map2.getLength()) {
			return false;
		}

		for (int i = 0; i < len; i++) {
			Node n1 = map1.item(i);
			if (n1.getNodeName() != null) {
				Node n2 = map2.getNamedItem(n1.getNodeName());
				if (n2 == null) {
					return false;
				} else if (!n1.getNodeValue().equals(n2.getNodeValue())) {
					return false;
				}
			}
		}
		return true;
	}

	public static void main(String[] args) {
		try {
			String sourcefile = "D:/file1.xml";
			String targetfile = "D:/file2.xml";

			boolean isdone = XmlMergeUtils.isMerging(sourcefile, targetfile);

			if (isdone) {
				LOGGER.debug("XML files have been merged.");
			} else {
				LOGGER.debug("XML files have NOT been merged.");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

/**
 * 复制子节点对象.
 * 
 * @author lj
 * 
 */
class DuplicateChildElementObject {
	private boolean needDuplicate = true;// 记录该节点是否需要复制
	private Element element = null;// 记录该节点的父节点

	public DuplicateChildElementObject() {
		super();
	}

	public boolean isNeedDuplicate() {
		return needDuplicate;
	}

	public void setNeedDuplicate(boolean needDuplicate) {
		this.needDuplicate = needDuplicate;
	}

	public Element getElement() {
		return element;
	}

	public void setElement(Element element) {
		this.element = element;
	}

}
