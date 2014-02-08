package com.ajmst.common.xml;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * class：       	XmlActivity
 * version：     	1.1
 * date：       		2010/08/18
 * description： This class is used to hold the data from the xml file
 * author：      	Ma Lipeng
 * */
public class XmlData
{
	private String root;
	
	private Hashtable<String, ArrayList<Object>> data;//data field ,hold the xml data
	
	/**
	 * Default Constructor
	 */
	public XmlData()
	{
		
	}
	/**
	 * Constructor
	 * @param data construct the XmlData with the given data 
	 */
	public XmlData(Hashtable<String, ArrayList<Object>> data)
	{
		super();
		this.data = data;
	}
	
	public XmlData(String root, Hashtable<String, ArrayList<Object>> data) 
	{
		super();
		this.root = root;
		this.data = data;
	}
	/**
	 * get the value by given key
	 * @param key the key of the hashtable
	 * @return
	 */
	public ArrayList<Object> get(String key)
	{
		return data.get(key);
	}
	
	public void put(String key, Object value)
	{
		ArrayList<Object> v = data.get(key);
		if(v != null)
		{
			v.add(value);
		}
		else
		{
			ArrayList<Object> temp = new ArrayList<Object>();
			temp.add(value);
			data.put(key, temp);
		}
	}
	
	/**
	 * get the value of the  corresponding key
	 * @param key
	 * @return the value of the specific key
	 */
	public Object getValue(String key)
	{
		ArrayList<Object> value = data.get(key);
		if(value != null)
		{
			if(value.size() == 1)
			{
				return value.get(0); //hashtable or string
			}
			return value;
		}
		return null;
	}
	
	/**
	 * 在方法getValue返回值的基础上将其转换为字符串
	 * @author caijun 2013-11-1
	 * @param key
	 * @return
	 */
	public String getValueStr(String key){
		Object value = getValue(key);
		if(value != null){
			return value.toString().trim();
		}
		return null;
	}
	/**
	 * get the XML data
	 * @return the return type is a Hashtable<String,ArrayList<Object>>
	 */
	public Hashtable<String, ArrayList<Object>> getData()
	{
		return data;
	}

	/**
	 * it is a function provide to set the XML data field by a given Hashtable
	 * @param result set the data field to data
	 */
	public void setData(Hashtable<String, ArrayList<Object>> data) 
	{
		this.data = data;
	}
	public String getRoot() {
		return root;
	}
	public void setRoot(String root) {
		this.root = root;
	}
	
}
