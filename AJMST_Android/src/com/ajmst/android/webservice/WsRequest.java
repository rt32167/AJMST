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
			response.setException(new Exception("ת������Ϊxmlʧ��:" + ExceptionUtil.getStackTrace(e)));
		}
		if(requestXml != null){
			response = call(serviceName,requestXml);
		}
		return response;
/*		WsResponse response = new WsResponse();
		try {
			String requestXml = XmlUtils.getXmlStr(request);
			// ����httpTransportSE�������
			HttpTransportSE ht = new HttpTransportSE(SERVICE_ENDPOINT);
			ht.debug = true;
			// ʹ��soap1.1Э�鴴��Envelop����
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			// ʵ����SoapObject����
			SoapObject soapRequest = new SoapObject(NAME_SPACE, SERVICE_MOTHED);
			soapRequest.addProperty("arg0", requestXml);
			soapRequest.addProperty("arg1", requestXml);
			// ��SoapObject��������ΪSoapSerializationEnvelope����Ĵ���SOAP��Ϣ
			envelope.bodyOut = soapRequest;
			// ����webService
			ht.call(null, envelope);
			// txt1.setText("����"+envelope.getResponse());
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
			// ����httpTransportSE�������
			//new HttpTransportSE(SERVICE_ENDPOINT);
			HttpTransportSE ht = new HttpTransportSE(SERVICE_ENDPOINT,60000);//���ó�ʱʱ��Ϊ60��
			ht.debug = true;
			// ʹ��soap1.1Э�鴴��Envelop����
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			// ʵ����SoapObject����
			SoapObject soapRequest = new SoapObject(NAME_SPACE, SERVICE_MOTHED);
			soapRequest.addProperty("arg0", serviceName);
			soapRequest.addProperty("arg1", request);
			
			// ��SoapObject��������ΪSoapSerializationEnvelope����Ĵ���SOAP��Ϣ
			envelope.bodyOut = soapRequest;
			
			// ����webService
			ht.call(null, envelope);
			// txt1.setText("����"+envelope.getResponse());
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
