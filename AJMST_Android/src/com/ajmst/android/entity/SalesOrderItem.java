package com.ajmst.android.entity;

import java.util.Date;

import com.j256.ormlite.field.DatabaseField;

public class SalesOrderItem implements java.io.Serializable{
	private static final long serialVersionUID = 4023214077706263185L;
	@DatabaseField(generatedId=true)
	private int id;
	@DatabaseField
	private String orderNo;
	@DatabaseField
	private int seq;
	@DatabaseField
	private Date createTime;
	@DatabaseField
	private String spid;
	@DatabaseField
	private String spbh;
	@DatabaseField
	private String spmch;
	@DatabaseField
	private String pihao;
	@DatabaseField
	private String shengccj;
	@DatabaseField
	private String shpgg;
	@DatabaseField
	private String dw;
	@DatabaseField
	private Double shl;
	@DatabaseField
	private Double lshj;
	
	
	/**
	 * 提供无参构造函数,ormlite用
	 */
	public SalesOrderItem() {
	}

	public SalesOrderItem(AdvSpkfk sp,String pihao,Double shl){
		this.setSpid(sp.getSpid());
		this.setSpbh(sp.getSpbh());
		this.setSpmch(sp.getSpmch());
		this.setShengccj(sp.getShengccj());
		this.setShpgg(sp.getShpgg());
		this.setDw(sp.getDw());
		if(sp.getLshj() != null){
			this.setLshj(sp.getLshj().doubleValue());
		}

		this.setPihao(pihao);
		this.setShl(shl);
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public int getSeq() {
		return seq;
	}
	public void setSeq(int seq) {
		this.seq = seq;
	}
	public String getSpid() {
		return spid;
	}
	public void setSpid(String spid) {
		this.spid = spid;
	}
	public String getSpbh() {
		return spbh;
	}
	public void setSpbh(String spbh) {
		this.spbh = spbh;
	}
	public String getSpmch() {
		return spmch;
	}
	public void setSpmch(String spmch) {
		this.spmch = spmch;
	}
	public String getPihao() {
		return pihao;
	}
	public void setPihao(String pihao) {
		this.pihao = pihao;
	}
	public Double getShl() {
		return shl;
	}
	public void setShl(Double shl) {
		this.shl = shl;
	}
	public Double getLshj() {
		return lshj;
	}
	public void setLshj(Double lshj) {
		this.lshj = lshj;
	}
	public String getShengccj() {
		return shengccj;
	}
	public void setShengccj(String shengccj) {
		this.shengccj = shengccj;
	}
	public String getShpgg() {
		return shpgg;
	}
	public void setShpgg(String shpgg) {
		this.shpgg = shpgg;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getDw() {
		return dw;
	}

	public void setDw(String dw) {
		this.dw = dw;
	}
	
	
}
