package com.ajmst.common.xml;

import com.thoughtworks.xstream.XStream;

/**
 * 
 * @author caijun created 2013-11-20
 */
public class XmlBeanUtils {
	public static String toXml(Object obj) {
		XStream xstream = new XStream();
		String xml = xstream.toXML(obj);
		return xml;
	}

	public static Object fromXml(String xml) {
		XStream xstream = new XStream();
		Object obj = xstream.fromXML(xml);
		return obj;
	}
}
