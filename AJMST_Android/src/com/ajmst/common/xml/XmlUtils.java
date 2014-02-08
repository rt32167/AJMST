package com.ajmst.common.xml;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXValidator;
import org.dom4j.io.XMLWriter;
import org.dom4j.util.XMLErrorHandler;

public class XmlUtils {
	static final String DEFAULT_ENCODING = "UTF-8";	//默认xml编码,不给定编码方式时采用默认编码方式来解析xml
	static final String DEFAULT_ROOT_START = "<?xml version=\"1.0\" encoding=\"utf-8\"?>";//默认xml根节点
	static final String DEFAULT_ROOT_NAME = "xml";//默认xml根节点
	/**
	 * 将指定路径的xml文件转换成字符串
	 * @author caijun 2013-11-1
	 * @param filePath
	 * @return
	 */
	public static String getXmlStr(String filePath) {
		String xmlString = null;
		if (filePath != null) {
			try {
				String tempR = readFile(filePath, null);
				String encode = getEncoding(tempR);
				xmlString = readFile(filePath, encode);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return xmlString;
	}
	
	@SuppressWarnings("rawtypes")
	public static String getXmlStr(Hashtable hashdata){
		return DEFAULT_ROOT_START + "<"+DEFAULT_ROOT_NAME+">" + hashToXmlStr(hashdata) + "</" + DEFAULT_ROOT_NAME + ">";
	}

	/**
	 * 将Xml字符串转换成XmlData,默认不进行schema验证
	 * @author caijun 2013-11-1
	 * @param xml
	 * @return
	 */
	public static XmlData getXmlData(String xml){
		return getXmlData(null, xml, false);
	}
	
	/**
	 * 将hashtable转换成字符串
	 * @author caijun 2013-11-1
	 * @param hashdata
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static String hashToXmlStr(Hashtable hashdata) {
		String resultStr = "";
		Enumeration<Object> keys = null;
		keys = hashdata.keys();
		while (keys.hasMoreElements()) {
			Object key = keys.nextElement();
			Object value = hashdata.get(key);

			if (value instanceof String
					|| (!(value instanceof java.util.List) && !(value instanceof java.util.Hashtable))) {
				resultStr += "<" + key + ">";
				resultStr += value.toString();
				resultStr += "</" + key + ">";
				resultStr += "\n";
			} else if (value instanceof java.util.List) {
				for (Object lvalue : (java.util.List) value) {
					if (lvalue instanceof String) {
						resultStr += "<" + key + ">";
						resultStr += lvalue.toString();
					} else {
						resultStr += "<" + key + ">\n";
						resultStr += hashToXmlStr((java.util.Hashtable) lvalue);
					}
					resultStr += "</" + key + ">";
					resultStr += "\n";
				}
			} else {
				resultStr += "<" + key + ">\n";
				resultStr += hashToXmlStr((java.util.Hashtable) value);
				resultStr += "</" + key + ">";
				resultStr += "\n";
			}
		}
		return resultStr;
	}
	
	/**
	 * this method is used to retrive data from xml file
	 * 
	 * @return XmlData,contains the source xml content
	 * @param none
	 */
	private static XmlData getXmlData(String xsd, String xml, boolean flag) {
		if (xml == null)
			return null;
		XmlData data = new XmlData();
		if (flag) {
			if (validateXmlByXSD(xsd, xml))// if the xml is validated by the
											// xsd,then perform retriving
			{
				try {
					Document xmlDocument = DocumentHelper.parseText(xml);
					Element root = xmlDocument.getRootElement();
					data = getHashtableFromParentNode(root);
					data.setRoot(root.getName());
				} catch (DocumentException e) {
					e.printStackTrace();
				}
			} else {
				throw new RuntimeException("The xml document is not-validate.");
			}
		} else {
			try {
				Document xmlDocument = DocumentHelper.parseText(xml);
				Element root = xmlDocument.getRootElement();
				data = getHashtableFromParentNode(root);
				data.setRoot(root.getName());
			} catch (DocumentException e) {
				e.printStackTrace();
			}
		}
		return data;
	}

	/**
	 * This function is used to validate the xml file after the xsd file and xml
	 * file has been appointed
	 * 
	 * @param xsd
	 * @param xml
	 * @return a boolean value, if return true, indicate the xml if legal,else
	 *         illegal xml document
	 * 
	 */
	private static boolean validateXmlByXSD(String xsd, String xml) {
		boolean flag = false;
		String xmlFileName = xml;
		String xsdFileName = xsd;
		if (xmlFileName == null || xsdFileName == null) {
			throw new RuntimeException("请指定需要验证的XML文件，以及标准的XSD文件！");
		}
		try {
			XMLErrorHandler errorHandler = new XMLErrorHandler();
			SAXParserFactory factory = SAXParserFactory.newInstance();
			factory.setValidating(true);
			factory.setNamespaceAware(true);
			SAXParser parser = factory.newSAXParser();
			Document xmlDocument = DocumentHelper.parseText(xmlFileName);
			parser.setProperty(
					"http://java.sun.com/xml/jaxp/properties/schemaLanguage",
					"http://www.w3.org/2001/XMLSchema");
			parser.setProperty(
					"http://java.sun.com/xml/jaxp/properties/schemaSource",
					xsdFileName);
			SAXValidator validator = new SAXValidator(parser.getXMLReader());
			validator.setErrorHandler(errorHandler);
			validator.validate(xmlDocument);

			XMLWriter writer = new XMLWriter(OutputFormat.createPrettyPrint());
			// if the content of the errorHandler is not empty, xml validate
			// failed, mark the flag to false
			if (errorHandler.getErrors().hasContent()) {
				System.out.println("XML文件通过XSD文件校验失败！");
				writer.write(errorHandler.getErrors());
				flag = false;
			} else {
				System.out.println("Good! XML文件通过XSD文件校验成功！");
				flag = true;
			}

		} catch (Exception ex) {
			System.out.println("XML文件: " + xmlFileName + " 通过XSD文件:"
					+ xsdFileName + "检验失败。\n原因： " + ex.getMessage());
			ex.printStackTrace();
		}
		return flag;
	}

	/**
	 * this method is used for get a hasgtable from the given node with the
	 * content of the node
	 * 
	 * @param root
	 *            , a xml node
	 * @return a hashtable with the content of the source xml
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static XmlData getHashtableFromParentNode(Element root) {
		Hashtable<String, ArrayList<Object>> result = new Hashtable<String, ArrayList<Object>>();// hold
																									// the
																									// results
		XmlData desData = new XmlData();
		Iterator<Element> it = root.elementIterator();// the root node
		while (it.hasNext()) {
			Element currentElement = it.next();
			String key = currentElement.getName();
			Object oldValue = result.get(key);// estimate if the value is
												// already in the hashtable
			Object newValue = null;// holds the
			if (currentElement.elements().size() != 0) {
				newValue = getHashtableFromParentNode(currentElement);
			} else {
				newValue = currentElement.getText();
			}

			if (oldValue != null) // the same kind node has already in the
									// hashtable
			{
				((ArrayList) oldValue).add(newValue);
			} else {
				ArrayList<Object> value = new ArrayList<Object>();
				value.add(newValue);
				result.put(key, value);
			}
		}
		desData.setData(result);
		desData.setRoot(root.getName());
		return desData;
	}

	@SuppressWarnings("resource")
	private static String readFile(String path, String encode)
			throws IOException {
		File file = new File(path);

		InputStream in = new FileInputStream(file);
		BufferedReader reader = null;
		if (encode != null)
			reader = new BufferedReader(new InputStreamReader(in, encode));
		else
			reader = new BufferedReader(new InputStreamReader(in));
		String line = reader.readLine();
		StringBuffer s = new StringBuffer();
		while (line != null) {
			s.append(line.trim());
			line = reader.readLine();
		}

		return s.toString();
	}

	/**
	 * 
	 * @author caijun 2013-11-1
	 * @param str
	 * @return
	 */
	private static String getEncoding(String str) {
		String encode = DEFAULT_ENCODING;
		try {
			if (str.equals(new String(str.getBytes(encode), encode))) {
				return encode;
			}
		} catch (Exception e) {
			
		}
		encode = "ISO-8859-1";
		try {
			if (str.equals(new String(str.getBytes(encode), encode))) {
				return encode;
			}
		} catch (Exception e) {

		}
		encode = "GB2312";
		try {
			if (str.equals(new String(str.getBytes(encode), encode))) {
				return encode;
			}
		} catch (Exception e) {

		}
		encode = "GBK";
		try {
			if (str.equals(new String(str.getBytes(encode), encode)))
				return encode;
		} catch (Exception e) {
		}
		
		return "";
	}
}
