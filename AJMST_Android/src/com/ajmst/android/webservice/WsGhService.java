package com.ajmst.android.webservice;

import java.util.Hashtable;
import java.util.List;

import android.util.Log;

import com.ajmst.commmon.entity.AjmstGh;
import com.ajmst.common.constants.IWebServiceName;
import com.ajmst.common.exception.ExceptionUtil;
import com.ajmst.common.xml.XmlBeanUtils;

public class WsGhService {
	final static int MAX_FETCH_SIZE = 200;
	final static String LOG_TAG = WsGhService.class.getSimpleName();
	
	@SuppressWarnings("unchecked")
	public static WsResponse getGhs(int sIdx, int size){
		WsResponse r = new WsResponse();
		Hashtable<String,Object> data = new Hashtable<String,Object>();
		data.put("StartIndex", sIdx);
		data.put("Size", size);
		Log.i(LOG_TAG, "��ʼ��ȡ�� " + sIdx + " �� " + (sIdx + size) + " ��");
		r = WsRequest.call(IWebServiceName.SERVICE_AJMST_GH_QUERY,data);
		if(r.isOk()){
			Log.i(LOG_TAG, "���óɹ�");
			String results[] = (String[]) r.getResult();
			String result = results[0];
			if ("1".endsWith(result)) {
				String xml = results[2];
				Log.i(LOG_TAG, "��ʼת��xml������");
				List<AjmstGh> ghs = (List<AjmstGh>)XmlBeanUtils.fromXml(xml);
				Log.i(LOG_TAG, "ת�����");
				r.setResult(ghs);
			}else{
				String desc = results[1];
				String info = "���óɹ�,������ʧ��ԭ��:" + desc;
				r.setIsOk(false);
				r.setException(new Exception(info));
			}
		}else{
			String info = "����ʧ��,��������ͷ�����:"
					+ ExceptionUtil.getStackTrace(r.getException());
			r.setException(new Exception(info));
		}
		return r;
	}
}
