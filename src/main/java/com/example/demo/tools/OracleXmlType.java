package com.example.demo.tools;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import oracle.sql.OPAQUE;
import oracle.xdb.XMLType;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.UserType;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * This is a generic implementation of XmlType for use with Hibernate 3.0 or >
 * 
 * This will work with Document objects > 4k. (I tested with one that is 124k)
 * 
 * To use this class you need to have your Hibernate Object have the selected object type as org.w3c.dom.Document in the
 * java class and then have the type in the hbm.xml defined as OracleXmlType
 * 
 * Example:
 * 
 * <code>
 * public class SomeClass {
 *         private Document xml;
 *         
 *         public Document getXml() { return xml; }
 *         public void setXml(Document _xml) { xml = _xml; }
 * }
 * 
 * SomeClass.hbm.xml: 
 * <property name="xml" column="XML" not-null="true" 
 *         type="mypackage.OracleXmlType"/>
 * 
 * </code>
 * 
 * 
 * Oracle jars required: xdb.jar xmlparserv2.jar (Used by XmlType).
 * 
 * 
 * @author sseaman
 */
public class OracleXmlType implements UserType, Serializable {

	private static final long serialVersionUID = 2308230823023L;
	@SuppressWarnings("rawtypes")
	private static final Class RETURNDCLASS = Document.class;
	private static final int[] SQL_TYPES = { oracle.xdb.XMLType._SQL_TYPECODE };

	@Override
	public int[] sqlTypes() {
		return SQL_TYPES;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Class returnedClass() {
		return RETURNDCLASS;
	}

	@Override
	public int hashCode(Object obj) {
		return obj.hashCode();
	}

	@Override
	public Object assemble(Serializable cached, Object owner) throws HibernateException {
		try {
			return OracleXmlType.stringToDom((String) cached);
		} catch (Exception e) {
			throw new HibernateException("Could not assemble String to Document", e);
		}
	}

	@Override
	public Serializable disassemble(Object obj) throws HibernateException {
		try {
			return OracleXmlType.domToString((Document) obj);
		} catch (Exception e) {
			throw new HibernateException("Could not disassemble Document to Serializable", e);
		}
	}

	@Override
	public Object replace(Object orig, Object tar, Object owner) {
		return deepCopy(orig);
	}

	@Override
	public boolean equals(Object arg0, Object arg1) throws HibernateException {
		if (arg0 == null && arg1 == null) {
			return true;
		} else if (arg0 == null && arg1 != null) {
			return false;
		} else {
			try {
				return OracleXmlType.domToString((Document) arg0).equals(OracleXmlType.domToString((Document) arg1));
			} catch (TransformerException e) {
				return false;
			}
		}
	}

	@Override
	public Object nullSafeGet(ResultSet rs, String[] names, SharedSessionContractImplementor session, Object arg2) throws HibernateException, SQLException {

		// XMLType xmlType = (XMLType) rs.getObject(names[0]);
		// return xmlType != null ? xmlType.getDocument() : null;

		// 如果是linux使用xmltype，则需要使用如下代码
		XMLType xmlType = null;
		OPAQUE opaque = (OPAQUE) rs.getObject(names[0]);
		if (opaque == null) {
			return null;
		}
		if (opaque instanceof XMLType) {
			xmlType = (XMLType) opaque;
		} else {
			xmlType = XMLType.createXML(opaque);
		}
		return xmlType != null ? xmlType.getDocument() : null;
	}

	@Override
	public void nullSafeSet(PreparedStatement st, Object value, int index, SharedSessionContractImplementor session) throws HibernateException, SQLException {
		OracleNativeExtractor extrator = new OracleNativeExtractor();
		Connection nativeConn = extrator.getNativeConnection(st.getConnection());

		try {
			XMLType xmlType = null;
			if (value != null) {
				xmlType = new oracle.xdb.XMLType(nativeConn, OracleXmlType.domToString((Document) value));
			}
			st.setObject(index, xmlType);
		} catch (Exception e) {
			throw new SQLException("Could not covert Document to String for storage");
		}
	}

	@Override
	public Object deepCopy(Object value) throws HibernateException {
		if (value == null) {
			return null;
		}

		return ((Document) value).cloneNode(true);
	}

	@Override
	public boolean isMutable() {
		return false;
	}

	protected static String domToString(Document document) throws TransformerException {
		TransformerFactory tFactory = TransformerFactory.newInstance();
		Transformer transformer = tFactory.newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		DOMSource source = new DOMSource(document);
		StringWriter sw = new StringWriter();
		StreamResult result = new StreamResult(sw);
		transformer.transform(source, result);
		return sw.toString();
	}

	protected static Document stringToDom(String xmlSource) throws SAXException, ParserConfigurationException,
			IOException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		return builder.parse(new ByteArrayInputStream(xmlSource.getBytes("UTF-8")));
	}

}
