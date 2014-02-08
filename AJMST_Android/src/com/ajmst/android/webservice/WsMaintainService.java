package com.ajmst.android.webservice;

import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import com.ajmst.android.util.DateTimeUtils;
import com.ajmst.commmon.entity.AjmstMaintain;
import com.ajmst.common.constants.IWebServiceName;
import com.ajmst.common.xml.XmlBeanUtils;

public class WsMaintainService {
	final static int MAX_FETCH_SIZE = 200;
	
	@SuppressWarnings("unchecked")
	public static WsResponse getMaintain(Date date){
		List<AjmstMaintain> maintain = new ArrayList<AjmstMaintain>();
		int sIdx = 0;
		WsResponse response = new WsResponse();
		while(true){
			Hashtable<String,Object> data = new Hashtable<String,Object>();
			//data.put("ServiceName", IWebServiceName.SERVICE_SPKFK_QUERY_MAINTAIN);
			data.put("StartIndex", sIdx);
			data.put("Size", MAX_FETCH_SIZE);
			data.put("MainDate", DateTimeUtils.formatDate(date, "yyyy-MM-dd"));
			response = WsRequest.call(IWebServiceName.SERVICE_SPKFK_QUERY_MAINTAIN,data);
			if(response.isOk() != true){
				break;
			}else{
				String results[] = (String[]) response.getResult();
				String result = results[0];
				if ("1".endsWith(result)) {
					String xml = results[2];
					List<AjmstMaintain> subMaintain = (List<AjmstMaintain>)XmlBeanUtils.fromXml(xml);
					maintain.addAll(subMaintain);
					sIdx = sIdx + MAX_FETCH_SIZE;
					//��ʵ�ʻ�ȡ�ĸ������������С,˵���Ѿ�ȡ��ĩβ,����Ҫ��ȡ��һ��
					if(subMaintain.size() < MAX_FETCH_SIZE){
						response.setResult(maintain);
						break;
					}
				}else{
					String desc = results[1];
					String info = "���óɹ�,������ʧ��ԭ��:" + desc;
					response.setIsOk(false);
					response.setException(new Exception(info));
					break;
				}
			}
		}
		return response;
	}
	
	/**
	 * �ϴ���������
	 * @author caijun 2013-12-5
	 * @param maintain
	 * @return
	 */
	public static WsResponse uploadMaintain(List<AjmstMaintain> maintain){
		String xml = XmlBeanUtils.toXml(maintain);
		//ֱ���ϴ�xml,�����ٸ�hashtable
		WsResponse response = WsRequest.call(IWebServiceName.SERVICE_SPKFK_UPLOAD_MAINTAIN,xml);
		return response;
	}
}
