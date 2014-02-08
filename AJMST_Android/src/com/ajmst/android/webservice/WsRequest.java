package com.ajmst.android.webservice;

import java.util.Hashtable;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.ajmst.common.exception.ExceptionUtil;
import com.ajmst.common.xml.XmlUtils;

public class WsRequest {
	private final static String NAME_SPACE = "http://server.webservice.ajmst.com/";
	private final static String SERVICE_ENDPOINT = "http://192.168.1.195:8080/AJMST_Server/ws/msgService";
	private final static String SERVICE_MOTHED = "request";
	
	@SuppressWarnings("rawtypes")
	public static WsResponse call(String serviceName,Hashtable request) {
		WsResponse response = null;
		String requestXml = null;
		try{
			requestXml = XmlUtils.getXmlStr(request);
		}catch (Exception e) {
			response = new WsResponse();
			response.setIsOk(false);
			response.setException(new Exception("转换请求为xml失败:" + ExceptionUtil.getStackTrace(e)));
		}
		if(requestXml != null){
			response = call(serviceName,requestXml);
		}
		return response;
/*		WsResponse response = new WsResponse();
		try {
			String requestXml = XmlUtils.getXmlStr(request);
			// 创建httpTransportSE传输对象
			HttpTransportSE ht = new HttpTransportSE(SERVICE_ENDPOINT);
			ht.debug = true;
			// 使用soap1.1协议创建Envelop对象
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			// 实例化SoapObject对象
			SoapObject soapRequest = new SoapObject(NAME_SPACE, SERVICE_MOTHED);
			soapRequest.addProperty("arg0", requestXml);
			soapRequest.addProperty("arg1", requestXml);
			// 将SoapObject对象设置为SoapSerializationEnvelope对象的传出SOAP消息
			envelope.bodyOut = soapRequest;
			// 调用webService
			ht.call(null, envelope);
			// txt1.setText("看看"+envelope.getResponse());
			if (envelope.getResponse() != null) {
				SoapObject bodyIn = (SoapObject) envelope.bodyIn;
				String[] result = new String[3];
				result[0] = bodyIn.getPropertyAsString(0);
				result[1] = bodyIn.getPropertyAsString(1);
				result[2] = bodyIn.getPropertyAsString(2);
				response.setResult(result);
			}
		} catch (Exception e) {
			response.setException(e);
		}
		return response;*/
	}
	
	public static WsResponse call(String serviceName,String request) {
		WsResponse response = new WsResponse();
		try {
			// 创建httpTransportSE传输对象
			//new HttpTransportSE(SERVICE_ENDPOINT);
			HttpTransportSE ht = new HttpTransportSE(SERVICE_ENDPOINT,60000);//设置超时时间为60秒
			ht.debug = true;
			// 使用soap1.1协议创建Envelop对象
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			// 实例化SoapObject对象
			SoapObject soapRequest = new SoapObject(NAME_SPACE, SERVICE_MOTHED);
			soapRequest.addProperty("arg0", serviceName);
			soapRequest.addProperty("arg1", request);
			
			// 将SoapObject对象设置为SoapSerializationEnvelope对象的传出SOAP消息
			envelope.bodyOut = soapRequest;
			
			// 调用webService
			ht.call(null, envelope);
			// txt1.setText("看看"+envelope.getResponse());
			if (envelope.getResponse() != null) {
				SoapObject bodyIn = (SoapObject) envelope.bodyIn;
				String[] result = new String[3];
				result[0] = bodyIn.getPropertyAsString(0);
				result[1] = bodyIn.getPropertyAsString(1);
				result[2] = bodyIn.getPropertyAsString(2);
				response.setResult(result);
			}
		
		} catch (Exception e) {
			response.setException(e);
		}
		return response;
	}
}
