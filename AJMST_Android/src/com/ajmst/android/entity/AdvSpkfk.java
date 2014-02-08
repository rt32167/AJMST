package com.ajmst.android.entity;


import com.ajmst.commmon.entity.Spkfk;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class AdvSpkfk extends Spkfk{
	private static final long serialVersionUID = -3926164119706133607L;
	@DatabaseField
	private String gh;
	@DatabaseField
	private Integer maxQuantity;
	
	public String getGh() {
		return gh;
	}
	public void setGh(String gh) {
		this.gh = gh;
	}
	public Integer getMaxQuantity() {
		return maxQuantity;
	}
	public void setMaxQuantity(Integer maxQuantity) {
		this.maxQuantity = maxQuantity;
	}

}
