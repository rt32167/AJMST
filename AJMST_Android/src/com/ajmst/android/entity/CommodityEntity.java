package com.ajmst.android.entity;

import java.io.Serializable;
import java.sql.ResultSet;

/**
 * 
 * @author caijun
 * @version 2013-01-04 created
 */
public class CommodityEntity implements Serializable {

	private static final long serialVersionUID = 1704815977210868806L;
	String commodityID;
	String name;
	String barcode;
	String description;
	String mnemonicCode;
	String drugType;
	String type;
	String factory;
	String specification;
	String unit;
	String permissionNumber;
	String beActive;
	String isCustom;

	public CommodityEntity(){
	}
	
	public CommodityEntity(ResultSet rs) {
		try{
			this.commodityID = rs.getString("commodityID");
			this.name = rs.getString("name");
			this.barcode = rs.getString("barcode");
			this.description = rs.getString("description");
			this.mnemonicCode = rs.getString("mnemonicCode");
			this.drugType = rs.getString("drugType");
			this.type = rs.getString("type");
			this.factory = rs.getString("factory");
			this.specification = rs.getString("specification");
			this.unit = rs.getString("unit");
			this.permissionNumber = rs.getString("permissionNumber");
			this.beActive = rs.getString("beActive");
			this.isCustom = rs.getString("isCustom");
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public String getCommodityID() {
		return commodityID;
	}

	public void setCommodityID(String commodityID) {
		this.commodityID = commodityID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getMnemonicCode() {
		return mnemonicCode;
	}

	public void setMnemonicCode(String mnemonicCode) {
		this.mnemonicCode = mnemonicCode;
	}

	public String getDrugType() {
		return drugType;
	}

	public void setDrugType(String drugType) {
		this.drugType = drugType;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getFactory() {
		return factory;
	}

	public void setFactory(String factory) {
		this.factory = factory;
	}

	public String getSpecification() {
		return specification;
	}

	public void setSpecification(String specification) {
		this.specification = specification;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getPermissionNumber() {
		return permissionNumber;
	}

	public void setPermissionNumber(String permissionNumber) {
		this.permissionNumber = permissionNumber;
	}

	public String getBeActive() {
		return beActive;
	}

	public void setBeActive(String beActive) {
		this.beActive = beActive;
	}

	public String getIsCustom() {
		return isCustom;
	}

	public void setIsCustom(String isCustom) {
		this.isCustom = isCustom;
	}
}
