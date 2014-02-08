package com.ajmst.android.service;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import android.content.Context;
import android.util.Log;

import com.ajmst.android.entity.MsgQueue;
import com.ajmst.android.entity.SalesOrder;
import com.ajmst.android.webservice.WsRequest;
import com.ajmst.android.webservice.WsResponse;
import com.ajmst.commmon.util.DateTimeUtils;
import com.ajmst.common.constants.IWebServiceName;
import com.ajmst.common.exception.ExceptionUtil;
import com.ajmst.common.response.Response;
import com.ajmst.common.xml.XmlUtils;

public class MsgQueueService extends BaseService<MsgQueue>{
	String TAG = MsgQueueService.class.getSimpleName();
	private final static int MAX_SEND_NUM_PER_TIME = 10;//每次最大发送消息数
	
	
	public MsgQueueService(Context context) {
		super(context);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Response saveOrUpdate(MsgQueue msg) {
		Response r = new Response();
		try {
			MsgQueue msgExist = null;
			if(msg.getId() != null){
				msgExist = (MsgQueue) this.getDao().queryForId(msg.getId());//注意queryForId不能给null来查询,会报错
			}
			if (msgExist != null) {
				this.getDao().update(msg);
			} else {
				this.getDao().create(msg);
			}
		} catch (SQLException e) {
			r.setIsOk(false);
			r.setException(e);
		}
		return r;
	}
	
	@SuppressWarnings("unchecked")
	public List<MsgQueue> getSendableMsgs(){
		List<MsgQueue> msgs = new ArrayList<MsgQueue>();
		try {
			//msgs = (List<MsgQueue>)this.getDao().queryBuilder().orderBy("createTime", true).where().raw("state in(" + MsgQueue.MSG_QUEUE_STATE_NOT_SEND + ","+ MsgQueue.MSG_QUEUE_STATE_FAILED + ")").query();
			//正在发送的也可以再次发送,因为发送时用户可能退出程序,消息会停留在正在发送状态,发送的服务就一个,不会出现同时发送一个消息的情况
			msgs = (List<MsgQueue>)this.getDao().queryBuilder().orderBy("createTime", true).where().raw("state in(" + MsgQueue.MSG_QUEUE_STATE_NOT_SEND + ","+ MsgQueue.MSG_QUEUE_STATE_FAILED + ","+ MsgQueue.MSG_QUEUE_STATE_SENDING + ")").query();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return msgs;
	}
	
	
	public void sendMsgs(){
		Set<String> failTypes = new HashSet<String>();
		int sendCount = 0;
		List<MsgQueue> msgs = getSendableMsgs();
		for(MsgQueue msg: msgs){
			//相同类型的消息若已经失败,不能发其他的,保证相同类型的消息一定按照创建先后顺序发送
			String serviceName = msg.getServiceName();
			if(failTypes.contains(serviceName)){
				continue;
			}
			//超过每次最大发送数量,停止发送;注意同类型消息失败后,不计入发送次数
			if(sendCount > MAX_SEND_NUM_PER_TIME){
				break;
			}
			//标记为正在发送
			sendCount++;
			msg.setState(MsgQueue.MSG_QUEUE_STATE_SENDING);
			msg.setStartSendTime(new Date());
			saveOrUpdate(msg);
			//开始发送
			
			String xml = msg.getData();
			Log.i(TAG, "开始发送消息,service name:" + serviceName + ",data:\n" + xml);
			
			WsResponse r = WsRequest.call(serviceName, xml);
			if(r.isOk()){
				Log.i(TAG, "发送成功");
				msg.setState(MsgQueue.MSG_QUEUE_STATE_SUCEESS);
			}else{
				String failReason = ExceptionUtil.getStackTrace(r.getException());
				failTypes.add(serviceName);
				msg.setFailCount(msg.getFailCount() + 1);
				msg.setState(MsgQueue.MSG_QUEUE_STATE_FAILED);
				msg.setLastFailReason(failReason);
				Log.i(TAG, "发送失败,累计失败次数:" + msg.getFailCount() + ",原因:" + msg.getLastFailReason());
			}
			msg.setFinishSendTime(new Date());
			saveOrUpdate(msg);
		}
/*		Thread sendThread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(true){
					try{
						if(preSendTime != null && DateTimeUtils.getSeconds(preSendTime, new Date()) < SEND_TIME_GAP_SEC){
							Thread.sleep(1000);
						}
						
					}catch(Exception e){
						
					}
				}
			}
		});
		sendThread.start();*/
	}
	
	/**
	 * 创建修改零售价的消息
	 * @author caijun 2014-1-26
	 * @param spid 商品ID
	 * @param lshj 零售价
	 * @return
	 */
	public Response createMsg_Lshj(String spid,BigDecimal lshj){
		MsgQueue msg = new MsgQueue();
		msg.setServiceName(IWebServiceName.SERVICE_SPKFK_PRICE);
		msg.setCreateTime(new Date());
		msg.setState(MsgQueue.MSG_QUEUE_STATE_NOT_SEND);
		Hashtable data = new Hashtable();
		data.put("Spid", spid);
		data.put("Lshj", lshj);
		msg.setData(XmlUtils.getXmlStr(data));
		return this.saveOrUpdate(msg);
	}
}
