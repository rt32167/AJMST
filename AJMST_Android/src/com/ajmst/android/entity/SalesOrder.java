package com.ajmst.android.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.j256.ormlite.field.DatabaseField;

public class SalesOrder implements java.io.Serializable{
	private static final long serialVersionUID = -3335276902611835197L;
	@DatabaseField(id=true)
	private String orderNo;
	@DatabaseField
	private Date createTime;
	@DatabaseField
	private Date finishTime;
	@DatabaseField
	private String creator;
	@DatabaseField
	private String customer;
	@DatabaseField
	private int state;//ORDER_STATE_UNFINISH,ORDER_STATE_FINISHED
	
	public final static int ORDER_STATE_UNFINISH = 1;//未完成
	public final static int ORDER_STATE_FINISHED = 2;//完成

	private List<SalesOrderItem> items;
	
	/**
	 * 提供无参构造函数,ormlite用
	 */
	public SalesOrder(){
		
	}
	
	public SalesOrder(String orderNo) {
		this.setOrderNo(orderNo);
		this.setCreateTime(new Date());
		this.setState(ORDER_STATE_UNFINISH);
	}

	public void addItem(AdvSpkfk sp,String pihao,Integer shl){
		this.addItem(sp, pihao, Double.valueOf(shl));
	}
	
	public void addItem(AdvSpkfk sp,String pihao,Double shl){
		if(this.getItems() == null){
			this.setItems(new ArrayList<SalesOrderItem>());
		}
		SalesOrderItem item = new SalesOrderItem(sp, pihao, shl);
		item.setOrderNo(this.getOrderNo());
		item.setCreateTime(new Date());
		item.setSeq(this.getItems().size() + 1);
		this.getItems().add(item);
	}
	
	public void deleteItem(SalesOrderItem item){
		if(this.getItems() != null){
			this.getItems().remove(item);
		}
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getCustomer() {
		return customer;
	}

	public void setCustomer(String customer) {
		this.customer = customer;
	}

	public List<SalesOrderItem> getItems() {
		return items;
	}

	public void setItems(List<SalesOrderItem> items) {
		this.items = items;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public Date getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(Date finishTime) {
		this.finishTime = finishTime;
	}

	
}
