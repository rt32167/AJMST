package com.ajmst.android.entity;

import java.util.Date;

import com.j256.ormlite.field.DatabaseField;

public class MsgQueue implements java.io.Serializable{
	//TODO �ϴ����� �ϴ���Ʒ����(���롢�۸񡢹��)
	@DatabaseField(generatedId=true)
	private Integer id;
	@DatabaseField
	private String serviceName;
	@DatabaseField
	private int state;
	@DatabaseField
	private Date createTime;
	@DatabaseField
	private Date startSendTime;
	@DatabaseField
	private Date finishSendTime;
	@DatabaseField
	private int failCount;
	@DatabaseField
	private String data;//xml�ļ��ַ���,SQLite���õ��Ƕ�̬��������
	@DatabaseField
	private String lastFailReason;
	
	
	public final static int MSG_QUEUE_STATE_NOT_SEND = 1;//δ����
	public final static int MSG_QUEUE_STATE_SENDING = 2;//���ڷ���
	public final static int MSG_QUEUE_STATE_SUCEESS = 3;//���ͳɹ�
	public final static int MSG_QUEUE_STATE_FAILED = 4;//����ʧ��
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getStartSendTime() {
		return startSendTime;
	}
	public void setStartSendTime(Date startSendTime) {
		this.startSendTime = startSendTime;
	}
	public Date getFinishSendTime() {
		return finishSendTime;
	}
	public void setFinishSendTime(Date finishSendTime) {
		this.finishSendTime = finishSendTime;
	}
	public int getFailCount() {
		return failCount;
	}
	public void setFailCount(int failCount) {
		this.failCount = failCount;
	}
	public String getLastFailReason() {
		return lastFailReason;
	}
	public void setLastFailReason(String lastFailReason) {
		this.lastFailReason = lastFailReason;
	}
	
}
