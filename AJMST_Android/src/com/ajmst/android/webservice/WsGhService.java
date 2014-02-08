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
		Log.i(LOG_TAG, "开始获取第 " + sIdx + " 到 " + (sIdx + size) + " 个");
		r = WsRequest.call(IWebServiceName.SERVICE_AJMST_GH_QUERY,data);
		if(r.isOk()){
			Log.i(LOG_TAG, "调用成功");
			String results[] = (String[]) r.getResult();
			String result = results[0];
			if ("1".endsWith(result)) {
				String xml = results[2];
				Log.i(LOG_TAG, "开始转换xml到对象");
				List<AjmstGh> ghs = (List<AjmstGh>)XmlBeanUtils.fromXml(xml);
				Log.i(LOG_TAG, "转换完成");
				r.setResult(ghs);
			}else{
				String desc = results[1];
				String info = "调用成功,但返回失败原因:" + desc;
				r.setIsOk(false);
				r.setException(new Exception(info));
			}
		}else{
			String info = "调用失败,请检查网络和服务器:"
					+ ExceptionUtil.getStackTrace(r.getException());
			r.setException(new Exception(info));
		}
		return r;
	}
}
