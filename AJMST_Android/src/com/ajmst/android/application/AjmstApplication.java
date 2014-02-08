package com.ajmst.android.application;

import com.ajmst.android.entity.SalesOrder;

import android.app.Application;

public class AjmstApplication extends Application{
	private SalesOrder currSalesOrder;
	
	@Override
	public void onCreate() {
		super.onCreate();
	}
	public SalesOrder getCurrSalesOrder() {
		return currSalesOrder;
	}
	public void setCurrSalesOrder(SalesOrder currSalesOrder) {
		this.currSalesOrder = currSalesOrder;
	}

	
}
