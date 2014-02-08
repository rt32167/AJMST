package com.ajmst.android.webservice;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import android.util.Log;

import com.ajmst.android.util.DateTimeUtils;
import com.ajmst.commmon.entity.AjmstMaintain;
import com.ajmst.commmon.entity.Spkfk;
import com.ajmst.common.constants.IWebServiceName;
import com.ajmst.common.exception.ExceptionUtil;
import com.ajmst.common.xml.XmlBeanUtils;
import com.ajmst.common.xml.XmlData;
import com.ajmst.common.xml.XmlUtils;

public class WsSpkfkService {
	final static int MAX_FETCH_SIZE = 500;
	final static String LOG_TAG = WsSpkfkService.class.getSimpleName();
	
	@SuppressWarnings("unchecked")
	public static WsResponse getChinseSpkfk(int sIdx, int size) {
		Hashtable<String,Object> request = new Hashtable<String,Object>();
//		request.put("ServiceName",
//				IWebServiceName.SERVICE_SPKFK_QUERY_CHINESE_MEDICINE);
		request.put("StartIndex", sIdx);
		request.put("Size", size);

		WsResponse response = WsRequest.call(IWebServiceName.SERVICE_SPKFK_QUERY_CHINESE_MEDICINE,request);
		if (response.isOk()) {
			String results[] = (String[]) response.getResult();
			String result = results[0];
			if ("1".endsWith(result)) {
				String xml = results[2];
				response.setResult((List<Spkfk>) XmlBeanUtils.fromXml(xml));
			} else {
				String desc = results[1];
				String info = "调用成功,但返回失败原因:" + desc;
				response.setIsOk(false);
				response.setException(new Exception(info));
			}
		} else {
			String info = "调用失败,请检查网络和服务器:"
					+ ExceptionUtil.getStackTrace(response.getException());
			response.setException(new Exception(info));
		}
		return response;
	}
	
	/**
	 * 获取所有的重要资料,由于数据较大,分段获取后再汇总
	 * @author caijun 2013-12-2
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static WsResponse getChinseSpkfk() {
		List<Spkfk> spkfk = new ArrayList<Spkfk>();
		int sIdx = 0;
		final int fechSize = 600;
		WsResponse response = new WsResponse();
		while(true){
			response = getChinseSpkfk(sIdx, fechSize);
			if(response.isOk() != true){
				break;
			}else{
				List<Spkfk> subSpkfk = (List<Spkfk>)response.getResult();
				spkfk.addAll(subSpkfk);
				sIdx = sIdx + fechSize;
				//若实际获取的个数比请求个数小,说明已经取到末尾,不需要再取下一次
				if(subSpkfk.size() < fechSize){
					response.setResult(spkfk);
					break;
				}
			}
		}
		return response;
	}

	public static WsResponse changePrice(String spid, BigDecimal lshj) {
		WsResponse r = new WsResponse();
		Hashtable<String,Object> data = new Hashtable<String,Object>();
		//data.put("ServiceName", IWebServiceName.SERVICE_SPKFK_PRICE);
		data.put("Spid", spid);
		data.put("Lshj", lshj);
		r = WsRequest.call(IWebServiceName.SERVICE_SPKFK_PRICE,data);
		return r;
	}
	
	public static WsResponse getSpkfks(int sIdx, int size){
		WsResponse r = new WsResponse();
		Hashtable<String,Object> data = new Hashtable<String,Object>();
		data.put("StartIndex", sIdx);
		data.put("Size", size);
		Log.i(LOG_TAG, "开始获取第 " + sIdx + " 到 " + (sIdx + size) + " 个");
		r = WsRequest.call(IWebServiceName.SERVICE_SPKFK_QUERY,data);
		if(r.isOk()){
			Log.i(LOG_TAG, "调用成功");
			String results[] = (String[]) r.getResult();
			String result = results[0];
			if ("1".endsWith(result)) {
				String xml = results[2];
				Log.i(LOG_TAG, "开始转换xml到对象");
				List<Spkfk> sps = (List<Spkfk>)XmlBeanUtils.fromXml(xml);
				Log.i(LOG_TAG, "转换完成");
				r.setResult(sps);
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
	
	public static WsResponse getSpkfkSizeOfActive() {
		WsResponse response = new WsResponse();
		Hashtable<String,Object> data = new Hashtable<String,Object>();
		//data.put("ServiceName", IWebServiceName.SERVICE_SPKFK_PRICE);
		data.put("Active", "true");
		response = WsRequest.call(IWebServiceName.SERVICE_SPKFK_SIZE,data);
		if (response.isOk()) {
			String results[] = (String[]) response.getResult();
			String result = results[0];
			if ("1".endsWith(result)) {
				String xml = results[2];
				XmlData xmlData = XmlUtils.getXmlData(xml);
				Integer size = Integer.valueOf(xmlData.getValueStr("Size"));
				response.setResult(size);
			} else {
				String desc = results[1];
				String info = "调用成功,但返回失败原因:" + desc;
				response.setIsOk(false);
				response.setException(new Exception(info));
			}
		} else {
			String info = "调用失败,请检查网络和服务器:"
					+ ExceptionUtil.getStackTrace(response.getException());
			response.setException(new Exception(info));
		}
		return response;
	}
}
