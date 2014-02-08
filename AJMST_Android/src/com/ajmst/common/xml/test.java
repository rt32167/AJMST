package com.ajmst.common.xml;


public class test {

	/**
	 * @author caijun 2013-11-1
	 * @param args
	 */
	public static void main(String[] args) {
		
/*		String xml = XmlUtils.getXmlStr("C:\\caijun\\doc\\HZTobacco\\test\\过程评测\\测试数据\\HYP000009528.xml");
		System.out.println(xml);*/
		String xml = "<d>dsss</d>";
		XmlData xmlData = XmlUtils.getXmlData(xml);
		System.out.println(xmlData);
/*		Hashtable data = new Hashtable();
		data.put("d1", "a");
		data.put("d2", "b");
		List multData = new ArrayList();
		Hashtable subData = new Hashtable();
		subData.put("d3.1", "c1");
		subData.put("d3.2", "c2");
		multData.add(subData);
		data.put("d3", subData);
		List subMultData = new ArrayList();
		Hashtable subSubData = new Hashtable();
		subSubData.put("d3.3.1", "c11");
		subSubData.put("d3.3.2", "c22");
		subMultData.add(subSubData);
		subData.put("d3.3", subMultData);
		xml = XmlUtils.getXmlStr(data);
		System.out.println(xml);
		
		XmlData xmlData = XmlUtils.getXmlData(xml);
		System.out.println(xmlData);
		
		Object value = xmlData.getValue("d2");
		System.out.println(value);*/
		
	}

}
